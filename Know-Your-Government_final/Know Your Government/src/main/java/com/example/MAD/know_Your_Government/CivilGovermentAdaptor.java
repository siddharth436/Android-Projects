package com.example.MAD.know_Your_Government;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class CivilGovermentAdaptor extends RecyclerView.Adapter<CivilGovermentViewHolder> {
    MainActivity mainActivity;
    Context context;
    List<CivilGovermentOfficial> civilGovermentOfficialList;

    public CivilGovermentAdaptor(MainActivity mainActivity, List<CivilGovermentOfficial> civilGovermentOfficialList) {
        this.mainActivity =  mainActivity;
        this.civilGovermentOfficialList = civilGovermentOfficialList;
    }
    @Override
    public CivilGovermentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thisItemsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gov_list,
                parent, false);
        thisItemsView.setOnClickListener(mainActivity);

        return new CivilGovermentViewHolder(thisItemsView);
    }

    @Override
    public void onBindViewHolder(CivilGovermentViewHolder holder, int position) {

        CivilGovermentOfficial civilGovermentOfficial = civilGovermentOfficialList.get(position);
        String s = civilGovermentOfficial.getCivilOfficial().getOfficialParty();
        holder.cilvilOfficeNameText.setText(civilGovermentOfficial.getOfficeName());
        if(s.equalsIgnoreCase("Democratic") || s.equalsIgnoreCase("Democratic Party"))
            holder.civilOfficialNameText.setText(civilGovermentOfficial.getCivilOfficial().getOfficialName()+" "+"("+"Democratic"+")");
        else if(s.equalsIgnoreCase("Republician") || s.equalsIgnoreCase("Republican Party"))
            holder.civilOfficialNameText.setText(civilGovermentOfficial.getCivilOfficial().getOfficialName()+" "+"("+"Republican"+")");
        else
            holder.civilOfficialNameText.setText(civilGovermentOfficial.getCivilOfficial().getOfficialName()+" "+"("+s+")");


    }

    @Override
    public int getItemCount() {
        return civilGovermentOfficialList.size();
    }

}
