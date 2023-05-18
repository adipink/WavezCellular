package com.example.wavezcellular.utils;



public class User {
    public static final double DEFAULTLAT = 32.120998;
    public static final double DEFAULTLON = 34.805779;
    private String name;
    private String email;

    public User(){

    }

    public User(String name, String email){
        setName(name);
        setEmail(email);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String generateGuest() {
            long id = System.currentTimeMillis();
        return "Guest" + id;
    }
}
