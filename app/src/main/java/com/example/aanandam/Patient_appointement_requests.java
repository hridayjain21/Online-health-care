package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.aanandam.Adapter.RecyclerViewAdapter;
import com.example.aanandam.Adapter.patient_request_adapter;
import com.example.aanandam.util.doctor_journal_api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Patient_appointement_requests extends AppCompatActivity{
    private RecyclerView recyclerView;
    private patient_request_adapter recyclerViewAdapter;
    private TextView textView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Appointement");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointement_requests);
        final List<Appointement> appointementList = new ArrayList<>();
        String doc_userid = doctor_journal_api.getInstance().getDoctor_userid();
        recyclerView = findViewById(R.id.patient_appointement_request_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        textView = findViewById(R.id.patient_appointement_requests_textview);
        collectionReference.whereEqualTo("doctor_userid",doc_userid)
                .whereEqualTo("status",0)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots != null){
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                        Appointement appointement = snapshot.toObject(Appointement.class);
                        appointementList.add(appointement);
                    }
                    if(appointementList.size() != 0){
                        textView.setVisibility(View.INVISIBLE);
                    }
                    recyclerViewAdapter = new patient_request_adapter(Patient_appointement_requests.this,appointementList);
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