package com.example.administrator.verdict.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.verdict.R;
import com.example.administrator.verdict.VoteScreen;
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


public class Latest extends RecyclerView.Adapter<Latest.Hold> {

    private ArrayList<String> q;
    String userid;
    FirebaseAuth firebaseAuth;
    boolean flag;
    Context context;
    String total;
    public Latest(ArrayList<String> q) {
            this.q = q;
        }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycle1,parent,false);
        return new Hold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Hold holder, int position) {
        final String text = q.get(position);
        holder.txt.setText(text);
        holder.btn.setText("VOTE");

        final DatabaseReference getVoteCount = FirebaseDatabase.getInstance().getReference("all Votes").child(text);
        getVoteCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total = (String) dataSnapshot.child("Total Votes").getValue();
                if(total!=null)
                    holder.txt1.setText(" "+ total);
                else
                    holder.txt1.setText(" 0");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference time = FirebaseDatabase.getInstance().getReference("all questions");
        time.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    HashMap h = (HashMap) data.getValue();
                    String ques = (String) h.get("Question");
                    String vis = (String) h.get("visible");
                    if(text.equals(ques) && vis.equals("Yes"))  {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String cur_dNt = sdf.format(calendar.getTime());
                        String end_DNt = (String) h.get("End Date");
                        try{
                            SimpleDateFormat simdaf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date curr = simdaf.parse(cur_dNt);
                            Date end = simdaf.parse(end_DNt);
                            long difference = end.getTime() - curr.getTime();

                            int days = (int) (difference / (1000*60*60*24));
                            int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                            int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                            if(hours>=1 && days<1)
                                holder.txt2.setText(" "+ hours + " hrs");
                            if(hours>=1 && days>=1)
                                holder.txt2.setText(" "+days + " Days\n" + hours + " hrs");
                            else if(hours<1)
                                holder.txt2.setText(" "+ min + " mins");


                        }catch (ParseException p){
                            p.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(text);
                if(flag) {
                    Intent intent = new Intent(context, VoteScreen.class);
                    intent.putExtra("question", text);
                    context.startActivity(intent);
                }
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

    @Override
    public int getItemCount() {
        return q.size();
    }


    class Hold extends RecyclerView.ViewHolder{

            TextView txt,txt1,txt2;
            Button btn;

            public Hold(View itemView) {
                super(itemView);
                context = itemView.getContext();

                txt=itemView.findViewById(R.id.txt);
                btn = itemView.findViewById(R.id.btn);
                txt1 = itemView.findViewById(R.id.txtVoteLat);
                txt2 = itemView.findViewById(R.id.txtTimeLat);

            }
        }
    }
