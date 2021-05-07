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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aanandam.Appointement;
import com.example.aanandam.Chat_Activity;
import com.example.aanandam.R;
import com.example.aanandam.model.Message_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class message_adapter extends RecyclerView.Adapter<message_adapter.ViewHolder> {

    private Context context;
    private List<Message_class> messages;
    public message_adapter(Context context, List<Message_class> messages) {
        this.context = context;
        this.messages = messages;
    }
    @NonNull
    @Override
    public message_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_layout,parent,false);
        return new ViewHolder(view,context);
    }
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull message_adapter.ViewHolder holder, int position) {
        Message_class message = messages.get(position);
        if(message.getSenderid().equals(getCurrentUser().getUid())){
            holder.receivertv.setVisibility(View.GONE);
            holder.sendertv.setText(message.getMessage());
            Boolean isseen = message.getisseen();
            if(isseen)holder.isseen.setText("seen");
            else holder.isseen.setText("delivered");
            Log.d("message", "onBindViewHolder: "+message.getisseen());
        }else {
            holder.isseen.setVisibility(View.GONE);
            holder.receivertv.setText(message.getMessage());
            holder.sendertv.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView sendertv,receivertv,isseen;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            isseen = itemView.findViewById(R.id.seen_in_chatactivity);
            sendertv = itemView.findViewById(R.id.sender_back);
            receivertv = itemView.findViewById(R.id.receiver_back);
        }

    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

}
