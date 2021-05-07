package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.aanandam.Adapter.Doctor_appointement_adapter;
import com.example.aanandam.Adapter.my_docs_adapter;
import com.example.aanandam.model.doc_profile;
import com.example.aanandam.model.patient;
import com.example.aanandam.util.doctor_journal_api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class my_doctors extends AppCompatActivity {
    private RecyclerView my_docsrecyclerView;
    private my_docs_adapter my_docs_adapter;
    private TextView textView;
    private Toolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Appointement");
    private CollectionReference collectionReferencedoc = db.collection("doctors");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctors);
        final List<Appointement> appointementList = new ArrayList<>();
        final String patient_userid = doctor_journal_api.getInstance().getPatient_userid();
        toolbar = findViewById(R.id.toolbar_in_my_docs);
        my_docsrecyclerView = findViewById(R.id.my_docs_recycler_view);
        my_docsrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        my_docsrecyclerView.setHasFixedSize(true);
        textView = findViewById(R.id.my_docs_appointement_textview);
        textView.setVisibility(View.INVISIBLE);
        collectionReference.whereEqualTo("patient_userid",patient_userid)
                .whereEqualTo("status",1)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots != null){
                    Set<String> set = new HashSet<>();
                    for(final QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                        String docid = snapshot.getString("doctor_userid");
                        if(!set.contains(docid)){
                            Appointement appointement = snapshot.toObject(Appointement.class);
                            appointementList.add(appointement);
                            set.add(docid);
                        }
                    }
                    if(appointementList.size() == 0){
                        textView.setVisibility(View.VISIBLE);
                    }
                    my_docs_adapter = new my_docs_adapter(my_doctors.this, appointementList);
                    my_docsrecyclerView.setAdapter(my_docs_adapter);
                    my_docs_adapter.notifyDataSetChanged();
                }
            }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    }

