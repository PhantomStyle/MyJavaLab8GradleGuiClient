package dao;

import javafx.scene.paint.Color;

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
        String passwordFromDb = "";
        while (rs.next()) {
            passwordFromDb = rs.getString("PASSWORD");
        }
        return (password.equals(passwordFromDb));
    }

    public void incrementMessagesAmount(Connection conn, String login) throws SQLException {
        PreparedStatement stmt;
        String query = "UPDATE CREDENTIALS SET MESSAGES_AMOUNT = MESSAGES_AMOUNT + 1 WHERE LOGIN=?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, login);
        stmt.executeUpdate();
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

    public Color getColor(Connection conn, String login) throws SQLException {
        PreparedStatement stmt;
        String query = "SELECT color FROM Credentials WHERE login=?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, login);
        ResultSet rs = stmt.executeQuery();
        String color = "";
        while (rs.next()) {
            color = rs.getString("color");
            switch (color) {
                case "BLACK":
                    return Color.BLACK;
                case "RED":
                    return Color.RED;
                case "GREEN":
                    return Color.GREEN;
                case "BLUE":
                    return Color.BLUE;
            }
        }
        return null;
    }

    public void updateColor(Connection conn, String login, Color color) throws SQLException {
        PreparedStatement stmt;
        String query = "UPDATE CREDENTIALS SET COLOR=? WHERE LOGIN=?";
        stmt = conn.prepareStatement(query);
        if(color.equals(Color.BLACK)){
            stmt.setString(1, "BLACK");
        }
        if(color.equals(Color.GREEN)){
            stmt.setString(1, "GREEN");
        }
        if(color.equals(Color.RED)){
            stmt.setString(1, "RED");
        }
        if(color.equals(Color.BLUE)){
            stmt.setString(1, "BLUE");
        }
        stmt.setString(2, login);
        stmt.executeUpdate();
    }

    public void nullingAmountOfMessages(Connection conn) throws SQLException {
        PreparedStatement stmt;
        String query = "UPDATE CREDENTIALS SET MESSAGES_AMOUNT=0";
        stmt = conn.prepareStatement(query);
        stmt.executeUpdate();
    }
}
