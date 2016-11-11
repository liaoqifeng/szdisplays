package com.koch.controller.back;

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
import com.koch.entity.Logistics;
import com.koch.service.LogisticsService;
import com.koch.util.JsonUtil;
/**
 * 物流公司管理控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/logistics")
public class LogisticsController extends BaseController{
	@Resource
	private LogisticsService logisticsService;
	
    @RequestMapping(value="list")
	public String list(){
    	return "/back/sys/logistics_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(String name,Pager pager){
    	if(StringUtils.isNotEmpty(name)){
    		pager.getFilters().add(new Filter("name", Operator.eq, name));
    	}
    	pager.setOrderType(OrderType.asc);
    	pager.setOrderBy("orderList");
    	String result = "[]";
    	pager = logisticsService.findByPage(pager);
		if(pager.getList() != null && pager.getList().size()>0){
			
    		result = JsonUtil.toJsonIncludeProperties(new CustomerData(pager.getList(), pager.getTotalCount()), "logistics", new String[]{"id","name","url","orderList","createDate"});
    	}
    	return result;
    }
    
    @RequestMapping(value="view")
    public String view(){
    	return "/back/sys/logistics_add";
    } 
    
    @RequestMapping(value="add",method={RequestMethod.POST})
    public String add(Logistics logistics,RedirectAttributes redirectAttributes){
    	if(logistics != null){
    		logistics.setDeliverways(null);
    		logisticsService.save(logistics);
    	}
    	setRedirectAttributes(redirectAttributes, "Common.save.success");
    	return "redirect:/back/logistics/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap modelMap){
    	Logistics logistics = logisticsService.get(id);
    	modelMap.addAttribute("logistics",logistics);
    	return "/back/sys/logistics_edit";
    }
    
    @RequestMapping(value="edit",method={RequestMethod.POST})
	public String edit(Logistics logistics,RedirectAttributes redirectAttributes){
    	if(logistics != null){
    		Logistics old = this.logisticsService.get(logistics.getId());
    		BeanUtils.copyProperties(logistics, old, new String[]{"deliverways"});
    		logisticsService.update(old);
    	}
    	setRedirectAttributes(redirectAttributes, "Common.update.success");
    	return "redirect:/back/logistics/list.shtml";
    }
    
    @RequestMapping(value="delete",method={RequestMethod.POST})
    @ResponseBody
    public JsonMessage delete(Integer [] id){
    	this.logisticsService.delete(id);
    	return delete_success;
    }
}
