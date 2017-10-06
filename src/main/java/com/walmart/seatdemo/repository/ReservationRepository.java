package com.walmart.seatdemo.repository;

import com.walmart.seatdemo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by chuanwang on 10/4/17.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}