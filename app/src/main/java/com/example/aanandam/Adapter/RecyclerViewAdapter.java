package com.example.aanandam.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aanandam.R;
import com.example.aanandam.doctor_info;
import com.example.aanandam.model.doc_profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<doc_profile> doc_profileList;
    private List<doc_profile> doc_profileList_all;

    public RecyclerViewAdapter(Context context, List<doc_profile> doc_profileList) {
        this.context = context;
        this.doc_profileList = doc_profileList;
        this.doc_profileList_all = new ArrayList<>(doc_profileList);
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_card,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        String Imageurl;
        doc_profile doc_profile = doc_profileList.get(position);
        holder.doctor_name.setText(doc_profile.getFull_name());
        holder.speciality.setText(doc_profile.getSpecialty());
        holder.location.setText(doc_profile.getLocation());
        Imageurl = doc_profile.getImage_uri();
        Picasso.get()
                .load(Imageurl)
                .placeholder(R.drawable.doctor_image)
        .fit().into(holder.doctor_pic);

    }

    @Override
    public int getItemCount() {
        return doc_profileList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<doc_profile> filtered_list = new ArrayList<>();
            if(charSequence.toString().isEmpty()){
                filtered_list.addAll(doc_profileList_all);
            }
            else {
                for(doc_profile doc_profile : doc_profileList_all){
                    if(doc_profile.getFull_name().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filtered_list.add(doc_profile);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered_list;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            doc_profileList.clear();
            doc_profileList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView doctor_pic;
        private TextView doctor_name,speciality,location;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            ctx = context;
            doctor_name = itemView.findViewById(R.id.doctor_name_card);
            doctor_pic = itemView.findViewById(R.id.doctor_pic_card);
            speciality = itemView.findViewById(R.id.doctor_speciality_card);
            location = itemView.findViewById(R.id.doctor_location_card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            doc_profile doc_profile = doc_profileList.get(getAdapterPosition());
            String userid = doc_profile.getUserid();
            Intent intent = new Intent(context,doctor_info.class);
            intent.putExtra("userid",userid);
            context.startActivity(intent);
        }
    }

}
