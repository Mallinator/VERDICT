package com.example.administrator.verdict;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.verdict.RecyclerViews.question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class addQuestion extends AppCompatActivity {

    EditText ques,opt1,opt2,opt3,opt4;
    Button save;
    FirebaseAuth mauth;
    String q,o1,o2,o3,o4,userid,category;
    long i,number;
    Calendar c;
    //Spinner cat;
   // String [] categoryItemss = {"Technology","Science","Politics","Education"};


    ArrayList<String> list;
    ArrayList<Integer> bitmaps;

    RecyclerView options;
    int day;

    question qu;
    Button d1,d3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        list = new ArrayList<>();
        bitmaps = new ArrayList<Integer>();
        options = findViewById(R.id.options);

        d1 = findViewById(R.id.one_day);
        d1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                d1.setBackground(getDrawable(R.drawable.button_4));
                d3.setBackground(getDrawable(R.drawable.button_2));
                day = 1;
            }
        });
        d3 = findViewById(R.id.three_day);
        d3.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                d3.setBackground(getDrawable(R.drawable.button_4));
                d1.setBackground(getDrawable(R.drawable.button_2));
                day = 3;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        options.setLayoutManager(linearLayoutManager);

        list.add("Technology & Gadgets");
        list.add("Science & Nature");
        list.add("Politics");
        list.add("Education");
        list.add("Current Events");
        list.add("Lifestyle & Health");
        list.add("Arts & Entertainment");
        list.add("Sports & Games");
        list.add("Business & Finance");

        bitmaps.add(R.drawable.ques_tech);
        bitmaps.add(R.drawable.ques_science);
        bitmaps.add(R.drawable.ques_politics);
        bitmaps.add(R.drawable.ques_education);
        bitmaps.add(R.drawable.ques_current);
        bitmaps.add(R.drawable.ques_health);
        bitmaps.add(R.drawable.ques_art);
        bitmaps.add(R.drawable.ques_games);
        bitmaps.add(R.drawable.ques_business);



        qu = new question(bitmaps,list);
        options.setAdapter(qu);






        ques=findViewById(R.id.question);
        opt1=findViewById(R.id.answers1);
        opt2=findViewById(R.id.answers2);
        opt3=findViewById(R.id.answers3);
        opt4=findViewById(R.id.answers4);
        save=findViewById(R.id.create_poll);
        d1 = findViewById(R.id.one_day);
        d3 = findViewById(R.id.three_day);
        //cat=findViewById(R.id.categories);


       /* ArrayAdapter categ = new ArrayAdapter(this, android.R.layout.simple_spinner_item,categoryItemss);
        categ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat.setAdapter(categ);
        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                category = (String) adapterView.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                category = "Not specified";

            }
        });*/




        mauth=FirebaseAuth.getInstance();
        userid=mauth.getCurrentUser().getUid();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countUserQuestions();
                countAllQuestions();
                if(i!=0 && number!=0){
                    createQuestion();

                }
               // else {
                   // countAllQuestions();
                   // countUserQuestions();
                }//
           // }
        });
    }

    public void createQuestion() {

                SharedPreferences sharedPreferences = getSharedPreferences("Category",MODE_PRIVATE);
                category = sharedPreferences.getString("cat","");

                DatabaseReference UsersQuestion = FirebaseDatabase.getInstance().getReference().child("Questions").child(userid).child("User's Question " + i);
                q = ques.getText().toString();
                o1 = opt1.getText().toString();
                o2 = opt2.getText().toString();
                o3 = opt3.getText().toString();
                o4 = opt4.getText().toString();

                c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                c.add(Calendar.DATE,day);
                String end_date = df.format(c.getTime());


                Map options = new HashMap();
                options.put("Question", q);
                options.put("Option 1", o1);
                options.put("Option 2", o2);
                options.put("Option 3", o3);
                options.put("Option 4", o4);
                options.put("Category",category);
                options.put("visible","No");
                options.put("End Date",end_date);
                options.put("Total Votes","0");
                UsersQuestion.setValue(options);
                DatabaseReference DefaultVoteCount = FirebaseDatabase.getInstance().getReference("all Votes").child(q);

                DefaultVoteCount.child("Option 1").setValue("0");
                DefaultVoteCount.child("Option 2").setValue("0");
                DefaultVoteCount.child("Option 3").setValue("0");
                DefaultVoteCount.child("Option 4").setValue("0");


                final DatabaseReference setAllQuestions = FirebaseDatabase.getInstance().getReference().child("all questions").child("Question " + number);
                setAllQuestions.setValue(options);

                ques.setText("");
                opt1.setText("");
                opt2.setText("");
                opt3.setText("");
                opt4.setText("");

                Intent intent = new Intent(addQuestion.this,homeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(addQuestion.this,homeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void countUserQuestions(){
        DatabaseReference userQuestionCount=FirebaseDatabase.getInstance().getReference().child("Questions").child(userid);
        userQuestionCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i=dataSnapshot.getChildrenCount()+1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void countAllQuestions(){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference allQuestionCount=database.getReference("all questions");
        allQuestionCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    number = dataSnapshot.getChildrenCount()+1;
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

