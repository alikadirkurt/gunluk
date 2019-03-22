package com.example.kadir.gunlugum;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class User extends ArrayAdapter<String> {
   private final ArrayList<String> selected;
   private final ArrayList<String> userComment;
   private final ArrayList<String> userImage;
   private final ArrayList<String> usermail;
   private final Activity context;



    public User(ArrayList<String> selected,ArrayList<String> userComment, ArrayList<String> userImage,ArrayList<String> usermail, Activity context) {
        super(context,R.layout.customview,selected);
        this.selected = selected;
        this.context = context;
        this.userComment = userComment;
        this.userImage = userImage;
        this.usermail = usermail;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.customview,null,true);
        TextView listdate = customView.findViewById(R.id.userinfo);

        listdate.setText(selected.get(position));
        return customView;
    }
}
