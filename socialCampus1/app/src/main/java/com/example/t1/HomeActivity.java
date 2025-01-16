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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout postsContainer;
    private ImageButton eventButton, clubButton, uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Butonları ve layout'u bağlama
        eventButton = findViewById(R.id.eventButton);
        clubButton = findViewById(R.id.clubButton);
        uploadButton = findViewById(R.id.upload_button);
        postsContainer = findViewById(R.id.postsContainer);

        // Varsayılan Fragment (Event Listesi)
        if (savedInstanceState == null) {
            openFragment(new EventsListFragment());
        }

        // Event Butonu Tıklama
        eventButton.setOnClickListener(v -> openFragment(new EventsListFragment()));

        // Club Butonu Tıklama
        clubButton.setOnClickListener(v -> openFragment(new ClubsListFragment()));

        // Upload Butonu İşlevi
        uploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UploadHomeActivity.class);
            startActivity(intent);
        });

        // Intent'ten gelen verilerle gönderi ekleme
        String postText = getIntent().getStringExtra("post_text");
        String imageUriString = getIntent().getStringExtra("image_uri");

        if (postText != null) {
            addPost(postText, imageUriString);
        }
    }

    // Yeni Fragment Açma
    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Gönderi Ekleme
    private void addPost(String text, String imageUriString) {
        View postView = LayoutInflater.from(this).inflate(R.layout.item_post, null);

        TextView postTextView = postView.findViewById(R.id.post_text);
        ImageView postImageView = postView.findViewById(R.id.post_image);

        postTextView.setText(text);

        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            postImageView.setImageURI(imageUri);

            // Dinamik boyutlandırma
            postImageView.getLayoutParams().height = 200;
            postImageView.requestLayout();
        }

        postsContainer.addView(postView);
    }
}
