package com.example.t1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class AddPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editPostContent;
    private ImageView previewPhoto;
    private Uri imageUri;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        editPostContent = findViewById(R.id.editPostContent);
        previewPhoto = findViewById(R.id.previewPhoto);
        ImageButton btnAddPhoto = findViewById(R.id.btnAddPhoto);
        Button btnSharePost = findViewById(R.id.btnSharePost);

        btnAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSharePost.setOnClickListener(v -> {
            // EditText'teki paylaşım içeriğini al
            String postContent = editPostContent.getText().toString().trim();

            // Kullanıcının UID'sini al
            String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

            // Eğer oturum açık değilse, hata mesajı göster
            if (userId == null) {
                System.out.println("Kullanıcı oturumu açık değil!");
                return;
            }

            // Firestore'dan kullanıcı adını çek
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");

                            // Kullanıcı adı kontrolü
                            if (username != null && !username.isEmpty()) {
                                // Intent ile paylaşılacak veriler
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("postContent", postContent);
                                resultIntent.putExtra("username", username);
                                resultIntent.putExtra("imageUri", imageUri != null ? imageUri.toString() : null);

                                // Sonuç set ve aktiviteyi kapat
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            } else {
                                System.out.println("Kullanıcı adı Firestore'dan alınamadı.");
                            }
                        } else {
                            System.out.println("Firestore dökümanı bulunamadı!");
                        }
                    })
                    .addOnFailureListener(e -> {
                        System.out.println("Firestore sorgusu başarısız oldu: " + e.getMessage());
                        e.printStackTrace();
                    });
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                previewPhoto.setImageBitmap(bitmap);
                previewPhoto.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
