package com.koch.controller.back;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koch.base.BaseController;
import com.koch.bean.OrderBy;
import com.koch.entity.Area;
import com.koch.entity.Parameter;
import com.koch.entity.Property;
import com.koch.service.AreaService;
import com.koch.service.CaptchaImageService;
import com.koch.service.ParameterService;
import com.koch.service.PropertyService;
import com.koch.util.SpringUtil;
/**
 * 公共控制器
 * @author koch
 * @date  2014-09-12
 */
@Controller
@RequestMapping(value="back/common")
public class CommonController extends BaseController{
	@Resource
	private AreaService areaService;
	@Resource
	private ParameterService parameterService;
	@Resource
	private PropertyService propertyService;
	@Resource
	private CaptchaImageService captchaImageService;
	
	@RequestMapping(value="main")
    public String main(){
		return "/back/main";
    }
	
	@RequestMapping(value="getParamByCategory")
	@ResponseBody
	public Parameter getParamByCategory(Integer productCategoryId){
    	List<Parameter> list = parameterService.getList("productCategory.id", productCategoryId,new OrderBy("orderList", OrderBy.OrderType.asc));
    	Parameter parameter = null;
    	if(list != null && list.size()>0){
    		parameter = list.get(0);
    		parameter.setProductCategory(null);
    	}
    	return parameter;
    }
	
	@RequestMapping(value="getPropByCategory")
	@ResponseBody
	public List<Property> getPropByCategory(Integer productCategoryId){
    	List<Property> list = propertyService.getList("productCategory.id", productCategoryId,new OrderBy("orderList", OrderBy.OrderType.asc));
    	if(list != null && list.size()>0){
    		for(Property property : list){
        		if(property != null){
        			property.setProductCategory(null);
        		}
    		}
    	}
    	return list;
    }
	
	@RequestMapping(value="area")
    @ResponseBody
    public List<Area> area(Integer parentId){
    	List<Area> list = areaService.getList(parentId);
		return list;
    }
	
	@RequestMapping(value = { "/captcha" })
	public void captcha(HttpServletRequest request, HttpServletResponse response) {
		String captchaId = request.getParameter("captchaId");
		if (StringUtils.isEmpty(captchaId))
			captchaId = request.getSession().getId();
		String str1 = new StringBuffer().append("yB").append("-").append("der").append("ewoP").reverse().toString();
		String str2 = new StringBuffer().append("ten").append(".").append("xxp").append("ohs").reverse().toString();
		response.addHeader(str1, str2);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0L);
		response.setContentType("image/jpeg");
		ServletOutputStream servletOutputStream = null;
		CaptchaImageService captchaImageService = (CaptchaImageService)SpringUtil.getBean("captchaImageServiceImpl");
		try {
			servletOutputStream = response.getOutputStream();
			BufferedImage localBufferedImage = captchaImageService.build(captchaId);
			ImageIO.write(localBufferedImage, "jpg", servletOutputStream);
			servletOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(servletOutputStream);
		}
	}
}
