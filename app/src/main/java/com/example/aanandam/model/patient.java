package com.example.aanandam.model;

public class patient {
    private String Fullname;
    private String Age;
    private String Blood_group;
    private String contact;
    private String Imageurl;
    private String Medical_history;
    private String email;
    private String userid;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public patient() {
    }

    public patient(String fullname, String age, String blood_group, String contact, String imageurl, String medical_history, String email, String userid) {
        Fullname = fullname;
        Age = age;
        Blood_group = blood_group;
        this.contact  = contact;
        Imageurl = imageurl;
        Medical_history = medical_history;
        this.email = email;
        this.userid = userid;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getBlood_group() {
        return Blood_group;
    }

    public void setBlood_group(String blood_group) {
        Blood_group = blood_group;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }

    public String getMedical_history() {
        return Medical_history;
    }

    public void setMedical_history(String medical_history) {
        Medical_history = medical_history;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
