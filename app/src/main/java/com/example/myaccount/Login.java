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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(Login.this, Account.class);
            intent.putExtra("EMAIL", currentUser.getEmail());
            startActivity(intent);
            finish();
        }
    }

    public void toMain(View view) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(View view) {
        EditText et_email = findViewById(R.id.et_email);
        EditText et_password = findViewById(R.id.et_password);
        String mail = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
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

        //validation ok

        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(Login.this, Account.class);
                            intent.putExtra("EMAIL", mail);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Auth failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void moveToReset(View view) {
        Intent intent = new Intent(Login.this, ResetPassword.class);
        startActivity(intent);
        finish();
    }
}