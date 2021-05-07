package com.example.aanandam.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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
import com.example.aanandam.Common.Common;
import com.example.aanandam.R;
import com.example.aanandam.model.Message_class;
import com.example.aanandam.model.doc_profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class my_docs_adapter extends RecyclerView.Adapter<my_docs_adapter.ViewHolder> {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference = db.getReference().child("chats");
    private Context context;
    private List<Appointement> appointementList;
    public my_docs_adapter(Context context, List<Appointement> appointementList) {
        this.context = context;
        this.appointementList = appointementList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_doctor_item,parent,false);
        return new ViewHolder(view,context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Appointement appointement= appointementList.get(position);
        holder.doc_name.setText(appointement.getDoctor_name());
        holder.doc_no_no.setText(appointement.getDoc_contact());
        holder.doc_speciality.setText(appointement.getDoc_speciality());
        String Imageurl = appointement.getDoctor_image();


        Picasso.get().load(Imageurl)
                .noFade().fit().placeholder(R.drawable.doctor_image).into(holder.doc_image);

    }

    @Override
    public int getItemCount() {
        return appointementList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView doc_name,doc_no_no,doc_speciality,unseen;
        private CircularImageView doc_image;
        private Button call;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            ctx = context;
            doc_image = itemView.findViewById(R.id.doc_image_in_my_doc);
            doc_name = itemView.findViewById(R.id.doc_name_in_my_doc);
            doc_no_no = itemView.findViewById(R.id.doc_no_in_my_doc);
            doc_speciality = itemView.findViewById(R.id.doc_speeciality_in_my_doc);
//            unseen = itemView.findViewById(R.id.unseen_message_in_mydoc);
            call=itemView.findViewById(R.id.doc_callbtn_in_my_doc);
            call.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Appointement appointement = appointementList.get(getAdapterPosition());
                    Intent intent = new Intent(context, Chat_Activity.class);
                    intent.putExtra("receiver_id",appointement.getDoctor_userid());
                    intent.putExtra("receiver_name",appointement.getDoctor_name());
                    intent.putExtra("receiver_dp",appointement.getDoctor_image());
                    intent.putExtra("receiver_contact",appointement.getDoc_contact());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Appointement appointement = appointementList.get(pos);
            switch(view.getId()){
                case R.id.doc_callbtn_in_my_doc:
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + appointement.getDoc_contact()));
                    context.startActivity(intent);
                    break;

            }
        }
    }
}
