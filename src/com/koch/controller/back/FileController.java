package com.koch.controller.back;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.koch.base.BaseController;
import com.koch.bean.FileInfo;
import com.koch.bean.JsonMessage;
import com.koch.bean.FileInfo.FileType;
import com.koch.service.FileService;
import com.koch.util.JsonUtil;
/**
 * back upload controller
 * @author koch
 * @date  2013-11-13
 */
@Controller
@RequestMapping(value="back/file")
public class FileController extends BaseController{
	@Resource
	 private FileService fileService;
	
	@RequestMapping(value = { "/upload" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void upload(FileType fileType, MultipartFile file, HttpServletResponse response) {
		if (!this.fileService.isValid(fileType, file)) {
			String name  = file.getOriginalFilename();
			JsonUtil.toJson(response, "text/html; charset=UTF-8", JsonMessage.warn("Common.upload.error"));
		} else {
			String url = this.fileService.upload(fileType, file, false);
			if (url == null) {
				JsonUtil.toJson(response, "text/html; charset=UTF-8", JsonMessage.warn("Common.upload.success"));
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", "success");
			map.put("content", url);
			JsonUtil.toJson(response, "text/html; charset=UTF-8", map);
	    }
	}
	
	@RequestMapping(value = { "/browser" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<FileInfo> browser(String path, FileType fileType, FileInfo.OrderType orderType) {
		return this.fileService.browser(path, fileType, orderType);
	}
}
