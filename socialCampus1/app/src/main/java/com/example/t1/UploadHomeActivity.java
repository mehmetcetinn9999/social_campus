package com.example.t1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UploadHomeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView uploadImageView;
    private EditText editTextPost;
    private ImageButton uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_home);

        uploadImageView = findViewById(R.id.upload_image);
        editTextPost = findViewById(R.id.editTextPost);
        uploadButton = findViewById(R.id.upload_button);

        // Resim seçmek için tıklama
        uploadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Gönderiyi paylaşmak için tıklama
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });
    }

    // Galeriyi açma
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImageView.setImageURI(imageUri); // Seçilen resmi göster
        }
    }

    private void uploadPost() {
        String postText = editTextPost.getText().toString();

        if (postText.isEmpty() && imageUri == null) {
            Toast.makeText(this, "Please enter some text or select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("post_text", postText);

        if (imageUri != null) {
            intent.putExtra("image_uri", imageUri.toString());
        }

        startActivity(intent);
    }
}