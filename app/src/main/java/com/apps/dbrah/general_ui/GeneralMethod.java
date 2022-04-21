package com.apps.dbrah.general_ui;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;


import com.apps.dbrah.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }

    @BindingAdapter("image")
    public static void image(View view, String imageUrl) {

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                if (view instanceof CircleImageView) {
                    CircleImageView imageView = (CircleImageView) view;
                    if (imageUrl != null) {
                        RequestOptions options = new RequestOptions().override(view.getWidth(), view.getHeight());
                        Glide.with(view.getContext()).asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .load(imageUrl)
                                .centerCrop()
                                .apply(options)
                                .into(imageView);
                    }
                } else if (view instanceof RoundedImageView) {
                    RoundedImageView imageView = (RoundedImageView) view;

                    if (imageUrl != null) {

                        RequestOptions options = new RequestOptions().override(view.getWidth(), view.getHeight());
                        Glide.with(view.getContext()).asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .load(imageUrl)
                                .centerCrop()
                                .apply(options)
                                .into(imageView);

                    }
                } else if (view instanceof ImageView) {
                    ImageView imageView = (ImageView) view;

                    if (imageUrl != null) {

                        RequestOptions options = new RequestOptions().override(view.getWidth(), view.getHeight());
                        Glide.with(view.getContext()).asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .load(imageUrl)
                                .centerCrop()
                                .apply(options)
                                .into(imageView);
                    }
                }

            }
        });


    }

    @BindingAdapter("user_image")
    public static void user_image(View view, String imageUrl) {


        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (imageUrl != null) {

                Glide.with(view.getContext()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.circle_avatar)
                        .load(imageUrl)
                        .centerCrop()
                        .into(imageView);

            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (imageUrl != null) {

                Glide.with(view.getContext()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.circle_avatar)
                        .load(imageUrl)
                        .centerCrop()
                        .into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (imageUrl != null) {

                Glide.with(view.getContext()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.circle_avatar)
                        .load(imageUrl)
                        .centerCrop()
                        .into(imageView);
            }
        }

    }

    @BindingAdapter({"title", "from", "to"})
    public static void displayDate(TextView textView, String title, long from, long to) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        if (title != null) {
            textView.setText(title);
        } else {
            textView.setText(dateFormat.format(new Date(from * 1000)) + " - " + dateFormat.format(new Date(to * 1000)));
        }

    }

    @BindingAdapter("createDate")
    public static void createAtDate(TextView textView, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (date != null) {

            try {
                Date parse = dateFormat.parse(date);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                format.setTimeZone(TimeZone.getDefault());
                String d = format.format(parse);
                textView.setText(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {

        }

    }

    @BindingAdapter("createTime")
    public static void createAtTime(TextView textView, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (date != null) {

            try {
                Date parse = dateFormat.parse(date);

                SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                format.setTimeZone(TimeZone.getDefault());
                String time = format.format(parse);
                textView.setText(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {

        }

    }

    @BindingAdapter("status")
    public static void orderStatus(TextView textView, String status) {
       if (status!=null){
           if (status.equals("new")){
               textView.setText(R.string.new1);
           }else if (status.equals("offered")){
               textView.setText(R.string.new_offer);
           }else if (status.equals("accepted")){
               textView.setText(R.string.offer_accepted);

           }else if (status.equals("rejected")){
               textView.setText(R.string.rejected);

           }else if (status.equals("preparing")){
               textView.setText(R.string.preparing);

           }else if (status.equals("on_way")){
               textView.setText(R.string.on_the_way);

           }else if (status.equals("delivered")){
               textView.setText(R.string.delivered);

           }
       }

    }

    @BindingAdapter("orderProgress")
    public static void orderProgress(ProgressBar bar, String status) {
        if (status!=null){
            int maxProgress = 7;
            bar.setMax(maxProgress);
            int progress = 0;
            if (status.equals("new")){
                progress = 1;
            }else if (status.equals("offered")){
                progress = 2;

            }else if (status.equals("accepted")){
                progress = 3;

            }else if (status.equals("rejected")){
                progress = 4;

            }else if (status.equals("preparing")){
                progress = 5;

            }else if (status.equals("on_way")){
                progress = 6;

            }else if (status.equals("delivered")){
                progress = 7;

            }
            bar.setMax(maxProgress);
            bar.setProgress(progress);
        }

    }

    @BindingAdapter("rate")
    public static void rate(SimpleRatingBar bar,String rate) {
        if (rate!=null){
            bar.setRating(Float.parseFloat(rate));
        }

    }

}













