package com.example.administrator.verdict;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.verdict.RecyclerViews.card;
import com.example.administrator.verdict.RecyclerViews.completed_poll;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Completed extends AppCompatActivity {

    RecyclerView recyclerView;
    completed_poll c;

    ArrayList<String> p;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

        p = new ArrayList<>();

        final DatabaseReference compQues = FirebaseDatabase.getInstance().getReference().child("all questions");
        compQues.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    //arrayList.clear();
                    HashMap hashMap = (HashMap) snap.getValue();
                    assert hashMap != null;
                    String visibility = (String) hashMap.get("visible");
                    String s =(String) hashMap.get("Question");
                    if(!p.contains(s) && visibility.equals("Completed")) {
                        p.add(s);
                    }
                    c= new completed_poll(p);
                    //c.notifyDataSetChanged();
                    recyclerView.setAdapter(c);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.recycle_completed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        //c = new completed_poll(p);
        //recyclerView.setAdapter(c);

    }
}
