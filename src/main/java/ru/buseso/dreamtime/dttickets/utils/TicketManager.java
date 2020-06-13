package ru.buseso.dreamtime.dttickets.utils;

import ru.buseso.dreamtime.dttickets.DTTickets;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketManager {
    public static boolean hasAccount(String UUID) {
        try {
            ResultSet rs = DTTickets.mysql.query("SELECT * FROM accounts WHERE `UUID`='" + UUID + "'");
            if (rs.next()) {
                return (rs.getString("UUID") != null);
            }
        }
        catch (SQLException sQLException) {}
        return false;
    }

    public static boolean hasAccountByName(String Name) {
        try {
            ResultSet rs = DTTickets.mysql.query("SELECT * FROM accounts WHERE `NAME`='" + Name + "'");
            if (rs.next()) {
                return (rs.getString("UUID") != null);
            }
        }
        catch (SQLException sQLException) {}
        return false;
    }

    public static void createAccount(String UUID, String Name, String Password, String Email) {
        if (!hasAccount(UUID)) {
            DTTickets.mysql.update("INSERT INTO accounts(UUID, USERNAME, EMAIL, PASSWORD, LASTLOGIN, FIRSTLOGIN," +
                    " ACCOUNTRANK) VALUES ('" + UUID + "', '" + Name + "', '" + Email + "', '" + Password + "', '" +
                    System.currentTimeMillis() + "', '" + System.currentTimeMillis() + "', 0)");
        } else {
            updateName(UUID, Name);
        }
    }

    public static void updateName(String UUID, String Name) {
        DTTickets.mysql.update("UPDATE accounts SET `NAME`='" + Name + "' WHERE `UUID`='" + UUID + "'");
    }

    public static void updatePassword(String UUID, String Password) {
        DTTickets.mysql.update("UPDATE accounts SET `PASSWORD`='" + Password + "' WHERE `UUID`='" + UUID + "'");
    }

    public static void updateOtherPassword(String Name, String Password) {
        DTTickets.mysql.update("UPDATE accounts SET `PASSWORD`='" + Password + "' WHERE `NAME`='" + Name + "'");
    }
}
