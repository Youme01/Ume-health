package com.example.mhealthapp;

//Class For Pill ReminDEr
// User Will View This INFO
public class User {
    private String med_text;
    private String dose_text;
    private String food_text;
    private String time_text;

    public User(String med_txt,String dose_txt,String food_txt, String time_txt){
        this.med_text = med_txt;
        this.dose_text = dose_txt;
        this.food_text = food_txt;
        this.time_text = time_txt;
    }

    public void setMed_text(String med_text) {
        med_text = med_text;
    }
    public void setDose_text(String dose_text) {
        dose_text = dose_text;
    }
    public void setTime_text(String time_text) {
        time_text = time_text;
    }
    public String getMed_txt(){
        return med_text;
    }

    public String getDose_txt(){
        return dose_text;
    }

    public String getFood_txt(){
        return food_text;
    }

    public String getTime_txt(){
        return time_text;
    }
}