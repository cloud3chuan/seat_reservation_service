package com.walmart.seatdemo.controller;

import com.walmart.seatdemo.model.Reservation;
import com.walmart.seatdemo.model.Seat;
import com.walmart.seatdemo.model.SeatHold;
import com.walmart.seatdemo.model.SeatHoldExpireException;
import com.walmart.seatdemo.service.SeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Created by chuanwang on 10/3/17.
 */


@RestController
public class SeatReservationController {

  private static final Logger logger = LoggerFactory.getLogger(SeatReservationController.class);

  @Resource
  private SeatService seatService;

  @GetMapping("/api/seats")
  public List<Seat> retrieveSeats(
      @RequestParam(value="level", required = false) Integer level
  ) {
    if(level != null) {
      logger.info("Get available seats for level " + level);
      return seatService.findSeatsByLevel(level);
    } else {
      logger.info("Get all available seats");
      return seatService.findAllAvailableSeats();
    }
  }

  @GetMapping("/api/holdseats")
  public SeatHold holdSeats(
    @RequestParam(value="numSeats") Integer numSeats,
    @RequestParam(value="minLevel", required = false) Integer minLevel,
    @RequestParam(value="maxLevel", required = false) Integer maxLevel,
    @RequestParam(value="customerEmail") String customerEmail
  ) {
    logger.info("Hold " + numSeats + " seats for minLevel: " + minLevel +
        " maxLevel: " + maxLevel + " customerEmail: " + customerEmail);
    return seatService.holdSeats(numSeats, Optional.of(minLevel), Optional.of(maxLevel), customerEmail);
  }

  @PostMapping("/api/reservations")
  public Reservation reserveSeats (
      @RequestParam(value="seatHoldId") Integer seatHoldId,
      @RequestParam(value="customerEmail") String customerEmail
  ) throws SeatHoldExpireException {
    logger.info("Get reservations for seatHoldId: " + seatHoldId + " customerEmail: " + customerEmail);
    return seatService.reserveSeats(seatHoldId, customerEmail);
  }

  @GetMapping("/api/reservations")
  public List<Reservation> findAllReservations () {
    logger.info("Get all reservations");
    return seatService.findAllReservations();
  }
}
