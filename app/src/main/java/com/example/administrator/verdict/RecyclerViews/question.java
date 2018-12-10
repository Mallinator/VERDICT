package com.example.administrator.verdict.RecyclerViews;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.verdict.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences;

public class question extends RecyclerView.Adapter <question.hold>  {

    ArrayList<Integer> q;
    ArrayList<String> s;
    Context context;

    public question(ArrayList<Integer> q, ArrayList<String> s) {
        this.q = q;
        this.s = s;
    }

    @NonNull
    @Override
    public hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.question,parent,false);
        return new hold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final hold holder, final int position) {
        Integer bitmap = q.get(position);
        final String s1 = s.get(position);
        holder.img.setImageResource(bitmap);
        holder.txt.setText(s1);


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, v.getId()+ " ", Toast.LENGTH_SHORT).show();
                holder.card.setAlpha((float) 0.5);
                SharedPreferences preferences = context.getSharedPreferences("Category",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("cat",s1);
                Toast.makeText(context, s1, Toast.LENGTH_SHORT).show();
                editor.commit();
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return q.size();
    }



    class hold extends RecyclerView.ViewHolder{

        ImageView img;
        TextView txt;
        RelativeLayout relat;
        CardView card;

        public hold(View itemView) {
            super(itemView);
            context = itemView.getContext();
            img=itemView.findViewById(R.id.img);
            txt=itemView.findViewById(R.id.txt);
            card =itemView.findViewById(R.id.card);
            relat = itemView.findViewById(R.id.rela);
        }
    }
}
