package com.example.driverlicenserenewal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    ProgressBar progressBar;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private TextInputLayout passwordInput, fullNames, emailInput, birthDate,
            identityNo, licenceNumber, issueDate, expiryDate, vehicleCat;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DatePickerDialog datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");

        fullNames = findViewById(R.id.fullName);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.passwordInput);
        birthDate = findViewById(R.id.dateOfBirth);
        identityNo = findViewById(R.id.identityNumber);
        licenceNumber = findViewById(R.id.licenceNumber);
        issueDate = findViewById(R.id.dateOfIssue);
        expiryDate = findViewById(R.id.dateOfExpiry);
        vehicleCat = findViewById(R.id.vehicleCategory);
        progressBar = findViewById(R.id.progressBar);

        Button registerButton = findViewById(R.id.registerButton);


        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();


        birthDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthDate.getEditText().setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        issueDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        issueDate.getEditText().setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        expiryDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        expiryDate.getEditText().setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);

                //connecting to the database
                rootNode = FirebaseDatabase.getInstance();


                //Getting the user input data
                String name = fullNames.getEditText().getText().toString();
                String email = emailInput.getEditText().getText().toString();
                String password = passwordInput.getEditText().getText().toString();
                String gender = null;
                String dateOfBirth = birthDate.getEditText().getText().toString();
                String dateOfIssue = issueDate.getEditText().getText().toString();
                String dateOfExpiry = expiryDate.getEditText().getText().toString();
                String vehicleCategory = vehicleCat.getEditText().getText().toString();
                String identityNumber = identityNo.getEditText().getText().toString();
                String licenceNo = licenceNumber.getEditText().getText().toString();

                if (TextUtils.isEmpty(name)) {
                    fullNames.setError("Full Names Required");
                    fullNames.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    passwordInput.setError("Password required");
                    passwordInput.requestFocus();
                } else if (password.length() < 8) {
                    passwordInput.setError("Password should be 8 characters of more");
                    passwordInput.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    emailInput.setError("Email required");
                    emailInput.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Email incorrect");
                    emailInput.requestFocus();
                } else if (TextUtils.isEmpty(dateOfBirth)) {
                    birthDate.setError("Date of birth required");
                    birthDate.requestFocus();
                } else if (TextUtils.isEmpty(dateOfIssue)) {
                    issueDate.setError("Date of issue required");
                    issueDate.requestFocus();
                } else if (TextUtils.isEmpty(dateOfExpiry)) {
                    expiryDate.setError("Date of expiry required");
                    expiryDate.requestFocus();
                } else if (TextUtils.isEmpty(vehicleCategory)) {
                    vehicleCat.setError("Date of issue required");
                    vehicleCat.requestFocus();
                } else if (TextUtils.isEmpty(identityNumber)) {
                    identityNo.setError("Date of issue required");
                    identityNo.requestFocus();
                } else if (TextUtils.isEmpty(licenceNo)) {
                    licenceNumber.setError("Date of issue required");
                    licenceNumber.requestFocus();
                } else if (radioGroup.getCheckedRadioButtonId() == -1) {
                    radioButton.setError("Date of issue required");
                    radioButton.requestFocus();
                } else {
                    gender = radioButton.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(name, email, password, gender, dateOfBirth, dateOfIssue, dateOfExpiry,
                            vehicleCategory, identityNumber, licenceNo);
                }
            }
        });

    }

    private void registerUser(String name, String email, String password, String gender, String dateOfBirth, String dateOfIssue,
                              String dateOfExpiry, String vehicleCategory, String identityNumber, String licenceNo) {
        //Authenticating user

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(licenceNo).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            DriverInfo driverInfo = new DriverInfo(name, email, gender, password, dateOfBirth, dateOfIssue,
                                    dateOfExpiry, vehicleCategory, identityNumber, licenceNo);

                            reference = rootNode.getReference("Users");

                            reference.child(licenceNo).setValue(driverInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {
                                        //Sending verification email
//                                      firebaseUser.sendEmailVerification();

                                        //Opening the user's profile
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG)
                                                .show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Registration not successful. Please try again", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                            progressBar.setVisibility(View.GONE);
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                passwordInput.setError("Password should be 8 characters of more");
                                passwordInput.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                passwordInput.setError("Your email or password is incorrect");
                                passwordInput.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                emailInput.setError("Email already in use");
                                emailInput.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


}