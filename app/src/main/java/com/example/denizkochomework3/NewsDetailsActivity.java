package com.example.denizkochomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NewsDetailsActivity extends AppCompatActivity  {



    ImageView imgDetail;
    TextView titleDetail;
    TextView dateDetail;
    TextView newsDetail;
    NewsItem selected;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_news_details);

            imgDetail = findViewById(R.id.imgdetail);
            dateDetail = findViewById(R.id.datedetail);
            titleDetail = findViewById(R.id.titledetail);
            newsDetail = findViewById(R.id.newsdetail);

            selected = (NewsItem) getIntent().getExtras().getSerializable("news");

            new ImageDownloadTask(imgDetail).execute(selected);
            imgDetail.setImageBitmap(selected.getBitmap());
            dateDetail.setText(new SimpleDateFormat("dd/MM/yyy").format(selected.getNewsDate()));
            titleDetail.setText(selected.getTitle());
            newsDetail.setText(selected.getText());

            getSupportActionBar().setTitle("News Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_18dp);



        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int id=item.getItemId();
            if(id==R.id.comment_activity){

                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this,CommentsActivity.class);
                i.putExtra("news",selected);
                startActivity(i);
            }

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


