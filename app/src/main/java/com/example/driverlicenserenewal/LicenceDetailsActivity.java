package com.example.driverlicenserenewal;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LicenceDetailsActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String today;
    private MaterialTextView names, birthDate, profileGender, idNumber,
            innerLicenceNumber, categoryOfVehicle, issueDate, expiryDate;
    private ImageView innerProfilePicture;

    @SuppressLint("NewApi")
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


        MaterialButton renewBtn = findViewById(R.id.renewBtn);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        String dateExpiring = (String) expiryDate.getText();
        String dateIssued = (String) issueDate.getText();
        String licence = (String) innerLicenceNumber.getText();

        //to get the current system's date
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        today = day + "/" + (month + 1) + "/" + year;
        Date dateToday = null;
        Date expiringDay = null;
        try {
            dateToday = simpleDateFormat.parse(String.valueOf(today));
            expiringDay = simpleDateFormat.parse(String.valueOf(dateExpiring));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (dateToday.compareTo(expiringDay) > 0) {
            renewBtn.setBackgroundColor(RED);
            renewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Updating the dates of the licence in the database
                    databaseReference.child(licence).child("dateOfIssue").setValue(dateExpiring);
                    issueDate.setText(dateExpiring);

                    Date newExpiryDate = null;

                    try {
                        newExpiryDate = simpleDateFormat.parse(String.valueOf(dateExpiring));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(newExpiryDate);
                    cal.add(Calendar.YEAR, 5);


                    String finalNewExpiryDate = simpleDateFormat.format(cal.getTime());
                    expiryDate.setText(finalNewExpiryDate);

                    String newExpiringDate = (String) expiryDate.getText();
                    databaseReference.child(licence).child("dateOfExpiry").setValue(newExpiringDate);


                    renewBtn.setBackgroundColor(GREEN);
                    renewBtn.setEnabled(false);
                    //Toast.makeText(LicenceDetailsActivity.this, finalNewExpiryDate, Toast.LENGTH_LONG).show();

                }
            });
        } else {
            renewBtn.setBackgroundColor(GREEN);
            renewBtn.setEnabled(false);
        }


    }


    //getting the details from the database
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


        //setting the details to the textViews
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