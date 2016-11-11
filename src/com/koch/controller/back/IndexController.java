package com.koch.controller.back;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koch.base.BaseController;
import com.koch.entity.Article;
import com.koch.entity.Product;
import com.koch.service.ArticleService;
import com.koch.service.ProductService;
import com.koch.service.SearchService;

/**
 * back article controller
 * 
 * @author koch
 * @date 2013-05-17
 */
@Controller
@RequestMapping(value = "back/index")
public class IndexController extends BaseController {

	public enum Type {
		article, product;
	}

	@Resource
	private ArticleService articleService;
	@Resource
	private ProductService productService;
	@Resource
	private SearchService searchService;

	@RequestMapping(value = { "/build" }, method = { RequestMethod.GET })
	public String build(ModelMap model) {
		model.addAttribute("types", Type.values());
		return "/admin/index/build";
	}

	@RequestMapping(value = { "/build" }, method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> build(Type buildType, Boolean isPurge, Integer first, Integer count) {
		long startM = System.currentTimeMillis();
		if ((first == null) || (first.intValue() < 0))
			first = 0;
		if ((count == null) || (count.intValue() <= 0))
			count = 50;
		int i = 0;
		boolean bool = true;
		if (buildType == Type.article) {
			if (first.intValue() == 0 && isPurge != null && isPurge.booleanValue())
				this.searchService.purge(Article.class);
			List<Article> list = this.articleService.findList(null, null, null,first, count);
			Iterator<Article> iterator = list.iterator();
			while (iterator.hasNext()) {
				Article article = iterator.next();
				this.searchService.index(article);
				i++;
			}
			first = Integer.valueOf(first.intValue() + list.size());
			if (list.size() == count.intValue())
				bool = false;
		} else if (buildType == Type.product) {
			if (first.intValue() == 0 && isPurge != null && isPurge.booleanValue())
				this.searchService.purge(Product.class);
			List<Product> list = this.productService.findList(null, null, null,first, count);
			Iterator<Product> iterator = list.iterator();
			while (iterator.hasNext()) {
				Product product = iterator.next();
				this.searchService.index(product);
				i++;
			}
			first = Integer.valueOf(first.intValue() + list.size());
			if (list.size() == count.intValue())
				bool = false;
		}
		long endM = System.currentTimeMillis();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("first", first);
		map.put("buildCount", Integer.valueOf(i));
		map.put("buildTime", Long.valueOf(endM - startM));
		map.put("isCompleted", Boolean.valueOf(bool));
		return map;
	}
}
