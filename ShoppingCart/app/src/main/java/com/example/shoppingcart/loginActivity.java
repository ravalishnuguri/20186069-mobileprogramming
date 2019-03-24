package com.example.shoppingcart;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.example.shoppingcart.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.shoppingcart.Model.Users;

public class loginActivity extends AppCompatActivity {

    private EditText Inputnumber, Inputpassword;
    private Button login;
    private ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Inputnumber = (EditText) findViewById(R.id.login_phone_number);
        Inputpassword = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login_btn);
        loading = new ProgressDialog(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }
    private void Login() {
        String number = Inputnumber.getText().toString();
        String password = Inputpassword.getText().toString();
        if(TextUtils.isEmpty(number)) {
            Toast.makeText(this, "Please enter your number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else {
            loading.setTitle("Logging into Account");
            loading.setMessage("Please wait...");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            validate(number, password);
        }
    }

    private void validate(final String number, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(number).exists()) {
                    Users users = dataSnapshot.child("Users").child(number).getValue(Users.class);

                    if (users.getPhone().equals(number)) {
                        if (users.getPassword().equals(password)) {
                            Toast.makeText(loginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(loginActivity.this, ProductInfo.class);
                            Prevalent.currentOnlineUsers = users;

                            startActivity(intent);
                        } else {
                            loading.dismiss();
                            Toast.makeText(loginActivity.this, "Password is wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(loginActivity.this, "Account doesn't exist", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
