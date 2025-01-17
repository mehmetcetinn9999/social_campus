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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.usernameTextView.setText(post.getUsername());
        holder.contentTextView.setText(post.getContent());

        String formattedTimestamp = formatTimestamp(post.getTimestamp());
        holder.timestampTextView.setText(formattedTimestamp);

        if (post.getImageUri() != null && !post.getImageUri().isEmpty()) {
            holder.imageView.setVisibility(View.VISIBLE);

            // Glide (resim yükleme)
            Glide.with(holder.itemView.getContext())
                    .load(post.getImageUri())
                    .into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        //Post paylaşan detay fragmenti
        holder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            PostDetailFragment fragment = new PostDetailFragment();

            Bundle args = new Bundle();
            args.putString("username", post.getUsername());
            fragment.setArguments(args);
            fragment.show(activity.getSupportFragmentManager(), "postDetail");
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    //firebase den gelen long formatındaki zamanı tarihe çevir
    private String formatTimestamp(String timestamp) {
        try {
            long timeInMillis = Long.parseLong(timestamp);
            Date date = new Date(timeInMillis);

            return DateFormat.format("dd MMM yyyy, HH:mm", date).toString();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        TextView usernameTextView, contentTextView, timestampTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.textUsername);
            contentTextView = itemView.findViewById(R.id.textContent);
            timestampTextView = itemView.findViewById(R.id.textTimestamp);
            imageView = itemView.findViewById(R.id.post_image);
        }
    }
}
