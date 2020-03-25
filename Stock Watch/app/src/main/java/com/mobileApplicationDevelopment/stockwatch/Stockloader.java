package com.mobileApplicationDevelopment.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class Stockloader extends AsyncTask<String,Void,String> {
    private MainActivity mainActivity;
    private static final String DATAURL_1 = "https://cloud.iexapis.com/stable/stock/";
    private static final String DATAURL_2 = "quote?token=pk_19117d6ecdc24096aa8aa118791055cb";

    public Stockloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... strings) {
        String API_URL = DATAURL_1 + strings[0]+"/" + DATAURL_2;
        Uri uri = Uri.parse(API_URL);
        String url_string = uri.toString();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line).append("\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Stock stock = jsonToMap(s);
        mainActivity.setStock(stock);
    }

    private Stock jsonToMap(String s) {
        Stock temp_stock = new Stock();
        try {
            JSONObject jsonObject = new JSONObject(s);
            String symbol = jsonObject.getString("symbol");
            String name = jsonObject.getString("companyName");
            double price = jsonObject.getDouble("latestPrice");
            double priceChange = jsonObject.getDouble("change");
            double changePercentage = jsonObject.getDouble("changePercent");

            temp_stock.setCompanyName(name);
            temp_stock.setCompanySymbol(symbol);
            temp_stock.setPrice(price);
            temp_stock.setPriceChange(priceChange);
            temp_stock.setChangePercentage(changePercentage);
            return temp_stock;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
