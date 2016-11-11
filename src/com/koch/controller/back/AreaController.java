package com.koch.controller.back;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.koch.base.BaseController;
import com.koch.bean.JsonMessage;
import com.koch.bean.Message;
import com.koch.entity.Area;
import com.koch.service.AreaService;
import com.koch.util.JsonUtil;
/**
 * 地区管理控制器
 * @author koch
 * @date  2014-09-12
 */
@Controller
@RequestMapping(value="back/area")
public class AreaController extends BaseController{
	@Resource
	private AreaService areaService;
	
    @RequestMapping(value="list")
	public String list(){
		return "/back/sys/area_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(Integer parentId){
    	List<Area> list = areaService.getList(parentId);
    	return JsonUtil.toJson(list, "yyyy-MM-dd");
    }
    
    @RequestMapping(value="view/get")
    @ResponseBody
    public List<Area> ajaxArea(Integer parentId){
    	List<Area> list = areaService.getList(parentId);
		return list;
    }
    
	@RequestMapping(value="add",method={RequestMethod.POST})
	@ResponseBody
    public JsonMessage add(Integer parentId,String [] names){
		if(names != null && names.length > 0){
			for(String name : names){
				if(StringUtils.isEmpty(name))
					continue;
				Area area = new Area();
				area.setName(name);
				area.setParent(this.areaService.get(parentId));
				areaService.save(area);
			}
		}
    	return save_success;
    }
    
	@RequestMapping(value="edit",method={RequestMethod.POST})
	@ResponseBody
    public JsonMessage edit(Area area){
    	if(area != null){
    		Area old = areaService.get(area.getId());
			BeanUtils.copyProperties(old,area, new String[]{"name"});
			areaService.update(area);
    	}
		return update_success;
    }
	
	@RequestMapping(value="delete",method={RequestMethod.POST})
	@ResponseBody
    public JsonMessage delete(Integer id){
		areaService.delete(id);
    	return delete_success;
    }
}
