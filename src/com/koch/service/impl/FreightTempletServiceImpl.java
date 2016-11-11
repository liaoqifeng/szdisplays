package com.koch.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.koch.bean.Message;
import com.koch.dao.FreightTempletAttributeDao;
import com.koch.dao.FreightTempletDao;
import com.koch.dao.FreightTempletItemDao;
import com.koch.entity.FreightTemplet;
import com.koch.entity.FreightTempletAttribute;
import com.koch.entity.FreightTempletItem;
import com.koch.entity.Logistics;
import com.koch.service.FreightTempletAttributeService;
import com.koch.service.FreightTempletService;

@Service
public class FreightTempletServiceImpl extends BaseServiceImpl<FreightTemplet> implements FreightTempletService{
	@Resource
	private FreightTempletDao freightTempletDao;
	@Resource
	private FreightTempletItemDao freightTempletItemDao;
	@Resource
	private FreightTempletAttributeDao freightTempletAttributeDao;
	@Resource
	public void setBaseDao(FreightTempletDao freightTempletDao) {
		super.setBaseDao(freightTempletDao);
	}
	
	@Transactional
	public void save(FreightTemplet freightTemplet,List<FreightTempletItem> templetItems,List<FreightTempletAttribute> attributes){
		if(freightTemplet != null){
			if(templetItems != null && templetItems.size()>0){
				for(FreightTempletItem item : templetItems){
					item.setFreightTemplet(freightTemplet);
				}
				freightTemplet.setFreightTempletItems(templetItems);
			}
			if(attributes != null && attributes.size()>0){
				for(FreightTempletAttribute attr : attributes){
					attr.setFreightTemplet(freightTemplet);
				}
				freightTemplet.setFreightTempletAttributes(attributes);
			}
			freightTempletDao.save(freightTemplet);
			
		}
	}
	
	@Transactional
	public void update(FreightTemplet freightTemplet,List<FreightTempletItem> templetItems,List<FreightTempletAttribute> attributes){
		if(freightTemplet != null){
			freightTempletItemDao.delete("freighttempletId", freightTemplet.getId());
			freightTempletAttributeDao.delete("freighttempletId", freightTemplet.getId());
			if(templetItems != null && templetItems.size()>0){
				for(FreightTempletItem item : templetItems){
					item.setFreightTemplet(freightTemplet);
				}
				freightTemplet.setFreightTempletItems(templetItems);
			}
			if(attributes != null && attributes.size()>0){
				for(FreightTempletAttribute attr : attributes){
					attr.setFreightTemplet(freightTemplet);
				}
				freightTemplet.setFreightTempletAttributes(attributes);
			}
			freightTempletDao.update(freightTemplet);
		}
	}
	
}
