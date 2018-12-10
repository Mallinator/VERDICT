package com.example.administrator.verdict;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.verdict.RecyclerViews.completed_poll;
import com.example.administrator.verdict.RecyclerViews.my_polls;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Me_polls extends AppCompatActivity {

    private ArrayList<String> p;

    RecyclerView recyclerView;
    my_polls my;
    FirebaseAuth firebaseAuth;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_polls);

        p = new ArrayList<>();



        recyclerView = findViewById(R.id.recycle_mepoll);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference getUserQustions= FirebaseDatabase.getInstance().getReference("Questions").child(userid);
        getUserQustions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        HashMap h = (HashMap) dataSnapshot1.getValue();
                        String ques = (String) h.get("Question");
                        if(!p.contains(ques) && (ques!=null)) {
                            p.add(ques);
                        }
                        my = new my_polls(p);
                        recyclerView.setAdapter(my);
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
