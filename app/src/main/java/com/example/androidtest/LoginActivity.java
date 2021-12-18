package com.example.androidtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText txtID;
    private EditText txtPass;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidtest1-8b861-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtID = findViewById(R.id.txtLogin_id);
        txtPass = findViewById(R.id.txtLogin_pass);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnRegister).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        System.out.print(v.getId());
        switch (v.getId()){
            case R.id.btnLogin:
                Login();
                break;
            case R.id.btnRegister:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
        }
    }
    public  void Login(){
        String ID = txtID.getText().toString();
        String Pass = txtPass.getText().toString();

        if(ID.isEmpty() || Pass.isEmpty()){
            Toast.makeText(LoginActivity.this, "please fill all feild", Toast.LENGTH_LONG).show();
        }else {
            databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(ID) || snapshot.hasChild(Pass)){
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }else {
                        Toast.makeText(LoginActivity.this, "Login fail", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}