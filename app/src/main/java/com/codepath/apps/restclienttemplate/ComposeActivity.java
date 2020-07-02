package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    public static final  String TAG = "CompsoeActivity";
    public static  final int MAX_TWEET_LENGTH =  140;

    EditText etCompose;
    Button buttonTweet;
    long  statusId;

    TwitterClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApplication.getRestClient(this);

          statusId = -1;





        // connect the the mainActivity to the recycler VIEW
        etCompose = findViewById(R.id.etCompose);
        buttonTweet = findViewById(R.id.buttonTweet);
        //set a clickListener on button
        buttonTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TO GEET A TEXT OF WHAT THE USER HAS WRITTEN
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Sorry your tweet  cannnot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Sorry your tweet is too long", Toast.LENGTH_SHORT).show();
                    return;

                }

                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_SHORT).show();
                //make an api to twitter to publish the twitter
             client.publishedTweet(tweetContent,  statusId, new JsonHttpResponseHandler() {
                 @Override
                 public void onSuccess(int statusCode, Headers headers, JSON json) {
                     Log.e(TAG,"OnSuccess to publish tweet");
                     try {
                         Tweet tweet = Tweet.fromJson(json.jsonObject);
                         Log.i(TAG,"Pusblished Tweet says:" + tweet.body);
                         Intent intent = new Intent();
                         intent.putExtra("tweet", Parcels.wrap(tweet));
                         //set result code and bundled data for the result
                         setResult(RESULT_OK,intent);
                         finish();
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }

                 }

                 @Override
                 public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                   Log.e(TAG,"OnFaiilure To Publish Tweet", throwable);
                 }
             });
            }

        });

    }
}