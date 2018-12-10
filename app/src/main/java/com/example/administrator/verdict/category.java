package com.example.administrator.verdict;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import java.lang.reflect.Array;
import java.util.ArrayList;

public class category extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Button back, b;

    Button politics, news, life, arts, sports, business, science, technology, everything, all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        toolbar = findViewById(R.id.tool123);


        politics = findViewById(R.id.politics);
        news = findViewById(R.id.news);
        life = findViewById(R.id.life);
        arts = findViewById(R.id.art);
        sports = findViewById(R.id.sports);
        business = findViewById(R.id.business);
        science = findViewById(R.id.science);
        technology = findViewById(R.id.technology);
        everything = findViewById(R.id.everything);
        all = findViewById(R.id.all);


        politics.setOnClickListener(this);
        news.setOnClickListener(this);
        life.setOnClickListener(this);
        arts.setOnClickListener(this);
        sports.setOnClickListener(this);
        business.setOnClickListener(this);
        science.setOnClickListener(this);
        technology.setOnClickListener(this);
        everything.setOnClickListener(this);
        all.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()) {
            case R.id.politics:
                i =new Intent(category.this,After_category.class);
                i.putExtra("title","Politics");
                startActivity(i);
                break;
            case R.id.news:
                i =new Intent(category.this,After_category.class);
                i.putExtra("title","Current Events");
                startActivity(i);
                break;
            case R.id.life:
                i =new Intent(category.this,After_category.class);
                i.putExtra("title","Lifestyle & Health");
                startActivity(i);
                break;
            case R.id.art:
                i =new Intent(category.this,After_category.class);
                i.putExtra("title","Arts & Entertainment");
                startActivity(i);
                break;

            case R.id.sports:
                i =new Intent(category.this,After_category.class);
                i.putExtra("title","Sports & Games");
                startActivity(i);
                break;
            case R.id.business:
                i =new Intent(category.this,After_category.class);
                i.putExtra("title","Business & Finance");
                startActivity(i);
                break;
            case R.id.science:
                i =new Intent(category.this,After_category.class);
                i.putExtra("title","Science & Nature");
                startActivity(i);
                break;
            case R.id.technology:
                i =new Intent(category.this,After_category.class);
                i.putExtra("title","Technology & Gadgets");
                startActivity(i);
                break;
            case R.id.everything:
                i =new Intent(category.this,After_category.class);
                i.putExtra("title","Education");
                startActivity(i);
                break;
            case R.id.all:
                i =new Intent(category.this,After_category.class);
                i.putExtra("title","All Categories");
                startActivity(i);
                break;
        }
    }
}