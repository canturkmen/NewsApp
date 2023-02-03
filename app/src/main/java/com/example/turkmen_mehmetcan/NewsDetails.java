package com.example.turkmen_mehmetcan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.OffsetDateTime;

public class NewsDetails extends AppCompatActivity {
    private ImageView imgDetails;
    private TextView txtTitleDetails;
    private TextView txtDateDetails;
    private TextView txtContentDetails;
    private int id;

    Handler idNewsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            News news = (News)msg.obj;
            OffsetDateTime time = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                time = OffsetDateTime.parse(news.getDate());
            }
            String date = news.getDate();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                date = time.getDayOfMonth() + "/" + time.getMonth().getValue() + "/" + time.getYear();
            }
            txtDateDetails.setText(date);
            txtTitleDetails.setText(news.getTitle());
            txtContentDetails.setText(news.getContent());

            MainRepo repo = new MainRepo();
            repo.downloadImage(((MainApplication)getApplication()).srv, imgHandler, news.getImagePath());
            return true;
        }
    });

    Handler imgHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Bitmap img = (Bitmap) msg.obj;
            imgDetails.setImageBitmap(img);
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        String category = getIntent().getStringExtra("categoryName");
        setTitle(category);

        id = getIntent().getIntExtra("id", 1);
        imgDetails = findViewById(R.id.imgDetailedImage);
        txtTitleDetails = findViewById(R.id.txtDetailedTitle);
        txtDateDetails = findViewById(R.id.txtDetailedDate);
        txtContentDetails = findViewById(R.id.txtDetailedContent);

        MainRepo repo = new MainRepo();
        repo.getNewsById(((MainApplication)getApplication()).srv, idNewsHandler, id);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        } else if (item.getItemId() == R.id.mn_addComment) {
            Intent i = new Intent(NewsDetails.this, Comments.class);
            i.putExtra("id", id);
            startActivity(i);
        }
    return true;
    }
}