package com.example.turkmen_mehmetcan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AddComment extends AppCompatActivity {
    EditText name;
    EditText message;
    TextView errorMessage;
    Button postCommentButton;
    ProgressBar progressBar;
    int newsId;

    Handler postCommentHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            int value = (int)message.obj;

            progressBar.setVisibility(View.VISIBLE);
            name.setVisibility(View.INVISIBLE);
            postCommentButton.setVisibility(View.INVISIBLE);

            if(value == 1) {
                Intent i = new Intent(AddComment.this, Comments.class);
                i.putExtra("id", newsId);
                startActivity(i);
                finish();
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                name.setVisibility(View.VISIBLE);
                postCommentButton.setVisibility(View.VISIBLE);
                errorMessage.setVisibility(View.VISIBLE);
            }

            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        setTitle("Post Comment");

        newsId = getIntent().getIntExtra("news_id", -1);
        name = findViewById(R.id.txtPersonName);
        message = findViewById(R.id.txtPersonMessage);
        postCommentButton = findViewById(R.id.sendButton);
        progressBar = findViewById(R.id.progressBarList2);
        errorMessage = findViewById(R.id.errorMessage);

        progressBar.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);

        postCommentButton.setOnClickListener(v->{
            String enteredName = name.getText().toString();
            String enteredMessage = message.getText().toString();

            MainRepo repo = new MainRepo();
            repo.postCommentByNewsId(((MainApplication)getApplication()).srv, postCommentHandler, newsId,
                    enteredName, enteredMessage);
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }
}