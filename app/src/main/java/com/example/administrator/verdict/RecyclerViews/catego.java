package com.example.administrator.verdict.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;

public class catego extends RecyclerView.Adapter<catego.Hold> {

    private ArrayList<String> ques;
    String totalV;
    FirebaseAuth firebaseAuth;
    String userid,total;
    boolean flag;
    Context context;

    public catego(ArrayList<String> ques) {
        this.ques = ques;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.catego,parent,false);
        return new Hold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Hold holder, int position) {
        final String que = ques.get(position);
        if(que.equals(null))
            holder.txt1.setText("No Questions in this category");
        else {
            holder.txt1.setText(que);
            holder.btn.setText("VOTE");

            final DatabaseReference getVoteCount = FirebaseDatabase.getInstance().getReference("all Votes").child(que);
            getVoteCount.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    total = (String) dataSnapshot.child("Total Votes").getValue();
                    if (total != null)
                        holder.txt2.setText(" " + total);
                    else
                        holder.txt2.setText(" 0");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check(que);
                    if (flag) {
                        Intent intent = new Intent(context, VoteScreen.class);
                        intent.putExtra("question", que);
                        context.startActivity(intent);
                    }
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return ques.size();
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


    class Hold extends RecyclerView.ViewHolder{

        TextView txt1,txt2;
        Button btn;

        public Hold(View itemView) {
            super(itemView);
            context = itemView.getContext();
            txt1=itemView.findViewById(R.id.txt1);
            txt2=itemView.findViewById(R.id.txt2);
            btn = itemView.findViewById(R.id.view);

        }
    }
}
