package com.koch.controller.back;

import java.util.Iterator;

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
import com.koch.bean.Pager;
import com.koch.bean.Filter.Operator;
import com.koch.bean.Pager.OrderType;
import com.koch.entity.Spec;
import com.koch.entity.SpecAttribute;
import com.koch.entity.Spec.SpecType;
import com.koch.service.SpecService;
import com.koch.util.JsonUtil;
import com.koch.util.SpringUtil;
/**
 * 商品规格控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/spec")
public class SpecController extends BaseController{
	@Resource
	private SpecService specService;
	
    @RequestMapping(value="list")
	public String list(){
    	return "/back/product/spec_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(String name,Pager pager){
    	if(StringUtils.isNotEmpty(name)){
    		pager.getFilters().add(new Filter("name", Operator.eq, name));
    	}
    	pager.setOrderType(OrderType.asc);
    	pager.setOrderBy("orderList");
    	pager = specService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		result = JsonUtil.toJsonIncludeProperties(new CustomerData(pager.getList(), pager.getTotalCount()), "spec", new String[]{"id","name","value","typeName","remark","orderList"});
    	}
    	return result;
    }
    
    @RequestMapping(value="view")
    public String view(){
    	return "/back/product/spec_add";
    } 
    @RequestMapping(value="add",method={RequestMethod.POST})
    public String add(Spec spec,RedirectAttributes redirectAttributes){
    	Iterator<SpecAttribute> iterator = spec.getSpecAttributes().iterator();
		while (iterator.hasNext()) {
			SpecAttribute specAttribute = iterator.next();
			if (specAttribute == null || specAttribute.getName() == null) {
				iterator.remove();
			} else {
				if (spec.getSpecType() == SpecType.literal) {
					specAttribute.setUrl(null);
				}
				specAttribute.setSpec(spec);
			}
		}
    	specService.save(spec);
    	setRedirectAttributes(redirectAttributes, "Common.save.success");
    	return "redirect:/back/spec/list.shtml";
    }
    @RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap modelMap){
    	Spec spec = specService.get(id);
    	modelMap.addAttribute("spec",spec);
    	return "/back/product/spec_edit";
    }
    
    @RequestMapping(value="edit",method={RequestMethod.POST})
	public String edit(Spec spec,RedirectAttributes redirectAttributes){
		Iterator<SpecAttribute> iterator = spec.getSpecAttributes().iterator();
		while (iterator.hasNext()) {
			SpecAttribute specAttribute = iterator.next();
			if (specAttribute == null || specAttribute.getName() == null) {
				iterator.remove();
			} else {
				if (spec.getSpecType() == SpecType.literal) {
					specAttribute.setUrl(null);
				}
				specAttribute.setSpec(spec);
			}
		}
		Spec old = specService.get(spec.getId());
		spec.setProducts(old.getProducts());
    	specService.update(spec);
    	setRedirectAttributes(redirectAttributes, "Common.update.success");
    	return "redirect:/back/spec/list.shtml";
    }
    
    @RequestMapping(value="delete",method={RequestMethod.POST})
    @ResponseBody
    public JsonMessage delete(Integer [] id){
		if (id != null) {
			for (Integer i : id) {
				Spec spec = (Spec) this.specService.get(i);
				if (spec != null && spec.getProducts() != null && !spec.getProducts().isEmpty()) {
					return JsonMessage.error("call.spec.deleteExistProductNotAllowed",new Object[] { spec.getName() });
				}
			}
			this.specService.delete(id);
		}
		return delete_success;
    }
}
