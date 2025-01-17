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

public class ClubActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerViewClubs;
    private ClubAdapter clubAdapter;
    private List<Club> clubList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton addClubButton = findViewById(R.id.btn_add_club);
        addClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClubActivity.this, AddClubActivity.class);
                startActivity(intent);
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
                    startActivity(new Intent(ClubActivity.this, HomeActivity.class));
                } else if (id == R.id.menu_profile) {
                    Intent intent = new Intent(ClubActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.menu_logout) {
                    Toast.makeText(ClubActivity.this, "Çıkış yapılıyor...", Toast.LENGTH_SHORT).show();
                    finish(); // Çıkış için aktiviteyi kapat
                } else {
                    Toast.makeText(ClubActivity.this, "Bilinmeyen seçim", Toast.LENGTH_SHORT).show();
                }

                // Navigation Drawer'ı kapat
                drawerLayout.closeDrawers();
                return true;
            }
        });

        // RecyclerView Ayarları
        recyclerViewClubs = findViewById(R.id.recyclerViewClubs);
        recyclerViewClubs.setLayoutManager(new LinearLayoutManager(this));

        clubList = new ArrayList<>();

        clubAdapter = new ClubAdapter(clubList);
        recyclerViewClubs.setAdapter(clubAdapter);

        loadClubsFromFirestore();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_event) {
                startActivity(new Intent(ClubActivity.this, EventActivity.class));
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(ClubActivity.this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.nav_club) {
                // Zaten ClubActivity'deyiz, bir şey yapmaya gerek yok
                return true;
            }

            return false;
        });
    }

    private void loadClubsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("clubs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    clubList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        queryDocumentSnapshots.forEach(documentSnapshot -> {
                            String username = documentSnapshot.getString("username");
                            String clubName = documentSnapshot.getString("clubName");
                            String content = documentSnapshot.getString("content");
                            String imageUri = documentSnapshot.getString("imageUri");
                            Long timestamp = documentSnapshot.getLong("timestamp");

                            clubList.add(new Club(username, clubName, content, imageUri, timestamp));
                        });
                        clubAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Kulüpleri yüklerken hata oluştu: " + e.getMessage());
                });
    }
}
