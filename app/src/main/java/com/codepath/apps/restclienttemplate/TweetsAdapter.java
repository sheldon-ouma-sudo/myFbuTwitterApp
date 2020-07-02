package com.codepath.apps.restclienttemplate;

import android.content.Context;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
   Context context;
   List<Tweet> tweets;

  //Pass in the context and list of tweets
     public TweetsAdapter(Context context, List<Tweet> tweets){
         this.context =context;
         this.tweets =tweets;


     }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view =  LayoutInflater.from(context).inflate(R.layout.item_tweets, parent, false);
        return new ViewHolder(view);  
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         //Get the data
        Tweet tweet = tweets.get(position);


       // holder.tvBody.setText(tweet.body);
       // holder.tvScreenName.setText(tweet.user.screenName);
       // holder.tvTimeCreate;
       // Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);

        //Bind the tweet with the viewholder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }
    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
     tweets.addAll(list);
     notifyDataSetChanged();

    }


//passing the context and list of  tweets
    //For each row, inflate the layout
    //Bind the values based on the position of the elenent

   public  class  ViewHolder extends RecyclerView.ViewHolder{
       ImageView ivProfileImage;
       TextView tvBody;
       TextView tvScreenName;
       TextView tvTimeCreated;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
           tvBody = itemView.findViewById(R.id.tvBody);
           tvScreenName = itemView.findViewById(R.id.tvScreenName);
           tvTimeCreated = itemView.findViewById(R.id.tvTimeCreated);
       }

       public void bind(Tweet tweet) {
       tvBody.setText(tweet.body);
       tvScreenName.setText(tweet.user.screenName);
       tvTimeCreated.setText(getRelativeTimeAgo(tweet.timeCreated));
       Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);

           }



     }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }





}


