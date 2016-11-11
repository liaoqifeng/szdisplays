package com.koch.job;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.koch.service.OrderService;

@Component("orderJob")
@Lazy(false)
public class OrderJob {
  @Resource
  private OrderService orderService;
  
  @Scheduled(cron="${job.order_release_stock.cron}")
  public void releaseStock()
  {
    this.orderService.releaseStock();
  }
}
