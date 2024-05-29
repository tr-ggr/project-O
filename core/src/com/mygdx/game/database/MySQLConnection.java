package com.mygdx.game.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/oop2final";
    public static final String USER = "root";
    public static final String PASS = "";

    static Connection getConnection(){
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL,USER,PASS);
            System.out.println("DB connected");
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return c;
    }
}
