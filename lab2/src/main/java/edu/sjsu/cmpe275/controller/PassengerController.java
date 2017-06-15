package edu.sjsu.cmpe275.controller;


import edu.sjsu.cmpe275.model.*;
import edu.sjsu.cmpe275.repository.FlightRepository;
import edu.sjsu.cmpe275.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Passenger controller.
 */
@RestController
@RequestMapping(value = "/passenger")
public class PassengerController {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private FlightRepository flightRepository;

    /**
     * (1) Get a passenger back as JSON
     *
     * @param id the id
     * @return the passenger json
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPassengerJson(@PathVariable("id") String id) {

        return getPassenger(id);

    }

    /**
     * (2) Get a passenger back as XML.
     *
     * @param id    the id
     * @param isXml the is xml
     * @return the person xml
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, params = "xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getPersonXml(@PathVariable("id") String id, @RequestParam(value = "xml") String isXml) {
        if (isXml.equals("true")) {
            return getPassenger(id);

        } else {
            return new ResponseEntity<>(new BadRequest(new Response(400, "Xml param; found in invalid state!")),
                    HttpStatus.BAD_REQUEST);
        }

    }

    private ResponseEntity<?> getPassenger(String id) {
        //query db
        Passenger passenger = passengerRepository.findOne(id);

        if (passenger == null) {
            return new ResponseEntity<>(new BadRequest(new Response(404, "Sorry, the requested passenger with id " +
                    id + " does not exist")), HttpStatus.NOT_FOUND);

        } else {
            return new ResponseEntity<>(passenger, HttpStatus.OK);

        }

    }

    /**
     * (3) Create a passenger.
     *
     * @param firstname the firstname
     * @param lastname  the lastname
     * @param age       the age
     * @param gender    the gender
     * @param phone     the phone
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPassenger(@RequestParam(value = "firstname") String firstname,
                                             @RequestParam(value = "lastname") String lastname,
                                             @RequestParam(value = "age") int age,
                                             @RequestParam(value = "gender") String gender,
                                             @RequestParam(value = "phone") String phone) {
        try {
            // save to db
            Passenger passenger = passengerRepository.save(new Passenger(firstname, lastname, age, gender, phone));
            return new ResponseEntity<>(passenger, HttpStatus.OK);

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new BadRequest(new Response(400, "another passenger with the same" +
                    " number already exists")),
                    HttpStatus.BAD_REQUEST);

        }

    }

    /**
     * (4) Update a passenger.
     *
     * @param id        the id
     * @param firstname the firstname
     * @param lastname  the lastname
     * @param age       the age
     * @param gender    the gender
     * @param phone     the phone
     * @return the response entity
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePassenger(@PathVariable("id") String id, @RequestParam(value = "firstname") String firstname,
                                             @RequestParam(value = "lastname") String lastname,
                                             @RequestParam(value = "age") int age,
                                             @RequestParam(value = "gender") String gender,
                                             @RequestParam(value = "phone") String phone) {

        // placeholder for reservations
        List<Reservation> reservations = new ArrayList<>();
        // query if there are any existing reservations for this particular passenger
        Passenger passengerObj = passengerRepository.findOne(id);

        if (passengerObj != null) {
            reservations = passengerObj.getReservations();
        }

        Passenger passenger = new Passenger(firstname, lastname, age, gender, phone);
        passenger.setId(id);
        passenger.setReservations(reservations);

        try {
            // save to db
            Passenger passengerObjFromDb = passengerRepository.save(passenger);
            return new ResponseEntity<>(passengerObjFromDb, HttpStatus.OK);

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Response(400, "another passenger with the same number already exists"),
                    HttpStatus.BAD_REQUEST);

        }

    }

    /**
     * (5) Delete a passenger.
     *
     * @param id the id
     * @return the response entity
     */
    @Transactional(Transactional.TxType.REQUIRED)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePassenger(@PathVariable("id") String id) {
        // query db
        Passenger passenger = passengerRepository.findOne(id);
        if (passenger == null) {
            return new ResponseEntity<>(new Response(404, "Passenger with id " + id + " does not exist"),
                    HttpStatus.NOT_FOUND);

        } else {
            // placeholder for all flights of a particular passenger
            List<Flight> flights = new ArrayList<>();
            // update the number of available seats for the involved flights.
            for (Reservation reservation : passenger.getReservations()) {

                List<Flight> passengerFlights = reservation.getFlights();
                for (Flight flight : passengerFlights) {

                    flight.incrementSeatsLeftByOne();
                }

                flights.addAll(passengerFlights);
            }
            // batch call for performance
            flightRepository.save(flights);
            // remove passenger from db
            passengerRepository.delete(id);
            return new ResponseEntity<>(new Response(200, "Passenger with id " + id + " is deleted successfully"),
                    HttpStatus.OK);
        }

    }


}
