package com.example.myaccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Handler myHandler = new Handler();

    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(ResetPassword.this, Login.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void sentResetPassword(View view) {
        EditText et_email = findViewById(R.id.et_resetMail);
        String mail = et_email.getText().toString().trim();
        String emailRegex = "(\\w)+(.*)@(\\w+)(\\.)*[a-zA-Z]{2,3}";

        if (!mail.matches(emailRegex)) {
            Toast.makeText(this, "Invalid mail", Toast.LENGTH_SHORT).show();
            et_email.setError("Email invalid");
            et_email.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(mail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "Email sent", Toast.LENGTH_SHORT).show();
                            myHandler.postDelayed(timer, 2000);
                        } else {
                            Toast.makeText(ResetPassword.this, "An error has occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void goBack(View view) {
        Intent intent = new Intent(ResetPassword.this, Login.class);
        startActivity(intent);
        finish();
    }
}