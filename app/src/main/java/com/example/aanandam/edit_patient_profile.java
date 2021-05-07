package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aanandam.Common.Common;
import com.example.aanandam.model.doc_profile;
import com.example.aanandam.model.patient;
import com.example.aanandam.util.doctor_journal_api;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class edit_patient_profile extends AppCompatActivity implements View.OnClickListener{
    private static final int GALLERY_CODE = 1;
    private EditText patient_name,patient_contact,patient_email,patient_blood_group_real,patient_medical_his_real;
    private TextView patient_mediacl_his_icon,patient_full_name_icon,patient_contact_icon,patient_email_icon,patient_blood_group_icon;
    private ImageButton dp_changer;
    private ImageView profile_pic;
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;
    private CollectionReference collectionReference = db.collection("patients");
    private Toolbar toolbar;
    private StorageReference storageReference;
    private String userid,document_id,age;
    private Uri image_uri;
    private ProgressBar progressBar;
    private Button update_pro;
    private String click_dpchanger = "not_clicked";
    private String Imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_profile);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_in_edipat);
        patient_full_name_icon = findViewById(R.id.Full_name_patient_icon_in_edit_patient);
        patient_name = findViewById(R.id.Full_name_patient_in_edit_patient);
        dp_changer = findViewById(R.id.edit_profile_patient_profile_pic_change);
        patient_contact_icon = findViewById(R.id.contact_patient_icon_in_edit_patient);
        patient_contact = findViewById(R.id.contact_patient_in_edit_patient);
        patient_email_icon = findViewById(R.id.email_patient_icon_in_edit_patient);
        patient_email = findViewById(R.id.email_patient_in_edit_patient);
        patient_blood_group_icon = findViewById(R.id.Blood_group_patient_icon_in_edit_patient);
        patient_blood_group_real = findViewById(R.id.Blood_group_patient_in_edit_patient);
        patient_mediacl_his_icon = findViewById(R.id.Medical_history_patient_icon_in_edit_patient);
        patient_medical_his_real = findViewById(R.id.Medical_history_patient_in_edit_patient);
        profile_pic = findViewById(R.id.edit_profile_patient_profile_pic);
        toolbar = findViewById(R.id.edit_profile_paient_toolbar);
        update_pro = findViewById(R.id.button_editprofile_patient);

        if (doctor_journal_api.getInstance() != null) {
            userid = doctor_journal_api.getInstance().getPatient_userid();
        }
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentuser = firebaseAuth.getCurrentUser();
                if (currentuser != null) {
                } else {
                } }
        };
        dp_changer.setOnClickListener(this);
        update_pro.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser = firebaseAuth.getCurrentUser();
        assert currentuser != null;
        userid = currentuser.getUid();
        collectionReference.whereEqualTo("userid",userid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(image_uri == null){
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                patient_name.setText(snapshot.getString("fullname"));
                                patient_contact.setText(snapshot.getString("contact"));
                                age = snapshot.getString("age");
                                patient_email.setText(snapshot.getString("email"));
                                patient_blood_group_real.setText(snapshot.getString("blood_group"));
                                patient_medical_his_real.setText(snapshot.getString("medical_history"));
                                document_id = snapshot.getId();
                                Imageurl = snapshot.getString("imageurl");
                                Picasso.get().load(Imageurl)
                                        .fit().placeholder(R.drawable.ic_anon_user_48dp)
                                        .into(profile_pic);
                            }
                        }
                    }}
                });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.edit_profile_patient_profile_pic_change:
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_CODE);
                click_dpchanger = "Clicked";
                break;
            case R.id.button_editprofile_patient:
                progressBar.setVisibility(View.VISIBLE);
                update_profile();
                break;
        }
    }

    private void update_profile(){
        final String name = patient_name.getText().toString();
        final String contact = patient_contact.getText().toString();
        final String email = patient_email.getText().toString();
        final String blood_group = patient_blood_group_real.getText().toString();
        final String medical_history = patient_medical_his_real.getText().toString();
        if (!TextUtils.isEmpty(name) &&
                !TextUtils.isEmpty(contact) &&
                !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(blood_group) &&
                !TextUtils.isEmpty(medical_history)) {
            if(click_dpchanger.equals("got_new_data")){
                final StorageReference filepath = storageReference
                        .child("patient_image")
                        .child(userid).child(userid+ Calendar.getInstance().getTime());

                filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageurl = uri.toString();
                                patient patient = new patient();
                                patient.setImageurl(imageurl);
                                patient.setAge(age);
                                patient.setEmail(email);
                                patient.setFullname(name);
                                patient.setUserid(userid);
                                patient.setContact(contact);
                                patient.setBlood_group(blood_group);
                                patient.setMedical_history(medical_history);
                                Common.Currentpatientimage = imageurl;
                                collectionReference.document(document_id).set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(edit_patient_profile.this, "Your profile updated successsfully.", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(edit_patient_profile.this,doctor_list.class));
                                    }
                                });
                            }
                        });
                    }
                });
            }else{
                patient patient = new patient();
                patient.setImageurl(Imageurl);
                patient.setAge(age);
                patient.setEmail(email);
                patient.setFullname(name);
                patient.setUserid(userid);
                patient.setContact(contact);
                patient.setBlood_group(blood_group);
                patient.setMedical_history(medical_history);
                collectionReference.document(document_id).set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(edit_patient_profile.this, "Your profile updated successsfully.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(edit_patient_profile.this,doctor_list.class));
                    }
                });
            }
        }else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                image_uri = data.getData();
                profile_pic.setImageURI(image_uri);
                click_dpchanger = "got_new_data";
            }
        }
    }
}
