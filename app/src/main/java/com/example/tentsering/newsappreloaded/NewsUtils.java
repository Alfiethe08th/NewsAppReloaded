package com.example.tentsering.newsappreloaded;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsUtils {
private static final String LOG_TAG = NewsUtils.class.getSimpleName();
    HttpURLConnection connection;
    List<NewsObject> data;


    public List<NewsObject> extractItemData(String url){

        try{
            URL url1 = new URL(url);

            connection = (HttpURLConnection) url1.openConnection();
            connection.connect();
            InputStream inputStream= connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuffer buffer = new StringBuffer();
            while ((line=reader.readLine())!=null){

                buffer.append(line);
                String result = buffer.toString();
                data = new ArrayList<>();
                JSONObject jsonRoot = new JSONObject(result);
                JSONArray jsonArticles = jsonRoot.getJSONArray("articles");

                for(int i=0; i<jsonArticles.length(); i++) {
                    NewsObject newsObject = new NewsObject();
                    JSONObject jsonIndices = jsonArticles.getJSONObject(i);

                    JSONObject jsonSource = jsonIndices.getJSONObject("source"); //at i-th object
                    String newsBrand = jsonSource.getString("name");  //get String called name to store name of the news paper company
                    String newsImage = jsonIndices.getString("urlToImage");      //image url
                    String newsTitle = jsonIndices.getString("title");           //article title
                    String newsTime = jsonIndices.getString("publishedAt");      //time at which the article was published
                    String newsUrl = jsonIndices.getString("url");               //url to the whole article

                    newsObject.setmImageUrl(newsImage);
                    newsObject.setmArticleTitle(newsTitle);
                    newsObject.setmPublishedTime(newsTime);

                    newsObject.setmNewsCompany(newsBrand);
                    newsObject.setmNewsUrl(newsUrl);
                    data.add(newsObject);

                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }


}
