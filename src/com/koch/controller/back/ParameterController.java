package com.koch.controller.back;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.koch.entity.Parameter;
import com.koch.entity.ParameterItems;
import com.koch.entity.ProductCategory;
import com.koch.service.ParameterService;
import com.koch.service.ProductCategoryService;
import com.koch.util.JsonUtil;
/**
 * 商品参数控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/parameter")
public class ParameterController extends BaseController{
	@Resource
	private ParameterService parameterService;
	@Resource
	private ProductCategoryService productCategoryService;
	
    @RequestMapping(value="list")
	public String list(){
    	return "/back/product/parameter_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(String name,Pager pager){
    	if(StringUtils.isNotEmpty(name)){
    		pager.getFilters().add(new Filter("name", Operator.eq, name));
    	}
    	pager.setOrderType(OrderType.asc);
    	pager.setOrderBy("orderList");
    	pager = parameterService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		CustomerData data = new CustomerData(pager.getList(), pager.getTotalCount());
			result = JsonUtil.toJsonIncludeProperties(data,"productCategory",new String[]{"name"});
    	}
    	return result;
    }
    
	@RequestMapping(value="list/byTypeId")
	@ResponseBody
	public Parameter byTypeId(Integer typeId,HttpServletResponse response){
    	List<Parameter> list = parameterService.getList("productCategory.id", typeId,new OrderBy("orderList", com.koch.bean.OrderBy.OrderType.asc));
    	Parameter parameter = null;
    	if(list != null && list.size()>0){
    		parameter = list.get(0);
    		parameter.setProductCategory(null);
    	}
    	return parameter;
    }
    
    @RequestMapping(value="view")
    public String view(ModelMap model){
    	List<ProductCategory> categoryList = productCategoryService.findTree();
    	model.addAttribute("productCategorys", categoryList);
    	return "/back/product/parameter_add";
    }
    
    @RequestMapping(value="add",method={RequestMethod.POST})
    public String add(Parameter parameter,RedirectAttributes redirectAttributes){
    	Iterator<ParameterItems> iterator = parameter.getParameterItems().iterator();
		while (iterator.hasNext()) {
			ParameterItems parameterItems = iterator.next();
			if (parameterItems == null || parameterItems.getName() == null) {
				iterator.remove();
			} else {
				parameterItems.setParameter(parameter);
			}
		}
		parameter.setProductCategory(this.productCategoryService.get(parameter.getProductCategory().getId()));
    	parameterService.save(parameter);
    	setRedirectAttributes(redirectAttributes, "Common.save.success");
    	return "redirect:/back/parameter/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap model){
    	Parameter parameter = parameterService.get(id);
    	List<ProductCategory> categoryList = productCategoryService.findTree();
    	model.addAttribute("parameter",parameter);
    	model.addAttribute("productCategorys", categoryList);
    	return "/back/product/parameter_edit";
    }
    
    @RequestMapping(value="edit",method={RequestMethod.POST})
	public String edit(Parameter parameter,RedirectAttributes redirectAttributes){
    	Iterator<ParameterItems> iterator = parameter.getParameterItems().iterator();
		while (iterator.hasNext()) {
			ParameterItems parameterItems = iterator.next();
			if (parameterItems == null || parameterItems.getName() == null) {
				iterator.remove();
			} else {
				parameterItems.setParameter(parameter);
			}
		}
		parameter.setProductCategory(this.productCategoryService.get(parameter.getProductCategory().getId()));
    	parameterService.update(parameter);
    	setRedirectAttributes(redirectAttributes, "Common.update.success");
    	return "redirect:/back/parameter/list.shtml";
    }
    
    @RequestMapping(value="delete",method={RequestMethod.POST})
    @ResponseBody
    public JsonMessage delete(Integer [] id){
    	this.parameterService.delete(id);
		return delete_success;
    }
}
