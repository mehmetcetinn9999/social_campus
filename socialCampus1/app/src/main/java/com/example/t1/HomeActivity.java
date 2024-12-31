package com.example.t1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.widget.Button; // Button için import

public class HomeActivity extends AppCompatActivity {

    private LinearLayout postsContainer;
    private SearchView searchView;
    private ImageButton uploadButton1; // Upload Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        postsContainer = findViewById(R.id.postsContainer);
        searchView = findViewById(R.id.search_view);
        uploadButton1 = findViewById(R.id.upload_button);


        uploadButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UploadHomeActivity.class);
                startActivity(intent);
            }
        });


        String postText = getIntent().getStringExtra("post_text");
        String imageUriString = getIntent().getStringExtra("image_uri");


        if (postText != null) {
            addPost(postText, imageUriString);
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPosts(newText);
                return true;
            }
        });
    }


    private void addPost(String text, String imageUriString) {
        View postView = LayoutInflater.from(this).inflate(R.layout.item_post, null);

        TextView postTextView = postView.findViewById(R.id.post_text);
        ImageView postImageView = postView.findViewById(R.id.post_image);

        postTextView.setText(text);

        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            postImageView.setImageURI(imageUri);
        }

        postsContainer.addView(postView);
    }


    private void filterPosts(String query) {
        for (int i = 0; i < postsContainer.getChildCount(); i++) {
            View postView = postsContainer.getChildAt(i);
            TextView postTextView = postView.findViewById(R.id.post_text);
            String postText = postTextView.getText().toString();

            if (postText.toLowerCase().contains(query.toLowerCase())) {
                postView.setVisibility(View.VISIBLE);
            } else {
                postView.setVisibility(View.GONE);
            }
        }
    }
}


