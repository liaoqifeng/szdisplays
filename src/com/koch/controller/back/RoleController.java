package com.koch.controller.back;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

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
import com.koch.bean.DateJsonValueProcessor;
import com.koch.bean.JsonMessage;
import com.koch.bean.Message;
import com.koch.bean.Pager;
import com.koch.entity.Role;
import com.koch.service.ResourceService;
import com.koch.service.RoleService;
import com.koch.util.AuthUtils;
import com.koch.util.JsonUtil;
/**
 * back Resource controller
 * @author koch
 * @date  2013-05-17
 */
@Controller
@RequestMapping(value="back/role")
public class RoleController extends BaseController{
	@Resource
	private RoleService roleService;
	@Resource
	private ResourceService resourceService;
	
	@RequestMapping(value="list.shtml")
	public ModelAndView list(Pager pager){
		return new ModelAndView("/back/sys/role_list");
	}
	
	@RequestMapping(value="list/pager.shtml")
	@ResponseBody
	public String pager(Pager<Role> pager){
		pager = roleService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		result = JsonUtil.toJson(new CustomerData(pager.getList(), pager.getTotalCount()),"yyyy-MM-dd");
    	}
    	return result;
    }
	
	@RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap modelMap){
		Role role = roleService.get(id);
		modelMap.addAttribute("role", role);
		modelMap.addAttribute("auths", AuthUtils.get());
		return "/back/sys/role_edit";
	}
	@RequestMapping(value="edit",method={RequestMethod.POST})
	public String edit(String [] auths,Role role, RedirectAttributes redirectAttributes){
		if(auths != null && auths.length > 0){
			role.setAuths(Arrays.asList(auths));
		}
		Role old = this.roleService.get(role.getId());
		BeanUtils.copyProperties(role, old, new String[]{"admins"});
		roleService.update(role);
		setRedirectAttributes(redirectAttributes, "Common.update.success");
		return "redirect:/back/role/list.shtml";
	}
	@RequestMapping(value="view")
	public String view(ModelMap modelMap){
		modelMap.addAttribute("auths", AuthUtils.get());
		return "/back/sys/role_add";
	}
	
	@RequestMapping(value="add",method={RequestMethod.POST})
	public String add(String [] auths,Role role,RedirectAttributes redirectAttributes){
		if(auths != null && auths.length > 0){
			role.setAuths(Arrays.asList(auths));
		}
		role.setIsSystem(false);
		role.setAdmins(null);
		roleService.save(role);
		setRedirectAttributes(redirectAttributes, "Common.save.success");
		return "redirect:/back/role/list.shtml";
	}
	
	@RequestMapping(value="delete",method={RequestMethod.POST})
	@ResponseBody
	public JsonMessage delete(Integer[] id) {
		if (id != null) {
			for (Integer i : id) {
				Role role = (Role) this.roleService.get(i);
				if (role != null && (role.getIsSystem().booleanValue() || (role.getAdmins() != null && !role.getAdmins().isEmpty())))
					return JsonMessage.error( "call.role.deleteExistNotAllowed", new Object[] { role.getName() });
			}
			this.roleService.delete(id);
		}
		return delete_success;
	}
}
