package com.koch.controller.back;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koch.base.BaseController;
import com.koch.bean.CustomerData;
import com.koch.bean.JsonMessage;
import com.koch.bean.Message;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.bean.Setting;
import com.koch.bean.FileInfo.FileType;
import com.koch.entity.Brand;
import com.koch.entity.Goods;
import com.koch.entity.Grade;
import com.koch.entity.Parameter;
import com.koch.entity.ParameterItems;
import com.koch.entity.Product;
import com.koch.entity.ProductCategory;
import com.koch.entity.ProductImage;
import com.koch.entity.Property;
import com.koch.entity.Spec;
import com.koch.entity.SpecAttribute;
import com.koch.entity.Tag;
import com.koch.entity.Spec.SpecType;
import com.koch.entity.Tag.Type;
import com.koch.service.BrandService;
import com.koch.service.FileService;
import com.koch.service.GoodsService;
import com.koch.service.GradeService;
import com.koch.service.ProductCategoryService;
import com.koch.service.ProductImageService;
import com.koch.service.ProductService;
import com.koch.service.SpecAttributeService;
import com.koch.service.SpecService;
import com.koch.service.TagService;
import com.koch.util.JsonUtil;
import com.koch.util.SettingUtils;
/**
 * 商品控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back/product")
public class ProductController extends BaseController{
	
	private static Logger logger = Logger.getLogger(ProductController.class);
	
	@Resource
	private SpecService specService;
	@Resource
	private SpecAttributeService specAttributeService;
	@Resource
	private GradeService gradeService;
	@Resource
	private ProductService productService;
	@Resource
	private GoodsService goodsService;
	@Resource
	private ProductCategoryService productCategoryService;
	@Resource
	private BrandService brandService;
	@Resource
	private FileService fileService;
	@Resource
	private ProductImageService productImageService;
	@Resource
	private TagService tagService;
	
    @RequestMapping(value="list",method={RequestMethod.GET})
	public String list(ModelMap model){
    	List<Brand> brandList = brandService.getAll();
    	List<ProductCategory> categoryList = productCategoryService.findTree();
    	model.addAttribute("brands",brandList);
    	model.addAttribute("productCategorys", categoryList);
		return "/back/product/product_list";
    }
    
    @RequestMapping(value="list/pager")
    @ResponseBody
	public String pager(Integer productCategoryId,Integer brandId,Boolean isPublish,Boolean isGift,String name,String number,HttpServletResponse response,Pager pager){
    	ProductCategory productCategory = productCategoryService.get(productCategoryId);
    	Brand brand = brandService.get(brandId);
    	pager = productService.findByPager(productCategory, brand, isPublish, isGift,name, number, pager);
    	String result = "[]";
    	if(pager.getList() != null && pager.getList().size()>0){
    		try {
    			CustomerData data = new CustomerData(pager.getList(), pager.getTotalCount());
    			Map<String,String[]> filterMap = new HashMap<String, String[]>();
    			filterMap.put("product", new String[]{"id","number","name","productCategory","brand","stock","salePrice","costPrice","marketPrice","isPublish"});
    			filterMap.put("productCategory", new String[]{"name"});
    			filterMap.put("brand", new String[]{"name"});
    			result = JsonUtil.toJsonIncludeProperties(data, filterMap);
    		} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return result;
    }
    
	@RequestMapping(value="view",method={RequestMethod.GET})
    public String view(Product product, ModelMap model){
    	List<Spec> specList = specService.getAll();
    	List<Grade> gradeList = gradeService.getAll(new OrderBy("orderList"));
    	List<Brand> brandList = brandService.getAll();
    	List<ProductCategory> categoryList = productCategoryService.findTree();
    	List<Tag> tagList = tagService.getList("type", Type.product, new OrderBy("orderList"));
    	model.addAttribute("grades", gradeList);
    	model.addAttribute("specs", specList);
    	model.addAttribute("brands",brandList);
    	model.addAttribute("productCategorys", categoryList);
    	model.addAttribute("tags", tagList);
    	return "/back/product/product_add";
    }
    
    @RequestMapping(value="view/check")
    @ResponseBody
    public boolean check(String number,String oldNumber){
    	if(StringUtils.isEmpty(number))
    		return true;
    	if(StringUtils.equalsIgnoreCase(number, oldNumber))
    		return true;
    	return !productService.numberExists(number);
    }
    
	@RequestMapping(value="add",method={RequestMethod.POST})
	public String add(HttpServletRequest request,Product product,Integer [] specificationIds,Integer [] tagIds,RedirectAttributes redirectAttributes){
		if(product == null){
			setRedirectAttributes(redirectAttributes, "Common.save.error");
			return "redirect:/back/product/view.shtml";
		}
		
		Iterator<ProductImage> productImages = product.getProductImages().iterator();
		while(productImages.hasNext()){
			ProductImage productImage = productImages.next();
			if(productImage == null || productImage.isEmpty()){
				productImages.remove();
			}else if (productImage.getFile() != null && !productImage.getFile().isEmpty() && !this.fileService.isValid(FileType.image, productImage.getFile())){
				setRedirectAttributes(redirectAttributes, "Common.save.error");
				return "redirect:/back/product/view.shtml";
			}
		}
		
		ProductCategory productCategory = productCategoryService.get(product.getProductCategory().getId());
		product.setBrand(brandService.get(product.getBrand().getId()));
		product.setProductCategory(productCategory);
		product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
    	product.setIsTop(product.getIsTop()!=null);
    	product.setIsShow(product.getIsShow()!=null);
    	product.setIsGift(product.getIsGift()!=null);
    	product.setIsVipPrice(product.getIsVipPrice() != null);
    	product.setIsPublish(product.getIsPublish() != null);
    	
    	if(product.getMarketPrice() == null){
    		BigDecimal price = convertMarketPrice(product.getSalePrice());
    		product.setMarketPrice(price);
    	}
    	if(product.getScore() == null){
    		Integer score = convertPoint(product.getSalePrice());
    		product.setScore(score);
    	}
    	product.setLockStock(0);
    	product.setSalesCount(0);
    	product.setWeekSalesCount(0);
    	product.setMonthSalesCount(0);
    	product.setWeekSalesDate(new Date());
    	product.setMonthSalesDate(new Date());
    	
    	productImages = product.getProductImages().iterator();
    	while(productImages.hasNext()){
    		ProductImage productImage = productImages.next();
    		this.productImageService.build(productImage);
    	}
    	
    	Iterator<Grade> grades = gradeService.getAll().iterator();
    	while(grades.hasNext()){
    		Grade grade = grades.next();
    		String price = request.getParameter("memberPrice_"+grade.getId());
    		if(StringUtils.isNotEmpty(price)){
    			product.getMemberPrice().put(grade, new BigDecimal(price));
    		}else{
    			product.getMemberPrice().remove(grade);
    		}
    	}
    	
    	if(product.getProductCategory() != null && product.getProductCategory().getParameterSet().size() > 0){
    		Iterator<Parameter> parameters = product.getProductCategory().getParameterSet().iterator();
    		while(parameters.hasNext()){
    			Parameter param = parameters.next();
    			Iterator<ParameterItems> paramItems = param.getParameterItems().iterator();
    			while(paramItems.hasNext()){
    				ParameterItems item = paramItems.next();
    				String parameter = request.getParameter("parameter_"+item.getId());
    				if(StringUtils.isNotEmpty(parameter)){
    					product.getParameterItems().put(item.getId(), parameter);
    				}else{
    					product.getParameterItems().remove(item.getId());
    				}
    			}
    		}
    	}
		
    	if(product.getProductCategory() != null && product.getProductCategory().getPropertySet().size() > 0){
			Iterator<Property> propertys = product.getProductCategory().getPropertySet().iterator();
			while(propertys.hasNext()){
				Property item = propertys.next();
				String property = request.getParameter("property_"+item.getId());
				if(StringUtils.isNotEmpty(property)){
					product.setPropertyValue(item, property);
				}else{
					product.setPropertyValue(item, null);
				}
			}
		}
		
		Goods goods = new Goods();
		List<Product> products = new ArrayList<Product>();
		if(specificationIds != null && specificationIds.length > 0){
			for(int i=0;i<specificationIds.length;i++){
				Spec spec = this.specService.get(specificationIds[i]);
				String [] specAttributeIds = request.getParameterValues("specification_"+spec.getId());
				String [] attributeValues = request.getParameterValues("attribute_value_"+spec.getId());
				if(specAttributeIds == null || specAttributeIds.length <= 0)
					continue;
				for(int j=0;j<specAttributeIds.length;j++){
					if(i == 0){
						if(j == 0){
							product.setGoods(goods);
							product.setSpecs(new HashSet<Spec>());
							product.setSpecAttributes(new HashMap<SpecAttribute, String>());
							products.add(product);
						}else{
							Product localProduct = new Product();
				            BeanUtils.copyProperties(product, localProduct);
				            localProduct.setId(null);
				            localProduct.setNumber("");
				            localProduct.setCreateDate(null);
				            localProduct.setModifyDate(null);
				            localProduct.setLockStock(0);
				            localProduct.setSalesCount(0);
				            localProduct.setWeekSalesCount(0);
				            localProduct.setMonthSalesCount(0);
				            localProduct.setWeekSalesDate(new Date());
				            localProduct.setMonthSalesDate(new Date());
				            localProduct.setGoods(goods);
				            localProduct.setScore(0);
				            localProduct.setSpecAttributes(new HashMap<SpecAttribute, String>());
				            localProduct.setSpecs(new HashSet<Spec>());
				            products.add(localProduct);
						}
					}
					Product localProduct = products.get(j);
					SpecAttribute specAttribute = this.specAttributeService.get(Integer.valueOf(specAttributeIds[j]));
					localProduct.getSpecs().add(spec);
					String attributeValue = "";
					if(spec.getSpecType() == SpecType.image){
						attributeValue = StringUtils.isNotEmpty(attributeValues[j]) ? attributeValues[j] : "";
					}
					localProduct.getSpecAttributes().put(specAttribute, attributeValue);
				}
			}
		}else{
			product.setGoods(goods);
			product.setSpecs(null);
			product.setSpecAttributes(null);
			products.add(product);
		}
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		this.goodsService.save(goods);
    	
		setRedirectAttributes(redirectAttributes, "Common.save.success");
		return "redirect:/back/product/list.shtml";
    }
    
    @RequestMapping(value="edit/{id}",method={RequestMethod.GET})
	public String get(@PathVariable Integer id, ModelMap model){
    	if(id == null){
    		return "/back/product/list";
    	}
    	Product product = productService.get(id);
    	if(product == null){
    		return "/back/product/list";
    	}
    	
    	List<Spec> specList = specService.getAll();
    	List<Grade> gradeList = gradeService.getAll(new OrderBy("orderList"));
    	List<Brand> brandList = brandService.getAll();
    	List<ProductCategory> categoryList = productCategoryService.findTree();
    	List<Tag> tagList = tagService.getList("type", Type.product, new OrderBy("orderList"));
    	
    	String [] includeProperty = new String[20];
    	for(int i=0;i<20;i++){
    		includeProperty[i] = "property"+i;
    	}
    	model.addAttribute("propertys", JsonUtil.toJsonIncludeProperties(product, includeProperty));
    	model.addAttribute("grades", gradeList);
    	model.addAttribute("specs", specList);
    	model.addAttribute("brands",brandList);
    	model.addAttribute("productCategorys", categoryList);
    	model.addAttribute("tags", tagList);
    	model.addAttribute("product", product);
    	return "/back/product/product_edit";
    }
    
	@RequestMapping(value="edit",method={RequestMethod.POST})
    public String edit(HttpServletRequest request,Product product,Integer [] specificationIds,Integer [] specificationProductIds,Integer [] tagIds,RedirectAttributes redirectAttributes){
		if(product == null){
			setRedirectAttributes(redirectAttributes, "Common.update.error");
			return "redirect:/back/product/list.shtml";
		}
		
		Iterator<ProductImage> productImages = product.getProductImages().iterator();
		while(productImages.hasNext()){
			ProductImage productImage = productImages.next();
			if(productImage == null || productImage.isEmpty()){
				productImages.remove();
			}else if (productImage.getFile() != null && !productImage.getFile().isEmpty() && !this.fileService.isValid(FileType.image, productImage.getFile())){
				setRedirectAttributes(redirectAttributes, "Common.update.error");
				return "redirect:/back/product/edit/"+product.getId()+".shtml";
			}
		}
		
		ProductCategory productCategory = productCategoryService.get(product.getProductCategory().getId());
		product.setBrand(brandService.get(product.getBrand().getId()));
		product.setProductCategory(productCategory);
		product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
    	product.setIsTop(product.getIsTop()!=null);
    	product.setIsShow(product.getIsShow()!=null);
    	product.setIsGift(product.getIsGift()!=null);
    	product.setIsVipPrice(product.getIsVipPrice() != null);
    	product.setIsPublish(product.getIsPublish() != null);
    	
    	if(StringUtils.isEmpty(product.getNumber())){
    		setRedirectAttributes(redirectAttributes, "Common.update.error");
			return "redirect:/back/product/edit/"+product.getId()+".shtml";
    	}
    	Product origProduct = productService.get(product.getId());
    	if(origProduct == null){
    		setRedirectAttributes(redirectAttributes, "Common.update.error");
			return "redirect:/back/product/edit/"+product.getId()+".shtml";
    	}
    	if(product.getMarketPrice() == null){
    		BigDecimal price = convertMarketPrice(product.getSalePrice());
    		product.setMarketPrice(price);
    	}
    	if(product.getScore() == null){
    		Integer score = convertPoint(product.getSalePrice());
    		product.setScore(score);
    	}
    	
    	productImages = product.getProductImages().iterator();
    	while(productImages.hasNext()){
    		ProductImage productImage = productImages.next();
    		this.productImageService.build(productImage);
    	}
    	
    	Iterator<Grade> grades = gradeService.getAll().iterator();
    	while(grades.hasNext()){
    		Grade grade = grades.next();
    		String price = request.getParameter("memberPrice_"+grade.getId());
    		if(StringUtils.isNotEmpty(price)){
    			product.getMemberPrice().put(grade, new BigDecimal(price));
    		}else{
    			product.getMemberPrice().remove(grade);
    		}
    	}
    	
    	if(product.getProductCategory() != null &&  product.getProductCategory().getParameterSet().size() > 0){
    		Iterator<Parameter> parameters = product.getProductCategory().getParameterSet().iterator();
    		while(parameters.hasNext()){
    			Parameter param = parameters.next();
    			Iterator<ParameterItems> paramItems = param.getParameterItems().iterator();
    			while(paramItems.hasNext()){
    				ParameterItems item = paramItems.next();
    				String parameter = request.getParameter("parameter_"+item.getId());
    				if(StringUtils.isNotEmpty(parameter)){
    					product.getParameterItems().put(item.getId(), parameter);
    				}else{
    					product.getParameterItems().remove(item.getId());
    				}
    			}
    		}
    	}
		
    	if(product.getProductCategory() != null && product.getProductCategory().getPropertySet().size() > 0){
    		Iterator<Property> propertys = product.getProductCategory().getPropertySet().iterator();
    		while(propertys.hasNext()){
    			Property item = propertys.next();
    			String property = request.getParameter("property_"+item.getId());
    			if(StringUtils.isNotEmpty(property)){
    				product.setPropertyValue(item, property);
    			}else{
    				product.setPropertyValue(item, null);
    			}
    		}
    	}
		
		Goods goods = origProduct.getGoods();
		List<Product> products = new ArrayList<Product>();
		if(specificationIds != null && specificationIds.length > 0){
			for(int i=0;i<specificationIds.length;i++){
				Spec spec = this.specService.get(specificationIds[i]);
				String [] specAttributeIds = request.getParameterValues("specification_"+spec.getId());
				String [] attributeValues = request.getParameterValues("attribute_value_"+spec.getId());
				if(specAttributeIds == null || specAttributeIds.length <= 0)
					continue;
				for(int j=0;j<specAttributeIds.length;j++){
					if(i == 0){
						if(j == 0){
							String [] ignore = new String[]{"id","createDate","modifyDate","score","lockStock","salesCount","weekSalesCount","monthSalesCount","weekSalesDate","monthSalesDate","specs","specAttributes","goods","promotions","cartItems","giftItems","members","orderItems"};
							BeanUtils.copyProperties(product, origProduct, ignore);
							origProduct.setSpecs(new HashSet<Spec>());
							origProduct.setSpecAttributes(new HashMap<SpecAttribute, String>());
							products.add(origProduct);
						}else if(specificationProductIds != null && j < specificationProductIds.length){
							Product specProduct = productService.get(specificationProductIds[j]);
				              if (specProduct.getGoods() != goods){
				            	  setRedirectAttributes(redirectAttributes, "Common.update.error");
				            	  return "redirect:/back/product/edit/"+product.getId()+".shtml";
				              }
				              specProduct.setSpecs(new HashSet<Spec>());
				              specProduct.setSpecAttributes(new HashMap<SpecAttribute, String>());
				              products.add(specProduct);
						}else{
							Product localProduct = new Product();
				            BeanUtils.copyProperties(product, localProduct);
				            localProduct.setId(null);
				            localProduct.setCreateDate(null);
				            localProduct.setModifyDate(null);
				            localProduct.setGoods(goods);
				            localProduct.setScore(0);
				            localProduct.setSpecAttributes(new HashMap<SpecAttribute, String>());
				            localProduct.setSpecs(new HashSet<Spec>());
				            products.add(localProduct);
						}
					}
					Product localProduct = products.get(j);
					SpecAttribute specAttribute = this.specAttributeService.get(Integer.valueOf(specAttributeIds[j]));
					localProduct.getSpecs().add(spec);
					String attributeValue = "";
					if(spec.getSpecType() == SpecType.image){
						attributeValue = StringUtils.isNotEmpty(attributeValues[j]) ? attributeValues[j] : "";
					}
					localProduct.getSpecAttributes().put(specAttribute, attributeValue);
				}
			}
		}else{
			product.setSpecs(null);
			product.setSpecAttributes(null);
			String [] ignore = new String[]{"id","createDate","modifyDate","score","lockStock","salesCount","weekSalesCount","monthSalesCount","weekSalesDate","monthSalesDate","goods","promotions","cartItems","giftItems","members","orderItems"};
			BeanUtils.copyProperties(product, origProduct, ignore);
			products.add(origProduct);
		}
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		this.goodsService.update(goods);
    	
		setRedirectAttributes(redirectAttributes, "Common.update.success");
		return "redirect:/back/product/list.shtml";
    }
    
    @RequestMapping(value="delete",method={RequestMethod.POST})
    @ResponseBody
    public JsonMessage delete(Integer [] id){
    	productService.delete(id);
    	return delete_success;
    }
    
	private BigDecimal convertMarketPrice(BigDecimal price) {
		Setting setting = SettingUtils.get();
		Double scale = setting.getDefaultMarketPriceScale();
		return setting.setScale(price.multiply(new BigDecimal(scale.toString())));
	}

	private Integer convertPoint(BigDecimal price) {
		Setting setting = SettingUtils.get();
		Double scale = setting.getDefaultPointScale();
		return price.multiply(new BigDecimal(scale.toString())).intValue();
	}
}
