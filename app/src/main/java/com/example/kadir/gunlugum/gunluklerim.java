package com.example.kadir.gunlugum;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class gunluklerim extends AppCompatActivity {
    ListView listView;
    User adaptor;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String>  selectedDate;
    FirebaseUser user;
    String userID;
    Activity context;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_day,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_day){
            Intent ıntent = new Intent(getApplicationContext(),upload_activity.class);
            startActivity(ıntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gunluklerim);
       listView = findViewById(R.id.listView);
       selectedDate = new ArrayList<String>();
       database = FirebaseDatabase.getInstance();
       myRef = database.getReference();
       user = FirebaseAuth.getInstance().getCurrentUser();
       userID = user.getUid();
       adaptor = new User(selectedDate,this);
       listView.setAdapter(adaptor);


       getDataFromFirebase();


    }
    public void getDataFromFirebase(){
        DatabaseReference newRefence = database.getReference("Posts");
        newRefence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot ds : dataSnapshot.getChildren()){
                       HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                       selectedDate.add(hashMap.get("selecteddate"));
                       adaptor.notifyDataSetChanged();


                   }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
