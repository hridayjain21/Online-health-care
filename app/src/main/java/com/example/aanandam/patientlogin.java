package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class patientlogin extends AppCompatActivity implements View.OnClickListener {
    private Button login , create_account,forgot_password;
    private EditText enter_email , enter_password;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("patient");
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientlogin);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        login = findViewById(R.id.patientlogin_button);
        create_account = findViewById(R.id.patientcreate_account);
        enter_email = findViewById(R.id.enter_email_patient_login);
        enter_password = findViewById(R.id.enter_password_patient_login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar_patientlogin);
        forgot_password = findViewById(R.id.patientforgot_button);
        login.setOnClickListener(this);
        create_account.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.patientlogin_button):
                String email = enter_email.getText().toString();
                String password = enter_password.getText().toString();
                signupwithemailandpassword(email,password);
                break;
            case (R.id.patientcreate_account):
                startActivity(new Intent(patientlogin.this, patientcreate_account.class));
                break;
            case(R.id.patientforgot_button):
                password_reset(view);
        }
    }

    private void password_reset(View view) {
        final EditText rest_password = new EditText(view.getContext());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("Reset password");
        alertDialog.setMessage("Enter your email id.");
        alertDialog.setView(rest_password);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String mail = rest_password.getText().toString();
                firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(patientlogin.this,"Password reset link has been sent to your email.",Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(patientlogin.this,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });;
            }
        });
    }
    private void signupwithemailandpassword(String email, String password) {
        if(!TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(password)){
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if((task.isSuccessful())){
                            progressBar.setVisibility(View.VISIBLE);
                            FirebaseUser currentuser = firebaseAuth.getCurrentUser();
                            assert currentuser != null;
                            String userid = currentuser.getUid();
                            if(currentuser.isEmailVerified()){
                            collectionReference.whereEqualTo("patient_userid",userid).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if(!queryDocumentSnapshots.isEmpty()){
                                                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                                    doctor_journal_api doctor_journal_api = com.example.aanandam.util.doctor_journal_api.getInstance();
                                                    doctor_journal_api.setPatient_userid(snapshot.getString("patient_userid"));
                                                    doctor_journal_api.setPatient_username(snapshot.getString("patient_username"));
                                                    startActivity(new Intent(patientlogin.this,doctor_list.class));
                                                }
                                            }else {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(patientlogin.this,"please create your account first ",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                                Toast.makeText(patientlogin.this,"Please verify your email first.",Toast.LENGTH_LONG).show();
                            }
                            }else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(patientlogin.this,"please create your account first ",
                                        Toast.LENGTH_SHORT).show();
                            }}
                    });
        }
    }}
