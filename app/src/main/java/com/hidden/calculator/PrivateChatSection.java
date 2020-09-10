package com.hidden.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hidden.calculator.R.id.customToolbar;

public class PrivateChatSection extends AppCompatActivity {

    String key,username,currUser;
    DatabaseReference firebaseDatabase;
    EditText messageEditText;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat_section);
        Toolbar toolbar = findViewById(customToolbar);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mChat = new ArrayList<>();

        messageEditText = findViewById(R.id.messageEditText);
        key = getIntent().getStringExtra("key");
        currUser = getIntent().getStringExtra("currUser");
        username = getIntent().getStringExtra("username");
        setSupportActionBar(toolbar);
        TextView name = findViewById(R.id.name);
        name.setText(username);
        String destination;
        if(key.compareTo(currUser)<0) destination = key + currUser;
        else destination = currUser + key;
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("chats").child(destination);
        firebaseDatabase.keepSynced(true);

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    mChat.add(chat);
                }

                messageAdapter = new MessageAdapter(PrivateChatSection.this,mChat,currUser);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void back(View view){
        finish();
    }

    public void send(View view){
        String text = messageEditText.getText().toString();
        if(!text.isEmpty()) {
            DatabaseReference mData = firebaseDatabase.push();
            mData.child("message").setValue(text);
            mData.child("sender").setValue(currUser);
            mData.child("time").setValue(ServerValue.TIMESTAMP);
            messageEditText.setText("");
        }
    }

}