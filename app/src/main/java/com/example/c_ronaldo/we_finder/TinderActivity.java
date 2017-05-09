package com.example.c_ronaldo.we_finder;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TinderActivity extends AppCompatActivity {
    private final String TITLE = "WeFinder";
    ImageView portraitImageView;
    DatabaseReference firebaseRef;
    String urlStr;
    List<String> urlList = new ArrayList<>();
    int iThClick;
    int iMax;
    ProgressDialog loadUserProgress;
    ProgressDialog loadPhotoProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinder);
        setTitle(TITLE);
        iThClick = 0;
        portraitImageView = (ImageView)this.findViewById(R.id.mPortraitImageView);
        loadUserProgress = new ProgressDialog(this);
        loadPhotoProgress = new ProgressDialog(this);
        loadPortrait();
        loadUserProgress.setMessage("Loading user list, please wait");
        loadUserProgress.show();
    }

    public void loadPortrait(){
        firebaseRef = FirebaseDatabase.getInstance().getReference("users");
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Log.i("jojo","get users are "+postSnapshot.getValue().toString());
                    urlStr = postSnapshot.child("url").getValue().toString();
                    Log.i("jojo","get url i "+urlStr);
                    urlList.add(urlStr);
                    iMax = urlList.size();
                }
                loadUserProgress.dismiss();
                Picasso.with(TinderActivity.this).load(urlList.get(iThClick)).fit().centerCrop().into(portraitImageView);
                iThClick++;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onLikeClicked(View button){
        loadPhotoProgress.setMessage("Loading photos");
        if (iThClick<=iMax-1) {
            String url_i = urlList.get(iThClick);
            Picasso.with(TinderActivity.this).load(url_i).fit().centerCrop().into(portraitImageView);
            loadPhotoProgress.dismiss();
            iThClick++;
        }else{
            Toast.makeText(this, "No more users!",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void onDislikeClicked(View button){

    }

}