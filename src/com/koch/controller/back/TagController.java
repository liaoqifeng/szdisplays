package com.koch.controller.back;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
import com.koch.bean.Filter;
import com.koch.bean.JsonMessage;
import com.koch.bean.Pager;
import com.koch.bean.Filter.Operator;
import com.koch.bean.Pager.OrderType;
import com.koch.entity.Tag;
import com.koch.entity.Tag.Type;
import com.koch.service.TagService;
import com.koch.util.JsonUtil;
/**
 * 标签控制器
 * @author koch
 * @date  2015-03-17
 */
@Controller
@RequestMapping(value="back/tag")
public class TagController extends BaseController{
	@Resource
	private TagService tagService;
	
    @RequestMapping(value="list")
	public String list(){
    	return "/back/content/tag_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(String name, Pager<Tag> pager){
    	if(StringUtils.isNotEmpty(name)){
    		pager.getFilters().add(new Filter("name", Operator.eq, name));
    	}
    	pager.setOrderType(OrderType.asc);
    	pager.setOrderBy("orderList");
    	pager = tagService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		result = JsonUtil.toJsonIncludeProperties(new CustomerData(pager.getList(), pager.getTotalCount()),"tag",new String[]{"id","name","typeName","remark","orderList","image","createDate"});
    	}
    	return result;
    }
    
    @RequestMapping(value="view")
    public String view(ModelMap modelMap){
    	modelMap.addAttribute("types", Type.values());
    	return "/back/content/tag_add";
    } 
    
    @RequestMapping(value="add", method={RequestMethod.POST})
    public String add(Tag tag,RedirectAttributes redirectAttributes){
    	tag.setProducts(null);
    	tag.setArticles(null);
    	tagService.save(tag);
    	setRedirectAttributes(redirectAttributes, "Common.save.success");
    	return "redirect:/back/tag/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap modelMap){
    	Tag tag = tagService.get(id);
    	modelMap.addAttribute("tag",tag);
    	modelMap.addAttribute("types", Type.values());
    	return "/back/content/tag_edit";
    }
    
    @RequestMapping(value="edit", method={RequestMethod.POST})
	public String edit(Tag tag,RedirectAttributes redirectAttributes){
    	Tag old = tagService.get(tag.getId());
    	BeanUtils.copyProperties(tag, old, new String[]{"articles","products"});
    	tagService.update(old);
    	setRedirectAttributes(redirectAttributes, "Common.update.success");
    	return "redirect:/back/tag/list.shtml";
    }
    
    @RequestMapping(value="delete",method={RequestMethod.POST})
    @ResponseBody
    public JsonMessage delete(Integer [] id){
    	this.tagService.delete(id);
    	return delete_success;
    }
}
