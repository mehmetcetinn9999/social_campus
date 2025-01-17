package com.example.t1;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubViewHolder> {

    private List<Club> clubList;

    public ClubAdapter(List<Club> clubList) {
        this.clubList = clubList;
    }

    @NonNull
    @Override
    public ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_club, parent, false);
        return new ClubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubViewHolder holder, int position) {
        Club club = clubList.get(position);

        holder.usernameTextView.setText(club.getUsername());
        holder.clubNameTextView.setText(club.getClubName());
        holder.contentTextView.setText(club.getContent());

        String formattedTimestamp = formatTimestamp(club.getTimestamp());
        holder.timestampTextView.setText(formattedTimestamp);

        if (club.getImageUri() != null && !club.getImageUri().isEmpty()) {
            holder.clubImageView.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(club.getImageUri())
                    .into(holder.clubImageView);
        } else {
            holder.clubImageView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            ClubDetailFragment fragment = new ClubDetailFragment();

            Bundle args = new Bundle();
            args.putString("clubName", club.getClubName());
            fragment.setArguments(args);
            fragment.show(activity.getSupportFragmentManager(), "clubDetail");
        });
    }

    @Override
    public int getItemCount() {
        return clubList.size();
    }

    private String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp); // long türünü Date nesnesine dönüştür
        return DateFormat.format("dd MMM yyyy, HH:mm", date).toString(); // Tarihi formatlayıp String döndür
    }

    static class ClubViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, clubNameTextView, contentTextView, timestampTextView;
        ImageView clubImageView;

        public ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.textUsername);
            clubNameTextView = itemView.findViewById(R.id.textClubName);
            contentTextView = itemView.findViewById(R.id.textContent);
            timestampTextView = itemView.findViewById(R.id.textTimestamp);
            clubImageView = itemView.findViewById(R.id.clubImage);
        }
    }
}
