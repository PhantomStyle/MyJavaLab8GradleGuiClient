package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CredentialsDao {
    public boolean checkUser(Connection conn, String login, String password) throws SQLException {
        PreparedStatement stmt;
        String query = "SELECT password FROM Credentials WHERE login=?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, login);
        ResultSet rs = stmt.executeQuery();
        String passwordFromDb = rs.getString("password");
        return (password.equals(passwordFromDb));
    }

    public void incrementMessagesAmount(Connection conn, String login) throws SQLException {
        PreparedStatement stmt;
        String query = "UPDATE CREDENTIALS SET MESSAGES_AMOUNT = MESSAGES_AMOUNT + 1 WHERE LOGIN=?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, login);
        stmt.executeQuery();
    }

    public Map<String, Integer> getMessagesStatistic(Connection conn) throws SQLException {
        Map<String, Integer> statisticMap = new HashMap<>();
        PreparedStatement stmt;
        String query = "SELECT LOGIN, MESSAGES_AMOUNT FROM CREDENTIALS";
        stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            statisticMap.put(rs.getString("login"), rs.getInt("messages_amount"));
        }
        return statisticMap;
    }
}
