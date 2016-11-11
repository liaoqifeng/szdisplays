package com.koch.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.koch.bean.FileInfo;
import com.koch.bean.FileInfo.FileType;
import com.koch.bean.FileInfo.OrderType;

public interface FileService {
	
	public boolean isValid(FileType fileType,MultipartFile multipartFile);

	public String upload(FileType fileType,MultipartFile multipartFile, boolean paramBoolean);

	public String upload(FileType fileType, MultipartFile multipartFile);

	public String uploadLocal(FileType fileType,MultipartFile multipartFile);

	public List<FileInfo> browser(String paramString,FileType fileType, OrderType orderType);
}
