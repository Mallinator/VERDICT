package com.example.administrator.verdict;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.verdict.RecyclerViews.catego;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class After_category extends AppCompatActivity {


    RecyclerView recyclerView;
    TextView tit;
    catego cat;
    String categry;
    catego catgo;

    ArrayList<String> p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_category);

        p = new ArrayList<>();
        categry = getIntent().getExtras().getString("title");
        recyclerView = findViewById(R.id.recycle_after);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        tit = findViewById(R.id.title);
        tit.setText(getIntent().getExtras().getString("title"));

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("all questions");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap hashMap = (HashMap) snapshot.getValue();
                    assert hashMap != null;
                    String cat = (String) hashMap.get("Category");
                    String vis = (String) hashMap.get("visible");
                    if (categry.equals(cat) && vis.equals("Yes")) {
                        String s = (String) hashMap.get("Question");
                        if (!p.contains(s)) {
                            p.add(s);
                        }
                        catgo = new catego(p);
                        recyclerView.setAdapter(catgo);

                    }
                    else if (("All Categories").equals(categry) && vis.equals("Yes")){
                        String s = (String) hashMap.get("Question");
                        if (!p.contains(s)) {
                            p.add(s);
                        }
                        catgo = new catego(p);
                        recyclerView.setAdapter(catgo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
