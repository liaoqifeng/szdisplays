package com.koch.controller.back;

import java.util.Iterator;
import java.util.List;

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
import com.koch.bean.Filter;
import com.koch.bean.JsonMessage;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.bean.Filter.Operator;
import com.koch.bean.Pager.OrderType;
import com.koch.entity.ProductCategory;
import com.koch.entity.Property;
import com.koch.service.ProductCategoryService;
import com.koch.service.PropertyService;
import com.koch.util.JsonUtil;
/**
 * 商品属性控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/property")
public class PropertyController extends BaseController{
	@Resource
	private PropertyService propertyService;
	@Resource
	private ProductCategoryService productCategoryService;
	
    @RequestMapping(value="list")
	public String list(){
    	return "/back/product/property_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(String name,HttpServletResponse response,Pager pager){
    	if(StringUtils.isNotEmpty(name)){
    		pager.getFilters().add(new Filter("name", Operator.eq, name));
    	}
    	pager.setOrderType(OrderType.asc);
    	pager.setOrderBy("orderList");
    	pager = propertyService.findByPage(pager);
    	String result = "";
    	if(pager.getList() != null && pager.getList().size()>0){
    		CustomerData data = new CustomerData(pager.getList(), pager.getTotalCount());
			result = JsonUtil.toJsonIncludeProperties(data,"productCategory",new String[]{"name"});
    	}
    	return result;
    }
    
	@RequestMapping(value="list/byTypeId")
	@ResponseBody
	public List<Property> byTypeId(Integer typeId){
    	List<Property> list = propertyService.getList("productCategory.id", typeId,new OrderBy("orderList", com.koch.bean.OrderBy.OrderType.asc));
    	if(list != null && list.size()>0){
    		for(Property property : list){
        		if(property != null){
        			property.setProductCategory(null);
        		}
    		}
    	}
    	return list;
    }
    
    @RequestMapping(value="view",method={RequestMethod.GET})
    public String view(ModelMap model){
    	List<ProductCategory> categoryList =  productCategoryService.findTree();
    	model.addAttribute("productCategorys", categoryList);
    	return "/back/product/property_add";
    } 
    
    @RequestMapping(value="add",method={RequestMethod.POST})
    public String add(Property property, RedirectAttributes redirectAttributes){
		Iterator<String> iterator = property.getOptions().iterator();
		while (iterator.hasNext()) {
			String str = iterator.next();
			if (StringUtils.isEmpty(str)) {
				iterator.remove();
			}
		}
		property.setProductCategory(this.productCategoryService.get(property.getProductCategory().getId()));

		if (property.getProductCategory().getPropertySet().size() >= 20) {
			setRedirectAttributes(redirectAttributes,"call.property.addCountNotAllowed", new Object[] { 20 });
		} else {
			property.setPropertyIndex(null);
			this.propertyService.save(property);
			setRedirectAttributes(redirectAttributes, "Common.save.success");
		}
        return "redirect:/back/property/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}",method={RequestMethod.GET})
	public String get(@PathVariable Integer id,ModelMap model){
    	Property property = propertyService.get(id);
    	List<ProductCategory> categoryList = productCategoryService.findTree();
    	model.addAttribute("property",property);
    	model.addAttribute("productCategorys", categoryList);
    	return "/back/product/property_edit";
    }
    
    @RequestMapping(value="edit",method={RequestMethod.POST})
	public String edit(Property property, RedirectAttributes redirectAttributes){
    	Iterator<String> iterator = property.getOptions().iterator();
		while (iterator.hasNext()) {
			String str = iterator.next();
			if (StringUtils.isEmpty(str)) {
				iterator.remove();
			}
		}
		
		Property old = this.propertyService.get(property.getId());
		BeanUtils.copyProperties(property, old, new String[] { "propertyIndex", "productCategory" });
    	propertyService.update(old);
    	setRedirectAttributes(redirectAttributes, "Common.update.success");
    	return "redirect:/back/property/list.shtml";
    }
    
    @RequestMapping(value="delete",method={RequestMethod.POST})
    @ResponseBody
    public JsonMessage delete(Integer [] id){
    	this.propertyService.delete(id);
    	return delete_success;
    }
}
