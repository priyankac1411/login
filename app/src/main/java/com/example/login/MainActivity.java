package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
   private EditText Name;
   private EditText Password;
   private TextView Info;
   private Button Login;
   private int counter=5;
   private TextView userRegistration;
   private FirebaseAuth firebaseAuth;
   private ProgressDialog progressDialog;
   private TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Name=(EditText)findViewById(R.id.etName);
        Password=(EditText)findViewById(R.id.etPassword);
        Info=(TextView)findViewById(R.id.tvInfo);
        Login=(Button)findViewById(R.id.btLogin);
        userRegistration=(TextView)findViewById(R.id.tvRegister);
        forgotPassword=(TextView)findViewById(R.id.tvForgotPassword);
        Info.setText("No of attempts remaining:5");
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(),Password.getText().toString());
            }
        });
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,PasswordActivity.class));
            }
        });
    }
    private void validate(String userName,String userPassword)
    {
        progressDialog.setMessage("you can use my channel");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    checkEmailVerification();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                     counter--;
                     Info.setText("No of attempts remaining:" +counter);
                    progressDialog.dismiss();
                     if(counter==0)
                     {
                         Login.setEnabled(false);
                     }
                }
            }
        });
    }
    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag=firebaseUser.isEmailVerified();
        startActivity(new Intent(MainActivity.this,SecondActivity.class));
       /* if(emailflag){
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }
        else
        {
            Toast.makeText(this,"verify ur email",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }*/
    }
}
