package com.koch.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.koch.service.DeliverInfoService;
import com.koch.service.PaymentInfoService;

/**
 * 工具类 - 编号生成
 */

public class SerialNumberUtil {
	
	public static final String PRODUCT_SN_PREFIX = "SN_";// 货号前缀
	
	public static final String ORDER_SN_PREFIX = "";// 订单编号前缀
	public static final long ORDER_SN_FIRST = 100000L;// 订单编号起始数
	public static final long ORDER_SN_STEP = 1L;// 订单编号步长
	
	public static final String PAYMENT_SN_PREFIX = "";// 支付编号前缀
	public static final long PAYMENT_SN_MAX = 100L;// 支付编号最大数
	public static final long PAYMENT_SN_STEP = 1L;// 支付编号步长
	
	public static final String REFUND_SN_PREFIX = "";// 退款编号前缀
	public static final long REFUND_SN_FIRST = 100L;// 退款编号起始数
	public static final long REFUND_SN_STEP = 1L;// 退款编号步长
	
	public static final String DELIVER_SN_PREFIX = "";// 发货编号前缀
	public static final long DELIVER_SN_MAX = 100L;// 发货编号起始数
	public static final long DELIVER_SN_STEP = 1L;// 发货编号步长
	
	public static final String RESHIP_SN_PREFIX = "";// 退货编号前缀
	public static final long RESHIP_SN_FIRST = 100L;// 退货编号起始数
	public static final long RESHIP_SN_STEP = 1L;// 退货编号步长
	
	public static Long lastOrderSnNumber = null;
	public static Long lastPaymentSnNumber = null;
	public static Long lastRefundSnNumber = null;
	public static Long lastDeliverSnNumber = null;
	public static Long lastReshipSnNumber = null;
	public static Long lastTenpayTransactionIdNumber = null;
	
	static {
		if(lastPaymentSnNumber == null){
			PaymentInfoService paymentInfoService = (PaymentInfoService)SpringUtil.getBean("paymentInfoServiceImpl");
			String lastNumber = paymentInfoService.getLastPaymentNumber();
			if(StringUtils.isNotEmpty(lastNumber)){
				lastNumber = StringUtils.removeStartIgnoreCase(lastNumber, PAYMENT_SN_PREFIX);
				String dateNumber = lastNumber.substring(0, 8);
				String maxNumber = lastNumber.replace(dateNumber, "");
				lastPaymentSnNumber = Long.parseLong(maxNumber);
			}else{
				lastPaymentSnNumber = 0L;
			}
		}
		
		if(lastDeliverSnNumber == null){
			DeliverInfoService deliverInfoService = (DeliverInfoService)SpringUtil.getBean("deliverInfoServiceImpl");
			String lastNumber = deliverInfoService.getLastDeliverNumber();
			if(StringUtils.isNotEmpty(lastNumber)){
				lastNumber = StringUtils.removeStartIgnoreCase(lastNumber, DELIVER_SN_PREFIX);
				String dateNumber = lastNumber.substring(0, 8);
				String maxNumber = lastNumber.replace(dateNumber, "");
				lastDeliverSnNumber = Long.parseLong(maxNumber);
			}else{
				lastDeliverSnNumber = 0L;
			}
		}
	}
	private static Long getDateLong(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String date = format.format(new Date());
		Long today = Long.parseLong(date);
		return today;
	}
	/**
	 * 生成货号
	 * 
	 * @return 货号
	 */
	public synchronized static String buildProductSn() {
		String uuid = UUID.randomUUID().toString();
		return PRODUCT_SN_PREFIX + (uuid.substring(0, 8) + uuid.substring(9, 13)).toUpperCase();
	}
	
	/**
	 * 生成订单编号
	 * 
	 * @return 订单编号
	 */
	public synchronized static String buildOrderSn() {
		lastOrderSnNumber += ORDER_SN_STEP;
		return ORDER_SN_PREFIX + lastOrderSnNumber;
	}
	
	/**
	 * 生成支付编号
	 * 
	 * @return 支付编号
	 */
	public synchronized static String buildPaymentSn() {
		Long today = getDateLong();
		if(lastPaymentSnNumber.longValue() >= PAYMENT_SN_MAX)
			lastPaymentSnNumber = 0L;
		lastPaymentSnNumber += PAYMENT_SN_STEP;
		return PAYMENT_SN_PREFIX + today + lastPaymentSnNumber;
	}
	
	/**
	 * 生成退款编号
	 * 
	 * @return 退款编号
	 */
	public synchronized static String buildRefundSn() {
		lastRefundSnNumber += REFUND_SN_STEP;
		return REFUND_SN_PREFIX + lastRefundSnNumber;
	}
	
	/**
	 * 生成发货编号
	 * 
	 * @return 发货编号
	 */
	public synchronized static String buildDeliverSn() {
		Long today = getDateLong();
		if(lastDeliverSnNumber.longValue() >= DELIVER_SN_MAX)
			lastDeliverSnNumber = 0L;
		lastDeliverSnNumber += DELIVER_SN_STEP;
		return DELIVER_SN_PREFIX + today + lastDeliverSnNumber;
	}
	
	/**
	 * 生成退货编号
	 * 
	 * @return 退货编号
	 */
	public synchronized static String buildReshipSn() {
		lastReshipSnNumber += RESHIP_SN_STEP;
		return RESHIP_SN_PREFIX + lastReshipSnNumber;
	}

}