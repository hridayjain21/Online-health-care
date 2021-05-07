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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aanandam.Common.Common;
import com.example.aanandam.model.doc_profile;
import com.example.aanandam.model.patient;
import com.example.aanandam.util.doctor_journal_api;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Edit_profile_doctor extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_CODE = 1;
    private EditText doc_name,doc_contact,doc_email,doc_speciality,doc_qualification,doc_experience,doc_about_you,doc_address;
    private TextView doc_name_icon,doc_contact_icon,doc_email_icon,doc_qualification_icon,doc_speciality_icon,doc_experience_icon,doc_about_icon,doc_adress_icon;
    private ImageView dp_changer;
    private ImageView profile_pic;
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;
    private CollectionReference collectionReference = db.collection("doctors");
    private Toolbar toolbar;
    private StorageReference storageReference;
    private String userid,document_id;
    private Uri image_uri;
    private Button update_pro;
    private String Imageurl,click_dpchanger;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_doctor);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_in_edidoc);
        doc_name_icon = findViewById(R.id.Full_name_icon_in_edit_doctor);
        doc_name = findViewById(R.id.Full_name_in_edit_doctor);
        dp_changer = findViewById(R.id.edit_profile_doctor_profile_pic_change);
        doc_contact_icon = findViewById(R.id.contact_icon_in_edit_doctor);
        doc_contact = findViewById(R.id.contact_in_edit_doctor);
        doc_email_icon = findViewById(R.id.email_icon_in_edit_doctor);
        doc_email = findViewById(R.id.email_in_edit_doctor);
        doc_speciality_icon = findViewById(R.id.specialty_icon_in_edit_doctor);
        doc_speciality = findViewById(R.id.speciality_in_edit_doctor);
        doc_experience_icon = findViewById(R.id.experience_icon_in_edit_doctor);
        doc_adress_icon = findViewById(R.id.address_icon_in_edit_doctor);
        doc_address = findViewById(R.id.address_in_edit_doctor);
        doc_qualification_icon = findViewById(R.id.qualification_icon_in_edit_doctor);
        doc_qualification = findViewById(R.id.qualification_in_edit_doctor);
        doc_experience = findViewById(R.id.experience_in_edit_doctor);
        doc_about_icon = findViewById(R.id.about_doctor_icon_in_edit_doctor);
        doc_about_you = findViewById(R.id.about_in_edit_doctor);
        profile_pic = findViewById(R.id.edit_profile_doctor_profile_pic);
        toolbar = findViewById(R.id.edit_profile_doctor_toolbar);
        update_pro = findViewById(R.id.button_editprofile_doctor);

        if (doctor_journal_api.getInstance() != null) {
            userid = doctor_journal_api.getInstance().getDoctor_userid();
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
                                    doc_name.setText(snapshot.getString("full_name"));
                                    doc_contact.setText(snapshot.getString("contact_no"));
                                    doc_email.setText(snapshot.getString("email"));
                                    doc_experience.setText(snapshot.getString("experience"));
                                    doc_speciality.setText(snapshot.getString("specialty"));
                                    doc_qualification.setText(snapshot.getString("qualification"));
                                    doc_about_you.setText(snapshot.getString("about_you"));
                                    doc_address.setText(snapshot.getString("location"));
                                    document_id = snapshot.getId();
                                    String image_uri = snapshot.getString("image_uri");
                                     Imageurl = snapshot.getString("image_uri");
                                    Picasso.get().load(image_uri)
                                            .fit().placeholder(R.drawable.doctor_image)
                                            .into(profile_pic);
                                }
                            }
                        }}
                });

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.edit_profile_doctor_profile_pic_change:
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_CODE);
                break;
            case R.id.button_editprofile_doctor:
                update_pro.setBackgroundColor(R.color.white);
                progressBar.setVisibility(View.VISIBLE);
                update_profile();
                break;
        }
    }

    private void update_profile(){
        final String name = doc_name.getText().toString();
        final String contact = doc_contact.getText().toString();
        final String email = doc_email.getText().toString();
        final String experience = doc_experience.getText().toString();
        final String speciality = doc_speciality.getText().toString();
        final String about = doc_about_you.getText().toString();
        final String address = doc_address.getText().toString();
        final String quali = doc_qualification.getText().toString();
        if (!TextUtils.isEmpty(name) &&
                !TextUtils.isEmpty(contact) &&
                !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(experience) &&
                !TextUtils.isEmpty(speciality) &&
                !TextUtils.isEmpty(about) &&
                !TextUtils.isEmpty(quali) &&
                !TextUtils.isEmpty(address)
                && image_uri != null) {
            if(click_dpchanger.equals("got_new_data")){
                final StorageReference filepath = storageReference
                        .child("doctor_image")
                        .child(userid).child(userid+ Calendar.getInstance().getTime());

                filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageurl = uri.toString();
                                Common.Currentdoactorimage=imageurl;
                                doc_profile doctor_profile = new doc_profile();
                                doctor_profile.setFull_name(name);
                                doctor_profile.setContact_no(contact);
                                doctor_profile.setEmail(email);
                                doctor_profile.setLocation(address);
                                doctor_profile.setExperience(experience);
                                doctor_profile.setSpecialty(speciality);
                                doctor_profile.setAbout_you(about);
                                doctor_profile.setQualification(quali);
                                doctor_profile.setImage_uri(imageurl);
                                doctor_profile.setUserid(userid);
                                collectionReference.document(document_id).set(doctor_profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(Edit_profile_doctor.this, "Your profile updated successsfully.", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Edit_profile_doctor.this,Doctor_my_profile.class));
                                    }
                                });
                            }
                        });
                    }
                });
            }else{
                doc_profile doctor_profile = new doc_profile();
                doctor_profile.setFull_name(name);
                doctor_profile.setContact_no(contact);
                doctor_profile.setEmail(email);
                doctor_profile.setLocation(address);
                doctor_profile.setExperience(experience);
                doctor_profile.setSpecialty(speciality);
                doctor_profile.setAbout_you(about);
                doctor_profile.setQualification(quali);
                doctor_profile.setImage_uri(Imageurl);
                doctor_profile.setUserid(userid);
                collectionReference.document(document_id).set(doctor_profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Edit_profile_doctor.this, "Your profile updated successsfully.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Edit_profile_doctor.this,Doctor_my_profile.class));
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
                click_dpchanger  = "got_new_data";
            }
        }
    }
}

