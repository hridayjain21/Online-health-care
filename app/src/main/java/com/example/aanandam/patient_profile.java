package com.example.aanandam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aanandam.util.doctor_journal_api;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class patient_profile extends AppCompatActivity {
    private TextView patient_name,user_type,patient_contact,patient_email,patient_age_icon,patient_real_age,patient_blood_group_icon;
    private TextView patient_blood_group_real,patient_mediacl_his_icon,patient_medical_his_real;
    private ImageView phone_icon,email_icon;
    private ShapeableImageView profile_pic;
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private CollectionReference collectionReference = db.collection("patients");
    private Toolbar toolbar;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppatient_profile);
        patient_name = findViewById(R.id.patient_name_in_profile);
        user_type = findViewById(R.id.user_type_patient_pro);
        phone_icon = findViewById(R.id.patient_profile_phone_icon);
        email_icon = findViewById(R.id.patient_profile_email_icon);
        patient_contact = findViewById(R.id.patient_profile_phone_in_pro);
        patient_email = findViewById(R.id.patient_email_in_pro);
        patient_age_icon = findViewById(R.id.patient_age_in_pp);
        patient_real_age = findViewById(R.id.patient_real_age_in_pp);
        patient_blood_group_icon = findViewById(R.id.patient_blood_group_in_pp);
        patient_blood_group_real = findViewById(R.id.patient_bllod_group_real_in_pp);
        patient_mediacl_his_icon = findViewById(R.id.patient_medical_history_icon);
        patient_medical_his_real = findViewById(R.id.patient_medical_history_real_in_pp);
        profile_pic = findViewById(R.id.patient_profile_image_in_pp);
        toolbar = findViewById(R.id.my_profile_patient_toolbar);

        userid = doctor_journal_api.getInstance().getPatient_userid();
        Log.d("inppprofile", "onCreate: "+userid);


        collectionReference.whereEqualTo("userid",userid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                String image_uri = snapshot.getString("imageurl");
                                patient_name.setText(snapshot.getString("fullname"));
                                user_type.setText("Patient");
                                patient_contact.setText(snapshot.getString("contact"));
                                patient_email.setText(snapshot.getString("email"));
                                patient_real_age.setText(snapshot.getString("age"));
                                patient_blood_group_real.setText(snapshot.getString("blood_group"));
                                patient_medical_his_real.setText(snapshot.getString("medical_history"));
                                Picasso.get().load(image_uri)
                                        .fit().placeholder(R.drawable.ic_anon_user_48dp)
                                        .into(profile_pic);
                            }
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,doctor_list.class));
        super.onBackPressed();
    }
}
