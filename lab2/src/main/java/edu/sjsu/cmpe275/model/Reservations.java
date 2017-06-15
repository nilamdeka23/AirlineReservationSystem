package edu.sjsu.cmpe275.model;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * The type Reservation list.
 */
@XmlRootElement
public class Reservations {

    private List<Reservation> reservation;

    /**
     * Instantiates a new Reservation list.
     */
    public Reservations() {
    }

    /**
     * Instantiates a new Reservation list.
     *
     * @param reservation the reservation
     */
    public Reservations(List<Reservation> reservation) {
        this.reservation = reservation;

    }

    /**
     * Gets reservation.
     *
     * @return the reservation
     */
    public List<Reservation> getReservation() {
        return reservation;
    }

    /**
     * Sets reservation.
     *
     * @param reservation the reservation
     */
    public void setReservation(List<Reservation> reservation) {
        this.reservation = reservation;
    }

}
