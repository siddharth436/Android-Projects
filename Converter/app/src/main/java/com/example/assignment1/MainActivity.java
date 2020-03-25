package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static double miles_to_kilo = 1.60934;
    public static double kilo_to_miles = 0.621371;


    private TextView textView,textView1,textView2,result;
    private EditText ip;
    private RadioGroup rg;
    private static final String empty="";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.milesView);
        textView1 = findViewById(R.id.kilometresView);
        textView2 = findViewById(R.id.savedHistory);
        ip =    findViewById(R.id.milesValue);
        result = findViewById(R.id.kiloValue);
        rg = findViewById(R.id.radioGroup);


        textView2.setMovementMethod(new ScrollingMovementMethod());






    }



       public void radioClicked(View v) {
           String selectionText = ((RadioButton) v).getText().toString();
           result.setText("");

           if (selectionText.equals("Miles to Kilometers")) {
               textView.setText("Miles Value:");
               textView1.setText("Kilometers Value:");
           } else {
               textView.setText("Kilometers Value:");
               textView1.setText("Miles Value:");
           }
       }


       public void doConvert(View v) {


           int id = rg.getCheckedRadioButtonId();
           String value = textView2.getText().toString();
           switch (id) {
               case R.id.milesToKilo:
                   if(ip.getText().toString().equals("")){
                       ip.setText(empty);
                      break;
                   }
                   else {


                       double milesValue = Double.parseDouble(ip.getText().toString());
                       double KMS = milesValue * miles_to_kilo;
                       result.setText(String.format("%.1f", KMS));
                       value = milesValue + " Mi ==> " + String.format("%.1f", KMS) + " Km\n" + value;
                       textView2.setText(value);
                       break;
                   }
               case R.id.kiloToMiles:
                   if(ip.getText().toString().equals("")){
                       ip.setText(empty);
                       break;

                   }
                   else {
                       double kmsValue = Double.parseDouble(ip.getText().toString());
                       double MILES = kmsValue * kilo_to_miles;
                       result.setText(String.format("%.1f", MILES));
                       value = kmsValue + " Km ==> " + String.format("%.1f", MILES) + " Mi\n" + value;
                       textView2.setText(value);
                       break;
                   }
           }
           ip.setText("");
       }

    public void clearHistory(View v) {
        textView2.setText("");
        result.setText("");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("TextView", textView.getText().toString());
        outState.putString("TextView1", textView1.getText().toString());
        outState.putString("HISTORY", textView2.getText().toString());

        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);

        textView.setText(savedInstanceState.getString("TextView"));
        textView1.setText(savedInstanceState.getString("TextView1"));
        textView2.setText(savedInstanceState.getString("HISTORY"));
    }











   }








