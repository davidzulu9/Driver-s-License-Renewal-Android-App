package com.example.driverlicenserenewal;

import static android.graphics.Color.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LicenceDetailsActivity extends AppCompatActivity {

    private MaterialTextView names, birthDate, profileGender, idNumber,
            innerLicenceNumber, categoryOfVehicle, issueDate, expiryDate;
    private ImageView innerProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence_details);

        names = findViewById(R.id.names);
        birthDate = findViewById(R.id.birthDate);
        profileGender = findViewById(R.id.profileGender);
        idNumber = findViewById(R.id.idNumber);
        innerLicenceNumber = findViewById(R.id.innerLicenceNumber);
        categoryOfVehicle = findViewById(R.id.categoryOfVehicle);
        issueDate = findViewById(R.id.issueDate);
        expiryDate = findViewById(R.id.expiryDate);

        showDetails();


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.dd.yyyy");
        String currentDate = simpleDateFormat.format(new Date());

        String dateExpiring = (String) expiryDate.getText();

        MaterialButton renewBtn = findViewById(R.id.renewBtn);
        renewBtn.setBackgroundColor(RED);
        /*renewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    private void showDetails() {
        Intent intent = getIntent();
        String showNames = intent.getStringExtra("name");
        String showDateOfBirth = intent.getStringExtra("dateOfBirth");
        String showGender = intent.getStringExtra("gender");
        String showIdentityNumber = intent.getStringExtra("identityNumber");
        String showLicenceNo = intent.getStringExtra("licenceNo");
        String showVehicleCategory = intent.getStringExtra("vehicleCategory");
        String showDateOfIssue = intent.getStringExtra("dateOfIssue");
        String showDateOfExpiry = intent.getStringExtra("dateOfExpiry");


        names.setText(showNames);
        birthDate.setText(showDateOfBirth);
        profileGender.setText(showGender);
        idNumber.setText(showIdentityNumber);
        innerLicenceNumber.setText(showLicenceNo);
        categoryOfVehicle.setText(showVehicleCategory);
        issueDate.setText(showDateOfIssue);
        expiryDate.setText(showDateOfExpiry);
    }
}