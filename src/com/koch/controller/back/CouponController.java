package com.koch.controller.back;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
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
import com.koch.bean.ExcelView;
import com.koch.bean.JsonMessage;
import com.koch.bean.Pager;
import com.koch.entity.Admin;
import com.koch.entity.Coupon;
import com.koch.entity.CouponInfo;
import com.koch.service.CouponInfoService;
import com.koch.service.CouponService;
import com.koch.util.JsonUtil;
import com.koch.util.SpringUtil;
/**
 * 优惠券控制器
 * @author koch
 * @date  2015-04-02
 */
@Controller
@RequestMapping(value="back/coupon")
public class CouponController extends BaseController{
	@Resource
	private CouponService couponService;
	@Resource
	private CouponInfoService couponInfoService;
	
    @RequestMapping(value="list")
	public String list(){
		return "/back/marketing/coupon_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
   	public String pager(String name, Boolean isEnabled,Boolean isExchange,Boolean hasExpired,Pager pager){
    	pager = couponService.findPage(name, isEnabled, isExchange, hasExpired, pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		CustomerData data = new CustomerData(pager.getList(), pager.getTotalCount());
    		result = JsonUtil.toJsonIgnoreProperties(data,"coupon", new String[]{"couponInfos"});
    	}
    	return result;
    }
    
    @RequestMapping(value="view",method={RequestMethod.GET})
    public String view(ModelMap model){
    	return "/back/marketing/coupon_add";
    }
    
    @RequestMapping(value="add",method={RequestMethod.POST})
    public String add(Coupon coupon, ModelMap modelMap ,RedirectAttributes redirectAttributes){
    	coupon.setIsEnabled(coupon.getIsEnabled() != null);
    	coupon.setIsExchange(coupon.getIsExchange() != null);
    	if ((coupon.getStartDate() != null) && (coupon.getEndDate() != null)
				&& (coupon.getStartDate().after(coupon.getEndDate()))) {
			modelMap.addAttribute("coupon", coupon);
			setModelMap(modelMap, "call.coupon.beginDateGreaterThanEndDateNotAllowed");
			return "/back/marketing/coupon_add";
		}
		if ((coupon.getMinPrice() != null) && (coupon.getMaxPrice() != null)
				&& (coupon.getMinPrice().compareTo(coupon.getMaxPrice()) > 0)) {
			modelMap.addAttribute("coupon", coupon);
			setModelMap(modelMap, "call.coupon.minPriceGreaterThanMaxPriceNotAllowed");
			return "/back/marketing/coupon_add";
		}
		if (coupon.getIsExchange() && coupon.getScore() == null) {
			modelMap.addAttribute("coupon", coupon);
			setModelMap(modelMap, "call.coupon.pointIsNullNotAllowed");
			return "/back/marketing/coupon_add";
		}
		
		if (!coupon.getIsExchange())
			coupon.setScore(null);
		this.couponService.save(coupon);
		setRedirectAttributes(redirectAttributes, "Common.save.success");
		return "redirect:/back/coupon/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}",method={RequestMethod.GET})
	public String get(@PathVariable Integer id,ModelMap model){
    	model.addAttribute("coupon", couponService.get(id));
    	return "/back/marketing/coupon_edit";
    }
    
    @RequestMapping(value="edit",method={RequestMethod.POST})
	public String edit(Coupon coupon, ModelMap modelMap ,RedirectAttributes redirectAttributes){
    	coupon.setIsEnabled(coupon.getIsEnabled() != null);
    	coupon.setIsExchange(coupon.getIsExchange() != null);
    	if ((coupon.getStartDate() != null) && (coupon.getEndDate() != null)
				&& (coupon.getStartDate().after(coupon.getEndDate()))) {
			modelMap.addAttribute("coupon", coupon);
			setModelMap(modelMap, "call.coupon.beginDateGreaterThanEndDateNotAllowed");
			return "/back/marketing/coupon_add";
		}
		if ((coupon.getMinPrice() != null) && (coupon.getMaxPrice() != null)
				&& (coupon.getMinPrice().compareTo(coupon.getMaxPrice()) > 0)) {
			modelMap.addAttribute("coupon", coupon);
			setModelMap(modelMap, "call.coupon.minPriceGreaterThanMaxPriceNotAllowed");
			return "/back/marketing/coupon_add";
		}
		if (coupon.getIsExchange() && coupon.getScore() == null) {
			modelMap.addAttribute("coupon", coupon);
			setModelMap(modelMap, "call.coupon.minPriceGreaterThanMaxPriceNotAllowed");
			return "/back/marketing/coupon_add";
		}
		if (!coupon.getIsExchange())
			coupon.setScore(null);
		
		Coupon item = couponService.get(coupon.getId());
		BeanUtils.copyProperties(coupon, item, new String[]{"couponInfos","createDate"});
		couponService.update(item);
		setRedirectAttributes(redirectAttributes, "Common.update.success");
		return "redirect:/back/coupon/list.shtml"; 
    }
    
    @RequestMapping(value="delete")
    @ResponseBody
    public JsonMessage delete(Integer id){
    	return delete_success;
    }
    
    @RequestMapping(value={"build/{id}"}, method={RequestMethod.GET})
    public String build(@PathVariable Integer id, ModelMap model){
      Coupon coupon = (Coupon)this.couponService.get(id);
      model.addAttribute("coupon", coupon);
      model.addAttribute("totalCount", this.couponInfoService.count(coupon, null, null, null, null));
      model.addAttribute("usedCount", this.couponInfoService.count(coupon, null, null, null, true));
      return "/back/marketing/couponInfo_buid";
    }
    
    @RequestMapping(value={"export"}, method={RequestMethod.POST})
    public ModelAndView export(Integer id, Integer count, ModelMap model){
      if (count == null || count <= 0)
        count = 50;
      Coupon coupon = this.couponService.get(id);
      List<CouponInfo> list = this.couponInfoService.build(coupon, null, count);
      String str = SpringUtil.getMessage("call.coupon.code") +"-" + new SimpleDateFormat("yyyyMM").format(new Date()) + ".xls";
      String[] arrayOfString = new String[4];
      arrayOfString[0] = (SpringUtil.getMessage("call.coupon.type") + ": " + coupon.getName());
      arrayOfString[1] = (SpringUtil.getMessage("call.coupon.quantity") + ": " + count);
      arrayOfString[2] = (SpringUtil.getMessage("Common.field.operator") + ": " + new Admin().getUsername());
      arrayOfString[3] = (SpringUtil.getMessage("Common.field.createDate") + ": " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
      return new ModelAndView(new ExcelView(str, null, new String[] { "code" }, new String[] { SpringUtil.getMessage("call.coupon") }, null, null, list, arrayOfString), model);
    }
}
