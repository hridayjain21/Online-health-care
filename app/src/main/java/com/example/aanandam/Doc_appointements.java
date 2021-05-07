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

import com.example.aanandam.Adapter.Doctor_appointement_adapter;
import com.example.aanandam.Adapter.patient_appointement_adapter;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Doc_appointements extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Doctor_appointement_adapter recyclerViewAdapter;
    private TextView textView;
    private Toolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Appointement");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_appointements);
        final List<Appointement> appointementList = new ArrayList<>();
        String doc_userid = doctor_journal_api.getInstance().getDoctor_userid();
        toolbar = findViewById(R.id.toolbar_in_Docappointment);
        recyclerView = findViewById(R.id.Doctor_appointement_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        recyclerView.setHasFixedSize(true);
        textView = findViewById(R.id.Doctor_appointement_textview);
        collectionReference.whereEqualTo("doctor_userid",doc_userid)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots != null){
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                        Appointement appointement = snapshot.toObject(Appointement.class);
                        try {
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DATE,-1);
                            String curdate = date.format(c.getTime());
                            Date str_yes = simpleDateFormat.parse(curdate);
                            Date strdate_ap = simpleDateFormat.parse(appointement.getBook_date());
                            assert strdate_ap != null;
                            if(strdate_ap.after(str_yes)){
                                appointementList.add(appointement);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if(appointementList.size() != 0){
                        textView.setVisibility(View.INVISIBLE);
                    }
                    recyclerViewAdapter = new Doctor_appointement_adapter(Doc_appointements.this,appointementList);
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

