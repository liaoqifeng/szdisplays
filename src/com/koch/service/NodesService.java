package com.koch.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.entity.Nodes;
import com.koch.entity.Role;

public interface NodesService extends BaseService<Nodes>{
	List<Nodes> formatTree(Integer parentId);
	public String formatNodesToJson(String menus);
	public List<Nodes> formatNodes(String menus);
	public Map<Nodes,List<Nodes>> initNodesFromJson(String menus);
}
