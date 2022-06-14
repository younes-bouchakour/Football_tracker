package com.example.football_tracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MatchInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);

                ActionBar actionBar = getSupportActionBar();
                assert actionBar != null;
                actionBar.setDisplayHomeAsUpEnabled(true);

                TextView team1 = findViewById(R.id.team_name1);
                TextView team2 = findViewById(R.id.team_name2);
                TextView team1Score = findViewById(R.id.team1_score);
                TextView team2Score = findViewById(R.id.team2_score);
                TextView location = findViewById(R.id.location_address);

                Intent intent = getIntent();

                team1.setText(intent.getStringExtra("team1_name"));
                team2.setText(intent.getStringExtra("team2_name"));
                team1Score.setText(intent.getStringExtra("team1_score"));
                team2Score.setText(intent.getStringExtra("team2_score"));
                location.setText(intent.getStringExtra("match_location"));
    }
}