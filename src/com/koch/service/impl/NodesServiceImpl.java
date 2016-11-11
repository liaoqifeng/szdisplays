package com.koch.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.koch.bean.OrderBy;
import com.koch.bean.OrderBy.OrderType;
import com.koch.dao.NodesDao;
import com.koch.dao.RoleDao;
import com.koch.service.NodesService;
import com.koch.service.RoleService;
import com.koch.entity.Admin;
import com.koch.entity.Nodes;
import com.koch.entity.Role;

@Service
public class NodesServiceImpl extends BaseServiceImpl<Nodes> implements NodesService{
	@Resource
	private NodesDao nodesDao;
	@Resource
	public void setBaseDao(NodesDao nodesDao) {
		super.setBaseDao(nodesDao);
	}
	
	public List<Nodes> formatTree(Integer parentId){
		List<Nodes> nodes = getList("parentId",parentId,new OrderBy("orderList",OrderType.asc));
		for(Nodes node : nodes){
			if(parentId.equals(0))
				node.setIsParent(true);
			else
				node.setIsParent(nodesDao.isExist("parentId", node.getId()));
		}
		return nodes;
	}
	
	@SuppressWarnings("unchecked")
	public String formatNodesToJson(String menus){
		List<Nodes> nodes = formatNodes(menus);
		if(nodes != null)
			return JSONArray.fromObject(nodes).toString();
		return "[]";
	}
	
	public List<Nodes> formatNodes(String menus){
		List<Nodes> nodes = getList("parentId",0,new OrderBy("orderList",OrderType.asc));
		List<Integer> admins = new ArrayList<Integer>();
		if(StringUtils.isNotEmpty(menus))
			admins = JSONArray.toList(JSONArray.fromObject(menus),Integer.class);
		for(Nodes node : nodes){
			node.setIsParent(true);
			List<Nodes> leafs = getList("parentId",node.getId(),new OrderBy("orderList",OrderType.asc));
			for(Integer menu : admins){
				if(node.getId().equals(menu)){
					node.setChecked(true);
					break;
				}
			}
			for(Nodes leaf : leafs){
				leaf.setIsParent(nodesDao.isExist("parentId", leaf.getId()));
				for(Integer menu : admins){
					if(menu.equals(leaf.getId())){
						leaf.setChecked(true);
						break;
					}
				}
			}
			node.setChildren(leafs);
		}
		return nodes;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Nodes,List<Nodes>> initNodesFromJson(String menus){
		List<Nodes> admins = JSONArray.toList(JSONArray.fromObject(menus),Nodes.class);
		Map<Nodes,List<Nodes>> map = new LinkedHashMap<Nodes, List<Nodes>>();
		if(admins != null){
			for (Nodes nodes : admins) {
				if(nodes.getParentId().equals(0)){
					map.put(nodes, null);
				}
			}
			for(Map.Entry<Nodes,List<Nodes>> entry : map.entrySet()){
				Nodes node = entry.getKey();
				List<Nodes> list = entry.getValue();
				if(list == null)
					list = new ArrayList<Nodes>();
				for(Nodes nodes : admins){
					if(nodes.getParentId().equals(node.getId())){
						list.add(nodes);
					}
				}
				entry.setValue(list);
			}
		}
		return map;
	}
}
