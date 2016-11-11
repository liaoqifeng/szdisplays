package com.koch.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.ClassPathResource;

import com.koch.bean.Auth;
import com.koch.bean.Setting;

public final class AuthUtils {
	
	private static final CacheManager cacheManager = CacheManager.create();
	

	public static List<Auth> get() {
		Ehcache ehcache = cacheManager.getEhcache("auth");
		net.sf.ehcache.Element ehElement = ehcache.get(Auth.CACHE_KEY);
		List<Auth> auths;
		if (ehElement != null) {
			auths = (List<Auth>) ehElement.getObjectValue();
		} else {
			auths = new ArrayList<Auth>();
			try {
				File file = new ClassPathResource(GlobalConstant.AUTH_XML_PATH).getFile();
				Document document = new SAXReader().read(file);
				List<Element> list = document.selectNodes("/auth/authorities");
				Iterator<Element> iterator = list.iterator();
				while (iterator.hasNext()) {
					Element element = (org.dom4j.Element) iterator.next();
					String name = element.attributeValue("name");
					String value = element.attributeValue("value");
					String order = element.attributeValue("order");
					Auth auth = new Auth();
					auth.setName(name);
					auth.setValue(value);
					auth.setOrder(StringUtils.isEmpty(order) ? null : Integer.valueOf(order));
					
					List<Element> nodes = element.selectNodes("node");
					if(nodes != null && nodes.size() > 0){
						for(Element node : nodes){
							name = node.attributeValue("name");
							value = node.attributeValue("value");
							order = node.attributeValue("order");
							String perms = node.attributeValue("perms");
							String url = node.attributeValue("url");
							Auth e = new Auth();
							e.setName(name);
							e.setValue(value);
							e.setOrder(StringUtils.isEmpty(order) ? null : Integer.valueOf(order));
							e.setPerms(perms);
							e.setUrl(url);
							auth.getNodes().add(e);
						}
					}
					
					auths.add(auth);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			ehcache.put(new net.sf.ehcache.Element(Setting.CACHE_KEY, auths));
		}
		return auths;
	}

	public static List<String> getValues(){
		List<String> values = new ArrayList<String>();
		List<Auth> auths =  AuthUtils.get();
		for(Auth auth : auths){
			List<Auth> nodes = auth.getNodes();
			for(Auth node : nodes){
				values.add(node.getValue());
			}
		}
		return values;
	}
}
