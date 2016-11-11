package com.koch.controller.back;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koch.base.BaseController;
import com.koch.bean.CustomerData;
import com.koch.bean.JsonMessage;
import com.koch.bean.Pager;
import com.koch.entity.Coupon;
import com.koch.entity.CouponInfo;
import com.koch.entity.Member;
import com.koch.service.CouponInfoService;
import com.koch.service.CouponService;
import com.koch.service.MemberService;
import com.koch.util.JsonUtil;
/**
 * 优惠券控制器
 * @author koch
 * @date  2015-04-02
 */
@Controller
@RequestMapping(value="back/couponInfo")
public class CouponInfoController extends BaseController{
	@Resource
	private CouponService couponService;
	@Resource
	private CouponInfoService couponInfoService;
	@Resource
	private MemberService memberService;
	
    @RequestMapping(value="list")
	public String list(ModelMap modelMap){
    	List<Coupon> coupons = couponService.getAll();
    	modelMap.addAttribute("coupons", coupons);
		return "/back/marketing/couponInfo_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
   	public String pager(Integer couponId ,Boolean isUsed,String username,Pager pager){
    	Coupon coupon = null;
    	if(couponId != null){
    		coupon = new Coupon();
    		coupon.setId(couponId);
    	}
    	Member member = null;
    	if(StringUtils.isNotEmpty(username)){
    		member = memberService.findByUsername(username);
    	}
    	pager = couponInfoService.findByPage(coupon, member, null, null, isUsed, pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		CustomerData data = new CustomerData(pager.getList(), pager.getTotalCount());
    		Map<String,String[]> filterMap = new HashMap<String,String[]>();
    		filterMap.put("coupon", new String[]{"name","startDate","endDate","isEnabled"});
    		filterMap.put("couponInfo", new String[]{"id","code","isUsed","usedDate","coupon","member"});
    		filterMap.put("member", new String[]{"username"});
    		result = JsonUtil.toJsonIncludeProperties(data, filterMap);
    	}
    	return result;
    }
    
    @RequestMapping(value="change")
    @ResponseBody
   	public JsonMessage change(Integer id ){
    	if(id == null) return JsonMessage.error("Common.operator.error");
    	CouponInfo couponInfo = couponInfoService.get(id);
    	couponInfo.setIsUsed(false);
    	couponInfo.setMember(null);
    	couponInfo.setUsedDate(null);
    	couponInfoService.update(couponInfo);
    	return JsonMessage.success("Common.operator.success");
    }
}
