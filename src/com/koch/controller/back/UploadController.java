package com.koch.controller.back;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.koch.base.BaseController;
import com.koch.bean.JsonMessage;
import com.koch.util.JsonUtil;
/**
 * back upload controller
 * @author koch
 * @date  2013-11-13
 */
@Controller
@RequestMapping(value="back/upload")
public class UploadController extends BaseController{
	
    @RequestMapping(value="image")
    public String image(MultipartFile file,String dir,HttpServletRequest request,HttpServletResponse response){
		if(!file.isEmpty()){
			String uploadImagePath = "";//uploadImage(file, dir, request);
			if(StringUtils.isNotEmpty(uploadImagePath)){
				ajaxJson(JsonUtil.toJson(""), response);
			}else{
				ajaxJson(JsonUtil.toJson(JsonMessage.error("Common.upload.error")), response);
			}
		}else{
			ajaxJson(JsonUtil.toJson(JsonMessage.error("Common.upload.error")), response);
		}
		return null;
    }
}
