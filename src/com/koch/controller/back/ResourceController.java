package com.koch.controller.back;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.koch.base.BaseController;
import com.koch.bean.CustomerData;
import com.koch.bean.DateJsonValueProcessor;
import com.koch.bean.JsonMessage;
import com.koch.bean.Message;
import com.koch.bean.Pager;
import com.koch.bean.Pager.OrderType;
import com.koch.service.ResourceService;
/**
 * back Resource controller
 * @author koch
 * @date  2013-05-17
 */
@Controller
@RequestMapping(value="back/resource")
public class ResourceController extends BaseController{
	@Resource
	private ResourceService resourceService;
	
	@RequestMapping(value="list.shtml")
	public ModelAndView list(Pager pager){
		return new ModelAndView("/back/sys/resource_list");
    }
	
	@RequestMapping(value="list/pager.shtml")
	@ResponseBody
	public String pager(HttpServletRequest request,HttpServletResponse response,Pager pager){
		pager.setOrderType(OrderType.asc);
		pager = resourceService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor());
    		result = JSONObject.fromObject(new CustomerData(pager.getList(), pager.getTotalCount()),jsonConfig).toString();
    	}
    	return result;
    }
	
	@RequestMapping(value="edit/{id}.shtml")
	public ModelAndView get(@PathVariable Integer id,ModelMap modelMap){
		com.koch.entity.Resource resource = resourceService.get(id);
		modelMap.addAttribute("resource", resource);
		List<com.koch.entity.Resource> resources = resourceService.getList("parentId",0);
		modelMap.addAttribute("resources", resources);
		return new ModelAndView("/back/sys/resource_edit");
	}
	@RequestMapping(value="edit")
	public String edit(com.koch.entity.Resource resource){
		resource.setIsSystem(resource.getParentId()==0?true:false);
		resourceService.update(resource);
		return "/back/resource/list.shtml";
	}
	@RequestMapping(value="view")
	public String view(ModelMap modelMap){
		List<com.koch.entity.Resource> resources = resourceService.getList("parentId",0);
		modelMap.addAttribute("resources", resources);
		return "/back/sys/resource_add";
	}
	
	@RequestMapping(value="add")
	public String add(com.koch.entity.Resource resource){
		resource.setIsSystem(resource.getParentId()==0?true:false);
		resourceService.save(resource);
		return "/back/resource/list.shtml";
	}
	
	@RequestMapping(value="delete")
	@ResponseBody
	public JsonMessage delete(Integer [] id){
		resourceService.delete(id);
		return delete_success;
	}
}
