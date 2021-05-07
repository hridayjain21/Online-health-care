package com.example.aanandam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aanandam.Common.Common;
import com.example.aanandam.model.TimeSlot;
import com.example.aanandam.util.doctor_journal_api;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Doctor_home extends AppCompatActivity implements View.OnClickListener {
    private Button profile,my_appointement,my_patients,patient_request,my_calendar,signout;
    SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
        Common.CurrentUserType = "Doctor";
        profile = findViewById(R.id.my_profile_in_dochome);
        my_calendar = findViewById(R.id.myCalendarBtn);
        profile.setOnClickListener(this);
        patient_request = findViewById(R.id.my_patient_request_in_dochome);
        my_calendar.setOnClickListener(this);
        patient_request.setOnClickListener(this);
        my_appointement = findViewById(R.id.my_appoitment_in_dochome);
        my_patients = findViewById(R.id.my_patients_in_dochome);
        String doc_userid = doctor_journal_api.getInstance().getDoctor_userid();
        signout = findViewById(R.id.signOutBtn);
        my_appointement.setOnClickListener(this);
        my_patients.setOnClickListener(this);
        signout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_profile_in_dochome:
                startActivity(new Intent(Doctor_home.this,Doctor_my_profile.class));
                break;
            case R.id.myCalendarBtn:
                startActivity(new Intent(Doctor_home.this,my_calendar_activity.class));
                break;
            case R.id.my_patient_request_in_dochome:
                startActivity(new Intent(Doctor_home.this,Patient_appointement_requests.class));
                break;
            case R.id.my_appoitment_in_dochome:
                startActivity(new Intent(Doctor_home.this,Doc_appointements.class));
                break;
            case R.id.my_patients_in_dochome:
                startActivity(new Intent(Doctor_home.this,my_patients.class));
                break;
            case R.id.signOutBtn:
                startActivity(new Intent(Doctor_home.this,SecondActivity.class));
                break;
        }
    }
}
