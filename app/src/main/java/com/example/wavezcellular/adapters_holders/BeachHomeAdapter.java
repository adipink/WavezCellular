package com.example.wavezcellular.adapters_holders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wavezcellular.Interfaces.BeachListener;
import com.example.wavezcellular.R;

import java.util.List;
import java.util.Map;

public class BeachHomeAdapter extends RecyclerView.Adapter<BeachHomeHolder> {

    Context context;
    List<Map.Entry<String, String>> entries;
    private BeachListener beachListener;

    public BeachHomeAdapter(Context context, List<Map.Entry<String, String>> entries, BeachListener listener) {
        this.context = context;
        this.entries = entries;
        this.beachListener = listener;
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

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beachListener.onBeachClicked(beachName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }
}
