package com.example.t1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        // Set username and content
        holder.usernameTextView.setText(event.getUsername());
        holder.contentTextView.setText(event.getContent());

        String formattedDate = event.getEventDate();
        String eventDetails = "Etkinlik Tarihi: " + formattedDate + " Fiyat: " + event.getPrice() + "TL";
        holder.eventDetailsTextView.setText(eventDetails);

        // Load and display the image if available
        if (event.getImageUri() != null && !event.getImageUri().isEmpty()) {
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(event.getImageUri()).into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            PostDetailFragment fragment = new PostDetailFragment();

            Bundle args = new Bundle();
            args.putString("username", event.getUsername());
            fragment.setArguments(args);
            fragment.show(activity.getSupportFragmentManager(), "postDetail");
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    private String formatDate(String timestamp) {
        try {
            long timeInMillis = Long.parseLong(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            return sdf.format(timeInMillis);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, contentTextView, eventDetailsTextView;
        ImageView imageView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.textUsername);
            contentTextView = itemView.findViewById(R.id.textContent);
            eventDetailsTextView = itemView.findViewById(R.id.textEventDetails);
            imageView = itemView.findViewById(R.id.event_image);
        }
    }
}
