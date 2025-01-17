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
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editPostContent;
    private ImageView previewPhoto;
    private ProgressBar progressBar;
    private Uri imageUri;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private StorageReference storageRef;
    //Thread
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        editPostContent = findViewById(R.id.editPostContent);
        previewPhoto = findViewById(R.id.previewPhoto);
        progressBar = findViewById(R.id.progressBar);
        ImageButton btnAddPhoto = findViewById(R.id.btnAddPhoto);
        Button btnSharePost = findViewById(R.id.btnSharePost);

        btnAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSharePost.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            String postContent = editPostContent.getText().toString().trim();
            String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

            if (userId == null) {
                progressBar.setVisibility(View.GONE);
                System.out.println("Kullanıcı oturumu açık değil");
                return;
            }

            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");

                            if (username != null && !username.isEmpty()) {
                                if (imageUri != null) {
                                    uploadImageAndSavePost(username, postContent);
                                } else {
                                    savePostToFirestore(username, postContent, null);
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                System.out.println("Kullanıcı adı bulunamadı.");
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            System.out.println("Kullanıcı dökümanı mevcut değil.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        System.out.println("Firestore sorgusu hatalı: " + e.getMessage());
                    });
        });
    }

    private void uploadImageAndSavePost(String username, String content) {
        executor.execute(() -> {
            try {
                String fileName = UUID.randomUUID().toString();
                StorageReference fileRef = storageRef.child("post_images/" + fileName);

                fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        savePostToFirestore(username, content, uri.toString());
                    }).addOnFailureListener(e -> {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            System.out.println("Görsel URL'si alınamadı: " + e.getMessage());
                        });
                    });
                }).addOnFailureListener(e -> {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        System.out.println("Görsel yükleme başarısız: " + e.getMessage());
                    });
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    System.out.println("Arka planda bir hata oluştu: " + e.getMessage());
                });
            }
        });
    }

    private void savePostToFirestore(String username, String content, String imageUrl) {
        executor.execute(() -> {
            HashMap<String, Object> post = new HashMap<>();
            post.put("username", username);
            post.put("content", content);
            post.put("imageUri", imageUrl);
            post.put("timestamp", System.currentTimeMillis());

            db.collection("posts").add(post).addOnSuccessListener(documentReference -> {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    System.out.println("Post kaydedildi");
                    finish();
                });
            }).addOnFailureListener(e -> {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    System.out.println("Post kaydedilemedi: " + e.getMessage());
                });
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
