package com.example.kadir.gunlugum;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_up extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText mailText;
    EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        mailText = findViewById(R.id.sign_in_email_editText);
        passwordText = findViewById(R.id.sign_in_password_editText);
       FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(getApplicationContext(),gunluklerim.class);
            startActivity(intent);
        }

    }
    public void registerol(View view){
        Intent intent = new Intent(getApplicationContext(),register_activity.class);
        startActivity(intent);
    }
    public void loginol(View view){
        if(mailText.getText().toString().matches("") || passwordText.getText().toString().matches("")){
            Toast.makeText(this, "Bilgiler eksik veya Hatalı", Toast.LENGTH_SHORT).show();
        }else{
        mAuth.signInWithEmailAndPassword(mailText.getText().toString(),passwordText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(Sign_up.this, "Başarılı", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),gunluklerim.class);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Sign_up.this, "Mail veya şifre hatalı", Toast.LENGTH_SHORT).show();
            }
        });}
    }
    @Override
    public void onBackPressed() {

        backButtonHandler();

    }
    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Sign_up.this);
        alertDialog.setTitle("Günlükten çıkıyor musun?");
        alertDialog.setMessage("Cıkmak istedigine emin misin?");
        alertDialog.setPositiveButton("EVET",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        System.exit(0);finish();
                    }
                });
        alertDialog.setNegativeButton("HAYIR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
}

