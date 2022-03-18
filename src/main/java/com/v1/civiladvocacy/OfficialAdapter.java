package com.v1.civiladvocacy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder>
{
    private static final String TAG = "OfficialAdapter";
    private List<Official> officials;
    private MainActivity mA;

    public OfficialAdapter(List<Official> officialList, MainActivity mainActivity)
    {
        this.officials = officialList;
        this.mA = mainActivity;
    }

    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.database_official,parent,false);
        itemView.setOnClickListener(mA);
        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position)
    {
        Official converter = officials.get(position);
        holder.addressforoffice.setText(converter.getPosition());
        holder.name.setText(converter.getIdentity());
        holder.side.setText(converter.getSide());
    }

    @Override
    public int getItemCount()
    {
        return officials.size();
    }
}
