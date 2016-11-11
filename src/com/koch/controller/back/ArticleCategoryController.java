package com.koch.controller.back;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koch.base.BaseController;
import com.koch.bean.JsonMessage;
import com.koch.entity.Article;
import com.koch.entity.ArticleCategory;
import com.koch.service.ArticleCategoryService;
import com.koch.util.JsonUtil;
/**
 * 文章分类控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/articleCategory")
public class ArticleCategoryController extends BaseController{
	@Resource
	private ArticleCategoryService articleCategoryService;
	
    @RequestMapping(value="list")
	public String list(){
		return "/back/content/category_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
   	public String pager(Integer parentId){
    	List<ArticleCategory> list = null;
    	if(parentId == null){
    		list = this.articleCategoryService.findRoots();
    	}else{
    		list = articleCategoryService.findChildren(articleCategoryService.get(parentId));
    	}
    	String result = "[]";
    	if(list != null && list.size()>0){
    		String [] propertys = new String[]{"id","name","title","keywords","describtion","orderList","text","state"};
			result = JsonUtil.toJsonIncludeProperties(list,propertys);
    	}
    	return result;
    }
    
    @RequestMapping(value="view",method={RequestMethod.GET})
    public String view(ModelMap model){
    	List<ArticleCategory> categoryList = articleCategoryService.findTree();
    	model.addAttribute("categorys", categoryList);
    	return "/back/content/category_add";
    }
    
    @RequestMapping(value="add",method={RequestMethod.POST})
    public String add(ArticleCategory type,Integer parentId,RedirectAttributes redirectAttributes){
    	type.setParent(this.articleCategoryService.get(parentId));
    	articleCategoryService.save(type);
    	setRedirectAttributes(redirectAttributes, "Common.save.success");
    	return "redirect:/back/articleCategory/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}",method={RequestMethod.GET})
	public String get(@PathVariable Integer id,ModelMap model){
    	ArticleCategory category = articleCategoryService.get(id);
    	List<ArticleCategory> categoryList = articleCategoryService.findTree();
    	model.addAttribute("categorys", categoryList);
    	model.addAttribute("category", category);
    	return "/back/content/category_edit";
    }
    
    @RequestMapping(value="edit",method={RequestMethod.POST})
	public String edit(ArticleCategory type,Integer parentId,RedirectAttributes redirectAttributes){
    	type.setParent(this.articleCategoryService.get(parentId));
    	if (type.getParent() != null) {
    		if(type.getParent().getId().equals(type.getId())){
    			setRedirectAttributes(redirectAttributes, "Common.update.error");
		    	return "redirect:/back/articleCategory/edit/"+type.getId()+".shtml";
    		}
			List<ArticleCategory> articleCategory = this.articleCategoryService.findChildren(type);
			if (articleCategory != null && articleCategory.contains(type.getParent())) {
				setRedirectAttributes(redirectAttributes, "Common.update.error");
		    	return "redirect:/back/articleCategory/edit/"+type.getId()+".shtml";
			}
		}
    	
    	ArticleCategory articleCategory = this.articleCategoryService.get(type.getId());
		BeanUtils.copyProperties(type, articleCategory, new String[] { "path", "level", "children", "articles" });
    	articleCategoryService.update(articleCategory);
    	setRedirectAttributes(redirectAttributes, "Common.update.success");
    	return "redirect:/back/articleCategory/list.shtml";
    }
    
    @RequestMapping(value="delete", method={RequestMethod.POST})
    @ResponseBody
    public JsonMessage delete(Integer id){
    	ArticleCategory articleCategory = this.articleCategoryService.get(id);
        if (articleCategory == null) {
          return delete_error;
        }
        Set<ArticleCategory> childrens = articleCategory.getChildren();
        if (childrens != null && !childrens.isEmpty()) {
          return JsonMessage.error("call.articleCategory.deleteExistChildrenNotAllowed", new Object[0]);
        }
        Set<Article> articles = articleCategory.getArticles();
        if (articles != null && !articles.isEmpty()) {
          return JsonMessage.error("call.articleCategory.deleteExistArticleNotAllowed", new Object[0]);
        }
        this.articleCategoryService.delete(id);
    	return delete_success;
    }
}
