package com.example.tentsering.newsappreloaded;


import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

abstract class Urltask extends AsyncTask<String, String, List<NewsObject>> {

    @Override
    protected List<NewsObject> doInBackground(String... strings) {
        NewsUtils newsUtils = new NewsUtils();
        List<NewsObject> list = newsUtils.extractItemData(strings[0]);
        return list;
    }

}

