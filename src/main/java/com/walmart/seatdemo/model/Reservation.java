package com.walmart.seatdemo.model;


import javax.persistence.*;

/**
 * Created by chuanwang on 10/4/17.
 */

@Entity
@Table(name="reservations")
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private String reservedBy;

  @Column(nullable = false)
  private String seatIds;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getReservedBy() {
    return reservedBy;
  }

  public void setReservedBy(String reservedBy) {
    this.reservedBy = reservedBy;
  }

  public String getSeatIds() {
    return seatIds;
  }

  public void setSeatIds(String seatIds) {
    this.seatIds = seatIds;
  }
}
