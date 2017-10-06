package com.walmart.seatdemo.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by chuanwang on 10/4/17.
 */
public class SeatHold {

  private int id;

  private String heldBy;

  private long timestamp;

  private List<Long> seats;

  public int getId() {
    return id;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .append(id)
        .append(heldBy)
        .append(timestamp)
        .append(seats)
        .toString();
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getHeldBy() {
    return heldBy;
  }

  public void setHeldBy(String heldBy) {
    this.heldBy = heldBy;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public List<Long> getSeats() {
    return seats;
  }

  public void setSeats(List<Long> seats) {
    this.seats = seats;
  }


}

