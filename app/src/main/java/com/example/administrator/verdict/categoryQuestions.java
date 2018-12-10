package com.example.administrator.verdict;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class categoryQuestions extends AppCompatActivity {

    String categoryItem,categoryId;
    ArrayList questionsList = new ArrayList();
    ArrayAdapter adapter;
    ListView categoryList;
    int i=1;
    String userid,ques,s;
    FirebaseAuth firebaseAuth;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_questions);
        categoryList = findViewById(R.id.catquesList);

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,questionsList);
        categoryList.setAdapter(adapter);
        categoryList.setClickable(true);


        categoryId = (String) getIntent().getExtras().get("CategoryName");

        DatabaseReference showCategory= FirebaseDatabase.getInstance().getReference("all questions");
        showCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    HashMap category = (HashMap) child.getValue();
                    assert category != null;
                    categoryItem = (String) category.get("Category");
                    if(categoryId.equals(categoryItem))
                    {
                          String s = (String) category.get("Question");
                          questionsList.add(s);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                ques= (String) adapterView.getItemAtPosition(i);
                check(ques);
                if(flag) {
                    Intent intent = new Intent(categoryQuestions.this, VoteScreen.class);
                    intent.putExtra("question", ques);
                   // intent.putExtra("IntentID",1);
                    startActivity(intent);
                }
                // else
                //   Toast.makeText(categoryQuestions.this, "Already Voted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void check(String string) {

        firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference checkVote = FirebaseDatabase.getInstance().getReference("Questions").child(userid).child("Votes").child(string);
        checkVote.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null)
                    flag=true;
                else {
                    flag = false;
                    //Toast.makeText(showQuestions.this, "Already voted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

