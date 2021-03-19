package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aanandam.util.doctor_journal_api;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private TextView Title;
    private Button get_started;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private CollectionReference collectionReference = db.collection("patient");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Title = findViewById(R.id.title);

        get_started = findViewById(R.id.get_started_button);
        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
        get_started.setVisibility(View.INVISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    firebaseUser = firebaseAuth.getCurrentUser();
                    final String Userid = firebaseUser.getUid();
                    collectionReference.whereEqualTo("patient_userid",Userid)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                                    @Nullable FirebaseFirestoreException e) {
                                    assert queryDocumentSnapshots != null;
                                    if(!queryDocumentSnapshots.isEmpty()){
                                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                            doctor_journal_api doctor_journal_api = new doctor_journal_api();
                                            doctor_journal_api.setPatient_userid(Userid);
                                            doctor_journal_api.setPatient_username(snapshot.getString("patient_username"));
                                            startActivity(new Intent(MainActivity.this,doctor_list.class));
                                        }
                                    }else{
                                        startActivity(new Intent(MainActivity.this,SecondActivity.class));
                                    }
                                }
                            });
            }else {get_started.setVisibility(View.VISIBLE);}
        }
        };
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

            @Override
            protected void onPause() {
                super.onPause();
                if (firebaseAuth != null) {
                    firebaseAuth.removeAuthStateListener(authStateListener);
                }
            } }
