package com.koch.controller.back;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koch.base.BaseController;
import com.koch.bean.CustomerData;
import com.koch.bean.Filter;
import com.koch.bean.JsonMessage;
import com.koch.bean.Message;
import com.koch.bean.Pager;
import com.koch.bean.Filter.Operator;
import com.koch.entity.Brand;
import com.koch.entity.Coupon;
import com.koch.entity.GiftItem;
import com.koch.entity.Grade;
import com.koch.entity.Product;
import com.koch.entity.ProductCategory;
import com.koch.entity.Promotion;
import com.koch.service.BrandService;
import com.koch.service.CouponService;
import com.koch.service.GradeService;
import com.koch.service.ProductCategoryService;
import com.koch.service.ProductService;
import com.koch.service.PromotionService;
import com.koch.util.JsonUtil;
/**
 * 促销活动控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/promotion")
public class PromotionController extends BaseController{
	
	@Resource
	private GradeService gradeService;
	@Resource
	private ProductCategoryService productCategoryService;
	@Resource
	private BrandService brandService;
	@Resource
	private CouponService couponService;
	@Resource
	private ProductService productService;
	@Resource
	private PromotionService promotionService;
	
	
	@RequestMapping(value="list")
	public String list(){
		return "/back/marketing/promotion_list";
    }
	
	@RequestMapping(value="list/pager")
	@ResponseBody
	public String pager(String name,String title,Pager pager){
		if(StringUtils.isNotEmpty(name)){
			pager.getFilters().add(new Filter("name", Operator.eq, name));
		}
		if(StringUtils.isNotEmpty(title)){
			pager.getFilters().add(new Filter("title", Operator.eq, title));
		}
		String result = "[]";
		pager = promotionService.findByPage(pager);
		if(pager.getList() != null && pager.getList().size() > 0){
			result = JsonUtil.toJsonIncludeProperties(new CustomerData(pager.getList(), pager.getTotalCount()), "promotion", new String[]{"id","name","title","startDate","endDate","minPrice","maxPrice","orderList","isFreeDeliver","isAllowedCoupon"});
		}
    	return result;
    }
	
	@RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap modelMap){
		initModelMap(id, modelMap);
		return "/back/marketing/promotion_edit";
    }
	
	private void initModelMap(Integer id,ModelMap modelMap){
		modelMap.addAttribute("promotion", promotionService.get(id));
		modelMap.addAttribute("grades", this.gradeService.getAll());
		modelMap.addAttribute("brands", this.brandService.findAll());
		modelMap.addAttribute("coupons", this.couponService.findAll());
		modelMap.addAttribute("categorys", productCategoryService.findTree());
	}
	
	@RequestMapping(value="edit",method={RequestMethod.POST})
	public String edit(Promotion promotion,Integer [] couponIds,Integer [] gradeIds,Integer [] brandIds,Integer [] categoryIds,Integer [] productIds,RedirectAttributes redirectAttributes,ModelMap modelMap){
		if (promotion.getStartDate() != null && promotion.getEndDate() != null && promotion.getStartDate().after(promotion.getEndDate())) {
			initModelMap(promotion.getId(), modelMap);
			setModelMap(modelMap, "call.coupon.beginDateGreaterThanEndDateNotAllowed");
			return "/back/marketing/promotion_edit";
		}
	    if (promotion.getMinPrice() != null && promotion.getMaxPrice() != null && promotion.getMinPrice().compareTo(promotion.getMaxPrice()) > 0) {
	    	initModelMap(promotion.getId(), modelMap);
			setModelMap(modelMap, "call.coupon.minPriceGreaterThanMaxPriceNotAllowed");
			return "/back/marketing/promotion_edit";
	    }
		
		promotion.setGrades(new HashSet<Grade>(this.gradeService.findList(gradeIds)));
	    promotion.setProductCategorys(new HashSet<ProductCategory>(this.productCategoryService.findList(categoryIds)));
	    promotion.setBrands(new HashSet<Brand>(this.brandService.findList(brandIds)));
	    promotion.setCoupons(new HashSet<Coupon>(this.couponService.findList(couponIds)));
	    promotion.setIsAllowedCoupon(promotion.getIsAllowedCoupon() != null);
	    promotion.setIsFreeDeliver(promotion.getIsFreeDeliver() != null);
	    if(productIds != null && productIds.length > 0){
	    	Iterator<Product> products = this.productService.findList(productIds).iterator();
		    while(products.hasNext()){
		    	Product product = products.next();
		    	if(!product.getIsGift()){
		    		promotion.getProducts().add(product);
		    	}
		    }
	    }
	    
	    Iterator<GiftItem> giftItems = promotion.getGiftItems().iterator();
	    while(giftItems.hasNext()){
	    	GiftItem item = giftItems.next();
	    	if(item == null || item.getProduct() == null || item.getProduct().getId() == null){
	    		giftItems.remove();
	    	}else{
	    		item.setProduct(this.productService.get(item.getProduct().getId()));
	    		item.setPromotion(promotion);
	    	}
	    }
	   
	    this.promotionService.update(promotion);
		setRedirectAttributes(redirectAttributes, "Common.update.success");
		return "redirect:/back/promotion/list.shtml";
	}
	
	@RequestMapping(value="view")
	public String view(ModelMap modelMap){
		modelMap.addAttribute("grades", this.gradeService.getAll());
		modelMap.addAttribute("brands", this.brandService.findAll());
		modelMap.addAttribute("coupons", this.couponService.findAll());
		modelMap.addAttribute("categorys", productCategoryService.findTree());
		return "/back/marketing/promotion_add";
	}
	
	private void initRedirectAttributes(RedirectAttributes redirectAttributes,String code){
		redirectAttributes.addFlashAttribute("grades", this.gradeService.getAll());
		redirectAttributes.addFlashAttribute("brands", this.brandService.findAll());
		redirectAttributes.addFlashAttribute("coupons", this.couponService.findAll());
		redirectAttributes.addFlashAttribute("categorys", productCategoryService.findTree());
		setRedirectAttributes(redirectAttributes, code);
	}
	
	@RequestMapping(value="add",method={RequestMethod.POST})
	public String add(Promotion promotion,Integer [] couponIds,Integer [] gradeIds,Integer [] brandIds,Integer [] categoryIds,Integer [] productIds,RedirectAttributes redirectAttributes){
		if (promotion.getStartDate() != null && promotion.getEndDate() != null && promotion.getStartDate().after(promotion.getEndDate())) {
			initRedirectAttributes(redirectAttributes, "call.coupon.beginDateGreaterThanEndDateNotAllowed");
			return "redirect:/back/promotion/view.shtml";
		}
	    if (promotion.getMinPrice() != null && promotion.getMaxPrice() != null && promotion.getMinPrice().compareTo(promotion.getMaxPrice()) > 0) {
	    	initRedirectAttributes(redirectAttributes, "call.coupon.minPriceGreaterThanMaxPriceNotAllowed");
	    	return "redirect:/back/promotion/view.shtml";
	    }
		
		promotion.setGrades(new HashSet<Grade>(this.gradeService.findList(gradeIds)));
	    promotion.setProductCategorys(new HashSet<ProductCategory>(this.productCategoryService.findList(categoryIds)));
	    promotion.setBrands(new HashSet<Brand>(this.brandService.findList(brandIds)));
	    promotion.setCoupons(new HashSet<Coupon>(this.couponService.findList(couponIds)));
	    promotion.setIsAllowedCoupon(promotion.getIsAllowedCoupon() != null);
	    promotion.setIsFreeDeliver(promotion.getIsFreeDeliver() != null);
	    if(productIds != null && productIds.length > 0){
	    	Iterator<Product> products = this.productService.findList(productIds).iterator();
		    while(products.hasNext()){
		    	Product product = products.next();
		    	if(!product.getIsGift()){
		    		promotion.getProducts().add(product);
		    	}
		    }
	    }
	    
	    Iterator<GiftItem> giftItems = promotion.getGiftItems().iterator();
	    while(giftItems.hasNext()){
	    	GiftItem item = giftItems.next();
	    	if(item == null || item.getProduct() == null || item.getProduct().getId() == null){
	    		giftItems.remove();
	    	}else{
	    		item.setProduct(this.productService.get(item.getProduct().getId()));
	    		item.setPromotion(promotion);
	    	}
	    }
	   
	    this.promotionService.save(promotion);
		setRedirectAttributes(redirectAttributes, "Common.save.success");
		return "redirect:/back/promotion/list.shtml";
	}
	
	@RequestMapping(value="delete",method={RequestMethod.POST})
	@ResponseBody
	public JsonMessage delete(Integer [] id){
		this.promotionService.delete(id);
		return delete_success;
	}
}
