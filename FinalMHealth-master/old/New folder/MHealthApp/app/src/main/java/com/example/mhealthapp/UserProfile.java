package com.example.mhealthapp;

public class UserProfile {

    public String username;
    public String useremail;
    public String userage;
    public String userheight;
    public String userweight;
    public String usergender;

    public UserProfile(String username, String useremail, String userage, String userheight, String userweight, String usergender) {
        this.userage = userage;
        this.useremail = useremail;
        this.username = username;
        this.userheight = userheight;
        this.userweight = userweight;
        this.usergender = usergender;
    }


}
