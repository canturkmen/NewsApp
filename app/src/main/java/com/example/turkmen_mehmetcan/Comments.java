package com.example.turkmen_mehmetcan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class Comments extends AppCompatActivity {

    int news_id;
    RecyclerView recView;
    ProgressBar progressBar;

    Handler commentHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            List<Comment> commentArray = (List<Comment>)message.obj;
            CommentsAdapter commentsAdapter = new CommentsAdapter(Comments.this, commentArray);
            recView.setAdapter(commentsAdapter);
            recView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        setTitle("Comments");

        news_id = getIntent().getIntExtra("id", -1);
        recView = findViewById(R.id.comment_rec_view);
        recView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBarList3);

        recView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        MainRepo repo = new MainRepo();
        repo.getCommentsByNewsId(((MainApplication)getApplication()).srv, commentHandler, news_id);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        } else if (item.getItemId() == R.id.mn_addComment2) {
            Intent i = new Intent(Comments.this, AddComment.class);
            i.putExtra("news_id", news_id);
            startActivity(i);
            finish();
            return true;
        }
        return true;
    }
}