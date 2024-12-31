package com.example.t1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UploadHomeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView uploadImageView;
    private EditText editTextPost;
    private ImageButton uploadButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_home);

        uploadImageView = findViewById(R.id.upload_image);
        editTextPost = findViewById(R.id.editTextPost);
        uploadButton1 = findViewById(R.id.upload_button);


        uploadButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost(v);
            }
        });
    }


    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImageView.setImageURI(imageUri);
        }
    }

    // Postu Upload etme
    public void uploadPost(View view) {
        String postText = editTextPost.getText().toString();

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("post_text", postText);

        if (imageUri != null) {
            intent.putExtra("image_uri", imageUri.toString());
        }

        startActivity(intent);
    }
}



