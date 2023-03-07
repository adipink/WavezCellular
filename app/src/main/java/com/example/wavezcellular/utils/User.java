package com.example.wavezcellular.utils;

import android.os.Bundle;

public class User {
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

    public static String getGuest(Bundle bundle) {
        Object guest = bundle.get("guest");
        if(guest == null) {
            long id = System.currentTimeMillis();
            guest = "Guest" + id;
            bundle.putString("guest", (String) guest);
        }
        return (String) guest;
    }
}
