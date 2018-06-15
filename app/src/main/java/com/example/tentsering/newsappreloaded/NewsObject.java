package com.example.tentsering.newsappreloaded;


//This NewsObject class will be passed inside "" later for smooth data transaction and setting up ForYouLayout fragment
public class NewsObject {

    private String mImageUrl;  //image url that will loaded in ImageView later
    private String mNewsCompany; //which news Company
    private String mArticleTitle;  //title of the article
    private String mPublishedTime; //at what time it was published
    private String mNewsUrl;

    /**
     *
     * @return image url
     */
    public String getImageUrl(){
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    /**
     *
     * @return which company posted the news
     */

    public String getNewsCompany(){
        return mNewsCompany;
    }

    public void setmNewsCompany(String mNewsCompany) {
        this.mNewsCompany = mNewsCompany;
    }

    /**
     *
     * @return title of the article at sight
     */
    public String getArticleTitle(){
        return mArticleTitle;
    }

    public void setmArticleTitle(String mArticleTitle) {
        this.mArticleTitle = mArticleTitle;
    }

    /**
     *
     * @return how many hours ago was this news published
     */
    public String getPublishedTime(){
        return mPublishedTime;
    }

    public void setmPublishedTime(String mPublishedTime) {
        this.mPublishedTime = mPublishedTime;
    }

    /**
     *
     * @return url to the whole article
     */
    public String getNewsUrl(){
        return mNewsUrl;
    }

    public void setmNewsUrl(String mNewsUrl) {
        this.mNewsUrl = mNewsUrl;
    }
}
