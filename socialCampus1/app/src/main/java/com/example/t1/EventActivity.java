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

public class EventActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerViewEvents;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // "+" Butonu Ayarları
        ImageButton addClubButton = findViewById(R.id.btn_add_event);
        addClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, AddEventActivity.class);
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
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_home) {
                startActivity(new Intent(EventActivity.this, HomeActivity.class));
                finish();
            } else if (id == R.id.menu_profile) {
                Toast.makeText(EventActivity.this, "Profil seçildi", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.menu_logout) {
                Toast.makeText(EventActivity.this, "Çıkış yapılıyor...", Toast.LENGTH_SHORT).show();
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // RecyclerView Ayarları
        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        recyclerViewEvents.setAdapter(eventAdapter);

        loadEventsFromFirestore();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_event) {
                // Zaten EventActivity'deyiz, bir şey yapmaya gerek yok
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(EventActivity.this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.nav_club) {
                startActivity(new Intent(EventActivity.this, ClubActivity.class));
                return true;
            }

            return false;
        });
    }

    private void loadEventsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    eventList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        queryDocumentSnapshots.forEach(documentSnapshot -> {
                            String username = documentSnapshot.getString("username");
                            String content = documentSnapshot.getString("content");
                            String imageUri = documentSnapshot.getString("imageUri");
                            String eventDate = documentSnapshot.getString("eventDate");
                            String price = documentSnapshot.getString("price");

                            System.out.println("Alınan Tarih: " + eventDate);



                            long timestamp = documentSnapshot.getLong("timestamp");

                            eventList.add(0, new Event(username, content, String.valueOf(timestamp), imageUri, eventDate, price));
                        });
                        eventAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Etkinlikleri yüklerken hata oluştu.", Toast.LENGTH_SHORT).show());
    }
}
