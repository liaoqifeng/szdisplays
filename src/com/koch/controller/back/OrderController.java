package com.koch.controller.back;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koch.base.BaseController;
import com.koch.bean.CustomerData;
import com.koch.bean.JsonMessage;
import com.koch.bean.Pager;
import com.koch.bean.Setting;
import com.koch.entity.Admin;
import com.koch.entity.Area;
import com.koch.entity.DeliverInfo;
import com.koch.entity.DeliverInfoItem;
import com.koch.entity.Deliverway;
import com.koch.entity.Logistics;
import com.koch.entity.Member;
import com.koch.entity.Order;
import com.koch.entity.OrderItem;
import com.koch.entity.PaymentInfo;
import com.koch.entity.Paymentway;
import com.koch.entity.Product;
import com.koch.entity.Refunds;
import com.koch.entity.Returns;
import com.koch.entity.ReturnsItem;
import com.koch.entity.SerialNumber;
import com.koch.entity.Order.DeliverStatus;
import com.koch.entity.Order.OrderStatus;
import com.koch.entity.Order.PaymentStatus;
import com.koch.entity.PaymentInfo.PaymentInfoStatus;
import com.koch.entity.PaymentInfo.PaymentInfoType;
import com.koch.entity.Refunds.RefundsType;
import com.koch.entity.SerialNumber.Type;
import com.koch.service.AdminService;
import com.koch.service.AreaService;
import com.koch.service.DeliverwayService;
import com.koch.service.LogisticsService;
import com.koch.service.OrderItemService;
import com.koch.service.OrderService;
import com.koch.service.PaymentwayService;
import com.koch.service.ProductService;
import com.koch.service.SerialNumberService;
import com.koch.util.JsonUtil;
import com.koch.util.SettingUtils;
import com.koch.util.SpringUtil;
/**
 * Back Order Controller
 * @author koch
 * @date  2015-03-12
 */
@Controller
@RequestMapping(value="back/order")
public class OrderController extends BaseController{
	@Resource
	private ProductService productService;
	@Resource
	private OrderService orderService;
	@Resource
	private DeliverwayService deliverwayService;
	@Resource
	private LogisticsService logisticsService;
	@Resource
	private PaymentwayService paymentwayService;
	@Resource
	private OrderItemService orderItemService;
	@Resource
	private AreaService areaService;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private AdminService adminService;

    @RequestMapping(value="list")
	public String list(){
		return "/back/order/order_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(HttpServletResponse response,Pager pager){
    	pager = orderService.findByPage(pager);
    	String result = "";
    	if(pager.getList() != null && pager.getList().size()>0){
    		
    		for(int i=0;i<pager.getList().size();i++){
    			Order order = (Order)pager.getList().get(i);
    			order.setOrderStatusName(SpringUtil.getMessage("OrderStatus."+order.getOrderStatus(), null));
    			order.setPayStatusName(SpringUtil.getMessage("PaymentStatus."+order.getPayStatus(), null));
    			order.setDeliverStatusName(SpringUtil.getMessage("DeliverStatus."+order.getDeliverStatus(), null));
    		}
    		try {
    			Map<String,String[]> filterMap = new HashMap<String, String[]>();
    			filterMap.put("orders", new String[]{"id","number","member","totalAmount","productAmount","orderStatus","orderStatusName","payStatusName","deliverStatusName","paymentwayName","deliverwayName","receiver","createDate"});
    			filterMap.put("member", new String[]{"username"});
    			result = JsonUtil.toJsonIncludeProperties(new CustomerData(pager.getList(), pager.getTotalCount()), filterMap);
    		} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return result;
    }
    
    @RequestMapping(value="view/{id}")
    public String view(@PathVariable Integer id,ModelMap model){
    	Order order = orderService.get(id);
    	List<Deliverway> deliverways = deliverwayService.getAll();
    	List<Paymentway> paymentways = paymentwayService.getAll();
    	List<Logistics> logisticses = logisticsService.getAll();
    	model.addAttribute("deliverways", deliverways);
    	model.addAttribute("paymentways", paymentways);
    	model.addAttribute("logisticses", logisticses);
    	model.addAttribute("paymentInfoTypes", PaymentInfoType.values());
    	model.addAttribute("refundsTypes", RefundsType.values());
    	model.addAttribute("order", order);
    	return "/back/order/order_view";
    }
    
    @RequestMapping(value="process",method=RequestMethod.POST)
	public String process(Integer orderId,RedirectAttributes redirectAttributes){
		Order order = orderService.get(orderId);
		if(order != null && (order.getOrderStatus() == OrderStatus.unprocessed) && !order.getIsExpired()){
			orderService.process(order, null);
			setRedirectAttributes(redirectAttributes,"call.order.confirmSuccess");
		}else{
			setRedirectAttributes(redirectAttributes,"call.order.confirmError");
		}
		return "redirect:/back/order/view/"+orderId+".shtml";
	}
    
    @RequestMapping(value="payment",method=RequestMethod.POST)
	public String payment(Integer orderId,Integer paymentwayId,PaymentInfo paymentInfo,RedirectAttributes redirectAttributes){
		Order order = orderService.get(orderId);
		Paymentway paymentway =  paymentwayService.get(paymentwayId);
		paymentInfo.setPaymentway(paymentway);
		
		if(order.getIsExpired() || order.getOrderStatus() != OrderStatus.processed){
			setRedirectAttributes(redirectAttributes,"call.order.expireOrStatusNotAllowed");
			return "redirect:/back/order/view/"+orderId+".shtml";
		}
		if(order.getPayStatus() != PaymentStatus.unpaid && order.getPayStatus() != PaymentStatus.partPaid){
			setRedirectAttributes(redirectAttributes,"call.order.paymentStatusNotAllowed");
			return "redirect:/back/order/view/"+orderId+".shtml";
		}
		if (paymentInfo.getPaidAmount().compareTo(new BigDecimal(0)) <= 0 || paymentInfo.getPaidAmount().compareTo(order.getTotalAmount()) > 0) {
			setRedirectAttributes(redirectAttributes,"call.order.paymentAmountError");
			return "redirect:/back/order/view/"+orderId+".shtml";
		}
		Member member = order.getMember();
		if(paymentInfo.getType() == PaymentInfoType.deposit && paymentInfo.getPaidAmount().compareTo(member.getDeposit()) > 0){
			setRedirectAttributes(redirectAttributes,"call.order.memberAccountInsufficient");
			return "redirect:/back/order/view/"+orderId+".shtml";
		}
		
		Admin admin = this.adminService.getCurrent();
	    if (order.getIsLockExpired(admin)) {
	    	setRedirectAttributes(redirectAttributes,"call.order.locked");
			return "redirect:/back/order/view/"+orderId+".shtml";
	    }
		
		paymentInfo.setOrder(order);
		paymentInfo.setNumber(serialNumberService.generate(SerialNumber.Type.payment));
		paymentInfo.setStatus(PaymentInfoStatus.success);
		paymentInfo.setMember(order.getMember());
		paymentInfo.setOperator(this.adminService.getCurrent().getUsername());
		
		Admin operator = this.adminService.getCurrent();
		orderService.payment(order, paymentInfo, operator);
		setRedirectAttributes(redirectAttributes,"call.order.paymentSuccess");
		return "redirect:/back/order/view/"+orderId+".shtml";
	}
    
    @RequestMapping(value="deliver",method=RequestMethod.POST)
	public String deliver(Integer orderId,Integer deliverwayId,Integer logisticsId,Integer areaId,DeliverInfo deliverInfo,RedirectAttributes redirectAttributes){
		Order order = orderService.get(orderId);
		if(order == null){
			setRedirectAttributes(redirectAttributes,"call.order.deliverError");
			return "redirect:/back/order/view/"+orderId+".shtml";
		}
		
		if (order.getIsExpired() || order.getOrderStatus() != OrderStatus.processed) {
			setRedirectAttributes(redirectAttributes,"call.order.expireOrStatusDeliverNotAllowed");
			return "redirect:/back/order/view/"+orderId+".shtml";
		}
		if (order.getDeliverStatus() != DeliverStatus.unDeliver && order.getDeliverStatus() != DeliverStatus.partDeliver) {
			setRedirectAttributes(redirectAttributes,"call.order.deliverStatusDeliverNotAllowed");
			return "redirect:/back/order/view/"+orderId+".shtml";
		}
	    
		Admin admin = this.adminService.getCurrent();
	    if (order.getIsLockExpired(admin)) {
	    	setRedirectAttributes(redirectAttributes,"call.order.locked");
			return "redirect:/back/order/view/"+orderId+".shtml";
	    }
		
		Iterator<DeliverInfoItem> deliverInfoItems = deliverInfo.getDeliverInfoItems().iterator();
		while(deliverInfoItems.hasNext()){
			DeliverInfoItem item = deliverInfoItems.next();
			if(item == null || StringUtils.isEmpty(item.getProductNumber()) || item.getProductQuantity() == null || item.getProductQuantity().intValue() <= 0){
				deliverInfoItems.remove();
			}else{
				OrderItem orderItem = order.getOrderItem(item.getProductNumber());
				if(orderItem == null || (item.getProductQuantity() > (orderItem.getProductQuantity() - orderItem.getDeliveryQuantity()))){
					setRedirectAttributes(redirectAttributes,"call.order.deliverQuantityException");
					return "redirect:/back/order/view/"+orderId+".shtml";
				}
				Product product = orderItem.getProduct();
				if(product != null && product.getStock() != null &&  item.getProductQuantity() > product.getStock()){
					setRedirectAttributes(redirectAttributes,"call.order.productStockLessThan",new Object[]{product.getName()});
					return "redirect:/back/order/view/"+orderId+".shtml";
				}
				item.setDeliverInfo(deliverInfo);
			}
		}
		
		deliverInfo.setOrder(order);
		Deliverway deliverway = deliverwayService.get(deliverwayId);
		deliverInfo.setDeliverway(deliverway != null ? deliverway : null);
		deliverInfo.setDeliverwayName(deliverway != null ? deliverway.getName() : "");
		Logistics logistics = logisticsService.get(logisticsId);
		deliverInfo.setLogistics(logistics != null ? logistics : null);
		deliverInfo.setLogisticsName(logistics != null ? logistics.getName() : "");
		Area area = areaService.get(areaId);
		deliverInfo.setArea(area != null ? area.getFullName() : "");
		
		deliverInfo.setNumber(serialNumberService.generate(Type.deliver));
		
		Admin operator = this.adminService.getCurrent();
		deliverInfo.setOperator(operator.getUsername());
		
		orderService.deliver(order, deliverInfo, null);
		setRedirectAttributes(redirectAttributes,"call.order.deliverSuccess");
		
		return "redirect:/back/order/view/"+orderId+".shtml";
	}
    
    @RequestMapping(value="complete",method=RequestMethod.POST)
	public String complete(Integer orderId,RedirectAttributes redirectAttributes){
    	Order order = orderService.get(orderId);
    	Admin operator = this.adminService.getCurrent();
    	if(order != null && !order.getIsExpired() && order.getOrderStatus() == OrderStatus.processed && !order.getIsLockExpired(operator)){
    		orderService.complete(order, operator);
    		setRedirectAttributes(redirectAttributes,"call.order.complete");
    	}else{
    		setRedirectAttributes(redirectAttributes,"call.order.processError");
    	}
    	return "redirect:/back/order/view/"+orderId+".shtml";
    }
    
    @RequestMapping(value="returns",method=RequestMethod.POST)
	public String returns(Integer orderId,Integer deliverwayId,Integer logisticsId,Integer areaId,Returns returns,RedirectAttributes redirectAttributes){
    	Order order = orderService.get(orderId);
    	
    	if(order == null){
			setRedirectAttributes(redirectAttributes,"call.order.returnsError");
			return "redirect:/back/order/view/"+orderId+".shtml";
		}
		if (order.getIsExpired() || order.getOrderStatus() != OrderStatus.processed) {
			setRedirectAttributes(redirectAttributes,"call.order.expireOrStatusReturnsNotAllowed");
			return "redirect:/back/order/view/"+orderId+".shtml";
		}
		if (order.getDeliverStatus() != DeliverStatus.deliver && order.getDeliverStatus() != DeliverStatus.partDeliver && order.getDeliverStatus() != DeliverStatus.partReturns) {
			setRedirectAttributes(redirectAttributes,"call.order.deliverStatusReturnsNotAllowed");
			return "redirect:/back/order/view/"+orderId+".shtml";
		}
    	
		Iterator<ReturnsItem> returnsItems = returns.getReturnsItems().iterator();
		while(returnsItems.hasNext()){
			ReturnsItem item = returnsItems.next();
			if(item == null || StringUtils.isEmpty(item.getProductNumber()) || item.getProductQuantity() == null || item.getProductQuantity().intValue() <= 0){
				returnsItems.remove();
			}else{
				OrderItem orderItem = order.getOrderItem(item.getProductNumber());
				if(orderItem == null || (item.getProductQuantity() > (orderItem.getProductQuantity() - orderItem.getReturnQuantity()))){
					setRedirectAttributes(redirectAttributes,"call.order.returnsQuantityException");
					return "redirect:/back/order/view/"+orderId+".shtml";
				}
				item.setReturns(returns);
			}
		}
		
		returns.setOrder(order);
		Deliverway deliverway = deliverwayService.get(deliverwayId);
		returns.setDeliverway(deliverway != null ? deliverway : null);
		returns.setDeliverwayName(deliverway != null ? deliverway.getName() : "");
		Logistics logistics = logisticsService.get(logisticsId);
		returns.setLogistics(logistics != null ? logistics : null);
		returns.setLogisticsName(logistics != null ? logistics.getName() : "");
		Area area = areaService.get(areaId);
		returns.setArea(area != null ? area.getFullName() : "");
		returns.setNumber(this.serialNumberService.generate(Type.returns));
		Admin operator = this.adminService.getCurrent();
		returns.setOperator(operator.getUsername());
		
		orderService.returns(order, returns, operator);
		setRedirectAttributes(redirectAttributes,"call.order.returnsSuccess");
    	return "redirect:/back/order/view/"+orderId+".shtml";
    }
    
    @RequestMapping(value="refunds",method=RequestMethod.POST)
	public String refunds(Integer orderId, Integer paymentwayId, Refunds refunds, RedirectAttributes redirectAttributes) {
		Order order = this.orderService.get(orderId);
		refunds.setOrder(order);
		Paymentway paymentway = this.paymentwayService.get(paymentwayId);
		refunds.setPaymentway(paymentway);
		if (order.getIsExpired() || order.getOrderStatus() != OrderStatus.processed) {
			setRedirectAttributes(redirectAttributes, "call.order.expireOrStatusRefundsNotAllowed");
			return "redirect:/back/order/view/" + orderId + ".shtml";
		}
		if (order.getPayStatus() != Order.PaymentStatus.paid && order.getPayStatus() != Order.PaymentStatus.partPaid && order.getPayStatus() != Order.PaymentStatus.partRefund) {
			setRedirectAttributes(redirectAttributes, "call.order.paymentStatusRefundsNotAllowed");
			return "redirect:/back/order/view/" + orderId + ".shtml";
		}
		if (refunds.getPaidAmount().compareTo(new BigDecimal(0)) <= 0 || (refunds.getPaidAmount().compareTo(order.getPaidAmount()) > 0)) {
			setRedirectAttributes(redirectAttributes, "call.order.refundsAmountError");
			return "redirect:/back/order/view/" + orderId + ".shtml";
		}
		
		Admin operator = this.adminService.getCurrent();
		if (order.getIsLockExpired(operator)) {
			setRedirectAttributes(redirectAttributes, "call.order.locked");
			return "redirect:/back/order/view/" + orderId + ".shtml";
		}
		refunds.setMember(order.getMember());
		refunds.setNumber(this.serialNumberService.generate(Type.refunds));
		refunds.setOperator(operator.getUsername());
		this.orderService.refunds(order, refunds, operator);
		setRedirectAttributes(redirectAttributes, "call.order.refundsSuccess");
		return "redirect:/back/order/view/"+orderId+".shtml";
	}
    
    @RequestMapping(value="invalid",method=RequestMethod.POST)
	public String invalid(Integer orderId,RedirectAttributes redirectAttributes){
    	Order order = orderService.get(orderId);
    	Admin operator = this.adminService.getCurrent();
    	if(order != null && !order.getIsExpired() && order.getOrderStatus() == OrderStatus.unprocessed && !order.getIsLockExpired(operator)){
    		this.orderService.invalid(order, operator);
    		setRedirectAttributes(redirectAttributes,"call.order.invalidSuccess");
    	}else{
    		setRedirectAttributes(redirectAttributes,"call.order.invalidError");
    	}
    	return "redirect:/back/order/view/"+orderId+".shtml";
    }
    
    @RequestMapping(value="edit/{id}")
    public String get(@PathVariable Integer id,ModelMap model){
    	Order order = orderService.get(id);
    	List<Deliverway> deliverways = deliverwayService.getAll();
    	List<Paymentway> paymentways = paymentwayService.getAll();
    	model.addAttribute("deliverways", deliverways);
    	model.addAttribute("paymentways", paymentways);
    	model.addAttribute("order", order);
    	return "/back/order/order_edit";
    }
    
	@RequestMapping(value="edit",method={RequestMethod.POST})
    public String edit(Order order,Integer areaId,RedirectAttributes redirectAttributes){
		Order oldOrder = orderService.get(order.getId());
		if(order == null){
			return "redirect:/back/order/list.shtml";
		}
		if(oldOrder.getIsExpired()){
			setRedirectAttributes(redirectAttributes, "call.order.expired");
			return "redirect:/back/order/list.shtml";
		}
		
		Admin admin = this.adminService.getCurrent();
		if(oldOrder.getIsLockExpired(admin)){
			setRedirectAttributes(redirectAttributes, "call.order.locked");
			return "redirect:/back/order/edit/"+order.getId()+".shtml";
		}
		
		if(oldOrder.getOrderStatus() == OrderStatus.completed || oldOrder.getOrderStatus() == OrderStatus.invalid){
			setRedirectAttributes(redirectAttributes, "call.order.orderStatusUpdateNotAllowed");
			return "redirect:/back/order/list.shtml";
		}
		if(oldOrder.getPayStatus() != PaymentStatus.unpaid){
			setRedirectAttributes(redirectAttributes, "call.order.paymentStatusUpdateNotAllowed");
			return "redirect:/back/order/list.shtml";
		}
		if(oldOrder.getDeliverStatus() != DeliverStatus.unDeliver){
			setRedirectAttributes(redirectAttributes, "call.order.deliverStatusUpdateNotAllowed");
			return "redirect:/back/order/list.shtml";
		}
		
		Iterator<OrderItem> orderItems = order.getOrderItems().iterator();
		while(orderItems.hasNext()){
			OrderItem orderItem = orderItems.next();
			if(orderItem.getId() != null){
				OrderItem item = orderItemService.get(orderItem.getId());
				BeanUtils.copyProperties(item,orderItem,new String[]{"productPrice","productQuantity"});
			}else{
				Product product = productService.findByNumber(orderItem.getProductNumber());
				orderItem.setProduct(product);
				orderItem.setProductName(product.getName());
				orderItem.setProductWeight(product.getWeight());
				orderItem.setOrder(oldOrder);
				orderItem.setDeliveryQuantity(0);
				orderItem.setReturnQuantity(0);
			}
		}
		String []properties = new String[]{"id","number","member","orderStatus","payStatus","weight","quantity",
				"deliverStatus","totalAmount","depositAmount","voucherAmount","paidAmount","promAmount","orderItems","paymentInfos","deliverInfos","returns",
				"refunds","depositInfos","logs","promotion","couponInfo","createDate"};
		BeanUtils.copyProperties(order,oldOrder,properties);
		oldOrder.setArea(areaService.get(areaId));
		oldOrder.setPaymentway(paymentwayService.get(order.getPaymentway().getId()));
		oldOrder.setDeliverway(deliverwayService.get(order.getDeliverway().getId()));
		if(!order.getIsInvoice()){
			order.setIsInvoice(false);
			order.setServiceAmount(new BigDecimal(0));
		}
		oldOrder.getOrderItems().clear();
		oldOrder.getOrderItems().addAll(order.getOrderItems());
		
		orderService.update(oldOrder,admin);
		setRedirectAttributes(redirectAttributes, "Common.update.success");
		return "redirect:/back/order/list.shtml";
    }
	
	@RequestMapping(value="addOrderItem")
	@ResponseBody
	public Map<String,Object> addOrderItem(String productNumber){
		Map<String,Object> hashMap = new HashMap<String,Object>();
	    Product product = productService.findByNumber(productNumber);
	    if(product == null){
	    	hashMap.put("status", "fail");
	    	hashMap.put("message", SpringUtil.getMessage("call.product.productNotExits", new Object[0]));
	    	return hashMap;
	    }
	    if(!product.getIsPublish())	{
	    	hashMap.put("status", "fail");
	    	hashMap.put("message", SpringUtil.getMessage("call.product.productNotPublish", new Object[0]));
	    	return hashMap;
	    }
	    hashMap.put("status", "success");
    	hashMap.put("productNumber", product.getNumber());
    	hashMap.put("productName", product.getName());
    	hashMap.put("productWeight", product.getWeight());
    	hashMap.put("productPrice", product.getSalePrice());
    	hashMap.put("totalPrice", product.getSalePrice());
    	return hashMap;
	}
	
	@RequestMapping(value="calculate")
	@ResponseBody
	public Map<String,Object> calculate(Order order){
		Map<String,Object> hashMap = new HashMap<String,Object>();
		if(order.getOrderItems() != null){
			Iterator<OrderItem> orderItems = order.getOrderItems().iterator();
			while(orderItems.hasNext()){
				OrderItem item = orderItems.next();
				String number = item.getProductNumber();
				Product product = productService.findByNumber(number);
				if(item.getProductQuantity().intValue() > product.getAvailableStock().intValue()){
					hashMap.put("status", "fail");
			    	hashMap.put("message", SpringUtil.getMessage("call.order.productStockLessThan", new Object[]{product.getName()}));
			    	return hashMap;
				}
			}
			Setting setting = SettingUtils.get();
			hashMap.put("totalWeight", order.getWeight());
			hashMap.put("totalQuantity", order.getQuantity());
			hashMap.put("totalAmount", setting.setScale(order.getTotalAmount()));
			hashMap.put("orderItems", order.getOrderItems());
			hashMap.put("status", "success");
			return hashMap;
		}else{
			hashMap.put("status", "fail");
	    	hashMap.put("message", SpringUtil.getMessage("Common.operator.error", new Object[0]));
	    	return hashMap;
		}
	}
	
	@RequestMapping(value="delete",method={RequestMethod.POST})
	@ResponseBody
    public JsonMessage delete(Integer [] id){
    	for(Integer i : id){
    		Order order = orderService.get(i);
    		if(order == null){
    			return delete_error;
    		}
    		Admin admin = new Admin();
    		if(order.getIsLockExpired(admin)){
    			return JsonMessage.error("call.order.locked", new Object[]{order.getNumber()});
    		}
    		if(order.getOrderStatus() != OrderStatus.unprocessed){
    			return JsonMessage.error("call.order.deleteOrderStatusNotAllowed", new Object[]{order.getNumber()});
    		}
    		orderService.delete(order);
    	}
    	return delete_success;
    }
}
