package com.koch.controller.front;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koch.base.BaseController;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.entity.Product;
import com.koch.entity.ProductCategory;
import com.koch.entity.Property;
import com.koch.service.ProductCategoryService;
import com.koch.service.ProductService;
import com.koch.service.PropertyService;

@Controller("frontIndexController")
@RequestMapping
public class IndexController extends BaseController{
	@Resource
	private ProductCategoryService productCategoryService;
	@Resource
	private ProductService productService;
	@Resource
	private PropertyService propertyService;
	
	@RequestMapping(value="menu")
	public String menu(ModelMap modelMap){
		List<ProductCategory> productCategories = productCategoryService.findRoots();
		modelMap.addAttribute("productCategories", productCategories);
		return "/front/common/header";
    }
	
	@RequestMapping(value="index")
	public String index(ModelMap modelMap){
		List<ProductCategory> productCategories = productCategoryService.findRoots();
		modelMap.addAttribute("productCategories", productCategories);
		return "/front/index";
    }
	
	private String getTitle(ProductCategory productCategory){
		String title = productCategory.getName();
		if(!productCategory.getLevel().equals(0)){
			ProductCategory parent = productCategory.getParent();
			if(parent != null){
				title = parent.getName() + "/" + title;
			}
			if(!parent.getLevel().equals(0)){
				parent = parent.getParent();
				title = parent.getName() + "/" + title; 
			}
		}
		return title;
	}
	
	@RequestMapping(value="list")
	public String list(ModelMap modelMap,Integer id,Pager<Product> pager){
		ProductCategory productCategory = productCategoryService.get(id);
		pager.setRows(1000);
		pager = productService.findByPager(productCategory, null, true, null, null, null, pager);
		modelMap.addAttribute("title",getTitle(productCategory));
		modelMap.addAttribute("list", pager.getList());
		return "/front/list";
    }
	
	@RequestMapping(value="get")
	public String get(ModelMap modelMap,Integer id){
		Product product = productService.get(id);
		modelMap.addAttribute("product", product);
		modelMap.addAttribute("title",getTitle(product.getProductCategory()));
		List<Property> propertys = propertyService.getList("productCategory", product.getProductCategory(),new OrderBy("orderList", OrderBy.OrderType.asc));
		modelMap.addAttribute("propertys", propertys);
		
		Pager<Product> pager = new Pager<Product>();
		pager.setRows(5);
		pager = productService.findByPager(product.getProductCategory().getParent(), null, true, null, null, null, pager);
		List<Product> products = pager.getList();
		if(products != null && products.size() > 5){
			int pageCount = products.size() / 5;
			if (products.size() % 5 > 0) {
				pageCount ++;
			}
			List<Product> list = new ArrayList<Product>();
			int p = new Random().nextInt(2);
			for (int i = 0; i < products.size(); i++) {
				if(i >= p * 5 && i < p * 5 + 5){
					list.add(products.get(i));
				}
			}
			modelMap.addAttribute("products", list);
		}else{
			modelMap.addAttribute("products", products);
		}
		return "/front/detail";
	}
	
	
}
