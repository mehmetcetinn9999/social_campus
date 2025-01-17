package com.example.t1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final int ADD_POST_REQUEST = 1;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // "+" Butonu Ayarları
        ImageButton addPostButton = findViewById(R.id.btn_add_post);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddPostActivity.class);
                startActivityForResult(intent, ADD_POST_REQUEST);
            }
        });

        // DrawerLayout ve NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Navigation Drawer Açma/Kapama
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation Menü Tıklamaları
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_home) {
                    Toast.makeText(HomeActivity.this, "Anasayfa seçildi", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.menu_profile) {
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.menu_logout) {
                    Toast.makeText(HomeActivity.this, "Çıkış yapılıyor...", Toast.LENGTH_SHORT).show();
                    finish(); // Çıkış için aktiviteyi kapat
                } else {
                    Toast.makeText(HomeActivity.this, "Bilinmeyen seçim", Toast.LENGTH_SHORT).show();
                }

                // Navigation Drawer'ı kapat
                drawerLayout.closeDrawers();
                return true;
            }
        });

        // RecyclerView Ayarları
        recyclerViewPosts = findViewById(R.id.recyclerViewPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();

        postAdapter = new PostAdapter(postList);
        recyclerViewPosts.setAdapter(postAdapter);

        loadPostsFromFirestore();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_event) {
                startActivity(new Intent(HomeActivity.this, EventActivity.class));
                return true;
            } else if (itemId == R.id.nav_home) {
                // Zaten HomeActivity'deyiz, bir şey yapmaya gerek yok
                return true;
            } else if (itemId == R.id.nav_club) {
                startActivity(new Intent(HomeActivity.this, ClubActivity.class));
                return true;
            }

            return false;
        });
    }

    private void loadPostsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .orderBy("timestamp") // Zaman damgasına göre sırala
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList.clear(); // Eski verileri temizle
                    if (!queryDocumentSnapshots.isEmpty()) {
                        queryDocumentSnapshots.forEach(documentSnapshot -> {
                            String username = documentSnapshot.getString("username");
                            String content = documentSnapshot.getString("content");
                            String imageUri = documentSnapshot.getString("imageUri");
                            long timestamp = documentSnapshot.getLong("timestamp");

                                // Listeye post ekle
                            postList.add(0, new Post(username, content, String.valueOf(timestamp), imageUri));
                        });
                        postAdapter.notifyDataSetChanged(); // Listeyi güncelle
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Postları yüklerken hata oluştu: " + e.getMessage());
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_POST_REQUEST && resultCode == RESULT_OK && data != null) {
            String postContent = data.getStringExtra("postContent");
            String imageUri = data.getStringExtra("imageUri");
            String username = data.getStringExtra("username");

            // Yeni post ekle
            postList.add(0, new Post(username, postContent, "Now", imageUri));
            postAdapter.notifyItemInserted(0);
        }
    }
}
