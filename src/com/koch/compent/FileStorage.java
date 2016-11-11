package com.koch.compent;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.stereotype.Component;

import com.koch.bean.FileInfo;

public abstract class FileStorage implements Comparable<FileStorage>{
	
	public final String getId() {
		return ((Component) getClass().getAnnotation(Component.class)).value();
	}

	public abstract String getName();

	public abstract String getVersion();

	public abstract String getAuthor();

	public abstract String getSiteUrl();

	public abstract String getInstallUrl();

	public abstract String getUninstallUrl();

	public abstract String getSettingUrl();

	public boolean getIsEnabled() {
		return true;
	}

	public String getAttribute(String name) {
		return "";
	}

	public Integer getOrderList() {
		return 0;
	}

	public abstract void upload(String path, File file, String contentType);

	public abstract String getUrl(String path);

	public abstract List<FileInfo> browser(String path);

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		FileStorage fileStorage = (FileStorage) obj;
		return new EqualsBuilder().append(getId(), fileStorage.getId()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	public int compareTo(FileStorage fileStorage) {
		return new CompareToBuilder()
				.append(getOrderList(), fileStorage.getOrderList()).append(getId(),
						fileStorage.getId()).toComparison();
	}
}
