package com.koch.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.koch.bean.FileInfo;
import com.koch.bean.Setting;
import com.koch.bean.FileInfo.FileType;
import com.koch.bean.FileInfo.OrderType;
import com.koch.compent.FileStorage;
import com.koch.service.FileService;
import com.koch.util.SettingUtils;

@Service
public class FileServiceImpl implements FileService,ServletContextAware {
	private ServletContext servletContext;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@Resource
	private TaskExecutor taskExecutor;
	@Resource
	private List<FileStorage> fileStorage;

	public List<FileInfo> browser(String path, FileType fileType, OrderType orderType) {
		if (path != null) {
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			if (!path.endsWith("/")) {
				path = path + "/";
			}
		} else {
			path = "/";
		}
		Setting setting = SettingUtils.get();
		String uploadPath = "";
		if (fileType == FileInfo.FileType.flash) {
			uploadPath = setting.getFlashUploadPath();
		} else if (fileType == FileInfo.FileType.media) {
			uploadPath = setting.getMediaUploadPath();
		} else if (fileType == FileInfo.FileType.file) {
			uploadPath = setting.getFileUploadPath();
		} else {
			uploadPath = setting.getImageUploadPath();
		}
		String url = StringUtils.substringBefore(uploadPath, "${");
		url = StringUtils.substringBeforeLast(url, "/") + path;
		List<FileInfo> list = new ArrayList<FileInfo>();
		if (url.indexOf("..") >= 0) {
			return list;
		}
		
		Iterator<FileStorage> fileStorages = fileStorage.iterator();
	    if (fileStorages.hasNext()){
	    	FileStorage fileStorage = fileStorages.next();
	    	list = fileStorage.browser(url);
	    }
	    if (orderType == FileInfo.OrderType.size) {
	      Collections.sort(list, new SizeComparator());
	    } else if (orderType == FileInfo.OrderType.type) {
	      Collections.sort(list, new TypeComparator());
	    } else {
	      Collections.sort(list, new NameComparator());
	    }
		return list;
	}

	public boolean isValid(FileType fileType, MultipartFile multipartFile) {
		if (multipartFile == null) {
			return false;
		}
		Setting setting = SettingUtils.get();
		if ((setting.getUploadMaxSize() != null)
				&& (setting.getUploadMaxSize().intValue() != 0)
				&& (multipartFile.getSize() > setting.getUploadMaxSize()
						.intValue() * 1024L * 1024L)) {
			return false;
		}
		String[] array = null;
		if (fileType == FileInfo.FileType.flash) {
			array = setting.getUploadFlashExtensions();
		} else if (fileType == FileInfo.FileType.media) {
			array = setting.getUploadMediaExtensions();
		} else if (fileType == FileInfo.FileType.file) {
			array = setting.getUploadFileExtensions();
		} else {
			array = setting.getUploadImageExtensions();
		}
		if (ArrayUtils.isNotEmpty(array)) {
			return FilenameUtils.isExtension(multipartFile.getOriginalFilename(), array);
		}
		return false;
	}
	
	private void execute(File file,FileStorage fileStorage, String source, String contentType) {
		try {
			this.taskExecutor.execute(new FileStorageTsak(file, fileStorage, source, contentType));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String upload(FileType fileType, MultipartFile multipartFile, boolean async) {
		if (multipartFile == null) {
			return null;
		}
		Setting setting = SettingUtils.get();
		String path = "";
		if (fileType == FileType.flash) {
			path = setting.getFlashUploadPath();
		} else if (fileType == FileType.media) {
			path = setting.getMediaUploadPath();
		} else if (fileType == FileType.file) {
			path = setting.getFileUploadPath();
		} else {
			path = setting.getImageUploadPath();
		}
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
			String dateString = simpleDateFormat.format(new Date());
			path +=  dateString + "/";
			String name = UUID.randomUUID().toString();
			String source = path + name + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			Collections.sort(this.fileStorage);
			Iterator<FileStorage> storages = this.fileStorage.iterator();
			if (storages.hasNext()) {
				FileStorage fileStorage = storages.next();
				File file = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				try {
					multipartFile.transferTo(file);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(async){
					execute(file, fileStorage, source, multipartFile.getContentType());
				}else{
					fileStorage.upload(source, file, multipartFile.getContentType());
				}
				return fileStorage.getUrl(source);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String upload(FileType fileType, MultipartFile multipartFile) {
		return upload(fileType, multipartFile, false);
	}

	public String uploadLocal(FileType fileType, MultipartFile multipartFile) {
		if (multipartFile == null) {
			return null;
		}
		Setting setting = SettingUtils.get();
		String path = "";
		if (fileType == FileType.flash) {
			path = setting.getFlashUploadPath();
		} else if (fileType == FileType.media) {
			path = setting.getMediaUploadPath();
		} else if (fileType == FileType.file) {
			path = setting.getFileUploadPath();
		} else {
			path = setting.getImageUploadPath();
		}
		    try
		    {
		    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
				String dateString = simpleDateFormat.format(new Date());
				path +=  dateString + "/";
				String name = UUID.randomUUID().toString();
				String source = path + name + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
				File file = new File(this.servletContext.getRealPath(source));
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				try {
					multipartFile.transferTo(file);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return source;
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		    }
		    return null;
	}

	
	class FileStorageTsak implements Runnable {
		private File file;
		private String source;
		private FileStorage fileStorage;
		private String contentType;

		public FileStorageTsak(File file, FileStorage fileStorage,String source,String contentType) {
			this.file = file;
			this.source = source;
			this.fileStorage = fileStorage;
			this.contentType = contentType;
		}

		public void run() {
			try {
				fileStorage.upload(this.source, this.file,this.contentType);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				FileUtils.deleteQuietly(this.file);
			}
		}
	}
	
	class SizeComparator implements Comparator<FileInfo> {
		private SizeComparator() {
		}

		public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
			return new CompareToBuilder().append(
					!fileInfos1.getIsDirectory().booleanValue(),
					!fileInfos2.getIsDirectory().booleanValue()).append(
					fileInfos1.getSize(), fileInfos2.getSize()).toComparison();
		}
	}
	
	class NameComparator implements Comparator<FileInfo> {
		private NameComparator() {}

		public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
			return new CompareToBuilder().append(
					!fileInfos1.getIsDirectory().booleanValue(),
					!fileInfos2.getIsDirectory().booleanValue()).append(
					fileInfos1.getName(), fileInfos2.getName()).toComparison();
		}
	}
	
	class TypeComparator implements Comparator<FileInfo> {
		private TypeComparator() {}

		public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
			return new CompareToBuilder().append(
					!fileInfos1.getIsDirectory().booleanValue(),
					!fileInfos2.getIsDirectory().booleanValue()).append(
					FilenameUtils.getExtension(fileInfos1.getName()),
					FilenameUtils.getExtension(fileInfos2.getName()))
					.toComparison();
		}
	}
}
