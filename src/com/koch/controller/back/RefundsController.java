package com.koch.controller.back;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koch.base.BaseController;
import com.koch.bean.CustomerData;
import com.koch.bean.Filter;
import com.koch.bean.Pager;
import com.koch.bean.Filter.Operator;
import com.koch.entity.PaymentInfo;
import com.koch.entity.Refunds;
import com.koch.service.PaymentInfoService;
import com.koch.service.RefundsService;
import com.koch.util.JsonUtil;
/**
 * 退款管理控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/refunds")
public class RefundsController extends BaseController{
	@Resource
	private RefundsService refundsService;
	
	@RequestMapping(value="list")
	public String list(){
		return "/back/order/refunds_list";
    }
	
	@RequestMapping(value="list/pager")
	@ResponseBody
	public String pager(String number,String orderNumber, String payee, Pager<Refunds> pager){
		if(StringUtils.isNotEmpty(number)){
			pager.getFilters().add(new Filter("number", Operator.eq, number));
		}
		if(StringUtils.isNotEmpty(orderNumber)){
			pager.getFilters().add(new Filter("order.number", Operator.eq, orderNumber));
		}
		if(StringUtils.isNotEmpty(payee)){
			pager.getFilters().add(new Filter("payee", Operator.eq, payee));
		}
		pager = refundsService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		Map<String, String[]> filterMap = new HashMap<String, String[]>();
    		filterMap.put("refunds", new String[]{"id","number","order","member","typeName","paymentwayName","bank","account","paidAmount","payee","createDate","modifyDate"});
    		filterMap.put("orders", new String[]{"number"});
    		filterMap.put("member", new String[]{"username"});
    		result = JsonUtil.toJsonIncludeProperties(new CustomerData(pager.getList(), pager.getTotalCount()), filterMap);
    	}
    	return result;
    }
	
	
	@RequestMapping(value="view")
	public String view(Integer id,ModelMap modelMap){
		modelMap.addAttribute("refunds", this.refundsService.get(id));
		return "/back/order/refunds_view";
	}
	
}
