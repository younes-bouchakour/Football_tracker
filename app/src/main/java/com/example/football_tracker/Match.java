package com.example.football_tracker;

public class Match {
    int id;
    static int totalMatch = 0;
    String team1;
    String team2;
    String team1Score;
    String team2Score;
    String location;

    public Match( String team1, String team2, String team1Score, String team2Score, String location ) {
        totalMatch ++;
        this.id = totalMatch;
        this.team1 = team1;
        this.team2 = team2;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.location = location;
    }
    public Match( int id, String team1, String team2, String team1Score, String team2Score, String location ) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.location = location;
    }
}
