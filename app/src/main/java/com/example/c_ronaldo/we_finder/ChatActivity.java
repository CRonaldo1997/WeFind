package com.example.c_ronaldo.we_finder;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    EditText message;
    String messageContent;
    ListView messageList;
    List<String> mscontentList = new ArrayList<>();
    ArrayAdapter<String> chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("Chat with "+ UserListActivity.userToChat );
        message = (EditText)findViewById(R.id.messageText);
        messageList = (ListView)findViewById(R.id.messageListView);

        chatAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mscontentList);
        messageList.setAdapter(chatAdapter);

//        FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
//        DatabaseReference DBRef = fireDB.getReference("chatchat/"+currentUser+"/"+userToChat);
        loadMessage();
    }

    public void loadMessage(){
        /*
        FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
        DatabaseReference DBRef = fireDB.getReference("Chat/"+TinderActivity.currentUser+"/"+UserListActivity.userToChat);
        ValueEventListener chatSessionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mscontentList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if(postSnapshot.getValue() != null){
//                        mscontentList.add(postSnapshot.getValue().toString());
                        ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                        String messageUser = chatMessage.getMessageUser();
                        String messageText = chatMessage.getMessageText();
                        Log.d("loadMessage", "text: " + messageText);
                        long messageTime = chatMessage.getMessageTime();
                        String formatTime =
                                android.text.format.DateFormat
                                       .format("MM-dd-yyyy (HH:mm:ss)", messageTime).toString();
                        String fullMessage = messageUser + " - " + formatTime + ":\n" + messageText;
                        mscontentList.add(fullMessage);
                    }
                }
                Collections.reverse(mscontentList);
                chatAdapter.notifyDataSetChanged();
            }

            public void onCancelled(DatabaseError firebaseError) {
                Log.i("rew","The read failed: " + firebaseError.toException());
            }

        };
        DBRef.addValueEventListener(chatSessionListener);
        */
        // Root -> user A: the user
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                                                       .child("Chat").child(TinderActivity.currentUser);
        // Node -> user B: friend
        DatabaseReference currentChat = databaseRef.child(UserListActivity.userToChat);
        currentChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    mscontentList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                        String messageUser = chatMessage.getMessageUser();
                        String messageText = chatMessage.getMessageText();
                        Log.d("loadMessage", "text: " + messageText);
                        long messageTime = chatMessage.getMessageTime();
                        String formatTime =
                                android.text.format.DateFormat
                                       .format("MM/dd/yyyy (HH:mm:ss)", messageTime).toString();
                        String fullMessage = messageUser + " - " + formatTime + ":\n" + messageText;
                        mscontentList.add(fullMessage);
                    }
                }
                Collections.reverse(mscontentList);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendMessageClicked(View button){
//        messageContent = TinderActivity.currentUser+":  " + message.getText().toString();

        FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
        DatabaseReference DBRef = fireDB.getReference("Chat");

//        DBRef.child(UserListActivity.userToChat).child(TinderActivity.currentUser).child(DBRef.push().getKey()).setValue(messageContent);
//        DBRef.child(TinderActivity.currentUser).child(UserListActivity.userToChat).child(DBRef.push().getKey()).setValue(messageContent);
        DBRef.child(UserListActivity.userToChat).child(TinderActivity.currentUser)
                .child(DBRef.push().getKey())
                .setValue(new ChatMessage(message.getText().toString(), TinderActivity.currentUser));
        DBRef.child(TinderActivity.currentUser).child(UserListActivity.userToChat)
                .child(DBRef.push().getKey())
                .setValue(new ChatMessage(message.getText().toString(), TinderActivity.currentUser));
        message.setText("");
    }

}
