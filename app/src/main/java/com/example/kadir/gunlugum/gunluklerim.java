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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.List;

public class gunluklerim extends AppCompatActivity {
    ListView listView;
    User adaptor;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String>  selectedDate;
    ArrayList<String> userfromComment;
     ArrayList<String> userfromImage;
    ArrayList<String> userfrommail;
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
            ıntent.putExtra("info","new");
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

       user = FirebaseAuth.getInstance().getCurrentUser();
       userID = user.getUid();
        myRef = database.getReference();
       adaptor = new User(selectedDate,userfromComment,userfromImage,userfrommail,this);
       listView.setAdapter(adaptor);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent = new Intent(getApplicationContext(),upload_activity.class);
               String value = listView.getItemAtPosition(position).toString();
               System.out.println("Position:" + value);
               startActivity(intent);
           }
       });


       getDataFromFirebase();


    }
    public void getDataFromFirebase(){
        DatabaseReference newRefence = database.getReference("Posts").child(userID);
        newRefence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    System.out.println("secilengun: " + selectedDate);
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
