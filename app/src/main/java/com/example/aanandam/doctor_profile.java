package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aanandam.model.doc_profile;
import com.example.aanandam.util.doctor_journal_api;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.Objects;

public class doctor_profile extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_CODE = 1;
    private static final int GALLERY_CODE_second = 2;
    private Uri image_uri;
    private ImageView image_platform;
    private ImageView load_pic;
    private EditText name;
    private EditText speciality;
    private EditText location;
    private EditText contact;
    private EditText qualification;
    private EditText experience;
    private EditText about_you;
    private String username;
    private String userid;
    private Button save_button;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("doc_profile");
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;
    private ProgressBar progressBar;
    private String document_id;
    private String image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        name = findViewById(R.id.name);
        speciality = findViewById(R.id.speciality);
        qualification = findViewById(R.id.qualification);
        progressBar = findViewById(R.id.doctor_progress_bar);
        experience = findViewById(R.id.experience);
        about_you = findViewById(R.id.about_you);
        location = findViewById(R.id.city);
        contact = findViewById(R.id.mobile);
        save_button = findViewById(R.id.save_button);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);
        load_pic = findViewById(R.id.load_image);
        image_platform = findViewById(R.id.image_platform);



        if (doctor_journal_api.getInstance() != null) {
            username = doctor_journal_api.getInstance().getDoctor_username();
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

        save_button.setOnClickListener(this);
        load_pic.setOnClickListener(this);




    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.save_button):
                if (document_id != null) {
                    update_profile();
                } else {
                    save_profile();
                }
                break;
            case (R.id.load_image):
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_CODE);
        }
    }

    private void update_profile() {
        progressBar.setVisibility(View.VISIBLE);
        final String doc_name = name.getText().toString();
        final String doc_speciality = speciality.getText().toString();
        final String doc_qualification = qualification.getText().toString();
        final String doc_experience = experience.getText().toString();
        final String doc_about = about_you.getText().toString();
        final String doc_city = location.getText().toString();
        final String doc_contact = contact.getText().toString();
        if (!TextUtils.isEmpty(doc_name) &&
                !TextUtils.isEmpty(doc_speciality) &&
                !TextUtils.isEmpty(doc_qualification) &&
                !TextUtils.isEmpty(doc_experience) &&
                !TextUtils.isEmpty(doc_about) &&
                !TextUtils.isEmpty(doc_city) &&
                !TextUtils.isEmpty(doc_contact)
                && image_uri != null) {
            final StorageReference filepath = storageReference
                    .child("doctor_image")
                    .child("my_image");

            filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageurl = uri.toString();
                            doc_profile doc_profile = new doc_profile();
                            doc_profile.setFull_name(doc_name);
                            doc_profile.setSpecialty(doc_speciality);
                            doc_profile.setQualification(doc_qualification);
                            doc_profile.setLocation(doc_city);
                            doc_profile.setAbout_you(doc_about);
                            doc_profile.setExperience(doc_experience);
                            doc_profile.setUsername(username);
                            doc_profile.setUserid(userid);
                            doc_profile.setImage_uri(imageurl);
                            doc_profile.setContact_no(doc_contact);

                            collectionReference.document(document_id).set(doc_profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(doctor_profile.this, "Your profile updated successsfully.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });
        }
    }


    private void save_profile() {
        progressBar.setVisibility(View.VISIBLE);
        final String doc_name = name.getText().toString();
        final String doc_speciality = speciality.getText().toString();
        final String doc_qualification = qualification.getText().toString();
        final String doc_experience = experience.getText().toString();
        final String doc_about = about_you.getText().toString();
        final String doc_city = location.getText().toString();
        final String doc_contact = contact.getText().toString();
        if (!TextUtils.isEmpty(doc_name) &&
                !TextUtils.isEmpty(doc_speciality) &&
                !TextUtils.isEmpty(doc_qualification) &&
                !TextUtils.isEmpty(doc_experience) &&
                !TextUtils.isEmpty(doc_about) &&
                !TextUtils.isEmpty(doc_city) &&
                !TextUtils.isEmpty(doc_contact)
                && image_uri != null) {
            final StorageReference filepath = storageReference
                    .child("doctor_image")
                    .child("my_image");

            filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageurl = uri.toString();
                            doc_profile doc_profile = new doc_profile();
                            doc_profile.setFull_name(doc_name);
                            doc_profile.setSpecialty(doc_speciality);
                            doc_profile.setQualification(doc_qualification);
                            doc_profile.setLocation(doc_city);
                            doc_profile.setAbout_you(doc_about);
                            doc_profile.setExperience(doc_experience);
                            doc_profile.setUsername(username);
                            doc_profile.setUserid(userid);
                            doc_profile.setImage_uri(imageurl);
                            doc_profile.setContact_no(doc_contact);

                            collectionReference.add(doc_profile).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    String doc_profile_userid = documentReference.getId();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(doctor_profile.this, "Your profile saved successsfully.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
        String userid = currentuser.getUid();
        collectionReference.whereEqualTo("userid", userid)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(image_uri == null){
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            name.setText(snapshot.getString("full_name"));
                            speciality.setText(snapshot.getString("specialty"));
                            qualification.setText(snapshot.getString("qualification"));
                            experience.setText(snapshot.getString("experience"));
                            about_you.setText(snapshot.getString("about_you"));
                            location.setText(snapshot.getString("location"));
                            contact.setText(snapshot.getString("contact_no"));
                            String imageurl = snapshot.getString("image_uri");
                            Picasso.get().load(imageurl)
                                    .placeholder(R.drawable.doctor_image)
                                    .into(image_platform);
                            document_id = snapshot.getId();
                        }

                    }
            }}

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doc_profile_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.doctorsignout:
                startActivity(new Intent(doctor_profile.this,SecondActivity.class));
                break;
            case R.id.doctor_tolist:
                startActivity(new Intent(doctor_profile.this,doctor_list.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                image_uri = data.getData();
                image_platform.setImageURI(image_uri);
            }
        }}
}
