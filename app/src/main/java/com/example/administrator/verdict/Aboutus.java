package com.example.administrator.verdict;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Aboutus extends AppCompatActivity implements View.OnClickListener {

    ImageView ul,ug,rl,rg,kl,kg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        ul = findViewById(R.id.utkarsh_profile);
        ug = findViewById(R.id.utkarsh_profile_g);
        rl = findViewById(R.id.rahul_profile);
        rg =  findViewById(R.id.rahul_profile_g);
        kl = findViewById(R.id.karma_profile);
        kg = findViewById(R.id.karma_profile_g);
        ul.setOnClickListener(this);
        ug.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId())
        {
            case R.id.utkarsh_profile:
                i = new Intent(Aboutus.this,Web.class);
                i.putExtra("title","https://www.linkedin.com/in/utkarshpaliwal/");
                startActivity(i);
                break;
            case R.id.rahul_profile:
                i = new Intent(Aboutus.this,Web.class);
                i.putExtra("title","https://www.linkedin.com/in/utkarshpaliwal/");
                startActivity(i);
                break;
            case R.id.utkarsh_profile_g:
                i = new Intent(Aboutus.this,Web.class);
                i.putExtra("title","http://github.com/Mallinator");
                startActivity(i);
                break;
        }
    }
}
