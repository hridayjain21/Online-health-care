package com.example.aanandam.util;

import android.app.Activity;
import android.app.Application;

public class doctor_journal_api extends Application {
    private String doctor_username;
    private String doctor_userid;
    private String patient_username;
    private String patient_userid;
    private static doctor_journal_api instance;

    public static doctor_journal_api getInstance(){
        if(instance == null){
            instance = new doctor_journal_api();
        }
        return instance;
    }

    public String getDoctor_username() {
        return doctor_username;
    }

    public void setDoctor_username(String doctor_username) {
        this.doctor_username = doctor_username;
    }

    public String getDoctor_userid() {
        return doctor_userid;
    }

    public void setDoctor_userid(String doctor_userid) {
        this.doctor_userid = doctor_userid;
    }

    public String getPatient_username() {
        return patient_username;
    }

    public void setPatient_username(String patient_username) {
        this.patient_username = patient_username;
    }

    public String getPatient_userid() {
        return patient_userid;
    }

    public void setPatient_userid(String patient_userid) {
        this.patient_userid = patient_userid;
    }
    public static void setInstance(doctor_journal_api instance) {
        doctor_journal_api.instance = instance;
    }
}
