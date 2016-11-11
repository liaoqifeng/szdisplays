package com.koch.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.PreparedExpression;
import org.wltea.expression.datameta.Variable;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koch.bean.Setting;
import com.koch.util.SettingUtils;


@Entity
@Table(name="coupon")
@JsonFilter("coupon")
public class Coupon extends BaseEntity{ 
	private static final long serialVersionUID = -2459178525172789403L;
	
	private String name;
	private String prefix;
	private Date startDate;
	private Date endDate;
	private String image;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private Boolean isEnabled;
	private Boolean isExchange;
	private String expression;
	private Integer score;
	private String introduction;
	
	private Set<CouponInfo> couponInfos = new HashSet<CouponInfo>();
	
	@NotEmpty
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Column
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	@Column
	public BigDecimal getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}
	@Column
	public BigDecimal getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}
	@Column
	public Boolean getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	@Column
	public Boolean getIsExchange() {
		return isExchange;
	}
	public void setIsExchange(Boolean isExchange) {
		this.isExchange = isExchange;
	}
	@NotEmpty
	@Column
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	@Column
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	@Column
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	@JsonIgnore
	@OneToMany(mappedBy="coupon",fetch=FetchType.LAZY,cascade = { CascadeType.ALL }, orphanRemoval = true)
	public Set<CouponInfo> getCouponInfos() {
		return couponInfos;
	}
	public void setCouponInfos(Set<CouponInfo> couponInfos) {
		this.couponInfos = couponInfos;
	}
	
	@Transient
	public boolean hasBegun() {
		return getStartDate() == null || new Date().after(getStartDate());
	}

	@Transient
	public boolean hasExpired() {
		return getEndDate() != null && new Date().after(getEndDate());
	}

	@Transient
	public BigDecimal calculatePrice(BigDecimal price,Integer quantity) {
		if(StringUtils.isNotEmpty(getExpression())){
			List<Variable> variables = new ArrayList<Variable>();
			if(getExpression().indexOf("价格") >= 0){
				variables.add(Variable.createVariable("价格", price.doubleValue()));
			}
			if(getExpression().indexOf("数量") >= 0){
				variables.add(Variable.createVariable("数量", quantity));
			}
			PreparedExpression pe = ExpressionEvaluator.preparedCompile(getExpression(), variables);
			Object result = pe.execute();
			if(result != null){
				price = new BigDecimal(result.toString());
			}
		}
		return price;
	}
	
	public static void main(String[] args){
		if(args.length == 0){
		args = new String[1];
		args[0] = "IK Expression V2.0.5";
		}
		//定义表达式
		String expression = "\"Hello \" + 版本";
		//给表达式中的变量 [版本] 付上下文的值
		List<Variable> variables = new ArrayList<Variable>();
		variables.add(Variable.createVariable("版本", args[0]));
		//预编译表达式
		PreparedExpression pe = ExpressionEvaluator.preparedCompile(expression, variables);
		//执行表达式
		Object result = pe.execute();
		System.out.println("Result = " + result);
		//更改参数，再次执行预编译式
		pe.setArgument("版本", "IK Expression V2.1.0");
		result = pe.execute();
		System.out.println("Result = " + result);
		}
}
