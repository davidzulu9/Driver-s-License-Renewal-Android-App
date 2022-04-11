package com.example.driverlicenserenewal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {


    private MaterialTextView userName, licenceNoDisplay, licenceExpiry;
    private ImageView profilePicture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        userName = findViewById(R.id.userName);
        licenceNoDisplay = findViewById(R.id.licenceNoDisplay);
        licenceExpiry = findViewById(R.id.licenceExpiry);
        profilePicture = findViewById(R.id.profilePicture);

        showLicence();

        MaterialCardView licenceDetails = findViewById(R.id.licenceDetails);
        licenceDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String licenceNo = (String) licenceNoDisplay.getText();
                //To see all the licence details
                userDetails(licenceNo);
            }
        });

    }

    private void showLicence() {

        Intent intent = getIntent();
        String showUserName = intent.getStringExtra("name");
        String showDateOfExpiry = intent.getStringExtra("dateOfExpiry");
        String showLicenceNo = intent.getStringExtra("licenceNo");

        userName.setText(showUserName);
        licenceNoDisplay.setText(showLicenceNo);
        licenceExpiry.setText(showDateOfExpiry);
    }

    private void userDetails(String licenceNo) {
        Query licenceDetails = FirebaseDatabase.getInstance().getReference("Users").orderByChild("licenceNo");
        licenceDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dbLicenceNo = snapshot.child(licenceNo).child("licenceNo").getValue(String.class);
                if (dbLicenceNo.equals(licenceNo)) {
                    String names = snapshot.child(licenceNo).child("name").getValue(String.class);
                    String birthDate = snapshot.child(licenceNo).child("dateOfBirth").getValue(String.class);
                    String gender = snapshot.child(licenceNo).child("gender").getValue(String.class);
                    String idNumber = snapshot.child(licenceNo).child("identityNumber").getValue(String.class);
                    String licenceNumber = snapshot.child(licenceNo).child("licenceNo").getValue(String.class);
                    String categoryOfVehicle = snapshot.child(licenceNo).child("vehicleCategory").getValue(String.class);
                    String issueDate = snapshot.child(licenceNo).child("dateOfIssue").getValue(String.class);
                    String expiryDate = snapshot.child(licenceNo).child("dateOfExpiry").getValue(String.class);

                    Intent intent = new Intent(HomeActivity.this, LicenceDetailsActivity.class);
                    intent.putExtra("name", names);
                    intent.putExtra("dateOfBirth", birthDate);
                    intent.putExtra("gender", gender);
                    intent.putExtra("identityNumber", idNumber);
                    intent.putExtra("licenceNo", licenceNumber);
                    intent.putExtra("vehicleCategory", categoryOfVehicle);
                    intent.putExtra("dateOfIssue", issueDate);
                    intent.putExtra("dateOfExpiry", expiryDate);

                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
