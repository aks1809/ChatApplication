package com.hidden.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    TextView one,two,three,four,five,six,seven,eight,nine,zero,zerozero,multiply,divide,add,subtract,decimal,clear,percentage,inputTextView,resultTextView;
    ImageView back;
    String tmp;

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        zero = findViewById(R.id.zeo);
        zerozero = findViewById(R.id.zerozero);
        multiply = findViewById(R.id.multi);
        divide = findViewById(R.id.divide);
        add = findViewById(R.id.add);
        subtract = findViewById(R.id.subtract);
        decimal = findViewById(R.id.decimal);
        clear = findViewById(R.id.clear);
        percentage = findViewById(R.id.percent);
        back = findViewById(R.id.back);
        inputTextView = findViewById(R.id.inputTextView);
        resultTextView = findViewById(R.id.resultTextView);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("username");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String uname = snapshot.getValue(String.class);
                    intent = new Intent(MainActivity.this,ChatSection.class);
                    intent.putExtra("username",uname);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            intent = new Intent(MainActivity.this,LoginActivity.class);
        }
    }

    public void performAction(View view){
        String query = view.getTag().toString();

        if(query.equalsIgnoreCase("C")){
            inputTextView.setText("");
            resultTextView.setText("");
        }
        else if(query.equalsIgnoreCase("back")){
            if(inputTextView.getText().toString().length()>1) {
                tmp = inputTextView.getText().toString();
                tmp = tmp.substring(0,tmp.length()-1);
                inputTextView.setText(tmp);
                tmp.replace(".", "");
                if (!android.text.TextUtils.isDigitsOnly(tmp)) {
                    calculate();
                }
            }else{
                inputTextView.setText("");
            }
        }
        else if(query.equalsIgnoreCase("0")||query.equalsIgnoreCase("00")){
            tmp = inputTextView.getText().toString();
            if(tmp.isEmpty()){
                inputTextView.setText('0');
            }
            else if(tmp.length()==1 && tmp.equalsIgnoreCase("0")){}
            else{
                inputTextView.setText(inputTextView.getText().toString()+query);
            }
            if(!tmp.equalsIgnoreCase(inputTextView.getText().toString())){
                calculate();
            }
        }
        else if(query.equalsIgnoreCase("=")){
            tmp = inputTextView.getText().toString();
            if(tmp.equalsIgnoreCase("1997")){
                startActivity(intent);
                finish();
            }
            tmp = resultTextView.getText().toString();
            if(tmp.length()>0){
                inputTextView.setText(tmp);
                resultTextView.setText("");
            }
        }
        else if(android.text.TextUtils.isDigitsOnly(query)){
            tmp = inputTextView.getText().toString();
            if(tmp.equalsIgnoreCase("0")&&tmp.length()==1){}
            else if(tmp.length()==0 && (query.equalsIgnoreCase("0")||query.equalsIgnoreCase("00"))){
                inputTextView.setText("0");
            }
            else {
                inputTextView.setText(inputTextView.getText().toString() + query);
            }
            if(!android.text.TextUtils.isDigitsOnly(tmp)){
                calculate();
            }
        }
        else{
            tmp = inputTextView.getText().toString();
            if(tmp.length()>0 && android.text.TextUtils.isDigitsOnly(tmp.substring(tmp.length()-1))){
                inputTextView.setText(tmp + query);
            }
            else if(query.equalsIgnoreCase("-") && tmp.length()==0){
                inputTextView.setText("-");
            }
        }
    }

    public void calculate(){
        tmp = inputTextView.getText().toString();
        Expression exp = new ExpressionBuilder(tmp).build();
        try {
            double result = exp.evaluate();
            if(Math.floor(result)==Math.ceil(result)){
                int i = (int) result;
                resultTextView.setText(Integer.toString(i));
            }
            else{
                resultTextView.setText(Double.toString(result));
            }
        }catch (Exception e){
            resultTextView.setText("");
        }
    }
}