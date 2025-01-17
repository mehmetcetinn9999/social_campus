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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AddClubActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editClubName, editClubContent;
    private ImageView previewPhoto;
    private Uri imageUri;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_club);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        editClubName = findViewById(R.id.editClubName);
        editClubContent = findViewById(R.id.editClubContent);
        previewPhoto = findViewById(R.id.previewPhoto);
        ImageButton btnAddPhoto = findViewById(R.id.btnAddPhoto);
        Button btnShareClub = findViewById(R.id.btnShareClub);

        btnAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnShareClub.setOnClickListener(v -> {
            String clubName = editClubName.getText().toString().trim();
            String clubContent = editClubContent.getText().toString().trim();
            String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

            if (userId == null) {
                System.out.println("Kullanıcı oturumu açık değil!");
                return;
            }

            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");

                            if (username != null && !username.isEmpty()) {
                                if (imageUri != null) {
                                    uploadImageAndSaveClub(username, clubName, clubContent);
                                } else {
                                    saveClubToFirestore(username, clubName, clubContent, null);
                                }
                            } else {
                                System.out.println("Kullanıcı adı bulunamadı.");
                            }
                        } else {
                            System.out.println("Kullanıcı dökümanı mevcut değil.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        System.out.println("Firestore sorgusu başarısız oldu: " + e.getMessage());
                    });
        });
    }

    private void uploadImageAndSaveClub(String username, String clubName, String clubContent) {
        String fileName = UUID.randomUUID().toString();
        StorageReference fileRef = storageRef.child("club_images/" + fileName);

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                saveClubToFirestore(username, clubName, clubContent, uri.toString());
            }).addOnFailureListener(e -> {
                System.out.println("Görsel URL'si alınamadı: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            System.out.println("Görsel yükleme başarısız: " + e.getMessage());
        });
    }

    private void saveClubToFirestore(String username, String clubName, String clubContent, String imageUrl) {
        HashMap<String, Object> club = new HashMap<>();
        club.put("username", username);
        club.put("clubName", clubName);
        club.put("content", clubContent);
        club.put("imageUri", imageUrl);
        club.put("timestamp", System.currentTimeMillis());

        db.collection("clubs").add(club).addOnSuccessListener(documentReference -> {
            System.out.println("Klüp başarıyla kaydedildi!");
            finish();
        }).addOnFailureListener(e -> {
            System.out.println("Klüp kaydedilemedi: " + e.getMessage());
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
