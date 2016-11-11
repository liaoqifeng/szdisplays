package com.koch.controller.back;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
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
import com.koch.bean.CustomerData;
import com.koch.bean.Filter;
import com.koch.bean.JsonMessage;
import com.koch.bean.Message;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.bean.Filter.Operator;
import com.koch.bean.Pager.OrderType;
import com.koch.entity.Grade;
import com.koch.service.GradeService;
import com.koch.util.JsonUtil;
import com.koch.util.SpringUtil;
/**
 * 会员等级控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/grade")
public class GradeController extends BaseController{
	@Resource
	private GradeService gradeService;
	
    @RequestMapping(value="list.shtml")
	public String list(){
		return "/back/member/grade_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(String name,Pager pager){
    	if(StringUtils.isNotEmpty(name)){
    		pager.getFilters().add(new Filter("name", Operator.eq, name));
    	}
    	pager.setOrderBy("orderList");
    	pager.setOrderType(OrderType.asc);
    	pager = gradeService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
        	result = JsonUtil.toJson(new CustomerData(pager.getList(), pager.getTotalCount())).toString();
    	}
    	return result;
    }
   
    @RequestMapping(value="list/all")
    @ResponseBody
	public List<Grade> all(){
    	List<Grade> list = gradeService.getAll();
    	return list;
    }
    
	@RequestMapping(value="view")
    public String view(){
    	return "/back/member/grade_add";
    }
	
	@RequestMapping(value = { "/checkName" }, method = { RequestMethod.GET })
	@ResponseBody
	public boolean checkName(String previousName, String name) {
		if (StringUtils.isEmpty(name)) {
			return false;
		}
		return this.gradeService.nameUnique(previousName, name);
	}
	  
	@RequestMapping(value = { "/checkAmount" }, method = { RequestMethod.GET })
	@ResponseBody
	public boolean checkAmount(BigDecimal previousAmount, BigDecimal amount) {
		if (amount == null) {
			return false;
		}
		return this.gradeService.amountUnique(previousAmount, amount);
	}
    
	@RequestMapping(value="add",method={RequestMethod.POST})
    public String add(Grade grade,RedirectAttributes redirectAttributes){
		if (this.gradeService.nameExists(grade.getName())) {
			setRedirectAttributes(redirectAttributes, "call.grade.exitGradeName");
	    	return "redirect:/back/grade/view.shtml";
		}
		if(grade.getExpValue() == null || this.gradeService.amountExists(grade.getExpValue())){
			setRedirectAttributes(redirectAttributes, "call.grade.exitAmount");
	    	return "redirect:/back/grade/view.shtml";
		}
		grade.setMembers(null);
		grade.setPromotions(null);
		gradeService.save(grade);
		setRedirectAttributes(redirectAttributes, "Common.save.success");
    	return "redirect:/back/grade/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap model){
    	Grade grade = gradeService.get(id);
    	model.addAttribute("grade",grade);
    	return "/back/member/grade_edit";
    }
    
	@RequestMapping(value="edit",method={RequestMethod.POST})
    public String edit(Grade grade, RedirectAttributes redirectAttributes){
		Grade old = this.gradeService.get(grade.getId());
		
		if (!this.gradeService.nameUnique(old.getName(),grade.getName())) {
			setRedirectAttributes(redirectAttributes, "call.grade.exitGradeName");
	    	return "redirect:/back/grade/edit/"+grade.getId()+".shtml";
		}
		if(grade.getExpValue() == null || !this.gradeService.amountUnique(old.getExpValue(),grade.getExpValue())){
			setRedirectAttributes(redirectAttributes, "call.grade.exitAmount");
			return "redirect:/back/grade/edit/"+grade.getId()+".shtml";
		}
		
		BeanUtils.copyProperties(grade, old, new String[]{"members","promotions"});
		gradeService.update(old);
		setRedirectAttributes(redirectAttributes, "Common.update.success");
    	return "redirect:/back/grade/list.shtml";
    }
	
	@RequestMapping(value="delete",method={RequestMethod.POST})
	@ResponseBody
    public JsonMessage delete(Integer [] id){
		if (id != null) {
			for (Integer i : id) {
				Grade grade = this.gradeService.get(i);
				if (grade != null && grade.getMembers() != null && !grade.getMembers().isEmpty()) {
					return JsonMessage.error("call.grade.deleteExistNotAllowed", new Object[] { grade.getName() });
				}
			}
			long count = this.gradeService.getTotalCount();
			if (id.length >= count) {
				return JsonMessage.error("Common.deleteAllNotAllowed", new Object[0]);
			}
			this.gradeService.delete(id);
		}
		return delete_success;
    }
}
