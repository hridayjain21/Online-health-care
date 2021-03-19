package com.example.aanandam.model;

import android.text.Editable;

public class doc_profile {
    private String Full_name;
    private String specialty;
    private String qualification;
    private String experience;
    private String location;
    private String about_you;
    private String contact_no;
    private String Image_uri;
    private String username;
    private String userid;

    public doc_profile() {
    }

    public doc_profile(String full_name, String specialty, String qualification, String experience, String location, String about_you, String  contact_no, String image_uri, String username, String userid) {
        Full_name = full_name;
        this.specialty = specialty;
        this.qualification = qualification;
        this.experience = experience;
        this.location = location;
        this.about_you = about_you;
        this.contact_no = contact_no;
        Image_uri = image_uri;
        this.username = username;
        this.userid = userid;
    }

    public String getFull_name() {
        return Full_name;
    }

    public void setFull_name(String full_name) {
        Full_name = full_name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAbout_you() {
        return about_you;
    }

    public void setAbout_you(String about_you) {
        this.about_you = about_you;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getImage_uri() {
        return Image_uri;
    }

    public void setImage_uri(String image_uri) {
        Image_uri = image_uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

