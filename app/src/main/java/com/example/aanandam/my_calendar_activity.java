package com.example.aanandam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aanandam.Adapter.MyTimeSlotAdapter;
import com.example.aanandam.Common.Common;
import com.example.aanandam.Interface.ITimeSlotLoadListener;
import com.example.aanandam.model.TimeSlot;
import com.example.aanandam.util.doctor_journal_api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class my_calendar_activity extends AppCompatActivity implements ITimeSlotLoadListener {
    DocumentReference doctorDoc;
    private RecyclerView recyclerView;
    private TextView textView;
    private HorizontalCalendarView horizontalCalendar;
    String doc_userid;
    private SimpleDateFormat simpleDateFormat;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    private android.app.AlertDialog alertDialog;
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar_activity);
        textView = findViewById(R.id.my_calendar_textview);
        recyclerView = findViewById(R.id.recycle_time_slot2);
        horizontalCalendar = findViewById(R.id.my_calendar_in_doc);
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        iTimeSlotLoadListener = this;
        alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(this)
                .build();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, 0);
        if(doctor_journal_api.getInstance() != null){
            doc_userid = doctor_journal_api.getInstance().getDoctor_userid();
        }
//        TimeSlot timeSlot = new TimeSlot();
//        timeSlot.setSlot(5);
//        timeSlot.setType("Accepted");
//        CollectionReference booking = FirebaseFirestore.getInstance()
//                .collection("Doctor_booked_dates")
//                .document(doc_userid)
//                .collection(simpleDateFormat.format(date.getTime()));
//        booking.add(timeSlot);
        loadAvailabelTimeSlotOfDoctor(doc_userid, simpleDateFormat.format(date.getTime()));
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 5);
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.my_calendar_in_doc)
                .range(startDate, endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if (Common.currentDate.getTimeInMillis() != date.getTimeInMillis()) {
                    Common.currentDate = date;
                    loadAvailabelTimeSlotOfDoctor(doc_userid, simpleDateFormat.format(date.getTime()));

                }

            }
        });
    }

    private void loadAvailabelTimeSlotOfDoctor(String curreentDoctor, final String bookDate) {
        alertDialog.show();
        CollectionReference date = FirebaseFirestore.getInstance()
                .collection("Doctor_booked_dates")
                .document(curreentDoctor)
                .collection(bookDate);
        date.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                }else{
                    List<TimeSlot> timeSlots = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                        TimeSlot slot = snapshot.toObject(TimeSlot.class);
                        timeSlots.add(slot);
                    }
                    iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
            }
        });
    }


    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(this,timeSlotList);
        recyclerView.setAdapter(adapter);
        alertDialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(this);
        recyclerView.setAdapter(adapter);
        alertDialog.dismiss();
    }
}
