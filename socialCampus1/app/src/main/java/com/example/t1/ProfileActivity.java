package com.example.t1;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextName, editTextSurname, editTextUsername, editTextEmail, editTextDepartment;
    private Button buttonUpdate;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String userId; // Dinamik kullanıcı ID'si

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextDepartment = findViewById(R.id.editTextDepartment);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid(); // Oturum açmış kullanıcının ID'si
            loadUserProfile();
        } else {
            Toast.makeText(this, "Kullanıcı oturum açmamış.", Toast.LENGTH_SHORT).show();
            finish(); // Eğer kullanıcı oturum açmamışsa aktiviteyi kapatabilirsiniz
        }

        buttonUpdate.setOnClickListener(v -> updateUserProfile());
    }

    private void loadUserProfile() {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            editTextName.setText(document.getString("name"));
                            editTextSurname.setText(document.getString("surname"));
                            editTextUsername.setText(document.getString("username"));
                            editTextEmail.setText(document.getString("email"));
                            editTextDepartment.setText(document.getString("department"));
                        } else {
                            Toast.makeText(ProfileActivity.this, "Profil bilgileri yüklenemedi.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUserProfile() {
        String name = editTextName.getText().toString().trim();
        String surname = editTextSurname.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String department = editTextDepartment.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(department)) {
            Toast.makeText(this, "Tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("surname", surname);
        user.put("username", username);
        user.put("email", email);
        user.put("department", department);

        db.collection("users").document(userId).update(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Profil güncellendi.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Profil güncellenirken hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
