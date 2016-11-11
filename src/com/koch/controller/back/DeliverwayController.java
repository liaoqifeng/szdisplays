package com.koch.controller.back;

import java.util.List;

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
import com.koch.entity.Logistics;
import com.koch.service.DeliverwayService;
import com.koch.service.LogisticsService;
import com.koch.util.JsonUtil;
/**
 * 配送方式管理控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/deliverway")
public class DeliverwayController extends BaseController{
	@Resource
	private DeliverwayService deliverwayService;
	@Resource
	private LogisticsService logisticsService;
	
    @RequestMapping(value="list")
	public String list(){
    	return "/back/sys/deliverway_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(String name,Pager pager){
    	if(StringUtils.isNotEmpty(name)){
    		pager.getFilters().add(new Filter("name", Operator.eq, name));
    	}
    	pager.setOrderType(OrderType.asc);
    	pager.setOrderBy("orderList");
    	pager = deliverwayService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		result = JsonUtil.toJsonIncludeProperties(new CustomerData(pager.getList(), pager.getTotalCount()),"deliverway", new String[]{"id","name","firstWeight","firstPrice","conWeight","conPrice","orderList","createDate"});
    	}
    	return result;
    }
    @RequestMapping(value="view")
    public String view(ModelMap model){
    	List<Logistics> list = logisticsService.getAll();
    	model.addAttribute("logistics",list);
    	return "/back/sys/deliverway_add";
    } 
    
    @RequestMapping(value="add", method={RequestMethod.POST})
    public String add(Deliverway deliverway,RedirectAttributes redirectAttributes){
    	deliverway.setLogistics(this.logisticsService.get(deliverway.getLogistics().getId()));
    	deliverway.setOrders(null);
    	deliverway.setPaymentways(null);
    	deliverwayService.save(deliverway);
    	setRedirectAttributes(redirectAttributes, "Common.save.success");
    	return "redirect:/back/deliverway/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap model){
    	Deliverway deliverway = deliverwayService.get(id);
    	List<Logistics> list = logisticsService.getAll();
    	model.addAttribute("deliverway", deliverway);
    	model.addAttribute("logistics", list);
    	return "/back/sys/deliverway_edit";
    }
    
    @RequestMapping(value="edit", method={RequestMethod.POST})
	public String edit(Deliverway deliverway, RedirectAttributes redirectAttributes){
    	deliverway.setLogistics(this.logisticsService.get(deliverway.getLogistics().getId()));
    	Deliverway old = this.deliverwayService.get(deliverway.getId());
    	BeanUtils.copyProperties(deliverway, old, new String[]{"paymentways","orders"});
    	deliverwayService.update(old);
    	setRedirectAttributes(redirectAttributes, "Common.update.success");
    	return "redirect:/back/deliverway/list.shtml";
    }
    
    @RequestMapping(value="delete", method={RequestMethod.POST})
    @ResponseBody
    public JsonMessage delete(Integer [] id){
    	long count = this.deliverwayService.getTotalCount();
		if (id.length >= count) {
			return JsonMessage.error("Common.deleteAllNotAllowed", new Object[0]);
		}
		this.deliverwayService.delete(id);
		return delete_success;
    }
}
