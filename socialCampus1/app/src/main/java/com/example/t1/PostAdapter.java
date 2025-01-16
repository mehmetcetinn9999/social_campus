package com.example.t1;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
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
        holder.timestampTextView.setText(post.getTimestamp());

        if (post.getImageUri() != null) {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageURI(Uri.parse(post.getImageUri()));
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

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

