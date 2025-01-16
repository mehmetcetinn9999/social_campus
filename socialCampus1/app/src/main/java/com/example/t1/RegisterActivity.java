package com.example.t1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, nameEditText, surnameEditText, usernameEditText, departmentEditText;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI components
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        nameEditText = findViewById(R.id.editTextName);
        surnameEditText = findViewById(R.id.editTextSurname);
        usernameEditText = findViewById(R.id.editTextUsername);
        departmentEditText = findViewById(R.id.editTextDepartment);
        signUpButton = findViewById(R.id.buttonSignUp);

        // Yükleme göstergesi (ProgressDialog)
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Kayıt yapılıyor...");
        progressDialog.setCancelable(false);

        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();
            String surname = surnameEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String department = departmentEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) ||
                    TextUtils.isEmpty(surname) || TextUtils.isEmpty(username) || TextUtils.isEmpty(department)) {
                Toast.makeText(RegisterActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Şifre en az 6 karakter olmalı", Toast.LENGTH_SHORT).show();
                return;
            }

            // Yükleme göstergesini göster
            progressDialog.show();

            // Kullanıcı adının benzersizliğini kontrol et
            db.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Kullanıcı adı zaten kullanılıyor
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Bu kullanıcı adı zaten alınmış!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Kullanıcı adı benzersiz, Firebase Authentication ile kullanıcı oluşturma
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(authTask -> {
                                        if (authTask.isSuccessful()) {
                                            // Kullanıcı oluşturulduktan sonra Firestore'a veri ekleme
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if (user != null) {
                                                String userId = user.getUid();
                                                Map<String, Object> userData = new HashMap<>();
                                                userData.put("name", name);
                                                userData.put("surname", surname);
                                                userData.put("username", username);
                                                userData.put("department", department);
                                                userData.put("email", email);

                                                db.collection("users").document(userId)
                                                        .set(userData)
                                                        .addOnSuccessListener(aVoid -> {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(RegisterActivity.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                                                            // LoginActivity'e yönlendirme
                                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            finish(); // Bu aktiviteyi kapat
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(RegisterActivity.this, "Veri kaydedilirken hata oluştu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        });
                                            }
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Kayıt başarısız: " + authTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Kullanıcı adı kontrol edilirken hata oluştu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}