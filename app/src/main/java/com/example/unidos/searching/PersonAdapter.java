package com.example.unidos.searching;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unidos.ElementoObservable;
import com.example.unidos.R;
import com.example.unidos.repository.Field;
import com.example.unidos.repository.ReportedPerson;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>{
    private static List<ReportedPerson> reportedPersonList;
    private static ReportedPerson reportedPerson;
    private static ElementoObservable<Boolean> isPersonSelected;
    private StorageReference storageReference;
    private Field field;
    private Date lastFifteenDays;

    public PersonAdapter(List<ReportedPerson> reportedPersonList) {
        field = new Field();
        this.reportedPersonList = reportedPersonList;
        isPersonSelected = new ElementoObservable<>();
    }

    public ElementoObservable<Boolean> getIsPersonSelected() {
        return isPersonSelected;
    }

    public ReportedPerson getReportedPerson() {
        return reportedPerson;
    }

    @NonNull
    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View personView = inflater.inflate(R.layout.person_item, parent, false);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, -15);
        lastFifteenDays = c.getTime();
        return new ViewHolder(personView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonAdapter.ViewHolder holder, int position) {
        ReportedPerson reportedPerson = reportedPersonList.get(position);
        holder.name.setText(reportedPerson.getFullName());
        holder.date.setText(field.dateToSimpleDate(reportedPerson.getRecSeenDate()));
        if(reportedPerson.isFound()) {
            holder.state.setText("Localizado");
            holder.cardView.setBackgroundResource(R.color.green);
        }else {
            holder.state.setText("No localizado");
            if(reportedPerson.getRecSeenDate().compareTo(lastFifteenDays) >= 0)
                holder.cardView.setBackgroundResource(R.color.red3);
            else
                holder.cardView.setBackgroundResource(R.color.orange);
        }
        holder.place.setText(String.valueOf(field.intToPlace(reportedPerson.getMissingPlace())));

        if(reportedPerson.getPhotoPath() != null) {
            if (!reportedPerson.getPhotoPath().isEmpty()) {
                storageReference = FirebaseStorage.getInstance().getReference().child(reportedPerson.getPhotoPath());
                Glide.with(holder.itemView.getContext())
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .fitCenter()
                        .into(holder.iv);
            }
        }else
            holder.iv.setImageResource(R.drawable.image_not_available);
    }

    @Override
    public int getItemCount() {
        return reportedPersonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView iv;
        public TextView name;
        public TextView date;
        public TextView state;
        public TextView place;
        CardView cardView;
        ConstraintLayout constraintLayout;

        public  ViewHolder(@NonNull View itemView){
            super(itemView);
            iv = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.tvName);
            date = itemView.findViewById(R.id.tvDate);
            state = itemView.findViewById(R.id.tvState);
            place = itemView.findViewById(R.id.tvPlace);
            cardView = itemView.findViewById(R.id.cardView);
            constraintLayout = itemView.findViewById(R.id.layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            reportedPerson = reportedPersonList.get(getAdapterPosition());
            isPersonSelected.setElemento(true);
        }

    }
}
