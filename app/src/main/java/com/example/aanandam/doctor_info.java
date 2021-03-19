package com.example.aanandam;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class doctor_info extends AppCompatActivity {
    private ImageView imageView;
    private TextView name,speciality,experience,qualification,about_you,contact;
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private CollectionReference collectionReference = db.collection("doc_profile");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String userid = bundle.getString("userid");
        imageView = findViewById(R.id.imageView_doc_pro);
        name = findViewById(R.id.doc_name_pro);
        speciality = findViewById(R.id.doc_speciality_pro);
        experience = findViewById(R.id.doc_experience_pro);
        qualification = findViewById(R.id.doc_qualification_pro);
        about_you = findViewById(R.id.doc_about_pro);
//        contact = findViewById(R.id.doc_contact_pro);
        collectionReference.whereEqualTo("userid",userid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                String image_uri = snapshot.getString("image_uri");
                                name.setText(snapshot.getString("full_name"));
                                speciality.setText("Speciality : " + snapshot.getString("specialty"));
                                experience.setText("Experience : "+ snapshot.getString("experience"));
                                qualification.setText("Qualification : "+snapshot.getString("qualification"));
                                about_you.setText(snapshot.getString("about_you"));
//                                contact.setText("Contact : "+snapshot.getString("contact_no"));
                                Picasso.get().load(image_uri)
                                        .fit().placeholder(R.drawable.doctor_image)
                                        .into(imageView);
                        }
                        }
                    }
                });
    }
}
