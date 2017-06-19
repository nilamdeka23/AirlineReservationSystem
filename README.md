# Airline Reservation System
A project to build **RESTful APIs** to implement a simple  airline reservation system using **Spring Boot** and **Spring Data JPA** CRUD operations.

You can read this Spring guide on how to build a [RESTful Web Service](https://spring.io/guides/gs/rest-service/). 

The general requirements and constraints were as follows,
* Each passenger can make one or more reservation. Time overlap is not allowed among any of his/her reservation.
* Each reservation may consist of one or more flights.
* Each flight can carry one or more passengers.
* Each flight uses one plane, which is an embedded object with four fields mapped to the corresponding four columns in the airline table.
* The total amount of passengers can not exceed the capacity of a plane.
* When a passenger is deleted, all reservation made by him/her are automatically cancelled.
* A flight can not be deleted if it needs to carry at least one passenger.

Additional requirements,
* All these operations should be transactional.
* Must use JPA for persistence.
* This system needs to be hosted on the cloud.

Incomplete  definitions of passenger, reservation, flight and plane were given as below,
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

## API description
#### 1. Get a passenger back as JSON
 > This JSON is meant for consumption of APIs, and may not render well in browsers unless extensions/plugs are installed.
#### 2. Get a passenger back as XML
 > This XML is meant for consumption of APIs, and may not render well in browsers unless extensions/plugs are installed.
#### 3. Create a passenger
 > This request creates a passenger’s record in the system.
 
 > For simplicity, all the passenger's fields including the phone number(firstname, lastname, age, and gender) are passed as query parameters, and it is assumed the request always comes with all the fields specified.
 
 > The uniqueness of phone numbers is enforced.
#### 4. Update a passenger
 > This request updates a passenger’s record in the system.
 
 > For simplicity, all the passenger's fields including the phone number(firstname, lastname, age, and gender) are passed as query parameters, and it is assumed the request always comes with all the fields specified.
#### 5. Delete a passenger
 > This request deletes the user with the given user ID.
 
 > The reservation made by the passenger is also deleted.
 
 > It updates the number of available seats for the involved flights.
#### 6. Get a reservation back as JSON
 
#### 7. Make a reservation
 > This request makes a reservation for a passenger.
 
 > Time-Overlap is not allowed for a certain passenger.
 
 > The total amount of passengers can not exceed the capacity of the reserved plane.
 
 > Receives a list of flights as input.
#### 8. Update a reservation 
 > This request update a reservation by adding and/or removing some flights.
 
 > If flightsAdded (or flightsRemoved) param exists, then its list of values cannot be empty.
 
 > If both additions and removals exist, the non-overlapping constraint considers the flights to be removed.
 
 > Update a reservation triggers a recalculation of its total price by summing up the price of each contained flight.    
#### 9. Search for reservations
 > This request allow to search for reservations by any combination of single passenger ID, departing city, arrival city, and flight number.
 
 > It assumes that at least one request parameter is specified.
#### 10. Cancel a reservation
 > This request cancel a reservation for a passenger.
 
 > It updates the number of available seats for the involved flight.
#### 11. Get a flight back as JSON
 > This JSON is meant for consumption of APIs, and may not render well in browsers unless extensions/plugs are installed.
#### 12. Get a flight back as XML
 > This XML is meant for consumption of APIs, and may not render well in browsers unless extensions/plugs are installed.
#### 13. Create or update a flight
 > This request creates/updates a new flight for the system.
 
 > For simplicity, all the fields are passed as query parameters, and it assunes the request always comes with all the fields specified.
 
 > The corresponding flight is created/updated accordingly.
 
 > Updates the seatsLeft when capacity is modified.
 
 > When attempting to reduce the existing capacity of a flight, the request fails with error code 400 if active reservation count for this flight is higher than the target capacity.

 > If change of a flight’s departure and/or arrival time causes a passenger to have overlapping flight time , this update cannot proceed and hence fails with error code 400.
 
 > Changing the price of a flight will not affect the total price of existing reservations.
#### 14. Delete a flight
 > This request deletes the flight for the given flight number.
 
 > You can not delete a flight that has one or more reservation, in which case, the deletion should fail with error code 400.

## Request/Response
Follow this [link](https://github.com/nilamdeka23/AirlineReservationSystem/blob/master/report.pdf) to view a detailed report on the exposed services.

## Cloud Deployment Steps
#### Technology chosen: Amazon Web Services(AWS).
##### Note: Prior to following the below steps, don't forget to set up your EC2 instance with Java Runtime Env(JRE) with 1.8 and MySQL. Also, open the port 8443 for the incoming requests from the Security Group of your instance.
![Alt Text](https://github.com/nilamdeka23/AirlineReservationSystem/blob/master/lab2.gif)

## Test framework
Junit

## Thank you note
To enalbe HTTPS in Spring Boot, I had immense help following this [link](http://drissamri.be/blog/java/enable-https-in-spring-boot/).

Also, Stackoverflow [thread](http://stackoverflow.com/questions/24497809/compare-intervals-jodatime-in-alist-for-overlap) was useful for time comparison.
    



    
    
