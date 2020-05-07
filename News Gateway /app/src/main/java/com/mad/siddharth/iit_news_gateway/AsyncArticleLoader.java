package com.mad.siddharth.iit_news_gateway;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class AsyncArticleLoader extends AsyncTask<String, Void, String> {

    private NewsService newsService;
    private String prefix="https://newsapi.org/v2/everything?sources=";
    private String apikey="&language=en&pageSize=100&apiKey=1d57e7f58d9e47bea201c0b52046c5b9";
    private static final String TAG = "jojo";

    public AsyncArticleLoader(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    protected String doInBackground(String... params) {
        String source = params[0];
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(prefix+source+apikey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            Log.d(TAG, "article_doInBackground: "+sb.toString());
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
        parseJson(s);
    }

    private void parseJson(String s) {
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray jArrSources = jObjMain.getJSONArray("articles");
            for (int i =0; i<jArrSources.length(); i++){
                JSONObject jObjSource = jArrSources.getJSONObject(i);
                String author = jObjSource.getString("author");
                String title = jObjSource.getString("title");
                String description = jObjSource.getString("description");
                String url = jObjSource.getString("url");
                String urlToImage = jObjSource.getString("urlToImage");
                String publishedAt = jObjSource.getString("publishedAt");

                newsService.addArticle(new Article(author, title, description, url, urlToImage, publishedAt, jArrSources.length(),i));
                if (isCancelled())
                    return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
