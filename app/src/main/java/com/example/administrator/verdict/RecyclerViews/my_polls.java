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
import android.widget.Toast;

import com.example.administrator.verdict.R;
import com.example.administrator.verdict.homeScreen;
import com.example.administrator.verdict.voteSummary;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class my_polls extends RecyclerView.Adapter<my_polls.Hold> {

    private ArrayList<String> ques;
    String totalV;
    Context context;

    public my_polls(ArrayList<String> ques) {
        this.ques = ques;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_polls,parent,false);
        return new Hold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Hold holder, int position) {
        final String que = ques.get(position);

        holder.txt1.setText(que);


        final DatabaseReference getVoteCount = FirebaseDatabase.getInstance().getReference("all Votes").child(que);
        getVoteCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalV = (String) dataSnapshot.child("Total Votes").getValue();
                if(totalV!=null)
                    holder.txt2.setText (totalV);
                else
                    holder.txt2.setText("0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference stat = FirebaseDatabase.getInstance().getReference().child("all questions");
        stat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        HashMap hashMap = (HashMap) child.getValue();
                        String q = (String) hashMap.get("Question");
                        if(que.equals(q)){
                        String visibility = (String) hashMap.get("visible");
                        if (visibility.equals("Yes")){
                            holder.txt3.setText("Ongoing");}
                        else if (visibility.equals("No"))
                            holder.txt3.setText("Not Approved");
                        else if (visibility.equals("Completed"))
                            holder.txt3.setText("Completed");
                        }
                    }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        holder.btn.setText("VIEW RESULT");
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,voteSummary.class);
                intent.putExtra("Question",que);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ques.size();
    }


    class Hold extends RecyclerView.ViewHolder{

        TextView txt1,txt2,txt3;
        Button btn;

        public Hold(View itemView) {
            super(itemView);
            context = itemView.getContext();

            txt1=itemView.findViewById(R.id.txt1);
            txt2=itemView.findViewById(R.id.txt2);
            txt3=itemView.findViewById(R.id.txt3);
            btn = itemView.findViewById(R.id.result);

        }
    }
}
