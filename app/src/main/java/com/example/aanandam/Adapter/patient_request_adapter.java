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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class patient_request_adapter extends RecyclerView.Adapter<patient_request_adapter.ViewHolder> {
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Appointement");
    private CollectionReference collectionReferenceslot = db.collection("Doctor_booked_dates");
    private List<Appointement> appointementList;
    public patient_request_adapter(Context context, List<Appointement> appointementList) {
        this.context = context;
        this.appointementList = appointementList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.patient_request_card,parent,false);
        return new ViewHolder(view,context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointement appointement= appointementList.get(position);
        String slot = Common.convertTimeSlotToString(appointement.getBook_slot());
        holder.time.setText(slot+" at "+appointement.getBook_date());
        holder.patient_name.setText(appointement.getPatient_name());
        holder.problem.setText(appointement.getProblem());
        holder.patient_no.setText(appointement.getPatient_contact());
    }

    @Override
    public int getItemCount() {
        return appointementList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView consultation,time,patient_name,patient_no,problem,prob_icon;
        private Button accept,reject;
        private ImageView name_icon,contact_icon;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            ctx = context;
            consultation = itemView.findViewById(R.id.patient_request_consultation);
            time = itemView.findViewById(R.id.patient_request_time);
            patient_name = itemView.findViewById(R.id.patient_request_patientname);
            patient_no = itemView.findViewById(R.id.patient_request_phoneno);
            prob_icon = itemView.findViewById(R.id.patient_request_probicon);
            problem = itemView.findViewById(R.id.patient_request_problem);
            accept = itemView.findViewById(R.id.patient_request_acceptbtn);
            reject = itemView.findViewById(R.id.patient_request_rejectbtn);
            name_icon = itemView.findViewById(R.id.patient_request_perrsonicon);
            contact_icon = itemView.findViewById(R.id.patient_request_phoneicon);
            accept.setOnClickListener(this);
            reject.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Appointement appointement = appointementList.get(getAdapterPosition());
            switch (view.getId()){
                case R.id.patient_request_acceptbtn:
                    appointement.setStatus(1);
                    String access_id = appointement.getDoctor_name()+"_"
                            +appointement.getPatient_name()+"_"+appointement.getBook_date()+"_slot_"+appointement.getBook_slot();
                    final DocumentReference documentReference = collectionReference.document(access_id);
                    documentReference.set(appointement);
                    final CollectionReference doc = collectionReferenceslot.document(appointement.getDoctor_userid())
                            .collection(appointement.getBook_date());
                    doc.whereEqualTo("slot",appointement.getBook_slot())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                                    TimeSlot slot = documentSnapshot.toObject(TimeSlot.class);
                                    slot.setType("Accepted");
                                    doc.document(documentSnapshot.getId()).set(slot);
                                    Toast.makeText(context, "Appointement is accepted", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                    view.setVisibility(View.INVISIBLE);
                    break;
                case R.id.patient_request_rejectbtn:
                    String accessid = appointement.getDoctor_name()+"_"
                            +appointement.getPatient_name()+"_"+appointement.getBook_date()+"_slot_"+appointement.getBook_slot();
                    final DocumentReference documentReference1 = collectionReference.document(accessid);
                    documentReference1.delete();
                    final CollectionReference doc1 = collectionReferenceslot.document(appointement.getDoctor_userid())
                            .collection(appointement.getBook_date());
                    doc1.whereEqualTo("slot",appointement.getBook_slot())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                                    TimeSlot slot = documentSnapshot.toObject(TimeSlot.class);
                                    doc1.document(documentSnapshot.getId()).delete();
                                    Toast.makeText(context, "Appointement is Deleted", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                    view.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }
}
