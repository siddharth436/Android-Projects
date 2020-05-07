package com.mad.siddharth.iit_news_gateway;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


public class AsyncSourceLoader extends AsyncTask<String, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private String prefix = "https://newsapi.org/v2/sources?language=en&country=us&category=";
    private String key= "&apiKey=1d57e7f58d9e47bea201c0b52046c5b9";


    public AsyncSourceLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    protected String doInBackground(String... params) {
        String category = params[0];


        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(prefix+category+key);
            String s = url.toString();



            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");

            return sb.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<NewsSource> countryList = parseJSON(s);
        if (countryList != null) {
            mainActivity.updateData(countryList);


        }
    }

    private ArrayList<NewsSource> parseJSON(String s) {
        ArrayList<NewsSource> countryList = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray jArrSources = jObjMain.getJSONArray("sources");
//            mainActivity.clearSource();

            for (int i =0; i<jArrSources.length(); i++){
                JSONObject jObjSource = jArrSources.getJSONObject(i);
                String id = jObjSource.getString("id");
                String name = jObjSource.getString("name");
                String url = jObjSource.getString("url");
                String category = jObjSource.getString("category");

                countryList.add(new NewsSource(id,name,url,category));


            }
            return countryList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
