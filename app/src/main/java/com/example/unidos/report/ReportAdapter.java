package com.example.unidos.report;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unidos.ElementoObservable;
import com.example.unidos.R;
import com.example.unidos.repository.Field;
import com.example.unidos.repository.Report;

import java.util.List;

public class ReportAdapter  extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private static Report report;
    private static List<Report> reportList;
    private static ElementoObservable<Integer> reportSelected;
    private Field field;

    protected ReportAdapter(List<Report> reportList){
        this.reportList = reportList;
        reportSelected = new ElementoObservable<>();
        field = new Field();
    }

    public ElementoObservable<Integer> getReportSelected() {
        return reportSelected;
    }

    public static Report getReport() {
        return report;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View reportView = inflater.inflate(R.layout.report_item, parent, false);
        return new ViewHolder(reportView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ViewHolder holder, int position) {
        Log.i("######", "lets set the values");
        Report report = reportList.get(position);
        holder.repType.setText(report.repTypeToString(report.getReportType()));
        holder.seenDate.setText(field.dateToSimpleDate(report.getSeenDate()));
        holder.lastPlace.setText(String.valueOf(report.getTown()));
    }

    @Override
    public int getItemCount(){return  reportList.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView iv;
        private TextView repType, seenDate, lastPlace;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            iv = itemView.findViewById(R.id.repIcon);
            repType = itemView.findViewById(R.id.repType);
            seenDate = itemView.findViewById(R.id.seenDate);
            lastPlace = itemView.findViewById(R.id.lastSeenPlace);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            reportSelected.setElemento(getAdapterPosition());
        }
    }
}
