package edu.sjsu.cmpe275.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The type Plane.
 */
@XmlRootElement
@Embeddable
public class Plane {

    @Column
    private int capacity;

    @Column
    private String model;

    @Column
    private String manufacturer;

    @Column(name = "year_of_manufacture")
    private int yearOfManufacture;

    /**
     * Instantiates a new Plane.
     */
    public Plane() {
    }

    /**
     * Instantiates a new Plane.
     *
     * @param capacity          the capacity
     * @param model             the model
     * @param manufacturer      the manufacturer
     * @param yearOfManufacture the year of manufacture
     */
    public Plane(int capacity, String model, String manufacturer, int yearOfManufacture) {
        this.capacity = capacity;
        this.model = model;
        this.manufacturer = manufacturer;
        this.yearOfManufacture = yearOfManufacture;
    }

    /**
     * Gets capacity.
     *
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets capacity.
     *
     * @param capacity the capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets model.
     *
     * @param model the model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets manufacturer.
     *
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets manufacturer.
     *
     * @param manufacturer the manufacturer
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Gets year of manufacture.
     *
     * @return the year of manufacture
     */
    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    /**
     * Sets year of manufacture.
     *
     * @param yearOfManufacture the year of manufacture
     */
    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

}