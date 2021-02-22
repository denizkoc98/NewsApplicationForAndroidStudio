package com.example.denizkochomework3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class PostCommentActivity extends AppCompatActivity  {
    Button btnSubmit;
    EditText name, message;
    ProgressDialog prgDialog;
    NewsItem selected;
    AlertDialog alert;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.post_comment);
        name = (EditText) findViewById(R.id.post_name);
        message = (EditText) findViewById(R.id.post_message);
        btnSubmit = (Button) findViewById(R.id.button);
        selected = (NewsItem) getIntent().getExtras().getSerializable("news");

        getSupportActionBar().setTitle("Post Comment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_18dp);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (name.getText().toString().length() != 0 && message.getText().toString().length() != 0) {

                    PostCommentTask tsk = new PostCommentTask();
                    tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/savecomment", name.getText().toString(), message.getText().toString());
                } else {

                    FragmentManager fm= getSupportFragmentManager();
                    ForCommentDialog dialog=  new ForCommentDialog();
                    dialog.show(fm, "");
                }
            }


        });

    }



    // public void taskCallClicked(View v){

        //   if(name.getText().toString().length()!= 0 && message.getText().toString().length()!= 0){

        //     PostCommentTask tsk= new PostCommentTask();
        //   tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/savecomment",name.getText().toString(), message.getText().toString());
        //}
        //else{

        //  alert.setTitle("Please fill all the fields");
        //finish();

        //}
        //}


        class PostCommentTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                prgDialog = new ProgressDialog(PostCommentActivity.this);
                prgDialog.setTitle("Loading");
                prgDialog.setMessage("Please wait...");
                prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                prgDialog.show();
            }

            @Override
            protected String doInBackground(String... strings) {

                StringBuilder strBuilder = new StringBuilder();
                String urlStr = strings[0];
                String name = strings[1];
                String message = strings[2];
                JSONObject obj = new JSONObject();
                try {
                    obj.put("name", name);
                    obj.put("text", message);
                    obj.put("news_id", String.valueOf(selected.getId()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.connect();

                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(obj.toString());


                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line = "";

                        while ((line = reader.readLine()) != null) {
                            strBuilder.append(line);
                        }

                    }


                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return strBuilder.toString();
            }

            @Override
            protected void onPostExecute(String s) {

                try {
                    JSONObject inputObj = new JSONObject(s);
                    prgDialog.dismiss();
                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // getMenuInflater().inflate(R.menu.forcomment,menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected (@NonNull MenuItem item){


            switch (item.getItemId()) {
                case android.R.id.home:
                    this.finish();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

