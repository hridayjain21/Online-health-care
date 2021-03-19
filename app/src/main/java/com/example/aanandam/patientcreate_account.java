package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aanandam.util.doctor_journal_api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class patientcreate_account extends AppCompatActivity {
    private Button create_account;
    private EditText username,password;
    private AutoCompleteTextView email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentuser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private CollectionReference collectionReference = db.collection("patient");
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientcreate_account);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        progressBar = findViewById(R.id.progress_bar_patient);
        username = findViewById(R.id.patient_username);
        email = findViewById(R.id.patient_email);
        password = findViewById(R.id.patient_password);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentuser = firebaseAuth.getCurrentUser();

                if (currentuser != null) {
                    //user is already loggedin..
                }else {
                    //no user yet...
                }

            }
        };
        progressBar.setVisibility(View.INVISIBLE);
        create_account = findViewById(R.id.patientcreate_accountfinal);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(username.getText().toString())
                        && !TextUtils.isEmpty(email.getText().toString())
                        && !TextUtils.isEmpty(password.getText().toString())
                ) {
                    Createaccountwithemailandpassword(username.getText().toString(),
                            email.getText().toString(),
                            password.getText().toString());
                }else {
                    Toast.makeText(patientcreate_account.this,"please fill all the fields",Toast.LENGTH_SHORT ).show();
                }
            }

            private void Createaccountwithemailandpassword(final String usernname, String email, String password) {
                if (!TextUtils.isEmpty(usernname)
                        && !TextUtils.isEmpty(email)
                        && !TextUtils.isEmpty(password)
                ){
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        currentuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(patientcreate_account.this,"please verify your email." +
                                                            "The verification mail has been sent to your email.",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(patientcreate_account.this,"Error "+e.getMessage(),Toast.LENGTH_LONG ).show();
                                            }
                                        });
                                        currentuser = firebaseAuth.getCurrentUser();
                                        assert currentuser != null;
                                        final String userid = currentuser.getUid();
                                        Map<String,String> patientobj = new HashMap<>();
                                        patientobj.put("patient_userid",userid);
                                        patientobj.put("patient_username",usernname);

                                        collectionReference.add(patientobj)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        documentReference.get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if(Objects.requireNonNull(task.getResult()).exists()){
                                                                            progressBar.setVisibility(View.INVISIBLE);
                                                                            String name = task.getResult().getString("patient_username");
                                                                            doctor_journal_api doctor_journal_api = com.example.aanandam.util.doctor_journal_api.getInstance();
                                                                            doctor_journal_api.setPatient_userid(userid);
                                                                            doctor_journal_api.setPatient_username(name);
                                                                            startActivity(new Intent(patientcreate_account.this,patientlogin.class));
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                });

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(patientcreate_account.this,"this account is already registered",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

}
