package com.example.proje11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<Post> {
    private Context context;
    private ArrayList<Post> posts;

    public PostAdapter(Context context, ArrayList<Post> posts) {
        super(context, 0, posts);
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        }

        Post post = posts.get(position);
        TextView username = convertView.findViewById(R.id.post_username);
        TextView postText = convertView.findViewById(R.id.post_text_content);

        username.setText(post.getUsername());
        postText.setText(post.getPostText());

        return convertView;
    }
}
