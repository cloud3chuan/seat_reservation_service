package com.walmart.seatdemo.model;

import javax.persistence.*;

/**
 * Created by chuanwang on 10/3/17.
 */

@Entity
@Table(name="seats")
public class Seat implements Comparable{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(nullable = false)
  private int level;

  @Column(nullable = false)
  private int number;

  @Column(nullable = false, columnDefinition = "TINYINT(1)")
  private Boolean reserved;

  @Override
  public int compareTo(Object o) {
    Seat another = (Seat)o;
    return this.getLevel() - another.getLevel();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public Boolean getReserved() {
    return reserved;
  }

  public void setReserved(Boolean reserved) {
    this.reserved = reserved;
  }
}
