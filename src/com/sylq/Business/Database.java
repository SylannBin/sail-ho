package com.sylq.Business;

import java.sql.*;
import java.util.Arrays;

import com.sylq.Model.Sale;
import com.sylq.Model.User;

import static com.sylq.Common.Utils.info;
import static com.sylq.Common.Utils.stringifyDate;
import static com.sylq.Common.Utils.warn;

class Database {

    /**
     * Iterate over users from the database and print a short
     * description of each of them.
     */
    public static void getUsers() {
        String sql = "SELECT"
                + " id, inscriptionDate, email, password, rank, firstname, lastname, address, phone"
                + " FROM Users";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            while (rs.next()) { System.out.println(sqlResultToUser(rs)); }

        } catch (SQLException | NullPointerException e) { warn(e.getMessage()); }
    }

    /**
     * Iterate over sales from the database and print a short
     * description of each of them.
     */
    public static void getSales() {
        String sql = "SELECT"
                + " id, seller, creationDate, latestUpdate, title, description, vehicleLocation,"
                + " vehicleBrand, vehicleModel, vehicleYear, mileage, proposedPrice, isAvailable"
                + " FROM Sales";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            while (rs.next()) { System.out.println(sqlResultToSale(rs)); }

        } catch (SQLException | NullPointerException e) { warn(e.getMessage()); }
    }

    /**
     * Select user by provided id.
     * @param id
     * @return The user with the provided id or null.
     */
    public static User getUser(int id) {
        String sql = "SELECT"
                + " id, inscriptionDate, email, password, rank, firstname, lastname, address, phone"
                + " FROM Users"
                + " WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { return sqlResultToUser(rs); }

        } catch (SQLException | NullPointerException e) { warn(e.getMessage()); }
        return null;
    }

    /**
     * Select user by provided email.
     * @param email
     * @return The user having the provided email or null.
     */
    public static User getUserByEmail(String email) {
        String sql = "SELECT"
                + " id, inscriptionDate, email, password, rank, firstname, lastname, address, phone"
                + " FROM Users"
                + " WHERE email = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { return sqlResultToUser(rs); }

        } catch (SQLException | NullPointerException e) { warn(e.getMessage()); }
        return null;
    }

    /**
     * Select sale by provided id.
     * @param id
     * @return The sale having the provided id or null.
     */
    public static Sale getSale(int id) {
        String sql = "SELECT"
                + " id, seller, creationDate, latestUpdate, title, description, vehicleLocation,"
                + " vehicleBrand, vehicleModel, vehicleYear, mileage, proposedPrice, isAvailable"
                + " FROM Sales"
                + " WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { return sqlResultToSale(rs); }

        } catch (SQLException | NullPointerException e) { warn(e.getMessage()); }
        return null;
    }

    /**
     * Select sale by provided seller and title.
     * @param seller owner of a sale
     * @param title
     * @return The sale having the provided information or null.
     */
    public static Sale getSaleBySellerAndTitle(User seller, String title) {
        String sql = "SELECT"
                + " id, seller, creationDate, latestUpdate, title, description, vehicleLocation,"
                + " vehicleBrand, vehicleModel, vehicleYear, mileage, proposedPrice, isAvailable"
                + " FROM Sales"
                + " WHERE seller = ? AND title = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setInt(1, seller.getId());
            pstmt.setString(2, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { return sqlResultToSale(rs); }

        } catch (SQLException | NullPointerException e) { warn(e.getMessage()); }
        return null;
    }

    /**
     * Insert the provided user into the database users table.
     * @param newUser
     * @return The last inserted id.
     */
    public static Integer createUser(User newUser) {
        String sql = "INSERT INTO Users"
                + " (inscriptionDate, email, password, rank, firstname, lastname, address, phone)"
                + " VALUES (?,?,?,?,?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, stringifyDate(newUser.getInscriptionDate()));
            pstmt.setString(2, newUser.getEmail());
            pstmt.setString(3, newUser.getPassword());
            pstmt.setInt   (4, newUser.getRank().getCode());
            pstmt.setString(5, newUser.getFirstname());
            pstmt.setString(6, newUser.getLastname());
            pstmt.setString(7, newUser.getAddress());
            pstmt.setString(8, newUser.getPhone());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1); // last inserted id

        } catch (SQLException | NullPointerException e) { warn(e.getMessage()); }
        return null;
    }

    /**
     * Insert the provided user into the database users table.
     * @param newSale
     * @return The last inserted id.
     */
    public static Integer createSale(Sale newSale) {
        String sql = "INSERT INTO Sales"
                + " (seller, creationDate, latestUpdate, title, description, vehicleLocation,"
                + " vehicleBrand, vehicleModel, vehicleYear, mileage, proposedPrice, isAvailable)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt    ( 1, newSale.getSeller().getId());
            pstmt.setString ( 2, stringifyDate(newSale.getCreationDate()));
            pstmt.setString ( 3, stringifyDate(newSale.getLatestUpdate()));
            pstmt.setString ( 4, newSale.getTitle());
            pstmt.setString ( 5, newSale.getDescription());
            pstmt.setString ( 6, newSale.getVehicleLocation());
            pstmt.setString ( 7, newSale.getVehicleBrand());
            pstmt.setString ( 8, newSale.getVehicleModel());
            pstmt.setInt    ( 9, newSale.getVehicleYear());
            pstmt.setInt    (10, newSale.getMileage());
            pstmt.setFloat  (11, newSale.getProposedPrice());
            pstmt.setBoolean(12, newSale.getIsAvailable());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1); // last inserted id

        } catch (SQLException | NullPointerException e) { warn(e.getMessage()); }
        return null;
    }

    /**
     * Update the user information with the provided updated user.
     * Need to have a defined id.
     * @param updatedUser
     */
    public static void updateUser(User updatedUser) {
        String sql = "UPDATE Users SET"
                + " email = ?, password = ?, rank = ?, firstname = ?, lastname = ?, address = ?, phone = ?"
                + " WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, updatedUser.getEmail());
            pstmt.setString(2, updatedUser.getPassword());
            pstmt.setInt   (3, updatedUser.getRank().getCode());
            pstmt.setString(4, updatedUser.getFirstname());
            pstmt.setString(5, updatedUser.getLastname());
            pstmt.setString(6, updatedUser.getAddress());
            pstmt.setString(7, updatedUser.getPhone());
            pstmt.setInt   (8, updatedUser.getId());
            pstmt.executeUpdate();

        } catch (SQLException | NullPointerException e) { warn(e.getMessage()); }
    }

    /**
     * Update the sale information with the provided updated sale.
     * Need to have a defined id.
     * @param updatedSale
     */
    public static void updateSale(Sale updatedSale) {
        String sql = "UPDATE Sales SET"
                   + " latestUpdate = ?, title = ?, description = ?, vehicleLocation = ?, vehicleBrand = ?,"
                   + " vehicleModel = ?, vehicleYear = ?, mileage = ?, proposedPrice = ?, isAvailable = ?"
                   + " WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString ( 1, stringifyDate(updatedSale.getLatestUpdate()));
            pstmt.setString ( 2, updatedSale.getTitle());
            pstmt.setString ( 3, updatedSale.getDescription());
            pstmt.setString ( 4, updatedSale.getVehicleLocation());
            pstmt.setString ( 5, updatedSale.getVehicleBrand());
            pstmt.setString ( 6, updatedSale.getVehicleModel());
            pstmt.setInt    ( 7, updatedSale.getVehicleYear());
            pstmt.setInt    ( 8, updatedSale.getMileage());
            pstmt.setFloat  ( 9, updatedSale.getProposedPrice());
            pstmt.setBoolean(10, updatedSale.getIsAvailable());
            pstmt.setInt    (11, updatedSale.getId());
            pstmt.executeUpdate();

        } catch (SQLException | NullPointerException e) {
            warn(e.getMessage());
        }
    }

    /**
     * Delete the user having the provided id from the database.
     * @param id user identity
     */
    public static void deleteUser(int id) {
        String sql = "DELETE FROM Users WHERE id = ?";
 
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
 
        } catch (SQLException | NullPointerException e) {
            warn(e.getMessage());
        }
    }

    /**
     * Delete the sale having the provided id from the database.
     * @param id sale identity
     */
    public static void deleteSale(int id) {
        String sql = "DELETE FROM Sales WHERE id = ?";
 
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
 
        } catch (SQLException | NullPointerException e) {
            warn(e.getMessage());
        }
    }

    /**
     * Create needed databases if they do not already exist.
     */
    public static boolean initDB() {
        int created = 0;
        if (initTableUser()) {
            info("Created Users table");
            created++;
        }
        if (initTableSale()) {
            info("Created Sales table");
            created++;
        }
        return created > 0;
    }

    /**
     * Add some content for testing purposes.
     * Lines that do not respect database constraints will not be added.
     */
    public static void fixtures() {

        User[] users = {
            new User(1, "2016-10-15 15:25:32", "romstock@gmail.com", "toc toc", 2, "Romain", "Vincent", "4 rue du bois, 38240 Meylan", "+33601020304"),
            new User(2, "2016-12-25 19:22:28", "sarayevo@gmail.com", "nope nope", 2, "Sarah", "Martin", "4 rue du bois, 38240 Meylan", "+33602020304"),
            new User(3, "2017-02-03 18:47:12", "charly@outlook.ch", "get rekt", 1, "Charly", "Testimonier", "Rue du Puits-Saint-Pierre 6, 1204 Genève, Suisse", "+41224183700"),
            new User(4, "2017-06-19 09:10:00", "alain@outlook.ch", "azerty", 1, "Alain", "Trépide", null, null),
            new User(5, "2017-08-01 11:50:46", "superggdu62@hotmail.fr", "introuvable", 1, "Gérard", "Rigoire", null, null)
        };
        for (User u: Arrays.asList(users)) createUser(u);

        Sale[] sales = {
            new Sale(users[2], "New Mercedes S class 27k€", "", "Grenoble", "Mercedes", "S class", 2017, 48744, 27000.00f),
            new Sale(users[3], "Fine BMW 325 2013 7999.99€", "", "Paris", "BMW", "325", 2013, 120561, 7999.99f),
            new Sale(users[4], "Fiat Punto 2014 80459km 2500€", "", "Biaritz", "Fiat", "Punto", 2014, 80459, 2500.00f)
        };
        for (Sale s: Arrays.asList(sales)) createSale(s);
    }

    /**
     * Get a connection to the database
     */
    private static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:dev.db");

        } catch (SQLException | NullPointerException e) { warn(e.getMessage()); }
        return null;
    }

    /**
     * Instantiate a User with the values of the provided database Result Set.
     * @param rs Result set (row) of a "Select User" query.
     * @return User
     * @throws SQLException
     */
    private static User sqlResultToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("inscriptionDate"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("rank"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("address"),
                rs.getString("phone")
        );
    }

    /**
     * Instantiate an Sale with the values of the provided database Result Set.
     * @param rs Result set (row) of a "Select Sale" query.
     * @return Sale
     * @throws SQLException
     */
    private static Sale sqlResultToSale(ResultSet rs) throws SQLException {
        return new Sale(
                rs.getInt("id"),
                getUser(rs.getInt("seller")),
                rs.getString("creationDate"),
                rs.getString("latestUpdate"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("vehicleLocation"),
                rs.getString("vehicleBrand"),
                rs.getString("vehicleModel"),
                rs.getInt("vehicleYear"),
                rs.getInt("mileage"),
                rs.getFloat("proposedPrice"),
                rs.getBoolean("isAvailable")
        );
    }

    /**
     * Create the users table in the database.
     */
    private static boolean initTableUser() {
        String sql = "CREATE TABLE Users ("
                + "\n    id              INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "\n    inscriptionDate TEXT    NOT NULL,"
                + "\n    email           TEXT    NOT NULL UNIQUE,"
                + "\n    password        TEXT,"
                + "\n    rank            INTEGER NOT NULL,"
                + "\n    firstname       TEXT,"
                + "\n    lastname        TEXT,"
                + "\n    address         TEXT,"
                + "\n    phone           TEXT"
                + "\n)";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            return true;

        } catch (SQLException | NullPointerException e) { warn(e.getMessage()); }
        return false;
    }

    /**
     * Create the sales table in the database.
     */
    private static boolean initTableSale() {
        String sql = "CREATE TABLE Sales ("
                + "\n    id              INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "\n    seller          INTEGER NOT NULL,"
                + "\n    creationDate    TEXT    NOT NULL,"
                + "\n    latestUpdate    TEXT,"
                + "\n    title           TEXT    NOT NULL,"
                + "\n    description     TEXT,"
                + "\n    vehicleLocation TEXT,"
                + "\n    vehicleBrand    TEXT,"
                + "\n    vehicleModel    TEXT,"
                + "\n    vehicleYear     INTEGER,"
                + "\n    mileage         INTEGER,"
                + "\n    proposedPrice   REAL,"
                + "\n    isAvailable     INTEGER NOT NULL,"
                + "\n    FOREIGN KEY (seller) REFERENCES Users (id),"
                + "\n    UNIQUE(seller, title) ON CONFLICT IGNORE"
                + "\n)";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            return true;

        } catch (SQLException | NullPointerException e) { warn(e.getMessage()); }
        return false;
    }
}
