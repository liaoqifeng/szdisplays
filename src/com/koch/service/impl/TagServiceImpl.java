package com.koch.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.TagDao;
import com.koch.entity.Tag;
import com.koch.service.TagService;

@Service
public class TagServiceImpl extends BaseServiceImpl<Tag> implements TagService{
	@Resource
	private TagDao tagDao;
	@Resource
	public void setBaseDao(TagDao tagDao) {
		super.setBaseDao(tagDao);
	}
	
	public List<Tag> findList(Tag.Type type){
		return tagDao.findList(type);
	}
}
