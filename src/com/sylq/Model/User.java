package com.sylq.Model;

import java.util.Date;

import static com.sylq.Common.Utils.databaseDate;

public class User {

    // FIELDS
    private int id;
    private final Date inscriptionDate;
    private String email;
    private String password;
    private Rank rank;
    private String firstname;
    private String lastname;
    private String address;
    private String phone;

    /**
     * Constructor to create a new user not existing in database.
     * @param email
     * @param password
     * @param firstname
     * @param lastname
     * @param address
     * @param phone
     */
    public User(String email, String password, String firstname, String lastname, String address, String phone) {
        inscriptionDate = new Date();
        rank = Rank.User;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phone = phone;
    }

    /**
     * Constructor to instantiate from an existing user in database.
     * @param id
     * @param inscriptionDate
     * @param email
     * @param password
     * @param rank
     * @param firstname
     * @param lastname
     * @param address
     * @param phone
     */
    public User(
            int id, String inscriptionDate, String email, String password, int rank,
            String firstname, String lastname, String address, String phone) {
        this.id = id;
        this.inscriptionDate = databaseDate(inscriptionDate);
        this.email = email;
        this.password = password;
        this.rank = Rank.values()[rank-1];
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phone = phone;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Inline description of User
     */
    @Override
    public String toString() {
        return id + ": " + email + " (" + rank + "), added " + inscriptionDate;
    }

    /**
     * Full detailed description of User
     */
    public void describe() {
        System.out.println("=========================================");
        System.out.println("User (" + id + ")");
        System.out.println("-----------------------------------------");
        System.out.println("Inscription date: " + inscriptionDate);
        System.out.println("Email:            " + email);
        System.out.println("Password:         ******");
        System.out.println("Rank:             " + rank);
        System.out.println("Firstname:        " + firstname);
        System.out.println("Lastname:         " + lastname);
        System.out.println("Address:          " + address);
        System.out.println("Phone:            " + phone);
        System.out.println("=========================================");
    }

    //
    // GETTERS
    //
    public int    getId()               { return id; }
    public Date   getInscriptionDate()  { return inscriptionDate; }
    public String getEmail()            { return email; }
    public String getPassword()         { return password; }
    public Rank   getRank()             { return rank; }
    public String getFirstname()        { return firstname; }
    public String getLastname()         { return lastname; }
    public String getAddress()          { return address; }
    public String getPhone()            { return phone; }

    public String getFullname() {
        if (firstname != null && lastname != null)
            return firstname + " " + lastname;
        if (firstname == null && lastname == null)
            return "Unnamed user";

        return firstname == null ? lastname : firstname;
    }

    //
    // SETTERS
    //
    public void setEmail     (String value) { if (value != null) email     = value; }
    public void setPassword  (String value) { if (value != null) password  = value; }
    public void setRank      (Rank   value) { if (value != null) rank      = value; }
    public void setFirstname (String value) { if (value != null) firstname = value; }
    public void setLastname  (String value) { if (value != null) lastname  = value; }
    public void setAddress   (String value) { if (value != null) address   = value; }
    public void setPhone     (String value) { if (value != null) phone     = value; }
}