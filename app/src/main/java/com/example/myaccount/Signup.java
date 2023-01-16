package com.example.myaccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
    }

    public void signup(View view) {
        EditText et_email = findViewById(R.id.et_emailSignup);
        EditText et_password = findViewById(R.id.et_passwordSignup);
        EditText et_passwordConfirm = findViewById(R.id.et_passwordSignupConfirm);

        String mail = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String passwordConfirm = et_passwordConfirm.getText().toString().trim();
        String emailRegex = "(\\w)+(.*)@(\\w+)(\\.)*[a-zA-Z]{2,3}";

        if (!mail.matches(emailRegex)) {
            Toast.makeText(this, "Invalid mail", Toast.LENGTH_SHORT).show();
            et_email.setError("Email invalid");
            et_email.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password >= 6", Toast.LENGTH_SHORT).show();
            et_password.setError("Invalid password");
            et_password.requestFocus();
            return;
        }

        if(passwordConfirm.length() < 6) {
            Toast.makeText(this, "Password >= 6", Toast.LENGTH_SHORT).show();
            et_passwordConfirm.setError("Invalid password ");
            et_passwordConfirm.requestFocus();
            return;
        }

        if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            et_passwordConfirm.setError("Passwords dont match");
            et_passwordConfirm.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Signup.this, "User created", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            Intent intent = new Intent(Signup.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Signup.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}