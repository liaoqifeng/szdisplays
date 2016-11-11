package com.koch.controller.back;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koch.base.BaseController;
import com.koch.bean.CustomerData;
import com.koch.bean.Filter;
import com.koch.bean.Pager;
import com.koch.bean.Filter.Operator;
import com.koch.entity.DeliverInfo;
import com.koch.service.DeliverInfoService;
import com.koch.util.JsonUtil;
/**
 * 发货管理控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/deliverInfo")
public class DeliverInfoController extends BaseController{
	@Resource
	private DeliverInfoService deliverInfoService;
	
	@RequestMapping(value="list")
	public String list(){
		return "/back/order/deliver_list";
    }
	
	@RequestMapping(value="list/pager")
	@ResponseBody
	public String pager(String number,String deliverCode,Pager<DeliverInfo> pager){
		if(StringUtils.isNotEmpty(number)){
			pager.getFilters().add(new Filter("number", Operator.eq, number));
		}
		if(StringUtils.isNotEmpty(deliverCode)){
			pager.getFilters().add(new Filter("deliverCode", Operator.eq, deliverCode));
		}
		pager = deliverInfoService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		result = JsonUtil.toJson(new CustomerData(pager.getList(), pager.getTotalCount()),"yyyy-MM-dd");
    	}
    	return result;
    }
	
	
	@RequestMapping(value="view")
	public String view(Integer id,ModelMap modelMap){
		modelMap.addAttribute("deliverInfo", this.deliverInfoService.get(id));
		return "/back/order/deliver_view";
	}
	
}
