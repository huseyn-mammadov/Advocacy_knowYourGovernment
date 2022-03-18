package com.v1.civiladvocacy;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewHolder extends RecyclerView.ViewHolder
{
    TextView addressforoffice;
    TextView name;
    TextView side;
    public OfficialViewHolder(@NonNull View itemView)
    {
        super(itemView);
        addressforoffice = itemView.findViewById(R.id.addressforoffice);
        name = itemView.findViewById(R.id.identity);
        side = itemView.findViewById(R.id.side);
    }
}
