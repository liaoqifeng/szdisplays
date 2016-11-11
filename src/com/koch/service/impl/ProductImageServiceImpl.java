package com.koch.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.util.ImageUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.koch.bean.Setting;
import com.koch.compent.FileStorage;
import com.koch.entity.ProductImage;
import com.koch.service.ProductImageService;
import com.koch.util.ImageUtil;
import com.koch.util.SettingUtils;

@Service
public class ProductImageServiceImpl implements ProductImageService,
		ServletContextAware {

	private ServletContext servletContext;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@Resource(name="taskExecutor")
	private TaskExecutor taskExecutor;
	@Resource
	private List<FileStorage> fileStorage;

	private void execute(String source, String large, String medium, String thumbnail, File file, String contentType) {
		try {
			this.taskExecutor.execute(new ProductImageStorageTsak(file, source, large, medium, thumbnail, contentType));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void build(ProductImage productImage) {
		MultipartFile multipartFile = productImage.getFile();
		if (multipartFile != null && !multipartFile.isEmpty()) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
			String dateString = simpleDateFormat.format(new Date());
			Setting setting = SettingUtils.get();
			String path = setting.getImageUploadPath() + dateString + "/";
			String name = UUID.randomUUID().toString();
			String source = path + name + "-source." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			String large = path + name + "-large." + "jpg";
			String medium = path + name + "-medium." + "jpg";
			String thumbnail = path + name + "-thumbnail." + "jpg";

			Collections.sort(this.fileStorage);
	        Iterator<FileStorage> storages = this.fileStorage.iterator();
	        while(storages.hasNext()){
	        	FileStorage fileStorage = storages.next();
	        	if(fileStorage.getIsEnabled()){
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
	    			
	    			execute(source, large, medium, thumbnail, file,multipartFile.getContentType());
	    			productImage.setSource(fileStorage.getUrl(source));
	    			productImage.setLarge(fileStorage.getUrl(large));
	    			productImage.setMedium(fileStorage.getUrl(medium));
	    			productImage.setThumbnail(fileStorage.getUrl(thumbnail));
	        	}
	        }
			
		}
	}
	
	class ProductImageStorageTsak implements Runnable {
		private File file;
		private String source;
		private String large;
		private String medium;
		private String thumbnail;
		private String contentType;

		public ProductImageStorageTsak(File file, String source, String large,
				String medium, String thumbnail, String contentType) {
			this.file = file;
			this.source = source;
			this.large = large;
			this.medium = medium;
			this.thumbnail = thumbnail;
			this.contentType = contentType;
		}

		public void run() {
			Collections.sort(fileStorage);
			Iterator<FileStorage> storages = fileStorage.iterator();
			while (storages.hasNext()) {
				FileStorage fileStorage = storages.next();
				if (fileStorage.getIsEnabled()) {
					Setting setting = SettingUtils.get();
					String dir = System.getProperty("java.io.tmpdir");
					File watermarkFile = new File(servletContext.getRealPath(setting.getWatermarkImage()));
					File largeFile = new File(dir + "/upload_" + UUID.randomUUID() + "." + "jpg");
					File mediumFile = new File(dir + "/upload_" + UUID.randomUUID() + "." + "jpg");
					File thumbnailFile = new File(dir + "/upload_" + UUID.randomUUID() + "." + "jpg");
					try {
						ImageUtil.zoom(this.file, largeFile, setting.getLargeProductImageWidth(), setting.getLargeProductImageHeight());
						ImageUtil.zoom(this.file, mediumFile, setting.getMediumProductImageWidth(), setting.getMediumProductImageHeight());
						ImageUtil.zoom(this.file, thumbnailFile, setting.getThumbnailProductImageWidth(), setting.getThumbnailProductImageHeight());
						//ImageUtils.addWatermark(localFile2, localFile2,
						// localFile1, localSetting.getWatermarkPosition(),
						// localSetting.getWatermarkAlpha().intValue());
						
						// ImageUtils.addWatermark(localFile3, localFile3,
						// localFile1, localSetting.getWatermarkPosition(),
						// localSetting.getWatermarkAlpha().intValue());
						// ImageUtils.zoom(this.IIIllIll, localFile4,
						// localSetting.getThumbnailProductImageWidth().intValue(),
						// localSetting.getThumbnailProductImageHeight().intValue());
						fileStorage.upload(this.source, this.file,this.contentType);
						fileStorage.upload(this.large, largeFile, "image/jpeg");
						fileStorage.upload(this.medium, mediumFile,"image/jpeg");
						fileStorage.upload(this.thumbnail, thumbnailFile,"image/jpeg");
					} finally {
						FileUtils.deleteQuietly(this.file);
						FileUtils.deleteQuietly(largeFile);
						FileUtils.deleteQuietly(mediumFile);
						FileUtils.deleteQuietly(thumbnailFile);
					}
					break;
				}
			}
		}
	}
}
