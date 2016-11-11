package com.koch.service;

import java.util.List;

import com.koch.entity.FreightTemplet;
import com.koch.entity.FreightTempletAttribute;
import com.koch.entity.FreightTempletItem;

public interface FreightTempletService extends BaseService<FreightTemplet>{
	public void save(FreightTemplet freightTemplet,List<FreightTempletItem> templetItems,List<FreightTempletAttribute> attributes);
	public void update(FreightTemplet freightTemplet,List<FreightTempletItem> templetItems,List<FreightTempletAttribute> attributes);
}
