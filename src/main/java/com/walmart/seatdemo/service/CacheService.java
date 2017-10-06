package com.walmart.seatdemo.service;

import com.walmart.seatdemo.model.SeatHold;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by chuanwang on 10/4/17.
 */

@Service
@PropertySource("classpath:application.properties")
public class CacheService {

  private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

  private static final String SEAT_HOLD_TIMEOUT = "seat_hold_timeout";

  private Cache seatHoldCache;


  @Autowired
  public CacheService(Environment environment) {

    int seatHoldTimeout = environment.getProperty(SEAT_HOLD_TIMEOUT, Integer.class);

    CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
      .withCache("seatHoldCache",
        CacheConfigurationBuilder
        .newCacheConfigurationBuilder(Integer.class, SeatHold.class, ResourcePoolsBuilder.heap(10))
          .withExpiry(Expirations.timeToLiveExpiration(Duration.of(seatHoldTimeout, TimeUnit.SECONDS)))
        )
        .build();
    cacheManager.init();

    this.seatHoldCache = cacheManager.getCache("seatHoldCache", Integer.class, SeatHold.class);
  }

  public void saveSeatHold(SeatHold seatHold) {
    int id = generateSeatHoldId(seatHold);
    seatHold.setId(id);

    logger.info("Put SeatHold " + id + " : " + seatHold.toString() + " into cache");
    getSeatHoldCache().put(id, seatHold);
  }

  public SeatHold getSeatHold(int id) {
    logger.info("Retrieve SeatHold " + id + " from cache");
    return (SeatHold)getSeatHoldCache().get(id);
  }

  private int generateSeatHoldId(SeatHold seatHold) {
    return new HashCodeBuilder(17, 37)
        .append(seatHold.getHeldBy())
        .append(seatHold.getTimestamp())
        .append(seatHold.getSeats())
        .toHashCode();
  }

  protected Cache getSeatHoldCache() {
    return seatHoldCache;
  }

}
