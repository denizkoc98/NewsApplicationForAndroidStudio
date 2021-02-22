package com.example.denizkochomework3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentsActivity extends AppCompatActivity  {

    ProgressDialog prgDialog;
    RecyclerView commentRecView;
    List<CommentItem> commentItems = new ArrayList<CommentItem>();
    CommentAdapter adp;
    NewsItem selected;

    @Override
    protected void onStart() {
        super.onStart();
        CommentTask tsk= new CommentTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid/" + String.valueOf(selected.getId()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        commentItems= new ArrayList<>();

        commentRecView= findViewById(R.id.rec_comment);
        commentRecView.setHasFixedSize(true);
        commentRecView.setLayoutManager(new LinearLayoutManager(this));
        adp = new CommentAdapter(commentItems, this);

        commentRecView.setAdapter(adp);

        selected=(NewsItem)getIntent().getExtras().getSerializable("news");




        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_18dp);
    }

    class CommentTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            prgDialog= new ProgressDialog(CommentsActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlStr= strings[0];
            StringBuilder buffer= new StringBuilder();

            try {
                URL url=new URL(urlStr);
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();

                BufferedReader reader= new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line="";

                while((line=reader.readLine())!= null) {
                    buffer.append(line);
                }

            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            commentItems.clear();
            Log.i("DEV",s);
            try {
                JSONObject obj= new JSONObject(s);

                if( obj.getInt("serviceMessageCode")==1) {
                    JSONArray arr= obj.getJSONArray("items");

                    for (int i=0; i<arr.length();i++) {
                        JSONObject current= (JSONObject) arr.get(i);



                        CommentItem item= new CommentItem(current.getInt("id"),
                                current.getString("name"),
                                current.getString("text")
                        );
                        commentItems.add(item);
                    }
                }else {
                    //there is a problem with service

                }
                Log.i("DEV Comments Size: ",String.valueOf(commentItems.size()));

                adp.setComments(commentItems);
                adp.notifyDataSetChanged();
                prgDialog.dismiss();
            } catch (JSONException e) {
                Log.e("DEV", e.getMessage());
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forcomment,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.write_activity){

            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,PostCommentActivity.class);
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
