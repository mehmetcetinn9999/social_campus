package com.example.t1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClubsListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClubAdapter adapter;
    private List<Club> clubsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clubs_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_clubs);
        ImageButton addButton = view.findViewById(R.id.add_button);

        // RecyclerView Ayarları
        clubsList = getDummyClubs();
        adapter = new ClubAdapter(clubsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // "+" Butonu ile ClubAddFragment'e Geçiş
        addButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new ClubAddFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private List<Club> getDummyClubs() {
        List<Club> clubs = new ArrayList<>();
        clubs.add(new Club("Theater Club", "Bringing creativity and acting together.", "theaterclub@university.com"));
        clubs.add(new Club("Art Club", "Explore creativity through painting.", "artclub@university.com"));
        return clubs;
    }
}
