package com.example.football_tracker;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;

public class AddNewMatch extends AppCompatActivity {

    TextView match_location;
    private static String db_url = "jdbc:mysql://10.0.2.2:3306/football_match_db";
    private static String db_user = "root";
    private static String db_password = "Psgbgt70";
    private static String database_request = "INSERT INTO `football_match_score` (team1, team2, team1_score, team2_score, location) VALUES (?,?,?,?,?);";
    FusedLocationProviderClient locationClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_match);

        TextView team1 = findViewById(R.id.team1);
        TextView team2 = findViewById(R.id.team2);
        TextView score_team1 = findViewById(R.id.score_team1);
        TextView score_team2 = findViewById(R.id.score_team2);
        match_location = findViewById(R.id.match_location);
        Button sendMatch = findViewById(R.id.sendMatch_button);
        Button location = findViewById(R.id.getMyLocation);

        locationClient = LocationServices.getFusedLocationProviderClient(this);
        ;


        sendMatch.setOnClickListener(v -> {
            if (validTexts(team1, team2, score_team1, score_team2, match_location)) {
                try {
                    insertMatchToDatabase(new Match(team1.getText().toString(), team2.getText().toString(), score_team1.getText().toString(), score_team2.getText().toString(), match_location.getText().toString()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(getApplicationContext(), "One match more in the list bruuuv", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        location.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getMatchLocation();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        });

    }

    private void getMatchLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationClient.getLastLocation().addOnCompleteListener(
                    task -> {
                        Location location = task.getResult();
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            System.out.println(latitude);
                            System.out.println(longitude);

                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(this, Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();

                                String fullAddress = address + " " + city + " " + state + " " + country + " " + postalCode;
                                match_location.setText(fullAddress);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            LocationRequest locationRequest = new LocationRequest()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(10000)
                                    .setFastestInterval(1000)
                                    .setNumUpdates(1);

                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void
                                onLocationResult(LocationResult locationResult) {
                                    Location location1 = locationResult.getLastLocation();
                                    double latitude = location1.getLatitude();
                                    double longitude = location1.getLongitude();

                                    Geocoder geocoder;
                                    List<Address> addresses;
                                    geocoder = new Geocoder(AddNewMatch.this, Locale.getDefault());

                                    try {
                                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                        String address = addresses.get(0).getAddressLine(0);
                                        String city = addresses.get(0).getLocality();
                                        String state = addresses.get(0).getAdminArea();
                                        String country = addresses.get(0).getCountryName();
                                        String postalCode = addresses.get(0).getPostalCode();

                                        String fullAddress = address + " " + city + " " + state + " " + country + " " + postalCode;

                                        match_location.setText(fullAddress);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getMatchLocation();
        } else {
            Toast.makeText(this, "You have not the permission to do what you want to do bruuu", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertMatchToDatabase(Match match) throws InterruptedException {
        String requestInsert = "INSERT INTO football_match_score (team1, team2, team1_score, team2_score, location) VALUES (?,?,?,?,?);";
        Thread thread = new Thread(() -> {
            try {
                Connection connection = DriverManager.getConnection(db_url, db_user, db_password);

                PreparedStatement statement = connection.prepareStatement(requestInsert);
                statement.setString(1, match.team1);
                statement.setString(2, match.team2);
                statement.setString(3, match.team1Score);
                statement.setString(4, match.team2Score);
                statement.setString(5, match.location);
                statement.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();
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


