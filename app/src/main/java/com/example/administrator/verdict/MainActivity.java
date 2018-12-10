package com.example.administrator.verdict;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    RelativeLayout r1,r2,r3;
    Button b1,b2;
    EditText e1;
    ImageView img1;

    Animation animation;

    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private String verificationId;
    TextView t;
    Handler handler;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img1 = (ImageView)findViewById(R.id.im1);

        animation = AnimationUtils.loadAnimation(this,R.anim.animations);

        img1.startAnimation(animation);


        r1=(RelativeLayout)findViewById(R.id.relativeLay);
        r2=(RelativeLayout)findViewById(R.id.relay2);
        r3=findViewById(R.id.relay3);
        handler = new Handler();
        handler.postDelayed(runnable,4000);
        e1=(EditText)findViewById(R.id.phnno);
        progressBar=(ProgressBar)findViewById(R.id.prog);
        b1=(Button)findViewById(R.id.blogin);
        t=(TextView)findViewById(R.id.mnum);
        img1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminSignIn.class);
                startActivity(intent);
                return true;
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        if(isOnline()){
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num= e1.getText().toString();
                if(num.isEmpty()||num.length()!=10){
                    e1.setError("Valid number is Required");
                    e1.requestFocus();
                    return;
                }
                String phn= "+" + "91" + num;
                sendVerificationCode(phn);
            }
        });
        }
        else{
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setTitle("No internet Connection");
            builder.setMessage("This application requires internet");
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

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        boolean value= Objects.requireNonNull(task.getResult()).getAdditionalUserInfo().isNewUser();
                        if(value){
                            Intent intent = new Intent(MainActivity.this,NewUserInfo.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        else {
                            Intent intent = new Intent(MainActivity.this, homeScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                    else
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
    }

    private void sendVerificationCode(String number)
    {
        t.setText("Verifying OTP");
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,30, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String otp=phoneAuthCredential.getSmsCode();
            if(otp!=null)
            {
                e1.setText(otp);
                verifyCode(otp);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent intent=new Intent(MainActivity.this,homeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
}
