package com.example.wavezcellular.adapters_holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wavezcellular.R;
import com.example.wavezcellular.utils.UserReport;

import java.util.List;
import java.util.Map;

public class BeachHomeAdapter extends RecyclerView.Adapter<BeachHomeHolder> {

    Context context;
    List<Map.Entry<String, String>> entries;

    public BeachHomeAdapter(Context context, List<Map.Entry<String, String>> entries) {
        this.context = context;
        this.entries = entries;
    }

    @NonNull
    @Override
    public BeachHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BeachHomeHolder(LayoutInflater.from(context).inflate(R.layout.beach_home_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BeachHomeHolder holder, int position) {
        String beachResult = entries.get(position).getValue();
        String beachName = entries.get(position).getKey();
        holder.name.setText(beachName);
        holder.result.setText(beachResult);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }
}
