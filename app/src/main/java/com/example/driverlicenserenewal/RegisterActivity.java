package com.example.driverlicenserenewal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout firstName, lastName, emailInput, dob, dateIssued,expiryDate,
            vehicleCat, idNumber, licenceNumber;
    private Button registerButton;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        emailInput = findViewById(R.id.email);
        dob = findViewById(R.id.dateOfBirth);
        dateIssued = findViewById(R.id.dateIssued);
        expiryDate = findViewById(R.id.expiryDate);
        vehicleCat = findViewById(R.id.vehicleCategory);
        idNumber = findViewById(R.id.identityNumber);
        licenceNumber = findViewById(R.id.licenceNumber);
        registerButton = findViewById(R.id.registerButton);

        radioGroup = findViewById(R.id.radioGroup);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //connecting to the database
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("User");

                //Getting the user input data
                String fname = firstName.getEditText().getText().toString();
                String lname = lastName.getEditText().getText().toString();
                String email = emailInput.getEditText().getText().toString();
                String gender = radioButton.getText().toString();
                String dateOfBirth = dob.getEditText().getText().toString();
                String dateOfIssue = dateIssued.getEditText().getText().toString();
                String dateOfExpiry = expiryDate.getEditText().getText().toString();
                String  vehicleCategory= vehicleCat.getEditText().getText().toString();
                String identity = idNumber.getEditText().getText().toString();
                String licence = licenceNumber.getEditText().getText().toString();

                DriverInfo driverInfo = new DriverInfo(fname, lname, email, gender, dateOfBirth, dateOfIssue,
                        dateOfExpiry, vehicleCategory, identity, licence);

                reference.child(licence).setValue(driverInfo);
            }
        });

    }

    public void checkButton(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);
    }
}