package com.example.administrator.verdict;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.verdict.RecyclerViews.Latest;
import com.example.administrator.verdict.RecyclerViews.Lifestyle;
import com.example.administrator.verdict.RecyclerViews.News;
import com.example.administrator.verdict.RecyclerViews.Politics;
import com.example.administrator.verdict.RecyclerViews.Technology;
import com.example.administrator.verdict.RecyclerViews.card;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.RotatingPlane;
import com.google.android.gms.common.internal.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class homeScreen extends AppCompatActivity {

    //TextView tname;

    FirebaseAuth mauth;
    String name, tm;
    Toolbar t;
    RecyclerView recyclerView,recycle1,recycle2,recycle3,recycle4,recycle5;
    ArrayList<String> arrayList= new ArrayList<>();
    Button postButton,voteButton,userCreatedQuestions;
    card c;
    int total=0;

    SpinKitView spin;
    TextView trendingtext,latesttext,techtext,politicstext,newstxt,lifetxt,txtint;
    ImageView tendingimg,latestimg,techimg,politicsimg,newsimg,lifeimg;

    Technology t1;
    Latest l,l1;
    Politics p;
    News n;
    Lifestyle life;

    FloatingActionMenu men;
    RelativeLayout rel;

    FloatingActionButton butt,ques,completed,my_poll;

    ArrayList<String> latest11 = new ArrayList<>();
    ArrayList<String> tech = null;
    ArrayList<String> politics = new ArrayList<>();
    ArrayList<String> news = new ArrayList<>();
    ArrayList<String> others = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        t = findViewById(R.id.tool);

        butt = findViewById(R.id.create);
        ques = findViewById(R.id.ques);
        completed = findViewById(R.id.completed);
        my_poll = findViewById(R.id.mypoll);
        txtint = findViewById(R.id.txtIntro);
        if(isOnline()){
            checkTime();



            butt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(homeScreen.this,addQuestion.class);
                    startActivity(i);
                }
            });

            ques.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(homeScreen.this,category.class);
                    startActivity(i);
                }
            });

            completed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i =new Intent(homeScreen.this,Completed.class);
                    startActivity(i);
                }
            });

            my_poll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i =new Intent(homeScreen.this,Me_polls.class);
                    startActivity(i);
                }
            });

            men = findViewById(R.id.floating);
            rel=findViewById(R.id.rel);



            men.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
                @Override
                public void onMenuToggle(boolean opened) {
                    if (opened)
                    {
                        rel.setAlpha((float) 0.4);
                    }
                    else{
                        rel.setAlpha(1);
                    }
                }
            });


            Initializer();
            SetText();
            Visible();

            //Sprite sprite = new Wave();
            Sprite sprite = new ChasingDots();
            //Sprite sprite = new RotatingPlane();

            spin.setIndeterminateDrawable(sprite);

            setSupportActionBar(t);
            tm = getGreetings();

            Recycle();

            mauth = FirebaseAuth.getInstance();
            String userid = mauth.getCurrentUser().getUid();
            final DatabaseReference current_userDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
            current_userDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name = dataSnapshot.child("Name").getValue().toString();
                    String mess = getGreetings();
                    txtint.setText(mess +" "+ name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(homeScreen.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

            displayQuestions();
        }
        else{
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection and open the application again");
            builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

            }
    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        return true;
    }

    public void Visible()
    {
        trendingtext.setVisibility(View.GONE);
        tendingimg.setVisibility(View.GONE);
        latesttext.setVisibility(View.GONE);
        latestimg.setVisibility(View.GONE);
        techtext.setVisibility(View.GONE);
        techimg.setVisibility(View.GONE);
        politicstext.setVisibility(View.GONE);
        politicsimg.setVisibility(View.GONE);
        newstxt.setVisibility(View.GONE);
        newsimg.setVisibility(View.GONE);
        spin.setVisibility(View.VISIBLE);
        lifetxt.setVisibility(View.GONE);
        lifeimg.setVisibility(View.GONE);
    }


    public void SetText()
    {
        politicstext.setText("Politics & Human Rights");
        techtext.setText("Technology & Gadgets");
        newstxt.setText("News & Current Events");
        lifetxt.setText("Lifestyle & Health");
    }

    public void Initializer()
    {
        spin=findViewById(R.id.spin);
        tendingimg=findViewById(R.id.trendingimage);
        trendingtext=findViewById(R.id.trendingtext);
        latestimg=findViewById(R.id.latestimag);
        latesttext=findViewById(R.id.latesttext);
        techimg=findViewById(R.id.techimg);
        techtext=findViewById(R.id.techtext);
        politicstext=findViewById(R.id.politicstext);
        politicsimg=findViewById(R.id.politicsimg);
        newsimg=findViewById(R.id.newsimg);
        newstxt=findViewById(R.id.newstext);
        lifeimg=findViewById(R.id.lifeimg);
        lifetxt=findViewById(R.id.lifetext);

    }


    public void Recycle()
    {
        recyclerView=findViewById(R.id.recycle);
        recycle1=findViewById(R.id.recycle1);
        recycle2=findViewById(R.id.recycle2);
        recycle3=findViewById(R.id.recycle3);
        recycle4=findViewById(R.id.recycle4);
        recycle5=findViewById(R.id.recycle5);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recycle1.setLayoutManager(linearLayoutManager1);
        recycle2.setLayoutManager(linearLayoutManager2);
        recycle3.setLayoutManager(linearLayoutManager3);
        recycle4.setLayoutManager(linearLayoutManager4);
        recycle5.setLayoutManager(linearLayoutManager5);

    }

    public void Visible1()
    {
        spin.setVisibility(View.GONE);
        tendingimg.setVisibility(View.VISIBLE);
        trendingtext.setVisibility(View.VISIBLE);
        latestimg.setVisibility(View.VISIBLE);
        latesttext.setVisibility(View.VISIBLE);
        techimg.setVisibility(View.VISIBLE);
        techtext.setVisibility(View.VISIBLE);
        politicsimg.setVisibility(View.VISIBLE);
        politicstext.setVisibility(View.VISIBLE);
        newsimg.setVisibility(View.VISIBLE);
        newstxt.setVisibility(View.VISIBLE);
        lifetxt.setVisibility(View.VISIBLE);
        lifeimg.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i= item.getItemId();
        switch (i){
            case R.id.itm1:
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(homeScreen.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.itm3:
                Intent i1  =new Intent(homeScreen.this,Aboutus.class);
                startActivity(i1);
                break;

        }
        return true;


    }

    public void checkTime(){
        final DatabaseReference time = FirebaseDatabase.getInstance().getReference("all questions");
        time.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    String quesid = child.getKey();
                    Log.d("keyyyyy",quesid);
                    HashMap m = (HashMap) child.getValue();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String cur_dNt = sdf.format(calendar.getTime());
                    String end_DNt = (String) m.get("End Date");
                    try{
                        SimpleDateFormat simdaf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date curr = simdaf.parse(cur_dNt);
                        Date end = simdaf.parse(end_DNt);



                        if(curr.after(end)){

                            final DatabaseReference updateVis = FirebaseDatabase.getInstance().getReference("all questions").child(quesid).child("visible");
                            updateVis.setValue("Completed");

                        }
                    }catch (ParseException p){
                        p.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void displayQuestions()
    {
        final DatabaseReference crd = FirebaseDatabase.getInstance().getReference().child("all questions");
        crd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                arrayList.clear();
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    //arrayList.clear();
                    HashMap hashMap = (HashMap) snap.getValue();
                    assert hashMap != null;
                    String visibility = (String) hashMap.get("visible");
                    String s = (String) hashMap.get("Question");
                    String vote = (String) hashMap.get("Total Votes");
                    if(vote!=null)
                    total = Integer.parseInt(vote);
                    Log.d("votaa", String.valueOf(total));
                    if(!arrayList.contains(s) && visibility.equals("Yes") && total>100) {
                        arrayList.add(s);
                        total = 0;
                    }
                    Log.d("lolofunc",arrayList.toString());
                    c= new card(arrayList);
                    //c.notifyDataSetChanged();
                    recyclerView.setAdapter(c);
                    //arrayList.clear();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(homeScreen.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("all questions");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               Visible1();

                tech =  new ArrayList<>();

                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    HashMap hashMap =(HashMap) snapshot.getValue();
                    assert hashMap!=null;
                    String cat =(String ) hashMap.get("Category");
                    String vis = (String) hashMap.get("visible");
                    System.out.println(cat);


                    if (("Technology & Gadgets").equals(cat) && vis.equals("Yes"))
                    {
                        String s =(String) hashMap.get("Question");
                        if(!tech.contains(s)) {
                            tech.add(s);
                            latest11.add(s);
                        }
                    }
                    else if (("Politics").equals(cat) && vis.equals("Yes") )
                    {
                        String s =(String) hashMap.get("Question");
                        if(!politics.contains(s)) {
                            politics.add(s);
                            latest11.add(s);
                        }
                    }
                    else if (("Education").equals(cat) && vis.equals("Yes") )
                    {
                        String s =(String) hashMap.get("Question");
                        if(!news.contains(s)) {
                            latest11.add(s);
                            news.add(s);
                        }

                    }
                    else if(("Lifestyle & Health").equals(cat) && vis.equals("Yes"))
                    {
                            String s = (String) hashMap.get("Question");
                            if(!others.contains(s)) {
                                latest11.add(s);
                                others.add(s);
                            }
                    }
                }

                l = new Latest(latest11);
                p = new Politics(politics);
                t1 = new Technology(tech);
                n = new News(news);
                life = new Lifestyle(others);


                recycle1.setAdapter(l);
                recycle2.setAdapter(t1);
                recycle3.setAdapter(p);
                recycle4.setAdapter(n);
                recycle5.setAdapter(life);
                //tech.clear();
                //news.clear();
                //latest11.clear();
                //others.clear();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public String getGreetings(){
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String greet= "";
        if(timeOfDay >= 0 && timeOfDay < 12)
            greet = "Good Morning";
        else if(timeOfDay < 16)
            greet = "Good Afternoon";
        else if (timeOfDay < 24)
             greet = "Good Evening";

        return greet;
    }
}
