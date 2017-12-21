package com.sylq.Business;

import com.sylq.Model.Sale;
import com.sylq.Model.Rank;
import com.sylq.Model.User;

import java.text.ParseException;
import java.util.*;

import static com.sylq.Common.Utils.*;


/**
 * Not connected
 *   1 Subscribe
 *   2 Connect
 *   3 Quit
 *
 * Connected
 *   1 View Users (admin only)
 *   2 View Sales
 *   3 Disconnect
 *
 * Sales | Users
 *   1 List
 *   2 New
 *   3 View detail
 *   4 Edit
 *   5 Delete
 *   6 Contact seller (sales only)
 *   7 Back
 */
public class Interact {

    /**
     *
     * @return
     */
    public static User connect() {
        Scanner scin = new Scanner(System.in);

        info("Please enter your credentials.");
        System.out.print("Login (email): ");
        String email = scin.nextLine();

        User authenticatedUser = Database.getUserByEmail(email);
        if (authenticatedUser == null) {
            error("Invalid email");
            return null;
        }

        System.out.print("Password: ");
        String password = scin.nextLine();

        if (!password.equals(authenticatedUser.getPassword())) {
            error("Invalid password");
            return null;
        }

        return authenticatedUser;
    }

    /**
     *
     * @return
     */
    public static User createUser() {
        Scanner scin = new Scanner(System.in);
        System.out.print("Enter your email: ");
        String email = scin.nextLine();

        if (Database.getUserByEmail(email) != null) {
            warn("Email is not available");
            return null;
        }

        System.out.print("Enter your password: ");
        String password = scin.nextLine();

        System.out.print("Confirm your password: ");
        String password2 = scin.nextLine();

        if (!password.equals(password2)) {
            error("Passwords don't match: " + password + ", " + password2);
            return null;
        }

        info("Optional information");
        String firstname = editField(scin, "First name");
        String lastname  = editField(scin, "Last name");
        String address   = editField(scin, "Address");
        String phone     = editField(scin, "Phone");

        User newUser = new User(email, password, firstname, lastname, address, phone);

        if (Database.createUser(newUser) == null) {
            error("Subscription failed");
            return null;
        }
        return newUser;
    }

    /**
     *
     * @param seller
     */
    public static void createSale(User seller) {
        Scanner scin = new Scanner(System.in);

        String title           = editField(scin, "Title");
        String description     = editField(scin, "Description");
        String vehicleLocation = editField(scin, "Vehicle location");
        String vehicleBrand    = editField(scin, "Vehicle brand");
        String vehicleModel    = editField(scin, "Vehicle model");
        Integer vehicleYear    = editBusinessYear(scin, "Vehicle year");
        Integer mileage        = editPositiveInteger(scin, "Mileage");
        Float  proposedPrice   = editPositiveFloat(scin, "Proposed price");

        Sale newSale = new Sale(
                seller, title, description, vehicleLocation, vehicleBrand,
                vehicleModel, vehicleYear, mileage, proposedPrice);

        if (Database.createSale(newSale) == null) {
            error("Creation of the sale table has failed");
        }
    }

    /**
     * Print the detail of the selected user. (Prompt for an id)
     */
    public static void viewUserDetail() {
        User user = Database.getUser(promptId());
        if (user == null) {
            warn("Unknown user id");
            return;
        }
        user.describe();
    }

    /**
     * Print the detail of the selected user. (Prompt for an id)
     */
    public static void viewSaleDetail() {
        Sale sale = Database.getSale(promptId());
        if (sale == null) {
            warn("Unknown user id");
            return;
        }
        sale.describe();
    }

    /**
     * Edit the selected user. (Prompt for an id)
     */
    public static void editUser() {
        User user = Database.getUser(promptId());
        if (user == null) {
            warn("Unknown user id");
            return;
        }

        Scanner scin = new Scanner(System.in);

        user.setEmail(editField(scin, "Email"));
        user.setPassword(editPassword(scin, user.getPassword()));
        user.setRank(editRank(scin, "Rank"));
        user.setFirstname(editField(scin, "First name"));
        user.setLastname(editField(scin, "Last name"));
        user.setAddress(editField(scin, "Address"));
        user.setPhone(editField(scin, "Phone"));

        Database.updateUser(user);
    }

    /**
     * Edit the selected sale. (Prompt for an id)
     */
    public static void editSale() {
        Sale sale = Database.getSale(promptId());
        if (sale == null) {
            warn("Unknown sale id");
            return;
        }

        Scanner scin = new Scanner(System.in);

        sale.setTitle(editField(scin, "title"));
        sale.setDescription(editField(scin, "description"));
        sale.setVehicleLocation(editField(scin, "vehicle location"));
        sale.setVehicleBrand(editField(scin, "vehicle brand"));
        sale.setVehicleModel(editField(scin, "vehicle model"));

        sale.setVehicleYear(editBusinessYear(scin, "Vehicle year"));
        sale.setMileage(editPositiveInteger(scin, "Mileage"));
        sale.setProposedPrice(editPositiveFloat(scin, "Proposed price"));

        Database.updateSale(sale);
    }

    /**
     * Delete the selected user. (Prompt for an id)
     */
    public static void deleteUser() {
        int userId = promptId();
        Database.deleteUser(userId);
    }

    /**
     * Delete the selected user. (Prompt for an id)
     */
    public static void deleteSale() {
        int saleId = promptId();
        Database.deleteSale(saleId);
    }

    /**
     * Send a message to the seller of an sale. (prompt for an id)
     * @param buyer current user of the app, who decides to contact the owner of a car.
     */
    public static void contactSeller(User buyer) {
        Scanner scin = new Scanner(System.in);
        Integer id = editPositiveInteger(scin, "Sale id: ");
        Sale sale = Database.getSale(id);
        if (sale != null)
            sale.contact();
        // TODO: Add a table for messages. Users can consult their messages
        // Database.addMessage(buyer, sale.getSeller());
    }

    /**
     * Accept the offer on a specified sale.
     * Makes it unavailable and tranfers money between accounts.
     */
    public static void acceptOffer(User buyer) {
        Scanner scin = new Scanner(System.in);
        Integer id = editPositiveInteger(scin, "Sale id: ");
        Sale sale = Database.getSale(id);
        if (sale != null)
            sale.sold(buyer);
    }

    /**
     * Print a menu from the provided list of choices and prompt user to choose an action.
     * @param choices List of labels for possible choices of this menu.
     */
    public static int menu(List<String> choices) {
        info("Choose an action:");
        for (int i = 0; i < choices.size(); i++) {
            System.out.printf("  %d %s\n", i + 1, choices.get(i));
        }
        return validateChoice(choices.size());
    }

    /**
     * Ask a number between 1 and the provided max choice from the user.
     * Validate it.
     * @param maxChoice maximum relevant value for this menu.
     * @return A number between 1 and the max choice or 0 in case of a wrong input.
     */
    private static int validateChoice(int maxChoice) {
        Scanner scin = new Scanner(System.in);
        int choice = scin.nextInt();
        if (choice < 0 || choice > maxChoice)
            choice = 0;

        return choice;
        // String input = "2"; //System.console().readLine();
        // try {
        //     int choice = Integer.parseInt(input);
        //     assert choice > 0;
        //     assert choice <= maxChoice;
        //     return choice;
        // } catch (NumberFormatException | AssertionError e) {
        //     return 0;
        // }
    }

    /**
     * Prompt for a valid user id.
     * @return The user having the input id or null.
     */
    private static int promptId() {
        Scanner scin = new Scanner(System.in);
        System.out.print("Enter id: ");
        return scin.nextInt();
    }

    /**
     * Prompt the user to enter a value that matches the field description.
     * @param scin scanner
     * @param field indicate what is edited
     * @return User input (string)
     */
    private static String editField(Scanner scin, String field) {
        System.out.print(field + ": ");
        return scin.nextLine();
    }

    /**
     * Prompt for a valid business year and verify it.
     * @param scin scanner
     * @param field indicate what is edited
     * @return The new value in case of success else null.
     */
    private static Integer editBusinessYear(Scanner scin, String field) {
        String input = editField(scin, field + " (Business year such as 2017): ");
        try {
            Integer verifiedInput = Integer.parseInt(input);
            assert verifiedInput <= 2100;
            assert verifiedInput >= 1900;
            return verifiedInput;
        } catch (AssertionError | NumberFormatException e) {
            error("Expected a valid year but got " + input);
            return null;
        }
    }

    /**
     * Prompt for a valid positive integer and verify it.
     * @param scin scanner
     * @param field indicate what is edited
     * @return The new value in case of success else null.
     */
    private static Integer editPositiveInteger(Scanner scin, String field) {
        System.out.print("Positive integer ");
        String input = editField(scin, field + " (Positive integer such as 105): ");
        try {
            Integer verifiedInput = Integer.parseInt(input);
            assert verifiedInput >= 0;
            return verifiedInput;
        } catch (AssertionError | NumberFormatException e) {
            error("Expected a positive number but got " + input);
            return null;
        }
    }

    /**
     * Prompt for a valid positive floating point number and verify it.
     * @param scin scanner
     * @param field indicate what is edited
     * @return The new value in case of success else null.
     */
    private static Float editPositiveFloat(Scanner scin, String field) {
        String input = editField(scin, field + " (Positive float such as 105.46): ");
        try {
            Float verifiedInput = Float.parseFloat(input);
            assert verifiedInput >= 0.0f;
            return verifiedInput;
        } catch (AssertionError | NumberFormatException e) {
            error("Expected a positive float number but got " + input);
            return null;
        }
    }

    /**
     * Prompt for a valid formatted date and verify it.
     * @param scin scanner
     * @param field indicate what is edited
     * @return The new value in case of success else null.
     */
    private static Date editDate(Scanner scin, String field) {
        String input = editField(scin, field + " (Format dd/mm/yyyy): ");
        try {
            return LOCALE_FORMAT.parse(input);
        } catch (ParseException e) {
            error("Expected a valid date but got " + input);
            return null;
        }
    }

    /**
     * Prompt for a valid Rank and verify it.
     * @param scin scanner
     * @param field indicate what is edited
     * @return The new value in case of success else null.
     */
    private static Rank editRank(Scanner scin, String field) {
        StringBuilder detail = new StringBuilder(" (Correct values are ");
        for (Rank r: Rank.values())
            detail.append(r.name());
        detail.append("): ");
        String input = editField(scin, field + detail);
        try {
            return Rank.valueOf(input);
        } catch (IllegalArgumentException e) {
            error("Expected a valid Rank but got " + input);
            return null;
        }
    }

    /**
     * Prompt for the old password and a new one. Verify that they match.
     * @param scin scanner
     * @param oldPassword
     * @return The new value in case of success else the old one.
     */
    private static String editPassword(Scanner scin, String oldPassword) {
        System.out.print("Old password: ");
        String inputPassword = scin.nextLine();

        if (oldPassword != null && !oldPassword.equals(inputPassword)) {
            error("Password is not valid");
            return null;
        }

        System.out.print("New password: ");
        String newPassword = scin.nextLine();

        if (oldPassword != null && !oldPassword.equals(newPassword)) {
            error("Passwords don't match");
            return oldPassword;
        }
        return newPassword;
    }
}
