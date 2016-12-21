package chat;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author João Leite 8140453
 */
public class ChatDB {

    private final String driver = "org.sqlite.JDBC";
    private final String db = "jdbc:sqlite:chatdb.db";
    
    public boolean checkUserDB(String username){ // check if a username allready exists
        
        Connection con = null;
        Statement stmt = null;
        
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(db);
            String query = "SELECT username FROM users WHERE username='" + username + "'";
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()){ // username exists
                stmt.close();
                rs.close();
                con.close();
                return true;
            }
            else    // username dont exist
                return false;

        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return false;
    }

    public boolean loginDB(String username, String password) throws SQLException { // verify login in database

        Connection con = null;
        Statement stmt = null;
        
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(db);
            String query = "SELECT username,password FROM users WHERE username='" + username + "' AND password='" + password + "'";
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) // username and password exists
            {
                stmt.close();
                rs.close();
                con.close();
                return true;
            }
            else
                return false;

        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        return false;
    }

    public boolean registerDB(String username, String password) { // register a user in database

        Connection con = null;
        Statement stmt = null;
        
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(db);
            con.setAutoCommit(false);
            String query = "INSERT INTO users (username,password) VALUES ('" + username + "','" + password + "')";
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            
            stmt.close();
            con.commit();
            con.close();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        if(checkUserDB(username))
            return true;
        else
            return false;
    }
}
