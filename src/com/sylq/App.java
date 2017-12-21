/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sylq;

import java.util.Arrays;

import com.sylq.Business.Database;
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
        Database.runFixtures();

        Utils.APPLICATION_LOG_LEVEL = Utils.LogLevel.INFO;

        info("Sale Ho! lasses and lads! Sale!");
        info("\nWelcome to this humble car sales application.\nSign in now to enjoy our incredible services!\n");
        while (runProgram) {
            // System.out.flush();
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
            case 3: runProgram = false;                  break;
            default:                                     break;
        }
    }

    private static void menuConnectedAsAdmin() {
        switch (Interact.menu(Arrays.asList("Users", "Sales", "Disconnect"))) {
            case 1: currentMenu = Menu.Users;     break;
            case 2: currentMenu = Menu.Sales;     break;
            case 3: currentUser = null;           break;
            default:                              break;
        }
    }

    private static void menuConnectedAsUser() {
        switch (Interact.menu(Arrays.asList("Sales", "Disconnect"))) {
            case 1: currentMenu = Menu.Sales;     break;
            case 2: currentUser = null;           break;
            default:                              break;
        }
    }

    private static void menuUsers() {
        currentMenu = Menu.Users; // Default

        switch (Interact.menu(Arrays.asList("List", "New", "Detail", "Edit", "Delete", "Contact seller", "Back to main menu"))) {
            case 1: Database.getUsers();                  break;
            case 2: Interact.createUser();                break;
            case 3: Interact.viewUserDetail();            break;
            case 4: Interact.editUser();                  break;
            case 5: Interact.deleteUser();                break;
            case 6: Interact.contactSeller(currentUser);  break;
            case 7: currentMenu = null;                   break;
            default:                                      break;
        }
    }

    private static void menuSales() {
        currentMenu = Menu.Sales; // Default

        switch (Interact.menu(Arrays.asList("List", "New", "Detail", "Edit", "Delete", "Back to main menu"))) {
            case 1: Database.getSales();              break;
            case 2: Interact.createSale(currentUser); break;
            case 3: Interact.viewSaleDetail();        break;
            case 4: Interact.editSale();              break;
            case 5: Interact.deleteSale();            break;
            case 6: currentMenu = null;               break;
            default:                                  break;
        }
    }
}