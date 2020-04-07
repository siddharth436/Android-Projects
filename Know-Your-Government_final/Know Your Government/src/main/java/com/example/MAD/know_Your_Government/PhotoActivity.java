package com.example.MAD.know_Your_Government;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;



public class PhotoActivity extends AppCompatActivity {
    TextView aboutTxtView;

    ImageView officialImgView;
    ConstraintLayout backgroundLayoutContraint;
    CivilGovermentOfficial mainCivilGovermentOfficial;
    TextView phtActLocTv;
    TextView name;
    TextView offc_party;
    ImageView officialLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        aboutTxtView = (TextView) findViewById(R.id.paOfcialInfoText);
        name = (TextView) findViewById(R.id.offc_name);


        officialImgView = (ImageView) findViewById(R.id.paImgView);
        backgroundLayoutContraint = (ConstraintLayout) findViewById(R.id.imgLogo);
        phtActLocTv = (TextView) findViewById(R.id.photoActBannerText);
        officialLogo=(ImageView) findViewById(R.id.imgLogo2);
        offc_party = (TextView) findViewById(R.id.offc_party_unique);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent ofcActIntent = getIntent();

        CivilGovermentOfficial civilGovermentOfficial = (CivilGovermentOfficial) ofcActIntent.getSerializableExtra(getString(R.string.SerializeGovementObject));
        mainCivilGovermentOfficial = civilGovermentOfficial;
        OfficialAddress locaAdd = civilGovermentOfficial.getBannerTextAddress();
        setOfficialImage(civilGovermentOfficial);
        aboutTxtView .setText(getOfficialInfoString(civilGovermentOfficial));
        aboutTxtView.setText(civilGovermentOfficial.getOfficeName());
        name.setText(civilGovermentOfficial.getCivilOfficial().getOfficialName());
        phtActLocTv.setText(locaAdd.getCity()+", "+locaAdd.getState());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent clickedNoteIntent = new Intent();
        setResult(RESULT_OK, clickedNoteIntent);
        finish();
    }


    String getOfficialInfoString(CivilGovermentOfficial civilGovermentOfficial){
        String aboutString = "";
        aboutString = civilGovermentOfficial.getOfficeName()
                + "\n"+ civilGovermentOfficial.getCivilOfficial().getOfficialName();
        if(civilGovermentOfficial.getCivilOfficial().getOfficialParty()!=null && civilGovermentOfficial.getCivilOfficial().getOfficialParty().length() > 0){
          //  aboutString =aboutString + "\n("+ civilGovermentOfficial.getCivilOfficial().getOfficialParty()+")";

            if(civilGovermentOfficial.getCivilOfficial().getOfficialParty().equalsIgnoreCase("Democratic Party")||
                    civilGovermentOfficial.getCivilOfficial().getOfficialParty().equalsIgnoreCase("Democratic")){
                int backgroundColor = getResources().getColor(R.color.colorBlue);
                backgroundLayoutContraint.setBackgroundColor(backgroundColor);

                aboutTxtView.setText(civilGovermentOfficial.getOfficeName());
                offc_party.setText("(Democratic Party)");

            }else if (civilGovermentOfficial.getCivilOfficial().getOfficialParty().equalsIgnoreCase("Republican Party")
                    || civilGovermentOfficial.getCivilOfficial().getOfficialParty().equalsIgnoreCase("Republican")){
                int myColor = getResources().getColor(R.color.colorRed);
                backgroundLayoutContraint.setBackgroundColor(myColor);
                offc_party.setText("(Republican Party)");
                aboutTxtView.setText(civilGovermentOfficial.getOfficeName());
            }
            else {
                int myColor = getResources().getColor(R.color.colorBlack);
                backgroundLayoutContraint.setBackgroundColor(myColor);
            }
        }
        return aboutString;
    }

    private void setOfficialImage(final CivilGovermentOfficial civilGovermentOfficial){

        final String photoUrl = civilGovermentOfficial.getCivilOfficial().getOfficialPhotoLink();
        if(civilGovermentOfficial.getCivilOfficial().getOfficialParty().equalsIgnoreCase("Democratic Party")){
            Picasso picasso = new Picasso.Builder(this).build();
            picasso.load(R.drawable.dem_logo).into(officialLogo);


        }
        else if(civilGovermentOfficial.getCivilOfficial().getOfficialParty().equalsIgnoreCase("Republican Party")) {
            Picasso picasso = new Picasso.Builder(this).build();
            picasso.load(R.drawable.rep_logo).into(officialLogo);
        }
        if ( photoUrl != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    // Here we try https if the http image attempt failed
                    final String changedUrl = photoUrl.replace("http:", "https:");

                    picasso.load(changedUrl)
                            .fit()
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(officialImgView);
                }
            }).build();

            picasso.load(photoUrl)
                    .fit()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(officialImgView);

        } else {
            Picasso.with(this).load(photoUrl)
                    .fit()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(officialImgView);

        }
    }

}
