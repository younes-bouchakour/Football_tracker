package com.example.football_tracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String db_url = "jdbc:mysql://10.0.2.2:3306/football_match_db";
    private static String db_user = "root";
    private static String db_password = "Psgbgt70";

    List<Match> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Match list");
        try {
            connectionDB();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        init();
        System.out.println(items);
    }

    public void connectionDB() throws InterruptedException {
        String requestSelect = "SELECT * FROM football_match_score;";
        Thread thread = new Thread(() -> {
            try {
                Connection connection = DriverManager.getConnection(db_url, db_user, db_password);

                Statement statement = connection.prepareStatement(requestSelect);
                ResultSet resultSet= statement.executeQuery(requestSelect);

                while (resultSet.next())
                   items.add(new Match(resultSet.getInt("ID"), resultSet.getString("team1"), resultSet.getString("team2"), resultSet.getString("team1_score"), resultSet.getString("team2_score"), resultSet.getString("location")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();
    }

    public void init() {
        ListAdapter listAdapter = new ListAdapter(items, this);
        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.take_picture) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.add_new_match) {
            Intent intent = new Intent(this, AddNewMatch.class);
            startActivity(intent);
            return true;
        }
        return true;
    }
}