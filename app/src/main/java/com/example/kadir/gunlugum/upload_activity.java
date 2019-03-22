package com.example.kadir.gunlugum;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class upload_activity extends AppCompatActivity {
    TextView date;
    ImageView postimage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    AutoCompleteTextView commentText;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Uri selectedImage;
    FirebaseUser user;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_activity);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        commentText = findViewById(R.id.commentText);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        date = findViewById(R.id.select_date);
        postimage = findViewById(R.id.taptoimage);
        Date tarih = new Date();
        SimpleDateFormat bugun = new SimpleDateFormat("dd/MM/yyyy");
        date.setText(bugun.format(tarih));

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        upload_activity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,mDateSetListener,year,month,dayOfMonth);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            month = month+1;
            String reeldate = (dayOfMonth + "/" + month +"/" + year);
            date.setText(reeldate);
            }
        };
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

    }
    
    public void topimage(View view){
    if(ContextCompat.checkSelfPermission(upload_activity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
    }else{
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);
    }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==1){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==2 && resultCode == RESULT_OK && data != null){
        selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                postimage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.kaydet,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.tik){
            UUID uuıd = UUID.randomUUID();
            final String imagename = "images/" + uuıd +".jpg";
        StorageReference storageReference = mStorageRef.child(imagename);
        storageReference.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //download url
                StorageReference newRefrance = FirebaseStorage.getInstance().getReference(imagename);
                newRefrance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                    String downloadUrl = uri.toString();
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userEmail = user.getEmail();
                        String userComment = commentText.getText().toString();
                        String selecteddate = date.getText().toString();
                        UUID uuıd1 = UUID.randomUUID();
                        String uuidString = uuıd1.toString();
                        myRef.child("Posts").child(userID).child(uuidString).child("usermail").setValue(userEmail);
                        myRef.child("Posts").child(userID).child(uuidString).child("downloadUrl").setValue(downloadUrl);
                        myRef.child("Posts").child(userID).child(uuidString).child("userComment").setValue(userComment);
                        myRef.child("Posts").child(userID).child(uuidString).child("selecteddate").setValue(selecteddate);
                        Toast.makeText(upload_activity.this, "Günlük Eklendi", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),gunluklerim.class);
                        startActivity(intent);
                    }
                });

            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(upload_activity.this, "Başarısız", Toast.LENGTH_SHORT).show();
            }
        });
        }
        return super.onOptionsItemSelected(item);
    }

}
