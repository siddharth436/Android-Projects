package com.mad.siddharth.iit_news_gateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ArticleFragment extends Fragment implements Serializable{
    private static final String TAG = "ArticleFrag";

    public static final ArticleFragment newInstance(Article article){
        ArticleFragment f = new ArticleFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("article", article);
        f.setArguments(bdl);
        return f;
    }
   

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final Article article;

        if (savedInstanceState == null)
            article = (Article) getArguments().getSerializable("article");
        else
            article = (Article) savedInstanceState.getSerializable("article");
        View v = inflater.inflate(R.layout.fragment_article, container, false);

        TextView titleTextView = (TextView) v.findViewById(R.id.titleTextView);
        TextView authorTexView = (TextView) v.findViewById(R.id.authorTextView);
        TextView dateTextView = (TextView) v.findViewById(R.id.dateTextView);
        TextView descriptionTextView = (TextView) v.findViewById(R.id.descript);
        TextView indexTextView = (TextView) v.findViewById(R.id.indexTextView);
        final ImageButton imageButton = (ImageButton) v.findViewById(R.id.image);

        titleTextView.setText(article.getTitle());
        String author=article.getAuthor();
        if(author.equalsIgnoreCase("null"))
            authorTexView.setText("");
        else
            authorTexView.setText(article.getAuthor());
        String date = (article.getPublishedAt().split("T")[0]);
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MMM dd, yyyy hh:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date da = null;
        String str = null;

        try {
            da = inputFormat.parse(article.getPublishedAt());
            str = outputFormat.format(da);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(str.equalsIgnoreCase("null"))
            dateTextView.setText("");
        else
            dateTextView.setText(str);



        descriptionTextView.setText(article.getDescription());
        indexTextView.setText(""+article.getIndex()+" of "+(article.getTotal()-90));
        Log.d(TAG, "onCreateView: " +article.getPublishedAt().split("T")[0]);

        if (article.getUrlToImage() != null){
            Picasso picasso = new Picasso.Builder(v.getContext()).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
                    final String changedUrl = article.getUrlToImage().replace("http:", "https:");
                    picasso.load(changedUrl) .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder) .into(imageButton);
                }
            }).build();

            picasso.load(article.getUrlToImage()) .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder) .into(imageButton);
        } else {
            Picasso.with(v.getContext()).load(article.getUrlToImage()) .error(R.drawable.brokenimage).placeholder(R.drawable.missingimage);
        }

        final Intent i = new Intent((Intent.ACTION_VIEW));
        i.setData(Uri.parse(article.getUrl()));
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
        descriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("article", getArguments().getSerializable("article"));
    }


}
