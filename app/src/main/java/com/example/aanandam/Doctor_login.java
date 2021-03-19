package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class Doctor_login extends AppCompatActivity implements View.OnClickListener{
    private Button login , create_account,forgot_button;
    private EditText enter_doc_email , enter_doc_password;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("doctors");
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        login = findViewById(R.id.doctorlogin_button);
        create_account = findViewById(R.id.doctorcreate_account);
        enter_doc_email = findViewById(R.id.enter_email_doc_login);
        enter_doc_password = findViewById(R.id.enter_password_doc_login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar_logindoctor);
        forgot_button = findViewById(R.id.doctorforgot_button);
        progressBar.setVisibility(View.INVISIBLE);

        login.setOnClickListener(this);
        create_account.setOnClickListener(this);
        forgot_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.doctorlogin_button):
                String email = enter_doc_email.getText().toString();
                String password = enter_doc_password.getText().toString();
                signupwithemailandpassword(email,password);
                break;
            case (R.id.doctorcreate_account):
                startActivity(new Intent(Doctor_login.this, doctorcreate_account.class));
                break;
            case (R.id.doctorforgot_button):
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
                            Toast.makeText(Doctor_login.this,"Password reset link has been sent to your email.",Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Doctor_login.this,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void signupwithemailandpassword(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(password)){
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                            FirebaseUser currentuser = firebaseAuth.getCurrentUser();
                            assert currentuser != null;
                            String userid = currentuser.getUid();

                            collectionReference.whereEqualTo("doctor_userid",userid).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if(!queryDocumentSnapshots.isEmpty()){
                                                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    doctor_journal_api doctor_journal_api = com.example.aanandam.util.doctor_journal_api.getInstance();
                                                    doctor_journal_api.setDoctor_userid(snapshot.getString("doctor_userid"));
                                                    doctor_journal_api.setDoctor_username(snapshot.getString("doctor_username"));
                                                    startActivity(new Intent(Doctor_login.this,doctor_profile.class));
                                                }
                                            }
                                        }
                                    });
                        }else{
                                Toast.makeText(Doctor_login.this,"Please Create your account first.",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }}

