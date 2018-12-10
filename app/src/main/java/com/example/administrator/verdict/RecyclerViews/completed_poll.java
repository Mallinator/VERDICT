package com.example.administrator.verdict.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.verdict.R;
import com.example.administrator.verdict.UserQuestions;
import com.example.administrator.verdict.voteSummary;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class completed_poll extends RecyclerView.Adapter<completed_poll.Hold> {

    private ArrayList<String> p;
    String totalV;
    Context context;
    public completed_poll(ArrayList<String> p) {
        this.p = p;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.completed_polls,parent,false);
        return new Hold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Hold holder, int position) {
        final String text = p.get(position);

        holder.txt.setText(text);

        final DatabaseReference getVoteCount = FirebaseDatabase.getInstance().getReference("all Votes").child(text);
        getVoteCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalV = (String) dataSnapshot.child("Total Votes").getValue();
                if(totalV!=null)
                    holder.total.setText("Votes: "+ totalV);
                else
                    holder.total.setText("Votes: 0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.btn.setText("RESULT");
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,voteSummary.class);
                intent.putExtra("Question",text);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return p.size();
    }


    class Hold extends RecyclerView.ViewHolder{

        TextView txt,total;
        Button btn;

        public Hold(View itemView) {
            super(itemView);
            context = itemView.getContext();

            txt=itemView.findViewById(R.id.text);
            btn = itemView.findViewById(R.id.result);
            total = itemView.findViewById(R.id.total);

        }
    }
}

