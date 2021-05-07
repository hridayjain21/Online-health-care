package com.example.aanandam.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aanandam.Appointement;
import com.example.aanandam.Chat_Activity;
import com.example.aanandam.R;
import com.example.aanandam.model.Message_class;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class my_patients_adapter extends RecyclerView.Adapter<my_patients_adapter.ViewHolder> {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference = db.getReference().child("chats");
    private Context context;
    private List<Appointement> appointementList;
    public my_patients_adapter(Context context, List<Appointement> appointementList) {
        this.context = context;
        this.appointementList = appointementList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_patient_item,parent,false);
        return new ViewHolder(view,context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Appointement appointement= appointementList.get(position);

        holder.patient_name.setText(appointement.getPatient_name());
        holder.paient_no.setText(appointement.getPatient_contact());
        String Imageurl = appointement.getPatient_image();
        Picasso.get().load(Imageurl)
                .noFade().fit().placeholder(R.drawable.ic_anon_user_48dp).into(holder.patient_image);
        final String patientid = appointement.getPatient_userid();
        final String myid = appointement.getDoctor_userid();
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
//                int count = 0;
//                for(DataSnapshot snapshot:datasnapshot.getChildren()){
//                    Message_class message_class = snapshot.getValue(Message_class.class);
//                    assert message_class != null;
//                    if(message_class.getSenderid().equals(patientid) && message_class.getReceiverid().equals(myid) && !message_class.getisseen()){
//                        count++;
//                    }
//                }
//                if(count > 0){
//                    holder.unseen.setVisibility(View.VISIBLE);
//                    holder.unseen.setText(""+count);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return appointementList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView patient_name,paient_no,unseen;
        private CircularImageView patient_image;
        private Button call;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            ctx = context;

            patient_image = itemView.findViewById(R.id.patient_image_in_my_patient);
//            unseen = itemView.findViewById(R.id.unseen_message_in_mypatient);
            patient_name = itemView.findViewById(R.id.patient_name_in_my_patient);
            paient_no = itemView.findViewById(R.id.patient_no_in_my_patient);
            call=itemView.findViewById(R.id.patient_callbtn_in_my_patient);
            call.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Appointement appointement = appointementList.get(getAdapterPosition());
                    Intent intent = new Intent(context, Chat_Activity.class);
                    intent.putExtra("receiver_id",appointement.getPatient_userid());
                    intent.putExtra("receiver_name",appointement.getPatient_name());
                    intent.putExtra("receiver_dp",appointement.getPatient_image());
                    intent.putExtra("receiver_contact",appointement.getPatient_contact());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Appointement appointement = appointementList.get(pos);
            switch(view.getId()){
                case R.id.patient_callbtn_in_my_patient:
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + appointement.getPatient_contact()));
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
