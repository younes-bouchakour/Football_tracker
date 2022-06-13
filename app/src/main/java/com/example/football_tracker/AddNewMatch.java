package com.example.football_tracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Connection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddNewMatch extends AppCompatActivity {

    private static String db_url = "jdbc:mysql://127.0.0.1:3306/football_match_db";
    private static String db_user = "root";
    private static String db_password = "Psgbgt70";
    private static String database_request = "INSERT INTO `football_match_score` (team1, team2, team1_score, team2_score, location) VALUES (?,?,?,?,?);";
    FusedLocationProviderClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_match);

        TextView team1 = findViewById(R.id.team1);
        TextView team2 = findViewById(R.id.team2);
        TextView score_team1 = findViewById(R.id.score_team1);
        TextView score_team2 = findViewById(R.id.score_team2);
        TextView match_location = findViewById(R.id.match_location);
        Button sendMatch = findViewById(R.id.sendMatch_button);
        Button location = findViewById(R.id.getCurrentLocation);

        locationClient =  LocationServices.getFusedLocationProviderClient(this);                          ;


        sendMatch.setOnClickListener(v -> {
            if (validTexts(team1, team2, score_team1, score_team2, match_location))
                databaseConnection(team1.getText().toString(), team2.getText().toString(), score_team1.getText().toString(), score_team2.getText().toString(), match_location.getText().toString());
            Toast.makeText(getApplicationContext(), "One match more in the list bruuuv", Toast.LENGTH_SHORT).show();
        });

        location.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationClient.getLastLocation();
            } else {

            }
        });

    }

    private void databaseConnection(String team1, String team2, String score_team1, String score_team2, String location_match){
    }

    private boolean validTexts(TextView team1, TextView team2, TextView score_team1, TextView score_team2, TextView match_location) {
        boolean valid = true;

        if (team1.getText().toString().trim().equals("")) {
            team1.setError("You must enter the team 1 name");
            valid = false;
        }
        if (team2.getText().toString().trim().equals("")) {
            team2.setError("You must enter the team 2 name");
            valid = false;
        }
        if (score_team1.getText().toString().trim().equals("")) {
            score_team1.setError("You must enter the team 1 score");
            valid = false;
        }
        if (score_team1.getText().toString().trim().equals("")) {
            score_team2.setError("You must enter the team2 score");
            valid = false;
        }
        return valid;
    }
}


