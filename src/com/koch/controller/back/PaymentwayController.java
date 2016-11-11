package com.koch.controller.back;

import java.util.HashSet;

import javax.annotation.Resource;

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
import com.koch.bean.Filter;
import com.koch.bean.JsonMessage;
import com.koch.bean.Pager;
import com.koch.bean.Filter.Operator;
import com.koch.bean.Pager.OrderType;
import com.koch.entity.Deliverway;
import com.koch.entity.Paymentway;
import com.koch.entity.Paymentway.PaymentwayType;
import com.koch.service.DeliverwayService;
import com.koch.service.PaymentwayService;
import com.koch.util.JsonUtil;
/**
 * 支付方式控制器
 * @author koch
 * @date  2013-05-17
 */
@Controller
@RequestMapping(value="back/paymentway")
public class PaymentwayController extends BaseController{
	@Resource
	private PaymentwayService paymentwayService;
	@Resource
	private DeliverwayService deliverwayService;
	
    @RequestMapping(value="list")
	public String list(){
    	return "/back/sys/paymentway_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(String name,Pager pager){
    	if(StringUtils.isNotEmpty(name)){
    		pager.getFilters().add(new Filter("name", Operator.eq, name));
    	}
    	pager.setOrderType(OrderType.asc);
    	pager.setOrderBy("orderList");
    	pager = paymentwayService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		result = JsonUtil.toJsonIncludeProperties(new CustomerData(pager.getList(), pager.getTotalCount()), "paymentway", new String[]{"id","name","timeout","image","remark","orderList","createDate"});
    	}
    	return result;
    }
    @RequestMapping(value="view")
    public String view(ModelMap modelMap){
    	modelMap.addAttribute("deliverways", deliverwayService.getAll());
    	modelMap.addAttribute("types", PaymentwayType.values());
    	return "/back/sys/paymentway_add";
    } 
    
    @RequestMapping(value="add", method={RequestMethod.POST})
    public String add(Paymentway paymentway,Integer [] deliverwaysIds,RedirectAttributes attrs){
    	paymentway.setDeliverways(new HashSet<Deliverway>(this.deliverwayService.findList(deliverwaysIds)));
    	paymentway.setOrders(null);
		paymentwayService.save(paymentway);
		setRedirectAttributes(attrs, "Common.save.success");
    	return "redirect:/back/paymentway/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap model){
    	Paymentway paymentway = paymentwayService.get(id);
    	model.addAttribute("paymentway",paymentway);
    	model.addAttribute("deliverways", deliverwayService.getAll());
    	model.addAttribute("types", PaymentwayType.values());
    	return "/back/sys/paymentway_edit";
    }
    
    @RequestMapping(value="edit", method={RequestMethod.POST})
	public String edit(Paymentway paymentway,Integer [] deliverwaysIds,RedirectAttributes attrs){
    	paymentway.setDeliverways(new HashSet<Deliverway>(this.deliverwayService.findList(deliverwaysIds)));
    	Paymentway old = paymentwayService.get(paymentway.getId());
    	BeanUtils.copyProperties(paymentway, old, new String[]{"orders"});
    	paymentwayService.update(old);
    	setRedirectAttributes(attrs, "Common.update.success");
    	return "redirect:/back/paymentway/list.shtml";
    }
    
    @RequestMapping(value="delete", method={RequestMethod.POST})
    @ResponseBody
    public JsonMessage delete(Integer [] id){
    	long count = this.paymentwayService.getTotalCount();
		if (id.length >= count) {
			return JsonMessage.error("Common.deleteAllNotAllowed", new Object[0]);
		}
		this.paymentwayService.delete(id);
		return delete_success;
    }
}
