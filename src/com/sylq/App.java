/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sylq;

import java.util.Arrays;

import com.sylq.Common.Utils;
import com.sylq.Model.Rank;
import com.sylq.Model.User;
import com.sylq.Business.Interact;

import static com.sylq.Common.Utils.*;


/**
 *
 * @author kiminonaha
 */
public class App {

    private enum Menu {
        Sales, Users
    }

    private static User currentUser = null;
    private static Menu currentMenu = null;
    private static boolean runProgram = true;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Utils.APPLICATION_LOG_LEVEL = Utils.LogLevel.INFO;

        Interact.initDB();

        success("Sale Ho! lasses and lads! Sale!");
        info("\nWelcome to this humble car sales application.\nSign in now to enjoy our incredible services!\n");

        while (runProgram) {
            if (currentUser == null) {
                menuNotConnected();
            }
            else if (currentMenu == Menu.Users) {
                menuUsers();
            }
            else if (currentMenu == Menu.Sales) {
                menuSales();
            }
            else if (currentUser.getRank() == Rank.Admin) {
                menuConnectedAsAdmin();
            }
            else {
                menuConnectedAsUser();
            }
        }
    }

    private static void menuNotConnected() {
        switch (Interact.menu(Arrays.asList("Subscribe", "Connect", "Quit"))) {
            case 1: currentUser = Interact.createUser(); break;
            case 2: currentUser = Interact.connect();    break;
            case 3: exitProgram();                       break;
            default: invalidAction();                    break;
        }
    }

    private static void menuConnectedAsAdmin() {
        switch (Interact.menu(Arrays.asList("Users", "Sales", "Disconnect"))) {
            case 1: currentMenu = Menu.Users; break;
            case 2: currentMenu = Menu.Sales; break;
            case 3: disconnect();             break;
            default: invalidAction();         break;
        }
    }

    private static void menuConnectedAsUser() {
        switch (Interact.menu(Arrays.asList("Sales", "Disconnect"))) {
            case 1: currentMenu = Menu.Sales; break;
            case 2: disconnect();             break;
            default: invalidAction();         break;
        }
    }

    private static void menuUsers() {
        currentMenu = Menu.Users; // Default

        switch (Interact.menu(Arrays.asList("List", "New", "Detail", "Edit", "Delete", "Back to main menu"))) {
            case 1: Interact.listUsers();                  break;
            case 2: Interact.createUser();                break;
            case 3: Interact.viewUserDetail();            break;
            case 4: Interact.editUser();                  break;
            case 5: Interact.deleteUser();                break;
            case 6: exitMenu();                           break;
            default: invalidAction();                     break;
        }
    }

    private static void menuSales() {
        currentMenu = Menu.Sales; // Default

        switch (Interact.menu(Arrays.asList("List", "Contact seller", "New", "Detail", "Edit", "Delete", "Back to main menu"))) {
            case 1: Interact.listSales();                break;
            case 2: Interact.contactSeller(currentUser); break;
            case 3: Interact.createSale(currentUser);    break;
            case 4: Interact.viewSaleDetail();           break;
            case 5: Interact.editSale();                 break;
            case 6: Interact.deleteSale();               break;
            case 7: exitMenu();                          break;
            default: invalidAction();                    break;
        }
    }

    private static void exitProgram() {
        print("See you around!");
        runProgram = false;
    }

    private static void disconnect() {
        print(currentUser.getEmail() + " disconnected successfully.");
        currentUser = null;
    }

    private static void exitMenu() {
        print("Back to main menu.");
        currentMenu = null;
    }

    private static void invalidAction() {
        warn("Not a valid action.");
    }
}