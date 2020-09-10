package com.hidden.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class ChatSection extends AppCompatActivity {

    HashSet<String> hashSet;
    ListView listView;
    String currUser;
    ArrayList<UserList> userLists;
    DatabaseReference databaseReference;
    private static UserAdapter userAdapter;
    public String currUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currUserName = getIntent().getStringExtra("username");
        String uName ="Welcome! " + getIntent().getStringExtra("username");
        this.setTitle(uName);
        setContentView(R.layout.activity_chat_section);
        hashSet = new HashSet<>();
        listView = findViewById(R.id.userList);
        userLists = new ArrayList<>();
        currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currUser).child("friends");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userLists.clear();
                hashSet.clear();
                for(DataSnapshot single : snapshot.getChildren()){
                    String key = single.getKey();
                    String name = single.child("username").getValue().toString();
                    userLists.add(new UserList(name,key));
                    hashSet.add(key);
                }
                userAdapter = new UserAdapter(userLists,getApplicationContext());
                listView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserList list = userLists.get(i);
                Intent intent = new Intent(ChatSection.this,PrivateChatSection.class);
                intent.putExtra("username",list.getUser());
                intent.putExtra("currUser",currUser);
                intent.putExtra("key",list.getKey());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addusers,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.addUsers){
            Intent intent = new Intent(ChatSection.this, AddUser.class);
            startActivityForResult(intent,1);
        }else if(item.getItemId()==R.id.logout){
            // logout
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ChatSection.this,LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String username = data.getStringExtra("username");
                String key = data.getStringExtra("key");
                if(!hashSet.contains(key)){
                    databaseReference.child(key).child("username").setValue(username);
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("friends");
                    dr.child(currUser).child("username").setValue(currUserName);
                }
            }
        }
    }
}