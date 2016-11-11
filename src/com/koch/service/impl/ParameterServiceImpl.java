package com.koch.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koch.bean.OrderListComparator;
import com.koch.dao.ParameterDao;
import com.koch.entity.Parameter;
import com.koch.entity.ParameterItems;
import com.koch.service.ParameterService;
import com.koch.util.JsonUtil;

@Service
public class ParameterServiceImpl extends BaseServiceImpl<Parameter> implements ParameterService{
	@Resource
	private ParameterDao parameterDao;
	@Resource
	public void setBaseDao(ParameterDao parameterDao) {
		super.setBaseDao(parameterDao);
	}
	@Transactional
	public Integer save(Parameter t) {
		List<ParameterItems> items = t.getParameterItems();
		if(t != null && items != null){
			for(ParameterItems item : items){
				item.setParameter(t);
			}
			ParameterItems [] params = items.toArray(new ParameterItems[items.size()]);
			Arrays.sort(params, new OrderListComparator());
			String json = JsonUtil.toJson(Arrays.asList(params)).toString();
			t.setValue(json);
		}
		return super.save(t);
	}
	@Transactional
	public Parameter update(Parameter t) {
		List<ParameterItems> items = t.getParameterItems();
		if(t != null && items != null){
			for(ParameterItems item : items){
				item.setParameter(t);
			}
			ParameterItems [] params = items.toArray(new ParameterItems[items.size()]);
			Arrays.sort(params, new OrderListComparator());
			String json = JsonUtil.toJson(Arrays.asList(params)).toString();
			t.setValue(json);
		}
		return super.update(t);
	}
	@Transactional
	public void delete(Integer[] ids) {
		// TODO Auto-generated method stub
		super.delete(ids);
	}
	
	
}
