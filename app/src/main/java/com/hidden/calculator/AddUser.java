package com.hidden.calculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddUser extends AppCompatActivity {

    EditText emailAddress;
    TextView errorTextView,errorTextView2;
    String currUser;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels,height = dm.heightPixels;
        int min_dist = Math.min(width,height);
        getWindow().setLayout((int)(min_dist*0.8),(int)(min_dist*0.8));

        emailAddress = findViewById(R.id.emailAddress);
        errorTextView = findViewById(R.id.errorTextView);
        errorTextView2 = findViewById(R.id.errorTextView2);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currUser = firebaseAuth.getCurrentUser().getUid();
    }

    public  void setViews(View view){
        errorTextView.setVisibility(View.GONE);
        errorTextView2.setVisibility(View.GONE);
    }

    public void search(View view){
        final String email = emailAddress.getText().toString();
        if(!email.isEmpty()){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                        if(currUser.equals(singleSnapshot.getKey())) {continue;}
                        if(singleSnapshot.child("email").getValue().toString().equals(email)){
                            // Found info now send back to parent intent
                            errorTextView.setText("");
                            String username = singleSnapshot.child("username").getValue().toString();
                            String key = singleSnapshot.getKey();
                            Intent intent =new Intent();
                            intent.putExtra("username",username);
                            intent.putExtra("key",key);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                    errorTextView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"Error Occurred",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            errorTextView2.setVisibility(View.VISIBLE);
        }
    }

    public void close(View view){
        finish();
    }
}