package com.koch.service.impl;

import java.util.Iterator;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koch.dao.SpecDao;
import com.koch.entity.Spec;
import com.koch.entity.SpecAttribute;
import com.koch.service.SpecService;
import com.koch.util.JsonUtil;

@Service
public class SpecServiceImpl extends BaseServiceImpl<Spec> implements SpecService{
	@Resource
	private SpecDao specDao;
	@Resource
	public void setBaseDao(SpecDao specDao) {
		super.setBaseDao(specDao);
	}
	
	@Transactional
	public Integer save(Spec spec) {
		Iterator<SpecAttribute> itor = spec.getSpecAttributes().iterator();
		while(itor.hasNext()){
			SpecAttribute attr = itor.next();
			if(attr != null && attr.getName() != null){
				attr.setSpec(spec);
			}else{
				itor.remove();
			}
		}
		spec.setValue(JsonUtil.toJson(spec.getSpecAttributes()));
		specDao.save(spec);
		return spec.getId();
	}

	@Transactional
	public Spec update(Spec spec) {
		Iterator<SpecAttribute> itor = spec.getSpecAttributes().iterator();
		while(itor.hasNext()){
			SpecAttribute attr = itor.next();
			if(attr != null && attr.getName() != null){
				attr.setSpec(spec);
			}else{
				itor.remove();
			}
		}
		spec.setValue(JsonUtil.toJson(spec.getSpecAttributes()));
		return specDao.update(spec);
	}
}
