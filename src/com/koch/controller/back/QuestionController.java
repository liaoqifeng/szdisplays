package com.koch.controller.back;

import javax.annotation.Resource;

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
import com.koch.bean.Pager;
import com.koch.bean.Filter.Operator;
import com.koch.bean.Pager.OrderType;
import com.koch.entity.Question;
import com.koch.service.QuestionService;
import com.koch.util.JsonUtil;
/**
 * 会员安全问题控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/question")
public class QuestionController extends BaseController{
	@Resource
	private QuestionService questionService;
	
    @RequestMapping(value="list")
	public String list(){
		return "/back/member/question_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(String name,Pager pager){
    	if(StringUtils.isNotEmpty(name)){
    		pager.getFilters().add(new Filter("value", Operator.like, name));
    	}
    	pager.setOrderBy("orderList");
    	pager.setOrderType(OrderType.asc);
    	pager = questionService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		result = JsonUtil.toJson(new CustomerData(pager.getList(), pager.getTotalCount()),"yyyy-MM-dd").toString();
    	}
    	return result;
    }
    
	@RequestMapping(value="view")
    public String view(){
    	return "/back/member/question_add";
    }
    
	@RequestMapping(value="add",method={RequestMethod.POST})
    public String add(Question question,RedirectAttributes redirectAttributes){
    	if(question != null){
    		question.setMembers(null);
    		questionService.save(question);
    	}
    	setRedirectAttributes(redirectAttributes, "Common.save.success");
    	return "redirect:/back/question/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap modelMap){
    	Question question = questionService.get(id);
    	modelMap.addAttribute("question",question);
    	return "/back/member/question_edit";
    }
    
	@RequestMapping(value="edit",method={RequestMethod.POST})
    public String edit(Question question,RedirectAttributes redirectAttributes){
    	if(question != null){
    		Question old = questionService.get(question.getId());
    		BeanUtils.copyProperties(question, old, new String[]{"members"});
    		questionService.update(old);
    	}
    	setRedirectAttributes(redirectAttributes, "Common.update.success");
    	return "redirect:/back/question/list.shtml";
    }
	
	@RequestMapping(value="delete",method={RequestMethod.POST})
	@ResponseBody
    public JsonMessage delete(Integer [] id){
		if (id != null) {
			for (Integer i : id) {
				Question question = this.questionService.get(i);
				if (question != null && question.getMembers() != null && !question.getMembers().isEmpty()) {
					return delete_error;
				}
			}
			this.questionService.delete(id);
		}
		return delete_success;
    }
}
