package com.example.aanandam.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aanandam.Appointement;
import com.example.aanandam.Common.Common;
import com.example.aanandam.R;
import com.example.aanandam.model.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class patient_appointement_adapter extends RecyclerView.Adapter<patient_appointement_adapter.ViewHolder> {
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Appointement> appointementList;
    public patient_appointement_adapter(Context context, List<Appointement> appointementList) {
        this.context = context;
        this.appointementList = appointementList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.patient_appointement,parent,false);
        return new ViewHolder(view,context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointement appointement= appointementList.get(position);
        String slot = Common.convertTimeSlotToString(appointement.getBook_slot());
        holder.time.setText(slot+" at "+appointement.getBook_date());
        holder.doc_name.setText(appointement.getDoctor_name());
        holder.problem.setText("Problem : "+appointement.getProblem());
        holder.doctor_no.setText(appointement.getDoc_contact());
        if(appointement.getStatus() == 0){
            holder.status.setText("Requested");
        }else{
            holder.status.setText("Accepted");
        }
        String Imageurl = appointement.getDoctor_image();
        Picasso.get().load(Imageurl)
                .noFade().fit().placeholder(R.drawable.doctor_image).into(holder.Doctor_image);
    }

    @Override
    public int getItemCount() {
        return appointementList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView consultation,time,doc_name,doctor_no,problem,statusicon,status;
        private CircularImageView Doctor_image;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            ctx = context;
            consultation = itemView.findViewById(R.id.patient_request_consultation);
            statusicon = itemView.findViewById(R.id.statusicon_image_in_patient_apointement);
            status = itemView.findViewById(R.id.status_in_patient_apointement);
            time = itemView.findViewById(R.id.appointementtime_in_patient_apointement);
            doc_name = itemView.findViewById(R.id.doc_name_in_patient_apointement);
            doctor_no = itemView.findViewById(R.id.doc_no_in_patient_apointement);
            problem = itemView.findViewById(R.id.patient_appointement_problem);
            Doctor_image = itemView.findViewById(R.id.doc_image_in_patient_apointement);
        }
    }
}
