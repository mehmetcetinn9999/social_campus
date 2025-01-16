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
import com.google.android.material.navigation.NavigationView;
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
                    Toast.makeText(HomeActivity.this, "Profil seçildi", Toast.LENGTH_SHORT).show();
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
        postList.add(new Post("User1", "This is a sample post content", "10:00 AM",null));
        postList.add(new Post("User2", "Another sample post", "10:30 AM", null));

        postAdapter = new PostAdapter(postList);
        recyclerViewPosts.setAdapter(postAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_POST_REQUEST && resultCode == RESULT_OK && data != null) {
            String postContent = data.getStringExtra("postContent");
            String imageUri = data.getStringExtra("imageUri");

            // Yeni post ekle
            postList.add(0, new Post("CurrentUser", postContent, "Now", imageUri));
            postAdapter.notifyItemInserted(0);
        }
    }
}
