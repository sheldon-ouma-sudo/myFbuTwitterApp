package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static  final String TAG = "TimelineActivity";
    public static final int REQUEST_CODE = 20;
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    long maxId;
    EndlessRecyclerViewScrollListener scrollListener;
    SwipeRefreshLayout swipeContainer;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApplication.getRestClient(this);
        swipeContainer = findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Log.i(TAG,"fetching new data!");
               popularHomeTimeline();
            }
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

            });




            //find the recycler view
         rvTweets  = findViewById(R.id.rvTweets);
        //init the list of tweets and adapter
       tweets = new ArrayList<>();
       adapter = new TweetsAdapter(this,tweets);
        //Recycler view setup:layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore" + page );
                loadMoreData();

            }
        };
//ADD SCROLL LISTENER TO THE RECYCLER VIEW
        rvTweets.addOnScrollListener(scrollListener);

        popularHomeTimeline();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     if(item.getItemId() == R.id.Compose) {
     //compose item has been selected

         //Navigate the compose activitiy

         //Intent takes in two parameters, where we are(this) and where we are going(compose activity)
         Intent intent = new Intent(this, ComposeActivity.class);

         startActivityForResult(intent,REQUEST_CODE);

         return true;
     }
     return onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if(requestCode==REQUEST_CODE && resultCode == RESULT_OK){
           //Get Data from the intent-tweet object
         Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
           //update the recycler view with the object

           tweets.add(0,tweet);
           //We added tweet into the timeline Activity and we are manually updating the adapter
           adapter.notifyItemInserted(0);
           //makes to the latest tweet
           rvTweets.smoothScrollToPosition(0);

       }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadMoreData() {
        //1.Send an API request to retrieve approriate paginated data
        client.getNextPageOfTweets(new JsonHttpResponseHandler()  {
            @Override
             public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG,"onSuccess for loadMoreData!"+ json.toString());
                //2.Dsesrialize and construct new model objects from the API response
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<Tweet> tweets = Tweet.fromJsonArray(jsonArray);

                    //4.Notify the adapter of the new items made with notifyingItemRangeInsertedd
                    //3.Apppend the new data ongects to the existing set of items inside the array of items
                   adapter.addAll(tweets);
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG,"onFailure for loadMoreData!", throwable);
            }
        }, tweets.get(tweets.size()-1).id);



    }

    private void popularHomeTimeline() {
        if(tweets.size() == 0){
            maxId = -1;
        }
        else{
            maxId = tweets.get(tweets.size()-1).id;
        }
        Log.d(TAG, "popularHomeTimeline: ");
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess!");
                JSONArray jsonArray = json.jsonArray;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                       adapter.clear();
                          adapter.addAll(Tweet.fromJsonArray(jsonArray));
                        swipeContainer.setRefreshing(false);
                    } catch (JSONException e) {
                        Log.e(TAG, "Json Exception", e);
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG,"onFailure!" + response, throwable);


            }
        });
    }
}