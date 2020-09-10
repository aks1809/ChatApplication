package com.hidden.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText email,password,username;
    Button signUpButton;
    TextView signInRoute;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.emailEdidtext);
        password = findViewById(R.id.passwordEdittext);
        signUpButton = findViewById(R.id.signUpButton);
        signInRoute = findViewById(R.id.signInRoute);
        username = findViewById(R.id.username);
        mAuth = FirebaseAuth.getInstance();
    }

    public void signIn(View view){
        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void signUp(View view){
        if(email.getText().toString().isEmpty()||password.getText().toString().isEmpty()||username.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid().toString());
                                databaseReference.child("email").setValue(email.getText().toString());
                                databaseReference.child("username").setValue(username.getText().toString());
                                // re-route
                                Intent intent = new Intent(SignUpActivity.this, ChatSection.class);
                                intent.putExtra("username", username.getText().toString());
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}