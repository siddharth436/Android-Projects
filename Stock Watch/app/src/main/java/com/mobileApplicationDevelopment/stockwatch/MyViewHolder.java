package com.mobileApplicationDevelopment.stockwatch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileApplicationDevelopment.stockwatch.R;

class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView companyName;
    public TextView companySymbol;
    public TextView price;
    public TextView priceChange;
    public TextView changePercentage;
    public ImageView arrow;
    public View dividerView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        companyName = itemView.findViewById(R.id.companynameTextView);
        companySymbol = itemView.findViewById(R.id.companysymbolTextView);
        price = itemView.findViewById(R.id.priceTextView);
        priceChange = itemView.findViewById(R.id.pricechangeTextView);
        changePercentage = itemView.findViewById(R.id.changepercentageTextView);
        arrow = itemView.findViewById(R.id.arrowImageView);
        dividerView = itemView.findViewById(R.id.divider);
    }
}
