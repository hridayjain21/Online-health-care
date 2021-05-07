package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aanandam.Common.Common;
import com.example.aanandam.model.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Confirm_appointement_activity extends AppCompatActivity {
    private TextView problem_icon,problem,time,doc_name,consulatation;
    private ImageView time_icon,doc_icon,consultation_icon;
    private Button cnfrm_btn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Appointement");
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointement_activity);
        problem_icon = findViewById(R.id.problem_icon_confirm_appointemnt);
        problem = findViewById(R.id.problem_confirm_appointemnt);
        time = findViewById(R.id.time_confirm_appointemnt);
        doc_name = findViewById(R.id.Doctor_confirm_appointemnt);
        consulatation = findViewById(R.id.consultation_confirm_appointemnt);
        time_icon = findViewById(R.id.time_icon_confirm_appointemnt);
        doc_icon = findViewById(R.id.doctor_icon_confirm_appointemnt);
        cnfrm_btn = findViewById(R.id.confirm_btn_in_confirm_appointement_activity);
        consultation_icon = findViewById(R.id.consultation_icon_confirm_appointemnt);
        final Appointement appointement = (Appointement) getIntent().getSerializableExtra("myappointment1");
        assert appointement != null;
        appointement.setStatus(0);
        problem.setText(appointement.getProblem());
        doc_name.setText(appointement.getDoctor_name());
        appointement.setAccess_id(appointement.getDoctor_name()+"_"+appointement.getPatient_name()+"_"+appointement.getBook_date()+"_slot_"+appointement.getBook_slot());
        final String tim_slot = Common.convertTimeSlotToString(appointement.getBook_slot());
        time.setText(tim_slot + " at "+appointement.getBook_date());
        cnfrm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                CollectionReference date = FirebaseFirestore.getInstance()
                        .collection("Doctor_booked_dates")
                        .document(appointement.getDoctor_userid())
                        .collection(appointement.getBook_date());
                TimeSlot timeSlot = new TimeSlot();timeSlot.setSlot(appointement.getBook_slot());timeSlot.setType("Requested");
                date.add(timeSlot);
                collectionReference.document(appointement.getAccess_id()).set(appointement)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Confirm_appointement_activity.this,"Appointement_saved\nYou can check appointement status in view appointements.",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Confirm_appointement_activity.this,doctor_list.class));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }
}
