package chat;

import java.sql.SQLException;
import org.json.JSONObject;

/**
 * @author João Leite 8140453
 */
public class ChatProtocol {

    public ChatProtocol() {
    }

    public String processAuth(JSONObject input) throws SQLException {

        ChatDB DBcheck = new ChatDB();

        switch (input.getString("command")) {
            case "/login":
                if (DBcheck.loginDB(input.getString("username"), input.getString("password"))) {
                    return "Login Successful!";
                } else {
                    return "Login Failed!";
                }
            case "/register":
                if (DBcheck.checkUserDB(input.getString("username"))) {
                    return "Username allready registered!";
                } else {
                    DBcheck.registerDB(input.getString("username"), input.getString("password"));
                    return "User registered successful! You can now login...";
                }
            default:
                return "Command not found! use /login or /register";
        }
    }

    public String processChat(JSONObject input) {

        switch (input.getString("command")) {
            case "/help": break;
                
            default:
                return "Command not found! use /help to find the command";
        }
        return "";
    }
}
