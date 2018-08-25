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
import com.wisdomgabriel.www.customerproj.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, password1, password2;
    private Button register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.movetosignin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });
        username = findViewById(R.id.username);
        password1  = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(password1.getText()) && !TextUtils.isEmpty(password2.getText())
                        && !TextUtils.isEmpty(username.getText())) {
                    if (password1.getText().toString().equals(password2.getText().toString())) {
                        registeruser(username.getText().toString(), password1.getText().toString());

                    } else {
                        Toast.makeText(RegisterActivity.this, "Please check your password...", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(RegisterActivity.this, "Please make sure all the fileds are filled...", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void registeruser(final String username, final String password) {

        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Registering users please wait...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("password", password);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed."+task.getException(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }


                });



    }
}
