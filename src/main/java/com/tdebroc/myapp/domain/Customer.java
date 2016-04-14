package com.tdebroc.myapp.domain;

/**
 * Created by thibautdebroca on 08/04/16.
 */

import javax.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

    @Transient
    private static long ID_INCREMENT = 1;

    @Id
    private long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    protected Customer() {}

    public Customer(String firstName, String lastName) {
        this.id = ID_INCREMENT++;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
            "Customer[id=%d, firstName='%s', lastName='%s']",
            id, firstName, lastName);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
