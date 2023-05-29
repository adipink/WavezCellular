package com.example.wavezcellular.adapters_holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wavezcellular.R;
import com.example.wavezcellular.activities.ShowActivity;
import com.example.wavezcellular.utils.UserReport;

import java.util.List;

public class UserReportAdapter extends RecyclerView.Adapter<UserReportHolder> {
    Context context;
    List<UserReport> userReports;

    public UserReportAdapter(Context context, List<UserReport> userReports) {
        this.context = context;
        this.userReports = userReports;
    }

    @NonNull
    @Override
    public UserReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserReportHolder(LayoutInflater.from(context).inflate(R.layout.user_report_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserReportHolder holder, int position) {
        holder.name.setText(userReports.get(position).getName());
        holder.comment.setText(userReports.get(position).getComment());
        Object o = userReports.get(position).getValue("review");
        double reviewDouble = ShowActivity.getDouble(o);
        holder.rating.setRating((float) reviewDouble);
    }

    @Override
    public int getItemCount() {
        return userReports.size();
    }
}


