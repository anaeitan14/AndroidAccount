package com.example.myaccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Account extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
//      String userEmail = mAuth.getCurrentUser().getEmail();
        String userEmail = getIntent().getExtras().getString("EMAIL");

        TextView tvEmailTitle = findViewById(R.id.tvEmailTitle);
        tvEmailTitle.setText(userEmail);
    }


    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(Account.this, Login.class);
        startActivity(intent);
        finish();
    }

    public void deleteUser(View view) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete your account?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.getInstance().getCurrentUser().delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(Account.this, Login.class);
                                            startActivity(intent);
                                            Toast.makeText(Account.this, "Account deleted!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(Account.this, "An error has occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void writeTask(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tasks");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        EditText et_task = findViewById(R.id.etNewTask);
        String task = et_task.getText().toString().trim();

        myRef.child("uid" + uid).setValue(task).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            et_task.setText("");
                        }
                    }
                });
    }
}