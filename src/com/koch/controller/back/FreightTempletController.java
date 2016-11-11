package com.koch.controller.back;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.koch.base.BaseController;
import com.koch.bean.CustomerData;
import com.koch.bean.JsonMessage;
import com.koch.bean.Message;
import com.koch.bean.Pager;
import com.koch.bean.Pager.OrderType;
import com.koch.entity.Area;
import com.koch.entity.Deliverway;
import com.koch.entity.FreightTemplet;
import com.koch.entity.FreightTempletAttribute;
import com.koch.entity.FreightTempletItem;
import com.koch.entity.Logistics;
import com.koch.service.AreaService;
import com.koch.service.DeliverwayService;
import com.koch.service.FreightTempletItemService;
import com.koch.service.FreightTempletService;
import com.koch.util.JsonUtil;

/**
 * back freightTemplet controller
 * @author koch
 * @date  2013-05-17
 */
@Controller
@RequestMapping(value="back/freightTemplet")
public class FreightTempletController extends BaseController{
	@Resource
	private DeliverwayService deliverwayService;
	@Resource
	private AreaService areaService;
	@Resource
	private FreightTempletService freightTempletService;
	
	@RequestMapping(value="list.shtml")
	public ModelAndView list(){
		List<Deliverway> deliverways = deliverwayService.getAll();
    	return new ModelAndView("/back/sys/freight_list","deliverways",deliverways);
    }
	
	@RequestMapping(value="list/pager.shtml")
	public ModelAndView pager(HttpServletResponse response,Pager pager,
			@RequestParam(required=false) String name,@RequestParam(required=false) Integer deliverwayId){
		Map<String, Object> parameters = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(name))
			parameters.put("name",name);
		if(deliverwayId != null)
			parameters.put("deliverway.id",deliverwayId);
		pager.setParameters(parameters);
    	pager.setOrderType(OrderType.asc);
    	pager.setOrderBy("orderList");
    	pager = freightTempletService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		result = JsonUtil.toJson(new CustomerData(pager.getList(), pager.getTotalCount())).toString();
    	}
    	ajaxJson(result, response);
    	return null;
    }
	 
	@RequestMapping(value="view.shtml")
    public ModelAndView view(ModelMap modelMap){
		List<Deliverway> deliverways = deliverwayService.getAll();
		modelMap.addAttribute("deliverways", deliverways);
		List<Area> areas = null;//areaService.getProvinceAndCityArea();
		modelMap.addAttribute("areas", areas);
    	return new ModelAndView("/back/sys/freight_add");
    } 
    @RequestMapping(value="view/add")
    public String add(FreightTemplet freightTemplet,@RequestParam String items,@RequestParam String attr)throws IOException{
    	if(freightTemplet != null){
    		if(StringUtils.isNotEmpty(items) && StringUtils.isNotEmpty(attr)){
    			List<FreightTempletItem> templetItems = JsonUtil.toList(items, FreightTempletItem.class);
				List<FreightTempletAttribute> attributes = JsonUtil.toList(attr, FreightTempletAttribute.class);
    			if(templetItems != null){
					freightTempletService.save(freightTemplet, templetItems,attributes);
				}
        	}
    	}
    	return "redirect:/back/freightTemplet/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap modelMap){
    	FreightTemplet freightTemplet = freightTempletService.get(id);
    	modelMap.addAttribute("freightTemplet", freightTemplet);
    	List<Deliverway> deliverways = deliverwayService.getAll();
    	modelMap.addAttribute("deliverways", deliverways);
		List<Area> areas = null;//areaService.getProvinceAndCityArea();
		modelMap.addAttribute("areas", areas);
    	return "/back/sys/freight_edit";
    }
    
    @RequestMapping(value="edit")
	public String edit(FreightTemplet freightTemplet,@RequestParam String items,@RequestParam String attr){
    	if(freightTemplet != null){
    		if(StringUtils.isNotEmpty(items) && StringUtils.isNotEmpty(attr)){
    			List<FreightTempletItem> templetItems = JsonUtil.toList(items, FreightTempletItem.class);
    			List<FreightTempletAttribute> attributes = JsonUtil.toList(attr, FreightTempletAttribute.class);
    			freightTempletService.update(freightTemplet, templetItems, attributes);
    		}
    	}
    	return "redirect:/back/freightTemplet/list.shtml";
    }
    
    @RequestMapping(value="delete")
    public JsonMessage delete(Integer [] id){
    	freightTempletService.delete(id);
    	return delete_success;
    }
}
