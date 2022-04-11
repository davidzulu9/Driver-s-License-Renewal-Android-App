package com.example.driverlicenserenewal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    DatabaseReference reference;
    private TextInputLayout emailText, passwordText, licenceField;
    private TextView signUpText;
    private ProgressBar progressBar;
    private FirebaseAuth profileAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Binding the widgets
        emailText = findViewById(R.id.textField);
        passwordText = findViewById(R.id.passwordField);
        signUpText = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progressBar);
        licenceField = findViewById(R.id.licenceField);


        profileAuth = FirebaseAuth.getInstance();


        MaterialButton loginButton = findViewById(R.id.loginButton);

        //Checking if the has entered the details and login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getEditText().getText().toString();
                String password = passwordText.getEditText().getText().toString();
                String licenceNo = licenceField.getEditText().getText().toString();

                if (TextUtils.isEmpty(email)) {
                    emailText.setError("email required");
                    emailText.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailText.setError("Email incorrect");
                    emailText.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    passwordText.setError("Password required");
                    passwordText.requestFocus();

                } else if (TextUtils.isEmpty(licenceNo)) {
                    passwordText.setError("Licence required");
                    passwordText.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    userLogin(email, password, licenceNo);
                }
            }
        });


        //To the register activity, if the user does not have an account
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });


    }

    private void userLogin(String email, String password, String licenceNo) {
        profileAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            //trying to get data from firebase
                            //reference = FirebaseDatabase.getInstance().getReference().child("Users").child(password);
                            Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("licenceNo").equalTo(licenceNo);

                            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    emailText.setError(null);
                                    passwordText.setError(null);

                                    String dbPassword = snapshot.child(licenceNo).child("password").getValue(String.class);
                                    if (dbPassword.equals(password)) {
                                        emailText.setError(null);
                                        passwordText.setError(null);

                                        String userName = snapshot.child(licenceNo).child("name").getValue(String.class);
                                        String expiryDate = snapshot.child(licenceNo).child("dateOfExpiry").getValue(String.class);
                                        String licence = snapshot.child(licenceNo).child("licenceNo").getValue(String.class);

                                        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);

                                        homeIntent.putExtra("name", userName);
                                        homeIntent.putExtra("dateOfExpiry", expiryDate);
                                        homeIntent.putExtra("licenceNo", licence);

                                        startActivity(homeIntent);


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    /*Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                    Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(homeIntent);*/
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
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