package com.tdebroc.myapp.repository;

/**
 * Created by thibautdebroca on 08/04/16.
 */
import com.tdebroc.filler.game.Cell;

import javax.persistence.*;
import javax.transaction.Transactional;

@Entity
public class Customer {

    @Transient
    private static long ID_INCREMENT = 1;

    @Id
    private long id;

    private String firstName;

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
