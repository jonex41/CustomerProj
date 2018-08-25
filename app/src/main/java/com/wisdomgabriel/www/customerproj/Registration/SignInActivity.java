package com.wisdomgabriel.www.customerproj.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wisdomgabriel.www.customerproj.MainActivity;
import com.wisdomgabriel.www.customerproj.R;

public class SignInActivity extends AppCompatActivity{



    private EditText username, password;
    private Button signin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        String usernamess = getIntent().getStringExtra("username");
        String passwordss = getIntent().getStringExtra("password");

        username.setText(usernamess);
        password.setText(passwordss);

        findViewById(R.id.movetoregister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        signin = findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(password.getText())
                        && !TextUtils.isEmpty(username.getText())) {

                        signinusers(username.getText().toString(), password.getText().toString());
                }else {
                    Toast.makeText(SignInActivity.this, "Please make sure all the fileds are filled...", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void signinusers(String username, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Registering users please wait...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Unable to sign in, please try again later...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
}
