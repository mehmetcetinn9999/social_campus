package com.example.t1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityLogin extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, signUpButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // UI bileşenleri
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.Login);
        signUpButton = findViewById(R.id.SignUp);

        // Giriş butonuna tıklama
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(ActivityLogin.this, "Lütfen e-posta adresinizi girin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(ActivityLogin.this, "Lütfen şifrenizi girin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase ile giriş yapma
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Giriş başarılı, HomeActivity'ye yönlendir
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(ActivityLogin.this, "Hoş geldiniz, " + (user != null ? user.getEmail() : "Kullanıcı"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityLogin.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Giriş başarısız
                            Toast.makeText(ActivityLogin.this, "Giriş başarısız: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Kayıt olma butonuna tıklama
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityLogin.this, ActivitySignUp.class);
            startActivity(intent);
        });
    }
}
