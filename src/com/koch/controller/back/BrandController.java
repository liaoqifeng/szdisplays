package com.koch.controller.back;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koch.base.BaseController;
import com.koch.bean.CustomerData;
import com.koch.bean.Filter;
import com.koch.bean.JsonMessage;
import com.koch.bean.Message;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.bean.Filter.Operator;
import com.koch.bean.Pager.OrderType;
import com.koch.entity.Brand;
import com.koch.entity.Product;
import com.koch.entity.Brand.BrandType;
import com.koch.service.BrandService;
import com.koch.util.FileUtil;
import com.koch.util.JsonUtil;
/**
 * 品牌控制器
 * @author koch
 * @date  2013-05-17
 */
@Controller
@RequestMapping(value="back/brand")
public class BrandController extends BaseController{
	@Resource
	private BrandService brandService;
	
    @RequestMapping(value="list")
	public String list(){
    	return "/back/product/brand_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(String name, Pager<Brand> pager){
    	if(StringUtils.isNotEmpty(name)){
    		pager.getFilters().add(new Filter("name", Operator.eq, name));
    	}
    	pager.setOrderType(OrderType.asc);
    	pager.setOrderBy("orderList");
    	pager = brandService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		try {
				result = JsonUtil.toJsonIgnoreProperties(new CustomerData(pager.getList(), pager.getTotalCount()),"brand",new String[]{"productSet","promotions","productCategorys"});
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
    	}
    	return result;
    }
    @RequestMapping(value="list/get")
    @ResponseBody
	public String get(String typeId){
    	List<Brand> list = new ArrayList<Brand>();
    	if(StringUtils.isNotEmpty(typeId))
    		list = brandService.getList("typeId", Integer.parseInt(typeId),new OrderBy("orderList"));
    	String result = "[]";
    	if(list != null && list.size()>0){
    		result = JsonUtil.toJsonIgnoreProperties(list,new String[]{"productSet","promotions","productCategorys"});
    	}
    	return result;
    }
    @RequestMapping(value="view")
    public String view(HttpServletRequest request,HttpServletResponse response){
    	return "/back/product/brand_add";
    } 
    
    @RequestMapping(value="add")
    public String add(Brand brand,RedirectAttributes redirectAttributes)throws IOException{
    	if(brand.getBrandType()!=BrandType.image){
    		brand.setImage("");
    	}
    	brandService.save(brand);
    	setRedirectAttributes(redirectAttributes, "Common.save.success");
    	return "redirect:/back/brand/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap modelMap){
    	Brand brand = brandService.get(id);
    	modelMap.addAttribute("brand",brand);
    	return "/back/product/brand_edit";
    }
    
    @RequestMapping(value="edit")
	public String edit(Brand brand,RedirectAttributes redirectAttributes){
    	if(brand.getBrandType()!=BrandType.image){
    		brand.setImage(null);
    	}
    	brandService.update(brand);
    	setRedirectAttributes(redirectAttributes, "Common.update.success");
    	return "redirect:/back/brand/list.shtml";
    }
    
    @RequestMapping(value="delete",method={RequestMethod.POST})
    @ResponseBody
    public JsonMessage delete(Integer [] id){
    	this.brandService.delete(id);
    	return delete_success;
    }
}
