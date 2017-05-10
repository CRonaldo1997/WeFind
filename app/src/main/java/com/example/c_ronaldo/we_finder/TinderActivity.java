package com.example.c_ronaldo.we_finder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

    private final int currentYear = 2017;

    private ImageView mUserImage;
    private TextView mUserName, mUserAge, mUserZodiac;
    private String urlStr;
    private List<String> urlList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private List<String> ageList = new ArrayList<>();
    private List<String> zodiacList = new ArrayList<>();
    private int iThClick;
    private int iMax;

    private ProgressDialog loadUserProgress;
    private ProgressDialog loadPhotoProgress;

    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference firebaseRef;

    private String currentEmail;
    public static String currentUser;
    String likeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinder);
        setTitle(TITLE);
        Log.i("onnCreate!","onCreateEXE");

        iThClick = 0;
        mUserImage = (ImageView)this.findViewById(R.id.user_image);
        loadUserProgress = new ProgressDialog(this);
        loadPhotoProgress = new ProgressDialog(this);

        mUserName = (TextView) findViewById(R.id.user_name);
        mUserAge = (TextView) findViewById(R.id.user_age);
        mUserZodiac = (TextView) findViewById(R.id.user_zodiac);

        mAuth = FirebaseAuth.getInstance();
        currentEmail = mAuth.getCurrentUser().getEmail();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    currentEmail = user.getEmail().toLowerCase();
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                } else {
//                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                }
//            }
//        };
//        currentEmail = mAuth.getCurrentUser().getEmail();

        loadPortrait();
        loadUserProgress.setMessage("Loading user list, please wait");
        loadUserProgress.show();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.signout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.action_chat:
                Intent goToChat = new Intent(this, UserListActivity.class);
                startActivity(goToChat);
                return true;
            case R.id.action_signout:
                Log.i("menu","SignOut menu clicked!");
                Toast.makeText(getApplication(), "Signed out!", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadPortrait(){

        firebaseRef = FirebaseDatabase.getInstance().getReference("users");
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                nameList.clear();
                urlList.clear();
                ageList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String targetUsername = postSnapshot.child("username").getValue().toString();
                    String targetEmail = postSnapshot.child("email")
                                                     .getValue().toString().toLowerCase();
                    Log.d("loadPortrait", "targetEmail = " + targetEmail);
                    Log.d("loadPortrait", "currentEmail = " + currentEmail);
                    if (targetEmail.equals(currentEmail)) {
                        currentUser = targetUsername;
                        Log.d("loadPortrait", "if: targetUser = " + targetUsername);
                        Log.d("loadPortrait", "if: currentUser = " + currentUser);
//                        mUserName.setText(currentUser);
//                        int age = currentYear - Integer.parseInt(postSnapshot.child("year").getValue().toString());
//                        mUserAge.setText(String.valueOf(age));
                        continue;
                    }
                    // Store the usernames for later use
                    nameList.add(targetUsername);
                    // Store the corresponding image uri for the stored users
                    Log.i("jojo","get users are "+postSnapshot.getValue().toString());
                    urlStr = postSnapshot.child("uri").getValue().toString();
                    Log.i("jojo","get url i "+urlStr);
                    urlList.add(urlStr);
                    iMax = urlList.size();
                    // Calculate and store the age of the corresponding user
                    int age = currentYear
                              - Integer.parseInt(postSnapshot.child("year").getValue().toString());
                    ageList.add(String.valueOf(age));
                    // Store the zodiac sign of the user
                    String zodiacSign = postSnapshot.child("zodiac").getValue().toString();
                    zodiacList.add(zodiacSign);
                }
                loadUserProgress.dismiss();
                Picasso.with(TinderActivity.this).load(urlList.get(iThClick))
                                                 .fit().centerCrop().into(mUserImage);
                mUserName.setText(nameList.get(iThClick));
                mUserAge.setText(ageList.get(iThClick));
                mUserZodiac.setText(zodiacList.get(iThClick));
//                iThClick++;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onLikeClicked(View button){

        loadPhotoProgress.setMessage("Loading photos");
        if (iThClick < iMax) {
//            String url_i = urlList.get(iThClick);
//            Picasso.with(TinderActivity.this).load(url_i).fit().centerCrop().into(mUserImage);
            loadPhotoProgress.dismiss();
//            iThClick++;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference likeTable = database.getReference("LikeTableJD");
//            String likeKey = likeTable.child(currentUser).push().getKey();
//            likeTable.child(currentUser).child(likeKey).setValue(nameList.get(iThClick));
            likeUser = nameList.get(iThClick);
            Log.d("onLikeClicked", "currentUser = " + currentUser);
            Log.d("onLikeClicked", "likeUser = " + likeUser);
            likeTable.child(currentUser).child(likeUser).setValue(likeUser);

            DatabaseReference likedUserRef = database.getReference("LikeTableJD/"+likeUser);
            likedUserRef.child("SetTime").setValue(String.valueOf(System.currentTimeMillis()));
            checkLikedUser();

            //update image
            iThClick++;
            if(iThClick != iMax) {
                String url_i = urlList.get(iThClick);
                Picasso.with(TinderActivity.this).load(url_i).fit().centerCrop().into(mUserImage);
                mUserName.setText(nameList.get(iThClick));
                mUserAge.setText(ageList.get(iThClick));
                mUserZodiac.setText(zodiacList.get(iThClick));
            }

        } else {
            Toast.makeText(this, "No more users!", Toast.LENGTH_LONG).show();
        }
    }

    public void onDislikeClicked(View button){
        if (iThClick < iMax) {

            iThClick++;
            if(iThClick!=iMax) {
                String url_i = urlList.get(iThClick);
                Picasso.with(TinderActivity.this).load(url_i).fit().centerCrop().into(mUserImage);
                mUserName.setText(nameList.get(iThClick));
                mUserAge.setText(ageList.get(iThClick));
                mUserZodiac.setText(zodiacList.get(iThClick));
            }
            loadPhotoProgress.dismiss();
        } else {
            Toast.makeText(this, "No more users!", Toast.LENGTH_LONG).show();
        }
    }

    public void checkLikedUser(){
        FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
        DatabaseReference DBRef = fireDB.getReference("LikeTableJD/"+likeUser);


        ValueEventListener likedUserListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Log.i("logSnapShot",postSnapshot.getValue().toString());
                    if(postSnapshot.getValue().toString().equals(currentUser)){
                        Toast.makeText(getApplication(), "Congratulations! "+likeUser+" also likes you!\nGo to chat!", Toast.LENGTH_LONG).show();
                        FirebaseDatabase fbRef = FirebaseDatabase.getInstance();
                        DatabaseReference matchedTable = fbRef.getReference("MatchedUserList");
                        matchedTable.child(currentUser).child(likeUser).setValue(likeUser);
                        matchedTable.child(likeUser).child(currentUser).setValue(currentUser);
                    }
                    else{
                        //

                    }
                }
            }

            public void onCancelled(DatabaseError firebaseError) {
                Log.i("rew","The read failed: " + firebaseError.toException());
            }

        };
        DBRef.addValueEventListener(likedUserListener);
    }


}