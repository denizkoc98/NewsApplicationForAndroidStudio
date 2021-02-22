package com.example.denizkochomework3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements NewsAdapter.NewsItemClickListener
        //implements NewsAdapter.NewsItemClickListener

{
    ProgressDialog prgDialog;
    RecyclerView newsRecView;
    List<NewsItem> data;
    NewsAdapter adp;
    Spinner spinner;
    String [] categories = {"All","Economics","Politics","Sports"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new ArrayList<>();
        newsRecView = findViewById(R.id.newsrec);
        adp = new NewsAdapter(data, this, new NewsAdapter.NewsItemClickListener() {
            @Override
            public void newItemClicked(NewsItem selectedNewsItem) {
                Toast.makeText(MainActivity.this,selectedNewsItem.getTitle(),Toast.LENGTH_SHORT).show();
            }
        } );
        newsRecView.setLayoutManager(new LinearLayoutManager(this));

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NewsTask tsk = new NewsTask();
                switch(position){
                    case 0:
                        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");
                        break;
                    case 1:
                        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/4");
                        break;
                    case 2:
                        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/6");
                        break;
                    case 3:
                        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/5");
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adp=new NewsAdapter(data, this,  this);
        newsRecView.setAdapter(adp);
    }

    @Override
    public void newItemClicked(NewsItem selectedNewsItem){
        Toast.makeText(MainActivity.this,selectedNewsItem.getTitle(),Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,NewsDetailsActivity.class);
        i.putExtra("news",selectedNewsItem);
        startActivity(i);
    }


    class NewsTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            prgDialog= new ProgressDialog(MainActivity.this);
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
            data.clear();
            Log.i("DEV",s);
            try {
                JSONObject obj= new JSONObject(s);

               if( obj.getInt("serviceMessageCode")==1) {
                   JSONArray arr= obj.getJSONArray("items");

                  for (int i=0; i<arr.length();i++) {
                    JSONObject current= (JSONObject) arr.get(i);

                    long date= current.getLong("date");
                    Date objDate= new Date(date);

                    NewsItem item= new NewsItem(current.getInt("id"),
                            current.getString("title"),
                            current.getString("text"),
                            current.getString("image"),
                            objDate
                            );
                        data.add(item);

                  }

               }else {
                    //there is a problem with service

               }
               Log.i("DEV",String.valueOf(data.size()));
                adp.notifyDataSetChanged();
                prgDialog.dismiss();
               } catch (JSONException e) {
                 Log.e("DEV", e.getMessage());
            }

        }
    }


}

