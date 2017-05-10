package com.example.c_ronaldo.we_finder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    List<String> userList = new ArrayList<>();
    ArrayAdapter<String> userListAdapter;
    ListView userListView;
    public static String userToChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle("Matched Users");
        userListView = (ListView)findViewById(R.id.chatListView);
        userListAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,userList);
        userListView.setAdapter(userListAdapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue =  (String)userListView.getItemAtPosition(position);
                userToChat = itemValue;
                Log.i("rew",userToChat+"clicked");
//
                //write userToChat to firebase

                Intent goToChat = new Intent(getApplication(),ChatActivity.class);
                startActivity(goToChat);
            }
        });

        LoadLikedUsersInFirebase();
    }



    public void LoadLikedUsersInFirebase(){
        ValueEventListener likedUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                Log.i("rew", "There are " + snapshot.getChildrenCount() + " newUser");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
//                    userList.add(postSnapshot.child("username").getValue().toString());
                    userList.add(postSnapshot.getValue().toString());
                }
                //get current user then go to get chat history!
//                loadHistoryUsers();
//                userList.remove(TinderActivity.currentUser);
                userListAdapter.notifyDataSetChanged();
            }
            public void onCancelled(DatabaseError firebaseError) {
                Log.i("rew","The read failed: " + firebaseError.toException());
            }
        };
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newUser = database.getReference("MatchedUserList/"+TinderActivity.currentUser);
        newUser.addValueEventListener(likedUserListener);
    }
}
