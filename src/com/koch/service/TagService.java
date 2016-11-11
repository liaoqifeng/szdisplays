package com.koch.service;

import java.util.List;

import com.koch.entity.Tag;

public interface TagService extends BaseService<Tag>{
	public List<Tag> findList(Tag.Type type);
}
