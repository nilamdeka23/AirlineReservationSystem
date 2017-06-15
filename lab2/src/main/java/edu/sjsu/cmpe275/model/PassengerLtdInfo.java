package edu.sjsu.cmpe275.model;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * The type Passenger ltd info.
 */
@XmlRootElement
public class PassengerLtdInfo {

    private String id;

    private String firstname;

    private String lastname;

    private int age;

    private String gender;

    private String phone;

    /**
     * Instantiates a new Passenger ltd info.
     */
    public PassengerLtdInfo() {
    }

    /**
     * Instantiates a new Passenger ltd info.
     *
     * @param passenger the passenger
     */
    public PassengerLtdInfo(Passenger passenger){
        this.id = passenger.getId();
        this.firstname = passenger.getFirstname();
        this.lastname = passenger.getLastname();
        this.age = passenger.getAge();
        this.gender = passenger.getGender();
        this.phone = passenger.getPhone();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets firstname.
     *
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets firstname.
     *
     * @param firstname the firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Gets lastname.
     *
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Sets lastname.
     *
     * @param lastname the lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Gets age.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets age.
     *
     * @param age the age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets gender.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets gender.
     *
     * @param gender the gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

}
