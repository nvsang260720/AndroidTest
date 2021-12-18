package com.example.androidtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends Activity implements View.OnClickListener{

    private EditText txtDate;
    private EditText txtID;
    private EditText txtPass;
    private EditText txtCheckPass;
    private EditText txtEmail;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidtest1-8b861-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtDate = findViewById(R.id.txtDate);
        txtID = findViewById(R.id.txtID);
        txtPass = findViewById(R.id.txtPass);
        txtCheckPass = findViewById(R.id.txtCheckPass);
        txtEmail = findViewById(R.id.txtEmail);

        findViewById(R.id.btnRegister).setOnClickListener(this);
        findViewById(R.id.btnRegister_Back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnRegister:
                Register();
                break;
            case R.id.btnRegister_Back:
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }
    public void Register(){

        //get text
        String id = txtID.getText().toString();
        String pass = txtPass.getText().toString();
        String checkpass = txtCheckPass.getText().toString();
        String date = txtDate.getText().toString();
        String email = txtEmail.getText().toString();

        //check null
        if (pass.isEmpty() || checkpass.isEmpty() || date.isEmpty()){
            Toast.makeText(RegisterActivity.this, "please fill all feild", Toast.LENGTH_LONG).show();
        }else if (!pass.equals(checkpass)){
            Toast.makeText(RegisterActivity.this, "Fail password", Toast.LENGTH_LONG).show();
        }else {
            databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(id)){
                        Toast.makeText(RegisterActivity.this, "ID is already", Toast.LENGTH_LONG).show();
                    }else {
                        databaseReference.child("user").child(id).child("id").setValue(id);
                        databaseReference.child("user").child(id).child("password").setValue(pass);
                        databaseReference.child("user").child(id).child("date").setValue(date);
                        databaseReference.child("user").child(id).child("email").setValue(email);

                        Toast.makeText(RegisterActivity.this, "Create account success", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }

}