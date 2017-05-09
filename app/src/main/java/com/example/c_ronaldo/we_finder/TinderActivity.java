package com.example.c_ronaldo.we_finder;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TinderActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private final String TITLE = "WeFinder";
    ImageView portraitImageView;
    DatabaseReference firebaseRef;
    String urlStr;
    List<String> urlList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    int iThClick;
    int iMax;
    ProgressDialog loadUserProgress;
    ProgressDialog loadPhotoProgress;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String currentEmail;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinder);
        setTitle(TITLE);
        iThClick = 0;
        portraitImageView = (ImageView)this.findViewById(R.id.mPortraitImageView);
        loadUserProgress = new ProgressDialog(this);
        loadPhotoProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    currentEmail = user.getEmail().toLowerCase();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
//        currentEmail = mAuth.getCurrentUser().getEmail();

        loadPortrait();
        loadUserProgress.setMessage("Loading user list, please wait");
        loadUserProgress.show();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.signout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mAuth.signOut();
        finish();
        return true;
    }

    public void loadPortrait(){
        firebaseRef = FirebaseDatabase.getInstance().getReference("users");
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String targetUsername = postSnapshot.child("username").getValue().toString();
                    String targetEmail = postSnapshot.child("email").getValue().toString().toLowerCase();
                    Log.d("loadPortrait", "targetEmail = " + targetEmail);
                    Log.d("loadPortrait", "currentEmail = " + currentEmail);
                    if (targetEmail.equals(currentEmail)) {
                        currentUser = targetUsername;
                        Log.d("loadPortrait", "if: targetUser = " + targetUsername);
                        Log.d("loadPortrait", "if: currentUser = " + currentUser);
                        continue;
                    }
                    nameList.add(targetUsername);

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
        if (iThClick < iMax) {
            String url_i = urlList.get(iThClick);
            Picasso.with(TinderActivity.this).load(url_i).fit().centerCrop().into(portraitImageView);
            loadPhotoProgress.dismiss();
//            iThClick++;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference likeTable = database.getReference("like");
//            String likeKey = likeTable.child(currentUser).push().getKey();
//            likeTable.child(currentUser).child(likeKey).setValue(nameList.get(iThClick));
            String likeUser = nameList.get(iThClick);
            Log.d("onLikeClicked", "currentUser = " + currentUser);
            Log.d("onLikeClicked", "likeUser = " + likeUser);
            likeTable.child(currentUser).child(likeUser).setValue(likeUser);
            iThClick++;
        } else {
            Toast.makeText(this, "No more users!", Toast.LENGTH_LONG).show();
        }
    }

    public void onDislikeClicked(View button){
        if (iThClick < iMax) {
            String url_i = urlList.get(iThClick);
            Picasso.with(TinderActivity.this).load(url_i).fit().centerCrop().into(portraitImageView);
            loadPhotoProgress.dismiss();
            iThClick++;
        } else {
            Toast.makeText(this, "No more users!", Toast.LENGTH_LONG).show();
        }
    }
}