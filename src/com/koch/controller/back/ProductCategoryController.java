package com.koch.controller.back;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koch.base.BaseController;
import com.koch.bean.JsonMessage;
import com.koch.bean.Message;
import com.koch.bean.OrderBy;
import com.koch.bean.OrderBy.OrderType;
import com.koch.entity.Brand;
import com.koch.entity.Product;
import com.koch.entity.ProductCategory;
import com.koch.entity.Property;
import com.koch.service.BrandService;
import com.koch.service.ProductCategoryService;
import com.koch.util.JsonUtil;
/**
 * 商品分类控制器
 * @author koch
 * @date  2013-05-17
 */
@Controller
@RequestMapping(value="back/productCategory")
public class ProductCategoryController extends BaseController{
	@Resource
	private ProductCategoryService productCategoryService;
	@Resource
	private BrandService brandService;
	
    @RequestMapping(value="list")
	public String list(){
		return "/back/product/category_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
   	public String pager(HttpServletResponse response,Integer parentId){
    	List<ProductCategory> list = null;
    	if(parentId == null){
    		list = productCategoryService.findRoots();
    	}else{
    		list = productCategoryService.getList("parent", productCategoryService.get(parentId), new OrderBy("orderList", OrderType.asc));
    		if(list != null && list.size() > 0){
    			Iterator<ProductCategory> itor = list.iterator();
    			while(itor.hasNext()){
    				ProductCategory p = itor.next();
    				if(p.getChildren() != null && p.getChildren().size() > 0){
    					p.setState("closed");
					}else{
						p.setState("open");
    				}
    			}
    		}
    	}
    	String result = "[]";
    	if(list != null && list.size()>0){
    		try {
    			String [] propertys = new String[]{"id","name","title","keywords","describtion","orderList","text","state"};
				result = JsonUtil.toJsonIncludeProperties(list,propertys);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
    	}
    	return result;
    }
    
    @RequestMapping(value="view",method={RequestMethod.GET})
    public String view(ModelMap model){
    	List<ProductCategory> categoryList = productCategoryService.findTree();
    	List<Brand> brandList = brandService.getAll();
    	model.addAttribute("productCategorys", categoryList);
    	model.addAttribute("brands",brandList);
    	return "/back/product/category_add";
    }
    
    @RequestMapping(value="add",method={RequestMethod.POST})
    public String add(ProductCategory type,Integer parentId,Integer [] brandIds,RedirectAttributes redirectAttributes)throws IOException{
    	type.setParent(productCategoryService.get(parentId));
    	type.setBrandSet(new HashSet<Brand>(brandService.findList(brandIds)));
    	productCategoryService.save(type);
    	setRedirectAttributes(redirectAttributes, "Common.save.success");
    	return "redirect:/back/productCategory/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}",method={RequestMethod.GET})
	public String get(@PathVariable Integer id,ModelMap model){
    	if(id == null){
    		return "/back/productCategory/list.shtml";
    	}
    	ProductCategory type = productCategoryService.get(id);
    	List<ProductCategory> categoryList = productCategoryService.findTree();
    	List<Brand> brandList = brandService.getAll();
    	model.addAttribute("productCategorys", categoryList);
    	model.addAttribute("type", type);
    	model.addAttribute("brands",brandList);
    	return "/back/product/category_edit";
    }
    
    @RequestMapping(value="edit",method={RequestMethod.POST})
	public String edit(ProductCategory type,Integer parentId,Integer [] brandIds,RedirectAttributes redirectAttributes){
    	type.setParent(productCategoryService.get(parentId));
    	type.setBrandSet(new HashSet<Brand>(brandService.findList(brandIds)));
		if (type.getParent() != null) {
			if(type.getParent().getId().equals(type.getId())){
				setRedirectAttributes(redirectAttributes, "Common.update.error");
		    	return "redirect:/back/productCategory/edit/"+type.getId()+".shtml";
			}
			List<ProductCategory> productCategorys = this.productCategoryService.findChildren(type);
			if (productCategorys != null && productCategorys.contains(type.getParent())) {
				setRedirectAttributes(redirectAttributes, "Common.update.error");
		    	return "redirect:/back/productCategory/edit/"+type.getId()+".shtml";
			}
		}
		ProductCategory productCategory = this.productCategoryService.get(type.getId());
		BeanUtils.copyProperties(type, productCategory, new String[] { "path", "level", "children", "productSet", "parameterSet", "propertySet", "promotions" });
    	productCategoryService.update(productCategory);
    	setRedirectAttributes(redirectAttributes, "Common.update.success");
    	return "redirect:/back/productCategory/list.shtml";
    }
    
    @RequestMapping(value="delete",method={RequestMethod.POST})
    @ResponseBody
    public JsonMessage delete(Integer id){
    	ProductCategory productCategory = this.productCategoryService.get(id);
        if (productCategory == null) {
          return delete_error;
        }
        Set<ProductCategory> childrens = productCategory.getChildren();
        if (childrens != null && !childrens.isEmpty()) {
          return JsonMessage.error("call.productCategory.deleteExistChildrenNotAllowed", new Object[0]);
        }
        Set<Product> products = productCategory.getProductSet();
        if (products != null && !products.isEmpty()) {
          return JsonMessage.error("call.productCategory.deleteExistProductNotAllowed", new Object[0]);
        }
        this.productCategoryService.delete(id);
        return delete_success;
    }
}
