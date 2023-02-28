package com.example.wavezcellular.adapters_holders;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wavezcellular.R;
import com.google.android.material.button.MaterialButton;

public class BeachHomeHolder extends RecyclerView.ViewHolder {

    LinearLayout linearLayout;
    MaterialButton name;
    MaterialButton result;
    public BeachHomeHolder(@NonNull View itemView) {
        super(itemView);
        linearLayout = itemView.findViewById(R.id.home_LinearLayout_beachData);
        result = itemView.findViewById(R.id.home_TXT_result);
        name = itemView.findViewById(R.id.home_TXT_beachName);
    }
}
