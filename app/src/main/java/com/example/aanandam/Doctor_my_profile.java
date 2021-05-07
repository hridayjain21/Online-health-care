package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aanandam.Common.Common;
import com.example.aanandam.util.doctor_journal_api;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class Doctor_my_profile extends AppCompatActivity {
    private TextView doc_name,speciality,doc_contact,doc_email,doc_qualification,doc_experience,doc_address,doc_about_you;
    private TextView doc_exp_icon,doc_about_icon,doc_qualification_icon,toolbar_text;
    private ImageView phone_icon,email_icon,address_icon;
    private ImageView profile_pic;
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private CollectionReference collectionReference = db.collection("doctors");
    private Toolbar toolbar;
    private String userid;
    private EditText problem;
    private Button book_ap;
    private Boolean from_search = false;
    private String image_uri;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_my_profile);
        doc_name = findViewById(R.id.my_profile_doctor_name);
        problem = findViewById(R.id.problem_in_my_doc_profile);
        speciality = findViewById(R.id.my_profile_doctor_specialty);
        doc_contact = findViewById(R.id.my_profile_doctor_phone);
        doc_email= findViewById(R.id.my_profile_doctor_email);
        doc_address= findViewById(R.id.my_profile_doctor_address);
        doc_qualification= findViewById(R.id.my_profile_doctor_qualification);
        ImageButton edit = findViewById(R.id.edit_profile_button_in_docpro);
        doc_experience= findViewById(R.id.my_profile_doctor_real_experience);
        doc_about_you= findViewById(R.id.my_profile_doctor_about_you_real);
        phone_icon = findViewById(R.id.my_profile_doctor_phone_icon);
        email_icon = findViewById(R.id.my_profile_doctor_email_icon);
        address_icon = findViewById(R.id.my_profile_doctor_address_icon);
        doc_qualification_icon = findViewById(R.id.my_profile_doctor_qualificatin_icon);
        doc_exp_icon = findViewById(R.id.my_profile_doctor_experience_icon);
        doc_about_icon = findViewById(R.id.my_profile_doctor_about_you_icon);
        toolbar_text = findViewById(R.id.my_profie_doctor_toolbar_text);
        book_ap = findViewById(R.id.book_appointement);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            userid = bundle.getString("doctor_userid");
            from_search = true;
            toolbar_text.setText("Doctor Profile");
            Common.CurrentUserid = doctor_journal_api.getInstance().getPatient_userid();
            Common.CurrentUserName = doctor_journal_api.getInstance().getPatient_fullname();
            edit.setVisibility(View.INVISIBLE);
        }
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Doctor_my_profile.this,Edit_profile_doctor.class));
            }
        });
        profile_pic = findViewById(R.id.my_profile_doctor_dp);
        toolbar = findViewById(R.id.my_profile_doc_toolbar);

        if (!from_search && doctor_journal_api.getInstance() != null) {
            userid = doctor_journal_api.getInstance().getDoctor_userid();
        }
        if(!from_search){
            problem.setVisibility(View.INVISIBLE);
            book_ap.setVisibility(View.INVISIBLE);
        }
        collectionReference.whereEqualTo("userid",userid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                image_uri = snapshot.getString("image_uri");
                                doc_name.setText(snapshot.getString("full_name"));
                                speciality.setText(snapshot.getString("specialty"));
                                doc_contact.setText(snapshot.getString("contact_no"));
                                doc_email.setText(snapshot.getString("email"));
                                doc_qualification.setText(snapshot.getString("qualification"));
                                doc_address.setText(snapshot.getString("location"));
                                doc_experience.setText(snapshot.getString("experience"));
                                doc_about_you.setText(snapshot.getString("about_you"));
                                Picasso.get().load(image_uri)
                                        .fit().placeholder(R.drawable.doctor_image)
                                        .into(profile_pic);
                            }
                        }
                    }
                });
        book_ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(problem.getText() != null){
                    String prob = problem.getText().toString();
                    Appointement appointement = new Appointement();
                    appointement.setDoctor_name(doc_name.getText().toString());
                    appointement.setPatient_name(doctor_journal_api.getInstance().getPatient_fullname());
                    appointement.setPatient_userid(doctor_journal_api.getInstance().getPatient_userid());
                    appointement.setDoctor_userid(userid);
                    appointement.setProblem(problem.getText().toString());
                    appointement.setDoc_speciality(speciality.getText().toString());
                    appointement.setPatient_image(Common.Currentpatientimage);
                    appointement.setDoc_contact(doc_contact.getText().toString());
                    appointement.setPatient_contact(Common.CurrentppatientPhone);
                    appointement.setDoctor_image(image_uri);
                    Intent intent = new Intent(Doctor_my_profile.this,BookApointement_for_patient.class);
                    intent.putExtra("my_appointement",appointement);
                    startActivity(intent);
                    Common.CurreentDoctor = userid;
                    Common.CurrentDoctorName = doc_name.getText().toString();
                    Common.Currentdoactorimage = image_uri;
                }else{
                    Toast.makeText(Doctor_my_profile.this,"Please mention problem",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(from_search == false){
            startActivity(new Intent(this,Doctor_home.class));
        }
        super.onBackPressed();
    }
}
