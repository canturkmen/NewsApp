package com.example.turkmen_mehmetcan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.NewsHolder> {

    Context context;
    List<News> newsArrayList;

    public MyAdapter(Context context, List<News> newsArrayList) {
        this.context = context;
        this.newsArrayList = newsArrayList;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.news_row_layout, parent, false);
        NewsHolder holder = new NewsHolder(root);
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        OffsetDateTime time = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            time = OffsetDateTime.parse(newsArrayList.get(holder.getAdapterPosition()).getDate());
        }
        String date = newsArrayList.get(holder.getAdapterPosition()).getDate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = time.getDayOfMonth() + "/" + time.getMonth().getValue() + "/" + time.getYear();
        }
        holder.txtDate.setText(date);
        holder.txtContent.setText(newsArrayList.get(holder.getAdapterPosition()).getTitle());
        MainApplication app = (MainApplication)((AppCompatActivity)context).getApplication();
        holder.downloadImage(app.srv, newsArrayList.get(holder.getAdapterPosition()).getImagePath());

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, NewsDetails.class);
                i.putExtra("id", newsArrayList.get(holder.getAdapterPosition()).getId());
                i.putExtra("categoryName", newsArrayList.get(holder.getAdapterPosition()).getCategory());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    class NewsHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtContent;
        ImageView imgList;
        ConstraintLayout row;
        boolean imageDownloaded;

        Handler imgHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bitmap img = (Bitmap)msg.obj;
                imgList.setImageBitmap(img);
                imageDownloaded = true;
                return true;
            }
        });

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.dateText);
            txtContent = itemView.findViewById(R.id.contentText);
            imgList = itemView.findViewById(R.id.imageNews);
            row = itemView.findViewById(R.id.row_list);
        }

        public void downloadImage(ExecutorService srv, String path) {
            if(!imageDownloaded) {
                MainRepo repo = new MainRepo();
                repo.downloadImage(srv, imgHandler, path);
            }
        }
    }
}
