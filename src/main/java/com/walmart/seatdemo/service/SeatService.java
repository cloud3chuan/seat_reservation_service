package com.walmart.seatdemo.service;

import com.walmart.seatdemo.model.Reservation;
import com.walmart.seatdemo.model.Seat;
import com.walmart.seatdemo.model.SeatHold;
import com.walmart.seatdemo.model.SeatHoldExpireException;
import com.walmart.seatdemo.repository.ReservationRepository;
import com.walmart.seatdemo.repository.SeatRepository;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chuanwang on 10/3/17.
 */
@Service
public class SeatService {

  private static final String SEAT_MIN_LEVEL = "seat_min_level";

  private static final String SEAT_MAX_LEVEL = "seat_max_level";

  private static final Logger logger = LoggerFactory.getLogger(SeatService.class);

  @Resource
  public Environment environment;

  @Resource
  private SeatRepository seatRepository;

  @Resource
  private ReservationRepository reservationRepository;

  @Resource
  private CacheService cacheService;

  @Transactional(readOnly = true)
  public List<Seat> findAllAvailableSeats() {
    List<Seat> seats = seatRepository.findAllSeats();
    return filterHeldSeats(seats);
  }

  @Transactional(readOnly = true)
  public List<Seat> findSeatsByLevel(int level) {
    // get all un-reserved seats first
    List<Seat> seats = seatRepository.findSeatsByLevel(level);

    // filter out all held seats
    return filterHeldSeats(seats);
  }


  public synchronized SeatHold holdSeats(int numSeats, Optional<Integer> minLevel,
                                         Optional<Integer> maxLevel, String customerEmail) {

    int min = minLevel.orElse(environment.getProperty(SEAT_MIN_LEVEL, Integer.class));
    int max = maxLevel.orElse(environment.getProperty(SEAT_MAX_LEVEL, Integer.class));

    SeatHold seatHold = new SeatHold();
    seatHold.setTimestamp(Instant.now().getEpochSecond());
    seatHold.setHeldBy(customerEmail);

    List<Long> seatIds = new ArrayList<>();
    List<Seat> seats = seatRepository.findSeatsByLevelRange(min, max);
    seats = filterHeldSeats(seats);
    Collections.sort(seats);

    for(int i = 0 ; i < Math.min(numSeats, seats.size()) ; i++) {
      seatIds.add(seats.get(i).getId());
    }
    seatHold.setSeats(seatIds);

    cacheService.saveSeatHold(seatHold);

    return seatHold;
  }

  public synchronized Reservation reserveSeats(int seatHoldId, String customerEmail) throws SeatHoldExpireException {

    SeatHold seatHold = cacheService.getSeatHold(seatHoldId);

    logger.info("SeatHold is " + seatHold);

    // cache expired or doesn't exist
    if(seatHold == null) {
      logger.info("SeatHold is expired");
      throw new SeatHoldExpireException("Seat holding expired");
    }

    StringBuilder sb = new StringBuilder();

    List<Long> seatIds = seatHold.getSeats();
    for (int i = 0; i < seatIds.size(); i++) {

      Seat seat = seatRepository.findOne(seatIds.get(i));
      seat.setReserved(true);
      seatRepository.save(seat);

      sb.append(seatIds.get(i));
      if (i < seatIds.size() - 1) sb.append(", ");
    }

    Reservation reservation = new Reservation();
    reservation.setReservedBy(customerEmail);
    reservation.setCode(UUID.randomUUID().toString());
    reservation.setSeatIds(sb.toString());

    reservationRepository.save(reservation);

    return reservation;
  }

  public List<Reservation> findAllReservations() {
    return reservationRepository.findAll();
  }

  private String retrieveSeatIds(SeatHold seatHold) {

    StringBuilder sb = new StringBuilder();

    List<Long> seatIds = seatHold.getSeats();
    for (int i = 0; i < seatIds.size(); i++) {
      sb.append(seatIds.get(i));
      if (i < seatIds.size() - 1) sb.append(", ");
    }

    return sb.toString();
  }

  private List<Seat> filterHeldSeats(List<Seat> seats) {
    Iterator<Cache.Entry<String, SeatHold>> heldSeats = cacheService.getSeatHoldCache().iterator();

    Set<Long> heldSeatIds = new HashSet<>();

    while(heldSeats.hasNext()) {
      Cache.Entry<String, SeatHold> entry = heldSeats.next();
      entry.getValue().getSeats().stream().forEach(seatId -> heldSeatIds.add(seatId));
    }

    return seats.stream().filter(seat -> !heldSeatIds.contains(seat.getId()))
        .collect(Collectors.toList());
  }
}
