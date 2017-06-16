# Airline Reservation System
A project to build **Restful APIs** to implement a simple  airline reservation system through database **CRUD** operations.

The general requirements and constraints are as follows,
* Each passenger can make one or more reservation. Time overlap is not allowed among any of his/her reservation.
* Each reservation may consist of one or more flights.
* Each flight can carry one or more passengers.
* Each flight uses one plane, which is an embedded object with four fields mapped to the corresponding four columns in the airline table.
* The total amount of passengers can not exceed the capacity of a plane.
* When a passenger is deleted, all reservation made by him/her are automatically cancelled.
* A flight can not be deleted if it needs to carry at least one passenger.

Incomplete  definitions of passenger, reservation, flight and plane are given below..
```java
package edu.sjsu.cmpe275.lab2;
public class Passenger {
 private String id;
 private String firstname;
 private String lastname;
 private int age;
 private String gender;
 private String phone; // Phone numbers must be unique ...
}
public class Reservation {
 private String orderNumber;
 private Passenger passenger;

 private int price; // sum of each flight’s price. private List<Flight> flights;
 ...
}
public class Flight {
 private String flightNumber; // Each flight has a unique flight number. private int price;
 private String from;
 private String to;
 /* Date format:  yy-mm-dd-hh, does not include minutes and seconds.
  ** Example: 2017-03-22-19
  */
 // The system only needs to supports PST. You can ignore other time zones.
 private Date departureTime;
 private Date arrivalTime;
 private int seatsLeft;
 private String description;
 private Plane plane; // Embedded
 private List < Passenger > passengers;
 ...
}
public class Plane {
 private int capacity;
 private String model;
 private String manufacturer;
 private int yearOfManufacture;
}
```

###### Note: For simplicity, no authentication or authorization is enforced for these requests.

### Features

### Sample Run

### Reading


    
    



    
    
