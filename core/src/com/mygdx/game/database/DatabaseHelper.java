package com.mygdx.game.database;


import java.sql.*;
import java.util.ArrayList;


public class DatabaseHelper {
    private static DatabaseHelper instance;


    private DatabaseHelper(){
        try(Connection c = MySQLConnection.getConnection();
            Statement s = c.createStatement()) {
            String table = "CREATE TABLE IF NOT EXISTS tblUser (" +
                    "userID INT PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(256) UNIQUE," +
                    "password VARCHAR(256) NOT NULL, " +
                    "highscore INT(255) NOT NULL, " +
                    "money INT(255) NOT NULL)";

            s.execute(table);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseHelper getInstance(){
        if (instance == null){
            instance = new DatabaseHelper();
        }

        return instance;

    }

    public boolean register(String username, String password){
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement checkUser = c.prepareStatement("SELECT * FROM tblUser WHERE username = ?")
        ) {
            System.out.println(username + " " + password + " " );

            checkUser.setString(1, username);
            ResultSet rs = checkUser.executeQuery();

            if (!rs.next()) {
                PreparedStatement s = c.prepareStatement("INSERT INTO tblUser (username, password, highscore, money) VALUES (?, ?, 0, 0)");
                s.setString(1, username);
                s.setString(2, password);
                s.execute();

                System.out.println("User registered successfully!");
                return true;
            } else {

                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User login(String username, String password){
        try(
                Connection c = MySQLConnection.getConnection();
                Statement s = c.createStatement();
        ) {
            String query = "SELECT * FROM tblUser WHERE username = '" + username + "' AND password = '" + password + "'";

            ResultSet resultset = s.executeQuery(query);
            if (resultset.next() ) {

                int id = s.getResultSet().getInt("userId");
                String name = s.getResultSet().getString("username");
                User user = new User(id, name, password);

                return user;
            } else {
                System.out.println("Invalid username or password");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<User> TopUsers(){
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM tbluser ORDER BY highscore DESC LIMIT 5";

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                int ID = rs.getInt("userID");
                String un = rs.getString("username");
                int score = rs.getInt("highscore");
                int unmoney = rs.getInt("money");
                users.add(new User( ID, un, score, unmoney));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean saveUser(int ID, int score, int currentmoney){
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement s = c.prepareStatement("UPDATE tbluser SET highscore = ? ," +
                    "money = ? WHERE userID = ?")){

            s.setInt(1, score);
            s.setInt(2, currentmoney);
            s.setInt(3,ID);
            s.executeUpdate();

            return true;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }




}
