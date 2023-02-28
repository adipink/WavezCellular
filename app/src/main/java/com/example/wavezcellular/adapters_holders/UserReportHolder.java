package com.example.wavezcellular.adapters_holders;

import android.view.View;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wavezcellular.R;
import com.google.android.material.textview.MaterialTextView;

public class UserReportHolder extends RecyclerView.ViewHolder {
    MaterialTextView name;
    MaterialTextView comment;
    RatingBar rating;
    //LinearLayout layout;
    public UserReportHolder(@NonNull View itemView) {
        super(itemView);
        //layout = itemView.findViewById(R.id.userReports_LinearLayout_report);
        name = itemView.findViewById(R.id.userReports_TXT_username);
        comment = itemView.findViewById(R.id.userReports_TXT_blurb);
        rating = itemView.findViewById(R.id.userReports_RB_review);
    }
}
