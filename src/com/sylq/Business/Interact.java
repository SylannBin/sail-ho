package com.sylq.Business;

import com.sylq.Model.Sale;
import com.sylq.Model.Rank;
import com.sylq.Model.User;

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
     * Init Database if needed and propose to fill it with data if tables were created.
     * Else do nothing.
     */
    public static void initDB() {
        Scanner scin = new Scanner(System.in);
        info("Checking database...");
        if (Database.initDB()) {
            print("Add example data? yes/No");
            String yn = scin.nextLine();
            if (yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("yes")) {
                Database.fixtures();
            }
        } else {
            info("Database is ok.");
        }
    }

    /**
     *
     * @return
     */
    public static User connect() {
        Scanner scin = new Scanner(System.in);

        print("Please enter your credentials.");
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

        success("Successfully authenticated as " + authenticatedUser.getEmail());

        return authenticatedUser;
    }

    /**
     * Create a new user.
     * @return The newly created user.
     */
    public static User createUser() {
        Scanner scin = new Scanner(System.in);

        String email = newField(scin, "email");
        if (email.isEmpty() || !emailIsValid(email)) {
            warn("Email is not valid");
            return null;
        }
        if (Database.getUserByEmail(email) != null) {
            warn("Email is not available");
            return null;
        }

        String password = newField(scin, "password");
        System.out.print("Confirm: ");
        if (!Objects.equals(password, scin.nextLine())) {
            error("Passwords don't match");
            return null;
        }

        print("Optional information");
        String firstname = newField(scin, "First name");
        String lastname  = newField(scin, "Last name");
        String address   = newField(scin, "Address");
        String phone     = newField(scin, "Phone");

        User newUser = new User(email, password, firstname, lastname, address, phone);

        if (Database.createUser(newUser) == null) {
            error("Subscription failed");
            return null;
        }

        success("User " + newUser.getEmail() + " successfully created!");

        return newUser;
    }

    /**
     * Create a new sale associated to the provided seller.
     * @param seller Owner of the Sale.
     * @return The newly created sale.
     */
    public static Sale createSale(User seller) {
        Scanner scin = new Scanner(System.in);

        String title = newField(scin, "Title");

        if (title.isEmpty() || Database.getSaleBySellerAndTitle(seller, title) != null) {
            warn("The title is required.");
            return null;
        }

        String description     = newField(scin, "Description");
        String vehicleLocation = newField(scin, "Vehicle location");
        String vehicleBrand    = newField(scin, "Vehicle brand");
        String vehicleModel    = newField(scin, "Vehicle model");

        String s_vehicleYear   = newField(scin, "Vehicle year (Business year such as 2017)");
        Integer vehicleYear    = validateBusinessYear(s_vehicleYear);

        String s_mileage       = newField(scin, "Mileage (Positive integer such as 105)");
        Integer mileage        = validatePositiveInteger(s_mileage);

        String s_proposedPrice = newField(scin, "Proposed price (Positive float such as 105.46)");
        Float  proposedPrice   = validatePositiveFloat(s_proposedPrice);

        Sale newSale = new Sale(
                seller, title, description, vehicleLocation, vehicleBrand,
                vehicleModel, vehicleYear, mileage, proposedPrice);

        if (Database.createSale(newSale) == null) {
            error("Creation of the sale table has failed");
            return null;
        }

        success("New sale " + newSale.getTitle() + " successfully added!");

        return newSale;
    }

    /**
     * Use Database method to list existing users.
     */
    public static void listUsers() {
        Database.getUsers();
    }

    /**
     * Use Database method to list existing sales.
     */
    public static void listSales() {
        Database.getSales();
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

        print("Modifying user " + user.getId() + ": " + user.getEmail());

        Scanner scin = new Scanner(System.in);

        user.setEmail(editField(scin, "Email"));

        if (!emailIsValid(user.getEmail())) {
            warn("Email is not valid");
            return;
        }
        user.setPassword(editPassword(scin, user.getPassword()));

        String rank = editField(scin, "Rank" + helpRank());
        user.setRank(validateRank(rank));

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

        print("Modifying sale " + sale.getId() + ": " + sale.getTitle());

        Scanner scin = new Scanner(System.in);

        sale.setTitle(editField(scin, "title"));
        sale.setDescription(editField(scin, "description"));
        sale.setVehicleLocation(editField(scin, "vehicle location"));
        sale.setVehicleBrand(editField(scin, "vehicle brand"));
        sale.setVehicleModel(editField(scin, "vehicle model"));

        String vehicleYear = editField(scin, "Vehicle year (Business year such as 2017)");
        sale.setVehicleYear(validateBusinessYear(vehicleYear));

        String mileage = editField(scin, "Mileage (Positive integer such as 105)");
        sale.setMileage(validatePositiveInteger(mileage));

        String proposedPrice = editField(scin, "Proposed price (Positive float such as 105.46)");
        sale.setProposedPrice(validatePositiveFloat(proposedPrice));

        Database.updateSale(sale);
    }

    /**
     * Delete the selected user. (Prompt for an id)
     */
    public static void deleteUser() {
        int userId = promptId();
        Database.deleteUser(userId);
        success("User deleted");
    }

    /**
     * Delete the selected user. (Prompt for an id)
     */
    public static void deleteSale() {
        int saleId = promptId();
        Database.deleteSale(saleId);
        success("Sale deleted");
    }

    /**
     * Send a message to the seller of an sale. (prompt for an id)
     * @param buyer current user of the app, who decides to contact the owner of a car.
     */
    public static void contactSeller(User buyer) {
        int id = promptId();
        Sale sale = Database.getSale(id);
        if (sale == null) {
            error("Invalid sale id");
            return;
        }
        sale.contact();
        // TODO: Add a table for messages. Users can consult their messages
        // Database.addMessage(buyer, sale.getSeller());
    }

    /**
     * Accept the offer on a specified sale.
     * Makes it unavailable and tranfers money between accounts.
     */
    public static void acceptOffer(User buyer) {
        Integer id = promptId();
        Sale sale = Database.getSale(id);
        if (sale != null)
            sale.sold(buyer);
    }

    /**
     * Print a menu from the provided list of choices and prompt user to choose an action.
     * @param choices List of labels for possible choices of this menu.
     */
    public static int menu(List<String> choices) {
        print("Choose an action:");
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
        int choice;
        try {
            choice = scin.nextInt();
            assert choice >= 0;
            assert choice <= maxChoice;
            return choice;
        } catch (InputMismatchException | AssertionError e) {
            return 0;
        }
    }

    /**
     * Prompt for a valid user id.
     * @return The user having the input id or null.
     */
    private static int promptId() {
        Scanner scin = new Scanner(System.in);
        System.out.print("Which one? (specify id): ");
        return scin.nextInt();
    }

    /**
     * Ask wether to modify the provided field or not.
     * @param scin
     * @param field
     * @return either true or false
     */
    private static boolean promptYesNo(Scanner scin, String field) {
        System.out.print("Change " + field + "? (y/n)");
        String yn = scin.next();
        scin.nextLine(); // consume newline now. Avoids next newLine to be skipped.

        return yn.equalsIgnoreCase("y");
    }

    /**
     * Prompt for a new value for the provided field.
     * @param scin
     * @param field
     * @return
     */
    private static String newField(Scanner scin, String field) {
        System.out.print(field + ": ");
        return scin.nextLine();
    }

    /**
     * Prompt for a value to replace the old one.
     * @param scin scanner
     * @param field indicate what is edited
     * @return User input (string)
     */
    private static String editField(Scanner scin, String field) {
        if (promptYesNo(scin, field)) {
            System.out.print("New value: ");
            return scin.nextLine();
        }
        return null;
    }

    /**
     * Ensure a string is a valid business year.
     * @param input value to verify
     * @return The new value in case of success else null.
     */
    private static Integer validateBusinessYear(String input) {
        if (input == null)
            return null; // null value is handled
        try {
            Integer verifiedInput = Integer.parseInt(input);
            if (verifiedInput < 1900 || 2100 < verifiedInput)
                throw new NumberFormatException();
            return verifiedInput;
        } catch (NumberFormatException e) {
            error("Expected a valid year but got " + input);
            return null;
        }
    }

    /**
     * Ensure a string is a valid positive integer.
     * @param input value to verify
     * @return The new value in case of success else null.
     */
    private static Integer validatePositiveInteger(String input) {
        if (input == null)
            return null; // null value is handled
        try {
            Integer verifiedInput = Integer.parseInt(input);
            if (verifiedInput < 0)
                throw new NumberFormatException();
            return verifiedInput;
        } catch (NumberFormatException e) {
            error("Expected a positive number but got " + input);
            return null;
        }
    }

    /**
     * Ensure a string is a valid floating point number.
     * @param input value to verify
     * @return The new value in case of success else null.
     */
    private static Float validatePositiveFloat(String input) {
        if (input == null)
            return null; // null value is handled
        try {
            Float verifiedInput = Float.parseFloat(input);
            if (verifiedInput < 0.0f)
                throw new NumberFormatException();
            return verifiedInput;
        } catch (NumberFormatException e) {
            error("Expected a positive float number but got " + input);
            return null;
        }
    }

    /**
     * Build a detailed help for the rank type.
     * @return The help string
     */
    private static String helpRank() {
        StringBuilder detail = new StringBuilder(" (Correct values are ");
        for (Rank r: Rank.values()) {
            detail.append(r.name());
            detail.append(", ");
        }
        detail.setLength(detail.length() - 2); // remove last ', '
        detail.append("): ");
        return detail.toString();
    }

    /**
     * Ensure a string is a valid rank.
     * @param input value to verify
     * @return The new value in case of success else null.
     */
    private static Rank validateRank(String input) {
        try {
            return Rank.valueOf(input);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Propose to change the password.
     * If changing, ask the old password and verify that it is valid.
     * If valid, ask for a new password twice, they must match.
     * @param scin scanner
     * @param oldPassword
     * @return The new value in case of success else the old one.
     */
    private static String editPassword(Scanner scin, String oldPassword) {
        if (!promptYesNo(scin, "password")) {
            return null;
        }

        System.out.print("Verify old password: ");
        String inputPassword = scin.nextLine();

        if (!Objects.equals(oldPassword, inputPassword)) {
            error("Password is not valid");
            return null;
        }

        System.out.print("New password: ");
        String newPassword = scin.nextLine();

        System.out.print("Confirm: ");
        if (!Objects.equals(newPassword, scin.nextLine())) {
            error("Passwords don't match");
            return null;
        }
        return newPassword;
    }
}
