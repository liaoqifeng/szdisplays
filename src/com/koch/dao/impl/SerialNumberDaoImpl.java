package com.koch.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.koch.dao.SerialNumberDao;
import com.koch.entity.SerialNumber;
import com.koch.entity.SerialNumber.Type;

@Repository("serialNumberDao")
public class SerialNumberDaoImpl extends BaseDaoImpl<SerialNumber> implements SerialNumberDao,InitializingBean {
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Value("${sn.product.prefix}")
	private String productPrefix;
	@Value("${sn.product.maxLo}")
	private int productMaxLo;
	@Value("${sn.order.prefix}")
	private String orderPrefix;
	@Value("${sn.order.maxLo}")
	private int orderMaxLo;
	@Value("${sn.payment.prefix}")
	private String paymentPrefix;
	@Value("${sn.payment.maxLo}")
	private int paymentMaxLo;
	@Value("${sn.refunds.prefix}")
	private String refundsPrefix;
	@Value("${sn.refunds.maxLo}")
	private int refundsMaxLo;
	@Value("${sn.deliver.prefix}")
	private String shippingPrefix;
	@Value("${sn.deliver.maxLo}")
	private int shippingMaxLo;
	@Value("${sn.returns.prefix}")
	private String returnsPrefix;
	@Value("${sn.returns.maxLo}")
	private int returnsMaxLo;
	
	private SerialNumberGenerate productGrenerate;
	private SerialNumberGenerate orderGrenerate;
	private SerialNumberGenerate paymentGrenerate;
	private SerialNumberGenerate refundsGrenerate;
	private SerialNumberGenerate shippingGrenerate;
	private SerialNumberGenerate returnsGrenerate;
	
	public void afterPropertiesSet() throws Exception {
		this.productGrenerate = new SerialNumberGenerate(Type.product, productPrefix, productMaxLo);
		this.orderGrenerate = new SerialNumberGenerate(Type.order, orderPrefix, orderMaxLo);
		this.paymentGrenerate = new SerialNumberGenerate(Type.payment, paymentPrefix, paymentMaxLo);
		this.refundsGrenerate = new SerialNumberGenerate(Type.refunds, refundsPrefix, refundsMaxLo);
		this.shippingGrenerate = new SerialNumberGenerate(Type.deliver, shippingPrefix, shippingMaxLo);
		this.returnsGrenerate = new SerialNumberGenerate(Type.returns, returnsPrefix, returnsMaxLo);
	}

	private Integer getLastValue(Type type) {
		String str = "select sn from SerialNumber sn where sn.type = :type";
		SerialNumber serialNumber = this.entityManager.createQuery(str,
				SerialNumber.class).setFlushMode(FlushModeType.COMMIT).setParameter("type", type).setLockMode(
						LockModeType.PESSIMISTIC_WRITE).getSingleResult();
		Integer lastValue = serialNumber.getLastValue();
		serialNumber.setLastValue(lastValue + 1);
		this.entityManager.merge(serialNumber);
		return lastValue;
	}

	public String generate(Type type) {
		Assert.notNull(type);
		if (type == Type.product) {
			return this.productGrenerate.generate();
		}
		if (type == Type.order) {
			return this.orderGrenerate.generate();
		}
		if (type == Type.payment) {
			return this.paymentGrenerate.generate();
		}
		if (type == Type.refunds) {
			return this.refundsGrenerate.generate();
		}
		if (type == Type.deliver) {
			return this.shippingGrenerate.generate();
		}
		if (type == Type.returns) {
			return this.returnsGrenerate.generate();
		}
		return null;
	}
	
	class SerialNumberGenerate {
		private Type type;
		private String prefix;
		private int maxLo;
		private int nextLo;
		private Integer lastValue;
		private Integer nextValue;

		public SerialNumberGenerate(Type type, String prefix, int maxLo) {
			this.type = type;
			if(prefix != null && prefix.indexOf("{") >= 0){
				prefix = (prefix != null ? prefix.replace("{", "").replace("}", "") : "");
				DateFormat format = new SimpleDateFormat(prefix);
				this.prefix = format.format(new Date());
			}else{
				this.prefix = prefix;
			}
			this.maxLo = maxLo;
			this.nextLo = (maxLo + 1);
		}

		public synchronized String generate() {
			if (this.nextLo > this.maxLo) {
				this.nextValue = getLastValue(this.type);
				this.nextLo = (this.nextValue.intValue() == 0 ? 1 : 0);
				this.lastValue = (this.nextValue.intValue() * (this.maxLo + 1));
			}
			return this.prefix + (this.lastValue + this.nextLo++);
		}
	}
}
