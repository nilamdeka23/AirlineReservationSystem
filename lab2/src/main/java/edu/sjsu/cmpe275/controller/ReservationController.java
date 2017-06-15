package edu.sjsu.cmpe275.controller;


import edu.sjsu.cmpe275.model.*;
import edu.sjsu.cmpe275.repository.FlightRepository;
import edu.sjsu.cmpe275.repository.PassengerRepository;
import edu.sjsu.cmpe275.repository.ReservationRepository;
import edu.sjsu.cmpe275.utils.DateTimeUtil;
import edu.sjsu.cmpe275.utils.IntervalStartComparator;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The type Reservation controller.
 */
@RestController
@RequestMapping(value = "/reservation")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private FlightRepository flightRepository;

    /**
     * (6) Get a reservation back as JSON.
     *
     * @param number the number
     * @return the reservation
     */
    @RequestMapping(value = "/{number}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getReservation(@PathVariable("number") String number) {
        //query db
        Reservation reservation = reservationRepository.findOne(number);

        if (reservation == null) {
            return new ResponseEntity<>(new BadRequest(new Response(404, "Reserveration with number " + number +
                    " does not exist")),
                    HttpStatus.NOT_FOUND);

        } else {

            Passenger passenger = passengerRepository.getPassengerByOrderNo(reservation.getOrderNumber());
            reservation.setPassenger(new PassengerLtdInfo(passenger));
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        }

    }

    /**
     * (7) Make a reservation.
     *
     * @param passengerId   the passenger id
     * @param flightNumbers the flight numbers
     * @return the response entity
     */
    @Transactional(Transactional.TxType.REQUIRED)
    @RequestMapping(method = RequestMethod.POST, params = {"passengerId", "flightLists"},
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> createReservation(@RequestParam(value = "passengerId") String passengerId,
                                               @RequestParam(value = "flightLists") List<String> flightNumbers) {

        // query db for passenger details
        Passenger passenger = passengerRepository.findOne(passengerId);

        if (passenger == null) {
            return new ResponseEntity<>(new BadRequest(new Response(404, "Passenger with id " + passengerId +
                    " does not exist")), HttpStatus.NOT_FOUND);

        }

        // flights placeholder
        List<Flight> flights = new ArrayList<>();

        // place holder to compute intervals: duration between arrival and departure for each flight
        List<Interval> intervals = new ArrayList<>();

        // placeholder for reservation price computation
        int price = 0;

        // query db for flight details
        Iterable<Flight> iFlights = flightRepository.findAll(flightNumbers);

        int loopCount = 0;

        for (Flight flight : iFlights) {

            // compute reservation price
            price += flight.getPrice();

            // compute interval
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh", Locale.US);

            try {
                Date departureDateTime = simpleDateFormat.parse(flight.getDepartureTime());
                Date arrivalDateTime = simpleDateFormat.parse(flight.getArrivalTime());

                intervals.add(new Interval(departureDateTime.getTime(), arrivalDateTime.getTime()));

            } catch (ParseException e) {
                e.printStackTrace();
                return new ResponseEntity<>(new BadRequest(new Response(500, "Invalid Date Format!")),
                        HttpStatus.BAD_REQUEST);

            }

            // The total amount of passengers can not exceed the capacity of the reserved plane.
            if (flight.decrementSeatsLeftByOne()) {
                return new ResponseEntity<>(new BadRequest(new Response(400, "The total amount of " +
                        "passengers can not exceed the capacity of the reserved plane.")),
                        HttpStatus.BAD_REQUEST);
            }

            flights.add(flight);
            loopCount++;
        }

        if (loopCount != flightNumbers.size()) {
            return new ResponseEntity<>(new BadRequest(new Response(404, "Flight/Flights not found!")), HttpStatus.NOT_FOUND);
        }

        List<Flight> passengerPreviousFlights = new ArrayList<>();
        // query passenger's previous reservation information
        for (Reservation reservation : passenger.getReservations()) {
            passengerPreviousFlights.addAll(reservation.getFlights());
        }

        for (Flight flight : passengerPreviousFlights) {
            // compute interval
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh", Locale.US);
            try {
                Date departureDateTime = simpleDateFormat.parse(flight.getDepartureTime());
                Date arrivalDateTime = simpleDateFormat.parse(flight.getArrivalTime());

                intervals.add(new Interval(departureDateTime.getTime(), arrivalDateTime.getTime()));

            } catch (ParseException e) {
                e.printStackTrace();
                return new ResponseEntity<>(new BadRequest(new Response(500, "Invalid Date Format!")),
                        HttpStatus.BAD_REQUEST);

            }
        }

        // sort the list in ascending order prior to evaluating overlap
        Collections.sort(intervals, new IntervalStartComparator());

        // check Time-Overlap for a certain passenger
        if (DateTimeUtil.isOverlapping(intervals)) {
            return new ResponseEntity<>(new BadRequest(new Response(400, "Time-Overlap is not allowed!")),
                    HttpStatus.BAD_REQUEST);
        }

        // db write
        Reservation reservation = reservationRepository.save(new Reservation(price, passenger, flights));
        // update flights db
        flightRepository.save(flights);

        if (reservation == null) {
            return new ResponseEntity<>(new BadRequest(new Response(500, "Internal Server Error!")),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }

        // hack: printing passenger at the app layer to avoid infinite recursion
        reservation.setPassenger(new PassengerLtdInfo(passenger));
        return new ResponseEntity<>(reservation, HttpStatus.OK);

    }

    /**
     * (8) Update a reservation.
     *
     * @param number         the number
     * @param flightsAdded   the flights added
     * @param flightsRemoved the flights removed
     * @return the response entity
     */
    @Transactional(Transactional.TxType.REQUIRED)
    @RequestMapping(value = "/{number}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePassenger(@PathVariable("number") String number,
                                             @RequestParam(value = "flightsAdded", required = false) List<String> flightsAdded,
                                             @RequestParam(value = "flightsRemoved", required = false) List<String> flightsRemoved) {
        // query db
        Reservation reservation = reservationRepository.findOne(number);

        if (reservation == null) {
            return new ResponseEntity<>(new BadRequest(new Response(404, "Reserveration with number " + number +
                    " does not exist")), HttpStatus.NOT_FOUND);
        }

        // If flightsAdded (or flightsRemoved) param exists, then its list of values cannot be empty.
        if (flightsAdded != null && flightsAdded.isEmpty()) {
            return new ResponseEntity<>(new BadRequest(new Response(400, "flightsAdded list cannot be empty, " +
                    "if param exists")), HttpStatus.BAD_REQUEST);
        }

        if (flightsRemoved != null && flightsRemoved.isEmpty()) {
            return new ResponseEntity<>(new BadRequest(new Response(400, "flightsRemoved list cannot be empty, " +
                    "if param exists")), HttpStatus.BAD_REQUEST);
        }

        // flights placeholders
        List<Flight> flights = reservation.getFlights();
        List<Flight> removedFlights = new ArrayList<>();
        List<Flight> addedFlights = new ArrayList<>();

        // placeholder for modified reservation price computation
        int price = reservation.getPrice();

        if (flightsRemoved != null) {

            for (String flightNumber : flightsRemoved) {

                for (Flight flight : flights) {

                    if (flightNumber.equals(flight.getFlightNumber())) {
                        // compute modified reservation price; part one
                        price -= flight.getPrice();
                        flights.remove(flight);

                        // to update flights db
                        flight.incrementSeatsLeftByOne();
                        removedFlights.add(flight);

                        break;
                    }

                }// end of inner loop

            }// end of outer loop

            // update flights db
            flightRepository.save(removedFlights);
        }

        int loopCount = 0;

        if (flightsAdded != null) {

            // place holder to compute intervals: duration between arrival and departure for each flight
            List<Interval> intervals = new ArrayList<>();

            // query db for flight details
            Iterable<Flight> iFlights = flightRepository.findAll(flightsAdded);

            for (Flight flight : iFlights) {

                // compute modified reservation price; part two
                price += flight.getPrice();

                // compute interval
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh", Locale.US);

                try {
                    Date departureDateTime = simpleDateFormat.parse(flight.getDepartureTime());
                    Date arrivalDateTime = simpleDateFormat.parse(flight.getArrivalTime());

                    intervals.add(new Interval(departureDateTime.getTime(), arrivalDateTime.getTime()));

                } catch (ParseException e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(new BadRequest(new Response(500, "Invalid Date Format!")),
                            HttpStatus.BAD_REQUEST);
                }

                // The total amount of passengers can not exceed the capacity of the reserved plane.
                if (flight.decrementSeatsLeftByOne()) {
                    return new ResponseEntity<>(new BadRequest(new Response(400, "The total amount of " +
                            "passengers can not exceed the capacity of the reserved plane.")),
                            HttpStatus.BAD_REQUEST);
                }

                // to update flights table
                addedFlights.add(flight);
                loopCount++;
            }

            if (loopCount != flightsAdded.size()) {
                return new ResponseEntity<>(new BadRequest(new Response(404, "Flight/Flights not found!")), HttpStatus.NOT_FOUND);
            }

            List<Flight> passengerPreviousFlights = new ArrayList<>();
            // query passenger's previous reservation information
            for (Reservation eachReservation : reservation.getPassengerFKey().getReservations()) {
                passengerPreviousFlights.addAll(eachReservation.getFlights());
            }

            for (Flight flight : passengerPreviousFlights) {
                // compute interval
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh", Locale.US);
                try {
                    Date departureDateTime = simpleDateFormat.parse(flight.getDepartureTime());
                    Date arrivalDateTime = simpleDateFormat.parse(flight.getArrivalTime());

                    intervals.add(new Interval(departureDateTime.getTime(), arrivalDateTime.getTime()));

                } catch (ParseException e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(new BadRequest(new Response(500, "Invalid Date Format!")),
                            HttpStatus.BAD_REQUEST);

                }
            }

            // sort the list in ascending order prior to evaluating overlap
            Collections.sort(intervals, new IntervalStartComparator());

            // check Time-Overlap for a certain passenger
            if (DateTimeUtil.isOverlapping(intervals)) {
                return new ResponseEntity<>(new BadRequest(new Response(400, "Time-Overlap is not allowed!")),
                        HttpStatus.BAD_REQUEST);
            }

            flights.addAll(addedFlights);
            // update flights db
            flightRepository.save(addedFlights);
        }

        // db write
        reservation.setFlights(flights);
        reservation.setPrice(price);
        Reservation reservationObjFromDb = reservationRepository.save(reservation);

        // hack: printing passenger at the app layer to avoid infinite recursion
        Passenger passenger = passengerRepository.getPassengerByOrderNo(reservationObjFromDb.getOrderNumber());
        reservationObjFromDb.setPassenger(new PassengerLtdInfo(passenger));

        return new ResponseEntity<>(reservationObjFromDb, HttpStatus.OK);

    }

    /**
     * (9) Search for reservations.
     *
     * @param passengerId  the passenger id
     * @param from         the from
     * @param to           the to
     * @param flightNumber the flight number
     * @return the response entity
     */
    @Transactional(Transactional.TxType.REQUIRED)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> searchReservation(@RequestParam(value = "passengerId", required = false) String passengerId,
                                               @RequestParam(value = "from", required = false) String from,
                                               @RequestParam(value = "to", required = false) String to,
                                               @RequestParam(value = "flightNumber", required = false) String flightNumber) {

        if (passengerId == null && from == null && to == null && flightNumber == null) {
            return new ResponseEntity<>(new BadRequest(new Response(400, "Atleast one parameter " +
                    "must be present!")),
                    HttpStatus.BAD_REQUEST);
        }

        List<Reservation> reservations = reservationRepository.searchForReservations(passengerId, from, to, flightNumber);

        // hack: printing passenger at the app layer to avoid infinite recursion
        for (Reservation reservation : reservations) {

            Passenger passenger = passengerRepository.getPassengerByOrderNo(reservation.getOrderNumber());
            reservation.setPassenger(new PassengerLtdInfo(passenger));
        }

        return new ResponseEntity<>(new Reservations(reservations), HttpStatus.OK);

    }

    /**
     * (10) Cancel a reservation.
     *
     * @param number the number
     * @return the response entity
     */
    @Transactional(Transactional.TxType.REQUIRED)
    @RequestMapping(value = "/{number}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteReservation(@PathVariable("number") String number) {
        // query db
        Reservation reservation = reservationRepository.findOne(number);
        if (reservation == null) {

            return new ResponseEntity<>(new Response(404, "Reservation with number " + number + " does not exist"),
                    HttpStatus.NOT_FOUND);

        } else {
            // cancel a reservation for a passenger
            Passenger passenger = reservation.getPassengerFKey();
            passenger.getReservations().remove(reservation);
            passengerRepository.save(passenger);

            // update the number of available seats for the involved flight
            List<Flight> flights = reservation.getFlights();
            for (Flight flight : flights) {
                flight.incrementSeatsLeftByOne();
            }
            // batch query for improved performance
            flightRepository.save(flights);

            // write to db
            reservationRepository.delete(number);
            return new ResponseEntity<>(new BadRequest(new Response(200, "Reservation with number " + number +
                    " is deleted successfully")), HttpStatus.OK);
        }

    }

}
