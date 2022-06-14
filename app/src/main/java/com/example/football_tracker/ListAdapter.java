package com.example.football_tracker;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    List<Match> matchList;
    Context context;

    public ListAdapter(List<Match> matchList, Context context) {
        this.matchList = matchList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Match match = matchList.get(position);
        holder.bindData(match);

        holder.relativeLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, MatchInfoActivity.class);

            intent.putExtra("id", match.id);
            intent.putExtra("team1_name", match.team1);
            intent.putExtra("team2_name", match.team2);
            intent.putExtra("team1_score", match.team1Score);
            intent.putExtra("team2_score", match.team2Score);
            intent.putExtra("match_location", match.location);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView team1_name, team2_name, team1_match_score, team2_match_score;

        RelativeLayout relativeLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            team1_name = itemView.findViewById(R.id.team1_name);
            team2_name = itemView.findViewById(R.id.team2_name);
            team1_match_score = itemView.findViewById(R.id.team1_match_score);
            team2_match_score = itemView.findViewById(R.id.team2_match_score);

            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }

        void bindData(final Match item) {
            team1_name.setText(item.team1);
            team2_name.setText(item.team2);
            team1_match_score.setText(item.team1Score);
            team2_match_score.setText(item.team2Score);
        }
    }
}