package com.koch.controller.back;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.koch.base.ActionException;
import com.koch.base.BaseController;
import com.koch.base.ServiceException;
import com.koch.bean.CustomerData;
import com.koch.bean.DateJsonValueProcessor;
import com.koch.bean.Message;
import com.koch.bean.OrderBy;
import com.koch.bean.OrderBy.OrderType;
import com.koch.bean.Pager;
import com.koch.service.NodesService;
import com.koch.service.ResourceService;
import com.koch.service.RoleService;
import com.koch.util.JavaMD5;
import com.koch.entity.Nodes;
import com.koch.entity.Role;
import org.apache.commons.collections.CollectionUtils;
/**
 * back Nodes controller
 * @author koch
 * @date  2013-05-17
 */
@Controller
@RequestMapping(value="back/nodes")
public class NodesController extends BaseController{
	@Resource
	private NodesService nodesService;
	
	@RequestMapping(value="list.shtml")
	public ModelAndView list(){
		return new ModelAndView("/back/sys/nodes_list");
    }
	
	@RequestMapping(value="list/pager.shtml")
	@ResponseBody
	public String pager(HttpServletRequest request,HttpServletResponse response,Pager pager){
		List<Nodes> nodeList = nodesService.formatNodes(null);
    	if(nodeList != null && nodeList.size() > 0){
    		for (Nodes node : nodeList) {
				node.setState("closed");
			}
    		return JSONArray.fromObject(nodeList).toString();
    	}
    	return "[]";
    }
	
	@RequestMapping(value="get.shtml")
	public String get(HttpServletRequest request,HttpServletResponse response,Integer id){
		List<Nodes> nodeList = nodesService.formatTree(id);
		String result = "[]";
		if(nodeList != null && nodeList.size()>0){
			result = JSONArray.fromObject(nodeList).toString();
		}
		ajaxJson(result, response);
		return null;
	}
	@RequestMapping(value="edit.shtml")
	public String edit(HttpServletRequest request,HttpServletResponse response,String node){
		if(StringUtils.isEmpty(node)){
			ajaxText("无数据!", response);
			return null;
		}
		//String str = "{\"id\":null,\"parentId\":1,\"level\":2,\"name\":\"管理员管理\",\"isParent\":false,\"orderList\":0,\"isPublish\":true,\"url\":\"/back/admin/list.shtml\"}";
		JSONObject object = JSONObject.fromObject(node);
		Nodes nodes = (Nodes)JSONObject.toBean(object,Nodes.class);
		if(nodes.getId() != null && !nodes.getId().equals("0")){
			nodesService.update(nodes);
			ajaxText("修改成功!", response);
		}else{
			Integer key = nodesService.save(nodes);
			nodes.setId(key);
			ajaxText(JSONObject.fromObject(nodes).toString(), response);
		}
		return null;
	}
	
	@RequestMapping(value="delete.shtml")
	public ModelAndView delete(HttpServletRequest request,HttpServletResponse response){
		String ids = request.getParameter("ids");
		List<String> list = JSONArray.toList(JSONArray.fromObject(ids));
		nodesService.delete(list.toArray(new String[list.size()]));
		ajaxText("删除成功!", response);
		return null;
	}
}
