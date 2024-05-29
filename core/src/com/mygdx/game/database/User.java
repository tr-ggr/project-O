package com.mygdx.game.database;

public class User{
    int userID;
    String username;
    String password;
    int highscore;
    int money;


    public User(int userID, String username, String password, int highscore, int money) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.highscore = highscore;
        this.money = money;
    }

    public User(int userID, String username, int highscore, int money) {
        this.userID = userID;
        this.username = username;
        this.highscore = highscore;
        this.money = money;
    }

    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getHighscore() {
        return highscore;
    }

    public int getMoney() {
        return money;
    }

}
