package com.koch.job;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.koch.service.CartService;

@Component("cartJob")
@Lazy(false)
public class CartJob {
	@Resource
	private CartService cartService;

	@Scheduled(cron = "${job.cart_evict_expired.cron}")
	public void evictExpired() {
		this.cartService.evictExpired();
	}
}
