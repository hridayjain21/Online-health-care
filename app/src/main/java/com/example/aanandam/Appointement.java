package com.example.aanandam;

import java.io.Serializable;

public class Appointement implements Serializable {
    private String patient_userid;
    private String doctor_userid;
    private String patient_name;
    private String doctor_name;
    private String book_date;
    private String problem;

    public String getDoc_speciality() {
        return doc_speciality;
    }

    public void setDoc_speciality(String doc_speciality) {
        this.doc_speciality = doc_speciality;
    }

    private String patient_image;
    private String doc_speciality;

    public String getDoctor_image() {
        return doctor_image;
    }

    public void setDoctor_image(String doctor_image) {
        this.doctor_image = doctor_image;
    }

    private String doctor_image;
    private int book_slot;
    private int  status;
    private String  access_id;
    private String  patient_contact;

    public String getPatient_contact() {
        return patient_contact;
    }

    public void setPatient_contact(String patient_contact) {
        this.patient_contact = patient_contact;
    }

    public String getDoc_contact() {
        return doc_contact;
    }

    public void setDoc_contact(String doc_contact) {
        this.doc_contact = doc_contact;
    }

    private String  doc_contact;

    public String getAccess_id() {
        return access_id;
    }

    public void setAccess_id(String access_id) {
        this.access_id = access_id;
    }

    public String getPatient_userid() {
        return patient_userid;
    }

    public void setPatient_userid(String patient_userid) {
        this.patient_userid = patient_userid;
    }

    public String getDoctor_userid() {
        return doctor_userid;
    }

    public void setDoctor_userid(String doctor_userid) {
        this.doctor_userid = doctor_userid;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getBook_date() {
        return book_date;
    }

    public void setBook_date(String book_date) {
        this.book_date = book_date;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getPatient_image() {
        return patient_image;
    }

    public void setPatient_image(String patient_image) {
        this.patient_image = patient_image;
    }

    public int getBook_slot() {
        return book_slot;
    }

    public void setBook_slot(int book_slot) {
        this.book_slot = book_slot;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
