package com.walmart.seatdemo.repository;

import com.walmart.seatdemo.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by chuanwang on 10/3/17.
 */

public interface SeatRepository extends JpaRepository<Seat, Long> {

  @Query("SELECT s FROM Seat s WHERE s.reserved = FALSE")
  List<Seat> findAllSeats();

  @Query("SELECT s FROM Seat s WHERE s.level = :level and s.reserved = FALSE")
  List<Seat> findSeatsByLevel(@Param("level") int level);

  @Query("SELECT s FROM Seat s WHERE s.level >= :minLevel and s.level <= :maxLevel and s.reserved = FALSE")
  List<Seat> findSeatsByLevelRange(@Param("minLevel") int minLevel, @Param("maxLevel") int maxLevel);
}
