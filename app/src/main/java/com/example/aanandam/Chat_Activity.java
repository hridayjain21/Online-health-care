package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aanandam.Adapter.message_adapter;
import com.example.aanandam.Common.Common;
import com.example.aanandam.model.Message_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Chat_Activity extends AppCompatActivity implements View.OnClickListener{
    private CircularImageView dp;
    private TextView username;
    private ImageButton call,back,send;
    private Toolbar toolbar;
    private EditText message_text;
    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference;
    private Message_class message;
    private String receiverid,receivername,senderid;
    private List<Message_class> message_List;
    private ValueEventListener seenlistener;
    private message_adapter message_adapter;
    private AlertDialog.Builder alert;
    private boolean seeing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_);
        seeing = true;
        toolbar  = findViewById(R.id.toolbar_in_chat);
        dp = findViewById(R.id.dp_in_chat_activity);
        username = findViewById(R.id.username_in_chat_activity);
        call = findViewById(R.id.callbtn_in_chat_activity);
        message_text = findViewById(R.id.message_edittext_in_chat);
        send = findViewById(R.id.send_button_in_chat);
        back = findViewById(R.id.backarrow_in_chat);
        alert = new AlertDialog.Builder(this);
        alert.setTitle("Caution : ");
        alert.setMessage("The chatting feature here is just for consultation/Treatments purpose and not for social talks.\n" +
                "Write your messages in a formal way");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Bundle bundle = getIntent().getExtras();
        assert bundle!=null;
        receiverid = bundle.getString("receiver_id");
        receivername = bundle.getString("receiver_name");
        final String receivercontact = bundle.getString("receiver_contact");
        String receiverimage = bundle.getString("receiver_dp");
        username.setText(receivername);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        senderid = user.getUid();
        recyclerView = findViewById(R.id.recyclerView_in_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        message = new Message_class();
        read_message(senderid,receiverid);
        if(seeing)seen_message(receiverid,senderid);
        Picasso.get().load(receiverimage).noFade().fit().placeholder(R.drawable.ic_anon_user_48dp).into(dp);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + receivercontact));
                startActivity(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_message();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.callbtn_in_chat_activity:

        }
    }
    private void read_message(final String myid, final String userid){
        message_List = new ArrayList<>();
        reference = database.getReference().child("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                message_List.clear();
                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    Message_class message = snapshot.getValue(Message_class.class);
                    assert message != null;
                    if(message.getSenderid().equals(myid) && message.getReceiverid().equals(userid) ||
                    message.getSenderid().equals(userid) && message.getReceiverid().equals(myid)){
                        message_List.add(message);
                    }

                }
                message_adapter = new message_adapter(Chat_Activity.this,message_List);
                recyclerView.setAdapter(message_adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void seen_message(final String rece, final String sed){
        reference = database.getReference().child("chats");
        seenlistener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    Message_class message = snapshot.getValue(Message_class.class);
                    assert message != null;
                    if(message.getSenderid().equals(rece) && message.getReceiverid().equals(sed) ){
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("isseen",true);
                        snapshot.getRef().updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.addListenerForSingleValueEvent(seenlistener);
    }
    public void send_message(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("senderid",senderid);
        hashMap.put("receiverid",receiverid);
        hashMap.put("message",message_text.getText().toString());
        hashMap.put("isseen",false);
        reference = database.getReference();
        reference.child("chats").push().setValue(hashMap);
        message_text.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        seeing = true;
    }

    @Override
    protected void onPause() {
        seeing = false;
        reference.removeEventListener(seenlistener);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

