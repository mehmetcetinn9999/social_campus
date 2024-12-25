package com.example.proje11;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewPostActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    EditText postText;
    ImageView postImage;
    Button btnSelectImage, btnPost;
    DBHelper dbHelper;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        postText = findViewById(R.id.post_text);
        postImage = findViewById(R.id.post_image);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnPost = findViewById(R.id.btn_post);
        dbHelper = new DBHelper(this);

        btnSelectImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnPost.setOnClickListener(view -> {
            String text = postText.getText().toString().trim();
            String imagePath = selectedImageUri != null ? selectedImageUri.toString() : null;

            if (text.isEmpty() && imagePath == null) {
                Toast.makeText(this, "Please provide text or an image", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.insertPost("User", text, imagePath)) {
                Toast.makeText(this, "Post added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                postImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}