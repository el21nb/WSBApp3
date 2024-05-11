package com.example.wsbapp3.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wsbapp3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

//https://github.com/Tesla-gamer/login-signup-app/blob/master/app/src/main/java/com/example/appproject/LoginActivity.java

/**
 * LoginActivity
 * Launcher page- allows login with authenticated Driver account, then loads MainActivityy
 */
public class LoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;


    /**
     * onCreate: load layout, find views, set Listeners
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //inflate layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //find views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.logIn);
        firebaseAuth = FirebaseAuth.getInstance();

        //login logic

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                //check valid details provided
                if (TextUtils.isEmpty(email)) {
                    etUsername.setError("No email provided.");
                    etUsername.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    etPassword.setError("No password provided.");
                    etPassword.requestFocus();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                checkUserRole();
                            } else {
                                Toast.makeText(LoginActivity.this, "Failed to login: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
    }

    /**Check that account is a Drivers account, and if so, launch MainActivity*/

    private void checkUserRole() {
        firebaseAuth.getCurrentUser().getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult result) {
                boolean isDriver = (boolean) result.getClaims().get("driver");
                if (isDriver) {
                    //If Driver, launch MainActivity
                    Toast.makeText(LoginActivity.this, "Driver signed in", Toast.LENGTH_SHORT);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    //If not a Driver, show warning
                    Toast.makeText(LoginActivity.this, "Not a driver account!", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}