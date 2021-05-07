package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.aanandam.Adapter.my_docs_adapter;
import com.example.aanandam.Adapter.my_patients_adapter;
import com.example.aanandam.util.doctor_journal_api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class my_patients extends AppCompatActivity {
    private RecyclerView recyclerView;
    private my_patients_adapter recyclerViewAdapter;
    private TextView textView;
    private Toolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Appointement");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patients);
        final List<Appointement> appointementList = new ArrayList<>();
        String doctor_userid = doctor_journal_api.getInstance().getDoctor_userid();
        toolbar = findViewById(R.id.toolbar_in_my_patients);
        recyclerView = findViewById(R.id.my_patients_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        recyclerView.setHasFixedSize(true);
        textView = findViewById(R.id.my_patients_appointement_textview);
        textView.setVisibility(View.INVISIBLE);
        collectionReference.whereEqualTo("doctor_userid",doctor_userid)
                .whereEqualTo("status",1)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Map<String,Appointement> map = new HashMap<>();
                if(queryDocumentSnapshots != null){
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                        Appointement appointement = snapshot.toObject(Appointement.class);
                        if(!map.containsKey(appointement.getPatient_userid())){
                            map.put(appointement.getPatient_userid(),appointement);
                            appointementList.add(appointement);
                       }
                    }
                    if(appointementList.size() == 0){
                        textView.setVisibility(View.VISIBLE);
                    }
                    recyclerViewAdapter = new my_patients_adapter(my_patients.this,appointementList);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
    }

