package com.koch.dao;

import java.util.List;

import com.koch.entity.Tag;

public interface TagDao extends BaseDao<Tag>{
	
	public List<Tag> findList(Tag.Type type);
	
}
