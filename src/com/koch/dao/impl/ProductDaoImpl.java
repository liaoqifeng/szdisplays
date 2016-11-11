package com.koch.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.stereotype.Repository;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.bean.Setting;
import com.koch.bean.OrderBy.OrderType;
import com.koch.dao.GoodsDao;
import com.koch.dao.ProductDao;
import com.koch.dao.SerialNumberDao;
import com.koch.entity.Brand;
import com.koch.entity.Goods;
import com.koch.entity.Product;
import com.koch.entity.ProductCategory;
import com.koch.entity.Promotion;
import com.koch.entity.Property;
import com.koch.entity.SerialNumber;
import com.koch.entity.SpecAttribute;
import com.koch.entity.Tag;
import com.koch.util.SettingUtils;

@Repository("productDao")
public class ProductDaoImpl extends BaseDaoImpl<Product> implements ProductDao {
	
	@Resource
	private SerialNumberDao serialNumberDao;
	@Resource
	private GoodsDao goodsDao;
	
	public boolean numberExists(String number) {
		if (number == null)
			return false;
		String str = "select count(*) from Product model where lower(model.number) = lower(:number)";
		Long count = (Long) this.entityManager.createQuery(str, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("number", number).getSingleResult();
		return count.longValue() > 0L;
	}
	public Product findByNumber(String number){
	    if (number == null)
	      return null;
	    String str = "from Product as model where lower(model.number) = lower(:number)";
	    return (Product)this.entityManager.createQuery(str, Product.class).setFlushMode(FlushModeType.COMMIT).setParameter("number", number).getSingleResult();
	}
	
	public List<Product> findList(ProductCategory productCategory, Brand brand,
			Promotion promotion, List<Tag> tags,
			Map<Property, String> propertyValue, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isPublish, Boolean isShow,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock,
			Boolean isStockAlert, Product.OrderType orderType, Integer count,
			List<Filter> filters, List<OrderBy> orders) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate predicate = criteriaBuilder.conjunction();
		if (productCategory != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(
					criteriaBuilder.equal(root.get("productCategory"),
							productCategory), criteriaBuilder.like(root.get(
							"productCategory").get("path"), "%,"
							+ productCategory.getId() + "," + "%")));
		}
		if (brand != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("brand"), brand));
		}
		if (promotion != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder
					.or(new Predicate[] {
							criteriaBuilder.equal(root.join("promotions",JoinType.LEFT), promotion),
							criteriaBuilder.equal(root.join("productCategory",JoinType.LEFT).join("promotions",JoinType.LEFT), promotion),
							criteriaBuilder.equal(root.join("brand",JoinType.LEFT).join("promotions",JoinType.LEFT), promotion) }));
		}
		if (propertyValue != null) {
			Iterator itor = propertyValue.entrySet().iterator();
			while (itor.hasNext()) {
				Map.Entry entry = (Map.Entry) itor.next();
				String property = "property"
						+ ((Property) entry.getKey()).getPropertyIndex();
				predicate = criteriaBuilder.and(predicate, criteriaBuilder
						.equal(root.get(property), entry.getValue()));
			}
		}
		if (startPrice != null && endPrice != null
				&& startPrice.compareTo(endPrice) > 0) {
			BigDecimal exchange = new BigDecimal(0);
			exchange = startPrice;
			startPrice = endPrice;
			endPrice = (BigDecimal) exchange;
		}
		if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.ge(root.get("price"), startPrice));
		}
		if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.le(root.get("price"), endPrice));
		}
		if (tags != null && !tags.isEmpty()) {
			predicate = criteriaBuilder.and(predicate, root.join("tags").in(tags));
			criteriaQuery.distinct(true);
		}
		if (isPublish != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isPublish"), isPublish));
		}
		if (isShow != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isShow"), isShow));
		}
		if (isTop != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isTop"), isTop));
		}
		if (isGift != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isGift"), isGift));
		}
		Path stockPath = root.get("stock");
		Path lockStockPath = root.get("lockStock");
		if (isOutOfStock != null) {
			if (isOutOfStock.booleanValue()) {
				predicate = criteriaBuilder.and(new Predicate[] {
						predicate,
						criteriaBuilder.isNotNull((Expression) stockPath),
						criteriaBuilder.lessThanOrEqualTo(
								(Expression) stockPath,
								(Expression) lockStockPath) });
			} else {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(
						criteriaBuilder.isNull((Expression) stockPath),
						criteriaBuilder.greaterThan((Expression) stockPath,
								(Expression) lockStockPath)));
			}
		}
		if (isStockAlert != null) {
			Setting setting = SettingUtils.get();
			if (isStockAlert.booleanValue()) {
				predicate = criteriaBuilder.and(new Predicate[] {
						predicate,
						criteriaBuilder.isNotNull((Expression) stockPath),
						criteriaBuilder.lessThanOrEqualTo(
								(Expression) stockPath, criteriaBuilder.sum(
										(Expression) lockStockPath, setting
												.getStockAlertCount())) });
			} else {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(
						criteriaBuilder.isNull((Expression) stockPath),
						criteriaBuilder.greaterThan((Expression) stockPath,
								criteriaBuilder.sum((Expression) lockStockPath,
										setting.getStockAlertCount()))));
			}
		}
		criteriaQuery.where(predicate);
		if (orderType == Product.OrderType.priceAsc) {
			orders.add(new OrderBy("price", OrderType.asc));
			orders.add(new OrderBy("createDate", OrderType.asc));
		} else if (orderType == Product.OrderType.priceDesc) {
			orders.add(new OrderBy("price", OrderType.desc));
			orders.add(new OrderBy("createDate", OrderType.desc));
		} else if (orderType == Product.OrderType.salesDesc) {
			orders.add(new OrderBy("sales", OrderType.asc));
			orders.add(new OrderBy("createDate", OrderType.asc));
		} else if (orderType == Product.OrderType.scoreDesc) {
			orders.add(new OrderBy("score", OrderType.desc));
			orders.add(new OrderBy("createDate", OrderType.desc));
		} else if (orderType == Product.OrderType.dateDesc) {
			orders.add(new OrderBy("createDate", OrderType.desc));
		} else {
			orders.add(new OrderBy("isTop", OrderType.desc));
			orders.add(new OrderBy("modifyDate", OrderType.desc));
		}
		return super.findList(criteriaQuery, null, count, filters, orders);
	}
	
	public List<Product> findList(Goods goods, Set<Product> excludes) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate predicate = criteriaBuilder.conjunction();
		if (goods != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("goods"), goods));
		}
		if (excludes != null && !excludes.isEmpty()) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.not(root.in(excludes)));
		}
		criteriaQuery.where(predicate);
		return this.entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count) {
		CriteriaBuilder localCriteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery localCriteriaQuery = localCriteriaBuilder
				.createQuery(Product.class);
		Root localRoot = localCriteriaQuery.from(Product.class);
		localCriteriaQuery.select(localRoot);
		Predicate localPredicate = localCriteriaBuilder.conjunction();
		localPredicate = localCriteriaBuilder.and(localPredicate,
				localCriteriaBuilder.equal(localRoot.get("isPublish"), Boolean
						.valueOf(true)));
		if (productCategory != null)
			localPredicate = localCriteriaBuilder.and(localPredicate,
					localCriteriaBuilder.or(localCriteriaBuilder.equal(
							localRoot.get("productCategory"), productCategory),
							localCriteriaBuilder.like(localRoot.get(
									"productCategory").get("treePath"), "%,"
									+ productCategory.getId() + "," + "%")));
		if (beginDate != null)
			localPredicate = localCriteriaBuilder.and(localPredicate,
					localCriteriaBuilder.greaterThanOrEqualTo(localRoot
							.get("createDate"), beginDate));
		if (endDate != null)
			localPredicate = localCriteriaBuilder.and(localPredicate,
					localCriteriaBuilder.lessThanOrEqualTo(localRoot
							.get("createDate"), endDate));
		localCriteriaQuery.where(localPredicate);
		localCriteriaQuery
				.orderBy(new javax.persistence.criteria.Order[] { localCriteriaBuilder
						.desc(localRoot.get("isTop")) });
		return super.findList(localCriteriaQuery, first, count, null, null);
	}
	
	@SuppressWarnings("unchecked")
	public Pager findByPager(ProductCategory productCategory,Brand brand,Boolean isPublish,Boolean isGift,String name,String number,Pager pager) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate predicate = criteriaBuilder.conjunction();
		if(productCategory != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").get("path"), "%" + productCategory.PATH_SEPARATOR + productCategory.getId() + productCategory.PATH_SEPARATOR + "%")));
		if(brand != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("brand"), brand));
		if(isPublish != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isPublish"), isPublish));
		if(isGift != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isGift"), isGift));
		if(StringUtils.isNotEmpty(name))
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("name"), name));
		if(StringUtils.isNotEmpty(number))
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("number"), number));
		criteriaQuery.where(predicate);
		return super.findByPager(criteriaQuery, pager);
	}
	
	public void build(Product product) {
		if (product == null) {
			return;
		}
		if (StringUtils.isEmpty(product.getNumber())) {
			String sn = "";
			do {
				sn = this.serialNumberDao.generate(SerialNumber.Type.product);
			} while (numberExists(sn));
			product.setNumber(sn);
		}
		
		StringBuffer fullName = new StringBuffer(product.getName());
		if (product.getSpecAttributes() != null && !product.getSpecAttributes().isEmpty()) {

			List<SpecAttribute> specAttributes = new ArrayList<SpecAttribute>(product.getSpecAttributes().keySet());
			Collections.sort(specAttributes, new SpecComparator(this));
			fullName.append("[");
			int i = 0;
			Iterator<SpecAttribute> iterator = specAttributes.iterator();
			while (iterator.hasNext()) {
				if (i != 0) {
					fullName.append(" ");
				}
				fullName.append(iterator.next().getName());
				i++;
			}
			fullName.append("]");
		}
		product.setFullName(fullName.toString());
	}
	
	public void delete(Product product) {
		if (product != null){
			Goods goods = product.getGoods();
			if ((goods != null) && (goods.getProducts() != null)){
				goods.getProducts().remove(product);
				if (goods.getProducts().isEmpty()){
					this.entityManager.remove(goods);
				}
			}
	    }
	    super.delete(product);
	}
	
	
	class SpecComparator implements Comparator<SpecAttribute> {
		
		SpecComparator(ProductDaoImpl paramProductDaoImpl) {}

		public int compare(SpecAttribute a1, SpecAttribute a2) {
			return new CompareToBuilder().append(a1.getSpec(), a2.getSpec()).toComparison();
		}
	}
}
