package edu.sjsu.cmpe275.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * The type Reservation.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "reservation")
public class Reservation {

    @XmlElement
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "order_no")
    private String orderNumber;

    @XmlElement
    @Column
    private int price;

    @XmlElement
    @ManyToMany
    @JoinTable(name = "flight_reservation",
            joinColumns = {@JoinColumn(name = "res_order_id", referencedColumnName = "order_no")},
            inverseJoinColumns = {@JoinColumn(name = "flyt_no", referencedColumnName = "flight_no")})
    private List<Flight> flights;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private Passenger passengerFKey;

    @XmlElement
    @Transient
    private PassengerLtdInfo passenger;

    /**
     * Instantiates a new Reservation.
     */
    public Reservation() {
    }

    /**
     * Instantiates a new Reservation.
     *
     * @param price         the price
     * @param passengerFKey the passengerFKey
     * @param flights       the flights
     */
    public Reservation(int price, Passenger passengerFKey, List<Flight> flights) {
        this.price = price;
        this.passengerFKey = passengerFKey;
        this.flights = flights;
    }

    /**
     * Gets order number.
     *
     * @return the order number
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * Sets order number.
     *
     * @param orderNumber the order number
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Gets flights.
     *
     * @return the flights
     */
    public List<Flight> getFlights() {
        return flights;
    }

    /**
     * Sets flights.
     *
     * @param flights the flights
     */
    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    /**
     * Gets passengerFKey.
     *
     * @return the passengerFKey
     */
    @JsonIgnore
    public Passenger getPassengerFKey() {
        return passengerFKey;
    }

    /**
     * Sets passengerFKey.
     *
     * @param passengerFKey the passengerFKey
     */
    public void setPassengerFKey(Passenger passengerFKey) {
        this.passengerFKey = passengerFKey;
    }

    /**
     * Gets passenger.
     *
     * @return the passenger
     */
    public PassengerLtdInfo getPassenger() {
        return passenger;
    }

    /**
     * Sets passenger.
     *
     * @param passenger the passenger
     */
    public void setPassenger(PassengerLtdInfo passenger) {
        this.passenger = passenger;
    }

}