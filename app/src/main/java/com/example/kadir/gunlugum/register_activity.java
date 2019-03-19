package com.example.kadir.gunlugum;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.UserProfileChangeRequest;

public class register_activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText nameText,emailText,passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);

        mAuth = FirebaseAuth.getInstance();
        nameText = findViewById(R.id.register_activity_name);
        emailText = findViewById(R.id.register_activity_email);
        passwordText = findViewById(R.id.register_activity_password);

    }
    public void register(View view ) {
        if(nameText.getText().toString().matches("") || emailText.getText().toString().matches("") || passwordText.getText().toString().matches("")){
            Toast.makeText(this, "Gerekli alanları doldurunuz", Toast.LENGTH_SHORT).show();
        }else{
        mAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(register_activity.this, "Kayıt Basarılı", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Sign_up.class);
                                startActivity(intent);
                            }

                        }
                    })
                .addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(register_activity.this, "Kayıt Başarısız", Toast.LENGTH_SHORT).show();
                }
            });}

        }
}



//  if (nameText.getText().toString() == null && emailText.getText().toString() == null && passwordText.getText().toString() == null) {