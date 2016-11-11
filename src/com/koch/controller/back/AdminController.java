package com.koch.controller.back;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

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
import com.koch.entity.Admin;
import com.koch.entity.Role;
import com.koch.service.AdminService;
import com.koch.service.NodesService;
import com.koch.service.RoleService;
import com.koch.util.JavaMD5;
import com.koch.util.JsonUtil;
/**
 * 管理员管理控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/admin")
public class AdminController extends BaseController{
	@Resource
	private AdminService adminService;
	@Resource
	private RoleService roleService;
	@Resource
	private NodesService nodesService;
	
	@RequestMapping(value="list")
	public String list(){
		return "/back/sys/admin_list";
    }
	
	@RequestMapping(value="list/pager")
	@ResponseBody
	public String pager(String username,Pager<Admin> pager){
		if(StringUtils.isNotEmpty(username)){
			pager.getFilters().add(new Filter("username", Operator.eq, username));
		}
		pager = adminService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		result = JsonUtil.toJson(new CustomerData(pager.getList(), pager.getTotalCount()));
    	}
    	return result;
    }
	
	@RequestMapping(value="edit/{id}")
	public String get(@PathVariable Integer id,ModelMap modelMap){
		List<Role> roles = roleService.getAll();
		modelMap.addAttribute("roles", roles);
		Admin admin = adminService.get(id);
		modelMap.addAttribute("admin", admin);
		return "/back/sys/admin_edit";
    }
	
	@RequestMapping(value="edit",method={RequestMethod.POST})
	public String edit(Integer [] roleIds,Admin admin,RedirectAttributes redirectAttributes){
		admin.setRoles(new HashSet<Role>(this.roleService.findList(roleIds)));
		Admin old = adminService.get(admin.getId());
		BeanUtils.copyProperties(admin,old,new String[]{"username","password","orders","isAccountExpired","isCredentialsExpired"});
		if(admin.getPassword() != null && !"".equals(admin.getPassword())){
			old.setPassword(JavaMD5.getMD5ofStr(admin.getPassword()));
		}
		adminService.update(old);
		setRedirectAttributes(redirectAttributes, "Common.update.success");
		return "redirect:/back/admin/list.shtml";
	}
	
	@RequestMapping(value="view")
	public String view(ModelMap modelMap){
		List<Role> roles = roleService.getAll();
		modelMap.addAttribute("roles", roles);
		return "/back/sys/admin_add";
	}
	
	@RequestMapping(value="add",method={RequestMethod.POST})
	public String add(Integer [] roleIds, Admin admin, RedirectAttributes redirectAttributes){
		admin.setOrders(null);
		admin.setRoles(new HashSet<Role>(this.roleService.findList(roleIds)));
		admin.setPassword(JavaMD5.getMD5ofStr(admin.getPassword()));
		admin.setIsAccountExpired(false);
		admin.setIsCredentialsExpired(false);
		adminService.save(admin);
		setRedirectAttributes(redirectAttributes, "Common.save.success");
		return "redirect:/back/admin/list.shtml";
	}
	
	@RequestMapping(value="delete",method={RequestMethod.POST})
	@ResponseBody
	public JsonMessage delete(Integer [] id){
		if(id != null){
			if (id.length >= this.adminService.getTotalCount())
			      return JsonMessage.error("Common.deleteAllNotAllowed", new Object[0]);
			adminService.delete(id);
		}
		return delete_success;
	}
}
