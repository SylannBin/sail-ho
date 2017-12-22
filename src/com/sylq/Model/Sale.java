package com.sylq.Model;

import java.util.Date;

import static com.sylq.Common.Utils.databaseDate;
import static com.sylq.Common.Utils.*;

public class Sale {
    // FIELDS
    private int id;
    private final User seller;
    private final Date creationDate;
    private Date latestUpdate;
    private String title;
    private String description;
    private String vehicleLocation;
    private String vehicleBrand;
    private String vehicleModel;
    private Integer vehicleYear;
    private Integer mileage;
    private Float proposedPrice;
    private boolean isAvailable;

    /**
     * Constructor to create a new user not existing in database.
     * @param seller
     */
    public Sale(
            User seller, String title, String description, String vehicleLocation, String vehicleBrand,
            String vehicleModel, Integer vehicleYear, Integer mileage, Float proposedPrice) {
        this.seller = seller;
        this.title = title;
        this.description = description;
        this.vehicleLocation = vehicleLocation.isEmpty() ? seller.getAddress() : vehicleLocation;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.vehicleYear = vehicleYear;
        this.mileage = mileage;
        this.proposedPrice = proposedPrice;
        creationDate = new Date();
        latestUpdate = null;
        isAvailable = true;
    }

    /**
     * Constructor to instantiate from an existing sale in database.
     * @param id
     * @param seller
     * @param creationDate
     * @param latestUpdate
     * @param title
     * @param description
     * @param vehicleLocation
     * @param vehicleBrand
     * @param vehicleModel
     * @param vehicleYear
     * @param mileage
     * @param proposedPrice
     * @param isAvailable
     */
    public Sale(
            int id, User seller, String creationDate, String latestUpdate, String title,
            String description, String vehicleLocation, String vehicleBrand, String vehicleModel,
            int vehicleYear, int mileage, float proposedPrice, boolean isAvailable) {
        this.id = id;
        this.seller = seller;
        this.creationDate = databaseDate(creationDate);
        this.latestUpdate = databaseDate(latestUpdate);
        this.title = title;
        this.description = description;
        this.vehicleLocation = vehicleLocation;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.vehicleYear = vehicleYear;
        this.mileage = mileage;
        this.proposedPrice = proposedPrice;
        this.isAvailable = isAvailable;
    }

    //
    // PUBLIC METHODS
    //

    /**
     * Print a simple inline description of the object.
     * @return
     */
    public String toString() {
        return id + ": " + title + ", added "+creationDate;
    }

    /**
     * Print a multiline detailed description of the object.
     */
    public void describe() {
        System.out.println("=========================================");
        System.out.println("Sale (" + id + ")");
        System.out.println("-----------------------------------------");
        System.out.println("Seller:           " + seller.getFullname());
        System.out.println("Creation date:    " + creationDate);
        System.out.println("Latest update:    " + latestUpdate);
        System.out.println("Description:      " + description);
        System.out.println("Location:         " + vehicleLocation);
        System.out.println("Available:        " + isAvailable);
        System.out.println("Brand:            " + vehicleBrand);
        System.out.println("Model:            " + vehicleModel);
        System.out.println("Year:             " + vehicleYear);
        System.out.println("Mileage:          " + mileage);
        System.out.println("Price:            " + proposedPrice);
        System.out.println("=========================================");
    }

    /**
     * Act the transaction
     * @param buyer The user who accepts the offer and pays for it.
     */
    public void sold(User buyer) {
        isAvailable = false;
        latestUpdate = new Date();
        print(buyer.getFullname() + "accepts the offer!\n"
            + seller.getFullname() + " receives " + this.proposedPrice + "€");
        // TODO: Add debit/credit system
    }

    /**
     * Print the contact information of the seller
     */
    public void contact() {
        System.out.println("Contact the seller:");
        System.out.println("  Phone: "+ seller.getPhone());
        System.out.println("  Email: "+ seller.getEmail());
    }

    //
    // GETTERS
    //
    public int     getId()              { return id; }
    public User    getSeller()       { return seller; }
    public Date    getCreationDate()    { return creationDate; }
    public Date    getLatestUpdate()    { return latestUpdate; }
    public String  getTitle()           { return title; }
    public String  getDescription()     { return description; }
    public String  getVehicleLocation() { return vehicleLocation; }
    public String  getVehicleBrand()    { return vehicleBrand; }
    public String  getVehicleModel()    { return vehicleModel; }
    public Integer getVehicleYear()     { return vehicleYear; }
    public Integer getMileage()         { return mileage; }
    public Float   getProposedPrice()   { return proposedPrice; }
    public boolean getIsAvailable()     { return isAvailable; }

    //
    // SETTERS
    //
    public void setTitle           (String  value) { title           = value; latestUpdate = new Date(); }
    public void setDescription     (String  value) { description     = value; latestUpdate = new Date(); }
    public void setVehicleLocation (String  value) { vehicleLocation = value; latestUpdate = new Date(); }
    public void setVehicleBrand    (String  value) { vehicleBrand    = value; latestUpdate = new Date(); }
    public void setVehicleModel    (String  value) { vehicleModel    = value; latestUpdate = new Date(); }
    public void setVehicleYear     (Integer value) { vehicleYear     = value; latestUpdate = new Date(); }
    public void setMileage         (Integer value) { mileage         = value; latestUpdate = new Date(); }
    public void setProposedPrice   (Float   value) { proposedPrice   = value; latestUpdate = new Date(); }
}