package com.example.tentsering.newsappreloaded;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeContainer;
    RecyclerviewAdapter adapter;
    RecyclerView recyclerView;
    Button view;
    final String webUrl = "https://newsapi.org/v2/top-headlines?country=us&apiKey=5745de3e99e743d6a81f176b5b8d7e76";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        //setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       swipeContainer.setRefreshing(false);
                   }
               },3000);
            }
        });

        new ViewData().execute(webUrl);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }


    class ViewData extends Urltask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final List<NewsObject> newsObjects) {
            super.onPostExecute(newsObjects);

                adapter = new RecyclerviewAdapter(recyclerView, MainActivity.this, newsObjects);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);

                adapter.setLoadMore(new Loadmore() {
                    @Override
                    public void onLoadMore() {
                        if(newsObjects.size()<=20){
                            newsObjects.add(null);
                            adapter.notifyItemInserted(newsObjects.size()-1);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    newsObjects.remove(newsObjects.size()-1);
                                    adapter.notifyItemRemoved(newsObjects.size());
                                    adapter.notifyDataSetChanged();
                                    adapter.setLoading();
                                }
                            }, 3000);
                        }
                    }
                });
        }
    }

}