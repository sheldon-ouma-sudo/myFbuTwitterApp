package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
@Parcel
//if adding a new variable I have to declare it and then iniitialize it with the value from the json with the EXACT matching name

public class Tweet {

    public  String body;
    public  String createdAt;
    public  long id;
    public String timeCreated;
    public User user;

    //empty constructor needed by the Parceler
    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        //tweet.createdAt = jsonObject.getString("created_at");
        tweet.id = jsonObject.getLong("id");
        tweet.timeCreated = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }

      public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
          List<Tweet> tweets = new ArrayList<>();
          for (int i= 0; i<jsonArray.length(); i++){
              tweets.add((fromJson(jsonArray.getJSONObject(i))));

          }
          return tweets;
      }



}
