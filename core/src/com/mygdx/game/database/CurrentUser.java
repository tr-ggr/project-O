package com.mygdx.game.database;

public class CurrentUser{
    private static CurrentUser instance = null;
    private String username;
    private String password;
    private int email;
    private int id;
    private int highscore;
    private int money;

    private CurrentUser() {
    }

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public void setUser(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.highscore = user.getHighscore();
        this.id = user.getUserID();
        this.money = user.getMoney();
    }

   public void removeUser(){
        this.username = null;
        this.password = null;
        this.highscore = 0;
        this.id = 0;
        this.money = 0;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEmail() {
        return email;
    }

    public void setEmail(int email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
