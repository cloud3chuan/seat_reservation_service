# Seat Reservation Services test1

This is a simple seat reservation services. It is implemented as a spring boot project. 

## How to build and run this service
 
In project root directory, assuming you have gradle installed already, simply do:

```gradle bootRun```

The service will be built and running on localhost:8080

## How to run unit tests

In project root directory, do:

```gradle test```

It will run through unit tests for controller.

## APIs

   * GET /api/seats[?level=1]
   
   This API will return all available seats or, if level is specified as parameter, 
   available seats in that level.
   
   * GET /api/holdseats?numSeats=10&minLevel=1&maxLevel=10&customerEmail=user@test.com
   
   This API will hold certain numbers of seats based on parameters. numSeats is the number 
   of seats user wants to hold. minLevel is the minimum level number. maxLevel is the maximum
   level number. customerEmail is user's email address. Depends on the available seats numbers,
   user may or may not get all seats he wants. For example if there are only 10 seats available
   in level 1 and level 2, but user asked for 20 seats, then he still just gets 10 seats in this
   case. The holding seat is only valid for certain time. Once the holding seats expire, they become
   available again.
   
   * POST /api/reservations?seatHoldId=123&customerEmail=user@test.com
   
   This API will reserve the seats held by user in previous API call. seatHoldId is returned ID
   from previous API call for a seat holding request. customerEmail is user's email address.
   
## Design and implementation notes

The project follows standard spring boot project convention. It includes model, service, and controller layers.
JPA repository is used for database query. h2database is used as embedded database to store Seat and Reservation information.

Since seat holding will expire after certain time, it is a short-living object, so it is not stored
in database. Instead Ehcache is used to store seat holding objects. Since Ehcache has built-in expiry support,
it is more robust than other solutions such as timer thread etc.

Every time when SeatService needs to retrieve available seats, it will query database to get all
un-reserved seats, then it will check ehcache to filter out those on-holding but not reserved seats.

## Database initialization and configurations

Two tables: seats and reservations are created in schema.sql. 3 levels and 10 seats for each level.
Seat holding time is set to 10 seconds in application.properties file for testing purpose.


## Regarding tests

Basic unit tests are implemented for controller methods. More unit tests for service layer could be added as future improvements.
To test functions, a Postman test collection "seat_reservation_collection.json" is created for this purpose. This collection does following tests:

1. Get all available seats at level 1
2. Hold 15 seats from level 1 to level 2
3. reserve these 15 seats, check result.
4. Hold 5 seats from level 1 to level 2 and wait until it expires.
5. try to reserve this expired holding, it should receive "Hold expires" message

To run this collection. Simply import the json file into Postman app and run it. You need to set up an environment to run collection. 
For more information, please go to [Postman website](https://www.getpostman.com/postman)

   
    
