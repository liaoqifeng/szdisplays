package com.koch.controller.back;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koch.base.BaseController;
import com.koch.bean.CustomerData;
import com.koch.bean.Filter;
import com.koch.bean.JsonMessage;
import com.koch.bean.Message;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.bean.Filter.Operator;
import com.koch.entity.Article;
import com.koch.entity.ArticleCategory;
import com.koch.entity.Brand;
import com.koch.entity.ProductCategory;
import com.koch.entity.Tag;
import com.koch.entity.Tag.Type;
import com.koch.service.ArticleCategoryService;
import com.koch.service.ArticleService;
import com.koch.service.TagService;
import com.koch.util.JsonUtil;
import com.koch.util.GlobalConstant.BooleanType;
/**
 * 文章管理控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/article")
public class ArticleController extends BaseController{
	@Resource
	private ArticleService articleService;
	@Resource
	private ArticleCategoryService articleCategoryService;
	@Resource
	private TagService tagService;
	
    @RequestMapping(value="list")
	public String list(ModelMap model){
    	model.addAttribute("categorys", this.articleCategoryService.findTree());
    	model.addAttribute("bools", BooleanType.values());
		return "/back/content/article_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
   	public String pager(Integer articleCategoryId,String title,Boolean isPublish, Boolean isTop,Pager<Article> pager){
    	ArticleCategory articleCategory = articleCategoryService.get(articleCategoryId);
    	if(articleCategoryId != null){
    		pager.getFilters().add(new Filter("articleCategory", Operator.eq, articleCategory));
    	}
    	if(StringUtils.isNotEmpty(title)){
    		pager.getFilters().add(new Filter("articleTitle", Operator.like, title));
    	}
    	if(isPublish != null){
    		pager.getFilters().add(new Filter("isPublish", Operator.eq, isPublish));
    	}
    	if(isTop != null){
    		pager.getFilters().add(new Filter("isTop", Operator.eq, isTop));
    	}
    	pager = articleService.findByPage(pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		CustomerData data = new CustomerData(pager.getList(), pager.getTotalCount());
			Map<String,String[]> filterMap = new HashMap<String, String[]>();
			filterMap.put("article", new String[]{"id","articleTitle","author","articleCategory","isPublish","isTop","hits","createDate"});
			filterMap.put("articleCategory", new String[]{"name"});
			result = JsonUtil.toJsonIncludeProperties(data, filterMap);
    	}
    	return result;
    }
    
    @RequestMapping(value="view",method={RequestMethod.GET})
    public String view(ModelMap model){
    	model.addAttribute("categorys", this.articleCategoryService.findTree());
        model.addAttribute("tags", this.tagService.getList("type", Type.article, new OrderBy("orderList")));
    	return "/back/content/article_add";
    }
    
    @RequestMapping(value="add",method={RequestMethod.POST})
    public String add(Article article,Integer articleCategoryId, Integer[] tagIds, RedirectAttributes redirectAttributes){
    	article.setPageCount(0);
    	article.setHits(0);
    	article.setIsPublish(article.getIsPublish() != null);
    	article.setIsTop(article.getIsTop() != null);
    	article.setIsRecommend(false);
		article.setArticleCategory(this.articleCategoryService.get(articleCategoryId));
		article.setTags(new HashSet<Tag>(this.tagService.findList(tagIds)));
    	articleService.save(article);
    	setRedirectAttributes(redirectAttributes, "Common.save.success");
    	return "redirect:/back/article/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}",method={RequestMethod.GET})
	public String get(@PathVariable Integer id,ModelMap model){
    	model.addAttribute("categorys", this.articleCategoryService.findTree());
        model.addAttribute("tags", this.tagService.getList("type", Type.article, new OrderBy("orderList")));
    	model.addAttribute("article", articleService.get(id));
    	return "/back/content/article_edit";
    }
    
    @RequestMapping(value="edit",method={RequestMethod.POST})
	public String edit(Article article,Integer articleCategoryId, Integer[] tagIds,RedirectAttributes redirectAttributes){
    	article.setArticleCategory(this.articleCategoryService.get(articleCategoryId));
		article.setTags(new HashSet<Tag>(this.tagService.findList(tagIds)));
		article.setIsPublish(article.getIsPublish() != null);
    	article.setIsTop(article.getIsTop() != null);
    	article.setIsRecommend(false);
		Article old = this.articleService.get(article.getId());
		BeanUtils.copyProperties(article, old, new String[]{"hits","pageCount"});
    	articleService.update(old);
    	setRedirectAttributes(redirectAttributes, "Common.update.success");
    	return "redirect:/back/article/list.shtml";
    }
    
    @RequestMapping(value="delete",method={RequestMethod.POST})
    @ResponseBody
    public JsonMessage delete(Integer [] id){
    	articleService.delete(id);
    	return delete_success;
    }
}
