package com.example.tentsering.newsappreloaded;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.*;
import java.time.Instant;
import java.text.SimpleDateFormat;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<NewsObject> newsObjectData;
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    List<TextView> textViews = new ArrayList<>();
    Loadmore onLoadMoreListener;
    Activity activity;
    boolean isLoading;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;

    public static final String EXTRA_MESSAGE = "com.example.NewsAppReload.adapter.MESSAGE";

    public RecyclerviewAdapter(RecyclerView recyclerview, Activity activity, List<NewsObject> newsObjectData){
        this.activity = activity;
        this.newsObjectData = newsObjectData;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerview.getLayoutManager();
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return newsObjectData.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    public void setLoadMore(Loadmore loadMore){
     this.onLoadMoreListener = loadMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.item_layout, parent, false);
            return new MyHolder(view);
        }
        else if(viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){

        if(holder instanceof  MyHolder) {
            final NewsObject newsObject = newsObjectData.get(position);
            MyHolder myHolder = (MyHolder) holder;
            Picasso.get().load(newsObject.getImageUrl()).into(myHolder.articleImage);
            myHolder.articleTitle.setText(newsObject.getArticleTitle());

            myHolder.publishedTime.setText(fromISOToCalendar(newsObject.getPublishedTime()));

            myHolder.wholeArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ShowMeThatWebPage.class);
                    String sendThis = newsObject.getNewsUrl();
                    intent.putExtra(EXTRA_MESSAGE, sendThis);
                    view.getContext().startActivity(intent);

                }
            });
        }
        else if(holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return newsObjectData == null? 0:newsObjectData.size();
    }

    public void setLoading() {
        this.isLoading = false;
    }

    private String fromISOToCalendar(String dateString){
        String printThis;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        Date date = new Date();

        try{
            date = dateFormat.parse(dateString);
        }catch (ParseException e){
            e.printStackTrace();
        }
        date.setHours(date.getHours());
        calendar.setTime(date);
        long millisFromISO= calendar.getTimeInMillis();
        //printThis = ""+millisFromISO;

        printThis = finalDisplay(millisFromISO);
        return printThis;
    }

    /**
     * This method takes in long variable type as a parameter and run it through conditional statement and determines
     * whether to display the time passed since the article published in hour, hour(s) or minute, and finally return/print a String result
     * and call the method in fromISOToCalendar() method which will then be called in onBindViewHolder, which will be set to the publishedTime variable.
     * Thus the user will be able to view how many time has published since the publication of the current article he/she is viewing.
     * @param milliseconds stands for date converted in milliseconds
     * @return final result that will be printed inside the fromISOToCalendar: x hour ago, x hour(s) ago, or x minutes ago.
     */

    private String finalDisplay(long milliseconds){

        double hourToMil = 3600000; // hour to milliseconds: 1*60*60*1000 = 3600000
        double timepassed;

        long millisFromSystem = Calendar.getInstance().getTimeInMillis();
        long difference = millisFromSystem - milliseconds;
        timepassed = difference/hourToMil;
        String sendThis = "";

        double actualTime = 0;

            actualTime = 4 + timepassed;  // somehow i was losing and gaining 4 hours in Android Studio while passing fromISOToCalendar() method
                                          // in onBindViewHolder, therefore i have to compensate by adding 4.

        String thatPlace = Double.toString(actualTime);
        int decimalplace = thatPlace.indexOf('.');
        int stringsize = thatPlace.length();
        String beforeDot = thatPlace.substring(0, decimalplace);
        String afterDot = thatPlace.substring(decimalplace, stringsize);

        double numbeforeDot = Double.valueOf(beforeDot);
        double numafterDot = Double.valueOf(afterDot);
        int minute = (int)(Math.round(numafterDot*60));

        if(actualTime < 1){
            sendThis = "tp: " + timepassed+"\n"+
                    minute + " minute ago";
        }else{
            sendThis = "tp: " + timepassed+"\n"+
                    (int) numbeforeDot + " hour ago";
        }
        return sendThis;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView articleTitle, publishedTime;
        ImageView articleImage;
        CardView wholeArticle;

        public MyHolder(View itemView) {
            super(itemView);

            articleImage = (ImageView) itemView.findViewById(R.id.article_image);
            articleTitle = (TextView) itemView.findViewById(R.id.article_title);
            publishedTime = (TextView) itemView.findViewById(R.id.article_time);
            wholeArticle = (CardView) itemView.findViewById(R.id.wholeArticleContent);
        }
    }

   class LoadingViewHolder extends RecyclerView.ViewHolder{
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView){
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.show_progress);
        }

    }
}