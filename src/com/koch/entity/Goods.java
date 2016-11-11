package com.koch.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "goods")
public class Goods extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 2423211523438491001L;
	
	private Set<Product> products = new HashSet<Product>();

	@OneToMany(mappedBy = "goods", fetch = FetchType.EAGER, cascade = { javax.persistence.CascadeType.ALL }, orphanRemoval = true)
	public Set<Product> getProducts() {
		return this.products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	@Transient
	public Map<SpecAttribute, String> getSpecAttributes() {
		Map<SpecAttribute, String> map = new HashMap<SpecAttribute, String>();
		if (getProducts() != null) {
			Iterator<Product> iterator = getProducts().iterator();
			while (iterator.hasNext()) {
				Product product = iterator.next();
				map.putAll(product.getSpecAttributes());
			}
		}
		return map;
	}
}
