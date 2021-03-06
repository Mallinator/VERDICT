package com.example.administrator.verdict;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class NewUserInfo extends AppCompatActivity {

    Button sb;
    FirebaseAuth mauth;
    EditText eName,eAge;
    String username;
    Button d1,d3;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_info);
        Toast.makeText(this, "New User", Toast.LENGTH_LONG).show();
        sb=findViewById(R.id.savebtn);
        eName=findViewById(R.id.userEdt);
        eAge=findViewById(R.id.ageEdt);

        d1 = findViewById(R.id.male);
        d1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                d1.setBackground(getDrawable(R.drawable.button_4));
                d3.setBackground(getDrawable(R.drawable.button_2));
                day = 1;
            }
        });
        d3 = findViewById(R.id.female);
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

        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInformation();
            }
        });
    }

    private void saveInformation() {
        final String name = eName.getText().toString();
        String age = eAge.getText().toString();
        if (name.isEmpty() || age.isEmpty()) {
            Toast.makeText(NewUserInfo.this, "Incomplete Details", Toast.LENGTH_LONG).show();
            return;
        }

        mauth=FirebaseAuth.getInstance();
        String userid=mauth.getCurrentUser().getUid();
        DatabaseReference current_userDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        Map newData = new HashMap();
        newData.put("Name", name);
        newData.put("Age",age);

        current_userDB.setValue(newData);

        Intent i= new Intent(this,homeScreen.class);
        startActivity(i);

    }
}
