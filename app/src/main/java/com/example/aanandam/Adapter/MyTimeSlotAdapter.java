package com.example.aanandam.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aanandam.Common.Common;
import com.example.aanandam.Interface.IRecyclerItemSelectedListener;
import com.example.aanandam.R;
import com.example.aanandam.model.TimeSlot;
import com.example.aanandam.util.doctor_journal_api;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {


    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;
    SimpleDateFormat simpleDateFormat;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @SuppressLint("SimpleDateFormat")
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.txt_time_slot.setText(Common.convertTimeSlotToString(position));
        if(timeSlotList.size()==0){
            holder.card_time_slot.setCardBackgroundColor(context.getColor(R.color.white));
            holder.txt_time_slot_description.setText("Available");
            holder.txt_time_slot_description.setTextColor(context.getColor(R.color.black));
            holder.txt_time_slot.setTextColor(context.getColor(R.color.black));

        }
        else{
            for (TimeSlot slotValue:timeSlotList){
                int slot = slotValue.getSlot();
                if(slot == position){

                    holder.card_time_slot.setTag(Common.DISABLE_TAG);
                    holder.card_time_slot.setCardBackgroundColor(context.getColor(R.color.grey));

                    holder.txt_time_slot_description.setText("Full");
                    if(slotValue.getType().equals("Requested"))
                        holder.txt_time_slot_description.setText("Choosen");
                    holder.txt_time_slot_description.setTextColor(context.getColor(R.color.white));
                    holder.txt_time_slot.setTextColor(context.getColor(R.color.white));
                }
            }
        }
        if (!cardViewList.contains(holder.card_time_slot))
            cardViewList.add(holder.card_time_slot);


            holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
                @Override
                public void onItemSelectedListener(View view, final int pos) {
                    for(CardView cardView:cardViewList) {
                        if (cardView.getTag() == null)
                            cardView.setCardBackgroundColor(context.getColor(R.color.white));
                    }
                    holder.card_time_slot.setCardBackgroundColor(context.getColor(R.color.orange));

                    Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                    intent.putExtra(Common.KEY_TIME_SLOT,pos);
                    Common.currentTimeSlot = pos ;
                    intent.putExtra(Common.KEY_STEP,2);
                    Log.e("pos ", "onItemSelectedListener: "+position );
                    localBroadcastManager.sendBroadcast(intent);
                    if(Common.CurrentUserType.equals("Doctor") && holder.txt_time_slot_description.getText().equals("Available")){
                        AlertDialog.Builder alert = new AlertDialog.Builder(holder.card_time_slot.getContext());
                        alert.setTitle("Block");
                        alert.setMessage("Are you sure you want to block?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String doc_userid = doctor_journal_api.getInstance().getDoctor_userid();
                                TimeSlot timeSlot = new TimeSlot();
                                timeSlot.setSlot(pos);
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                timeSlot.setType("Accepted");
//                                Log.d("on save", "onClick: " + pos + " type " + timeSlot.getType() +
//                                        "date " + simpleDateFormat.format(Common.currentDate.getTime()));
                                CollectionReference date =FirebaseFirestore.getInstance()
                                        .collection("Doctor_booked_dates")
                                        .document(doc_userid)
                                        .collection(simpleDateFormat.format(Common.currentDate.getTime()));
                                date.add(timeSlot);
                                Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });

                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });

                        alert.show();

                    }
                }
            });
        }



    @Override
    public int getItemCount() {
        return 14;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot,txt_time_slot_description;
        CardView card_time_slot;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView)itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView)itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description = (TextView)itemView.findViewById(R.id.txt_time_slot_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
        }
    }
}
