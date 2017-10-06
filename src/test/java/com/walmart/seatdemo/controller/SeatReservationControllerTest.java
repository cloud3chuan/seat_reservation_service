package com.walmart.seatdemo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.walmart.seatdemo.model.Reservation;
import com.walmart.seatdemo.model.Seat;
import com.walmart.seatdemo.model.SeatHold;
import com.walmart.seatdemo.service.SeatService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


/**
 * Created by chuanwang on 10/5/17.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = com.walmart.seatdemo.controller.SeatReservationController.class, secure = false)
public class SeatReservationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SeatService seatService;

  @Test
  public void retrieveSeats() throws Exception {

    List<Seat> seats = new ArrayList<>();
    Seat seat1 = new Seat();
    seat1.setId(1);
    seat1.setReserved(false);
    seat1.setLevel(1);
    seat1.setNumber(1);
    seats.add(seat1);

    Seat seat2 = new Seat();
    seat2.setId(2);
    seat2.setReserved(false);
    seat2.setLevel(2);
    seat2.setNumber(2);
    seats.add(seat2);

    Mockito.when(
        seatService.findAllAvailableSeats()).thenReturn(seats);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/seats")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    String expected = "[{id:1,level:1,number:1,reserved:false},{id:2,level:2,number:2,reserved:false}]";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

  @Test
  public void retrieveSeatsWithLevel() throws Exception {

    List<Seat> seats = new ArrayList<>();
    Seat seat1 = new Seat();
    seat1.setId(1);
    seat1.setReserved(false);
    seat1.setLevel(1);
    seat1.setNumber(1);
    seats.add(seat1);

    Mockito.when(
        seatService.findSeatsByLevel(1)).thenReturn(seats);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/seats?level=1")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    String expected = "[{id:1,level:1,number:1,reserved:false}]";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

  @Test
  public void holdSeats() throws Exception {

    List<Long> seats = Arrays.asList(1L,2L,3L);
    SeatHold seatHold = new SeatHold();
    seatHold.setId(1);
    seatHold.setHeldBy("user@test.com");
    seatHold.setTimestamp(123);
    seatHold.setSeats(seats);

    Mockito.when(
        seatService.holdSeats(1, Optional.of(1), Optional.of(2), "user@test.com"))
        .thenReturn(seatHold);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/holdseats?numSeats=1&minLevel=1&maxLevel=2&customerEmail=user@test.com")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    String expected = "{id:1,heldBy:user@test.com,timestamp:123,seats:[1,2,3]}";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }


  @Test
  public void reserveSeats() throws Exception {

    Reservation reservation = new Reservation();
    reservation.setSeatIds("1,2,3");
    reservation.setCode("abcd1234");
    reservation.setReservedBy("user@test.com");
    reservation.setId(123);

    Mockito.when(
        seatService.reserveSeats(Mockito.anyInt(), Mockito.anyString()))
        .thenReturn(reservation);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/reservations?seatHoldId=1234&customerEmail=user@test.com")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    System.out.println(result.getResponse().getContentAsString());

    String expected = "{id:123,code:\"abcd1234\",reservedBy:\"user@test.com\",seatIds:\"1,2,3\"}";

    JSONAssert.assertEquals(expected, result.getResponse()
        .getContentAsString(), false);
  }

}
