package com.example.aanandam.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aanandam.Appointement;
import com.example.aanandam.Common.Common;
import com.example.aanandam.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Doctor_appointement_adapter extends RecyclerView.Adapter<Doctor_appointement_adapter.ViewHolder> {
    private Context context;
    private List<Appointement> appointementList;
    public Doctor_appointement_adapter(Context context, List<Appointement> appointementList) {
        this.context = context;
        this.appointementList = appointementList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_appointement,parent,false);
        return new ViewHolder(view,context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointement appointement= appointementList.get(position);
        String slot = Common.convertTimeSlotToString(appointement.getBook_slot());
        holder.time.setText(slot+" at "+appointement.getBook_date());
        holder.patient_name.setText(appointement.getPatient_name());
        holder.problem.setText("Problem : "+appointement.getProblem());
        holder.patient_no.setText(appointement.getPatient_contact());
        String Imageurl = appointement.getPatient_image();
        Picasso.get().load(Imageurl)
                .noFade().fit().placeholder(R.drawable.ic_anon_user_48dp).into(holder.patient_image);
    }

    @Override
    public int getItemCount() {
        return appointementList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView consultation,time,patient_name,patient_no,problem;
        private CircularImageView patient_image;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            ctx = context;
            consultation = itemView.findViewById(R.id.consultation_in_doc_apointement);
            time = itemView.findViewById(R.id.appoitementtime_time_in_doc_apointement);
            patient_name = itemView.findViewById(R.id.patient_name_in_doc_apointement);
            patient_no = itemView.findViewById(R.id.patient_no_in_doc_apointement);
            problem = itemView.findViewById(R.id.problem_in_doc_apointement);
            patient_image = itemView.findViewById(R.id.patient_image_in_doc_apointement);
        }
    }
}
