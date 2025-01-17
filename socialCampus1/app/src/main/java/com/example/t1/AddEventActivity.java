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

public class AddEventActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editEventContent, editEventDate, editEventPrice;
    private ImageView previewEventPhoto;
    private Uri imageUri;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        editEventContent = findViewById(R.id.editEventContent);
        editEventDate = findViewById(R.id.editEventDate);
        editEventPrice = findViewById(R.id.editEventPrice);
        previewEventPhoto = findViewById(R.id.previewEventPhoto);
        ImageButton btnAddPhoto = findViewById(R.id.btnAddPhoto);
        Button btnShareEvent = findViewById(R.id.btnShareEvent);

        btnAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnShareEvent.setOnClickListener(v -> {
            String eventContent = editEventContent.getText().toString().trim();
            String eventDate = editEventDate.getText().toString().trim();
            String eventPrice = editEventPrice.getText().toString().trim();
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
                                    uploadImageAndSaveEvent(username, eventContent, eventDate, eventPrice);
                                } else {
                                    saveEventToFirestore(username, eventContent, eventDate, eventPrice, null);
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

    private void uploadImageAndSaveEvent(String username, String content, String date, String price) {
        String fileName = UUID.randomUUID().toString();
        StorageReference fileRef = storageRef.child("event_images/" + fileName);

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                saveEventToFirestore(username, content, date, price, uri.toString());
            }).addOnFailureListener(e -> {
                System.out.println("Görsel URL'si alınamadı: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            System.out.println("Görsel yükleme başarısız: " + e.getMessage());
        });
    }

    private void saveEventToFirestore(String username, String content, String date, String price, String imageUrl) {
        HashMap<String, Object> event = new HashMap<>();
        event.put("username", username);
        event.put("content", content);
        event.put("eventDate", date);
        event.put("price", price);
        event.put("imageUri", imageUrl);
        event.put("timestamp", System.currentTimeMillis());

        db.collection("events").add(event).addOnSuccessListener(documentReference -> {
            System.out.println("Etkinlik başarıyla kaydedildi!");
            finish();
        }).addOnFailureListener(e -> {
            System.out.println("Etkinlik kaydedilemedi: " + e.getMessage());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                previewEventPhoto.setImageBitmap(bitmap);
                previewEventPhoto.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}