package com.example.driverlicenserenewal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout emailText, passwordText;
    private TextView signUpText;

    private ProgressBar progressBar;
    private FirebaseAuth profileAuth;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Binding the widgets
        emailText = findViewById(R.id.textField);
        passwordText = findViewById(R.id.passwordField);
        signUpText = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progressBar);


        profileAuth = FirebaseAuth.getInstance();



        MaterialButton loginButton = findViewById(R.id.loginButton);

        //adding functionality
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailText.getEditText().getText().toString();
                String password = passwordText.getEditText().getText().toString();

                if (TextUtils.isEmpty(email)) {
                    emailText.setError("email required");
                    emailText.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailText.setError("Email incorrect");
                    emailText.requestFocus();
                }else if (TextUtils.isEmpty(password)) {
                    passwordText.setError("Password required");
                    passwordText.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    userLogin(email, password);
                }
                /*Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);*/
            }
        });



        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });


    }

    private void userLogin(String email, String password) {
        profileAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                }else{
                    try {
                        throw task.getException();
                    }  catch (FirebaseAuthInvalidCredentialsException e) {
                        passwordText.setError("Your email or password is incorrect");
                        passwordText.requestFocus();
                    } catch (FirebaseAuthInvalidUserException e) {
                        emailText.setError("User does not exist");
                        emailText.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}