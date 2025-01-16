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

public class EventsListFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_events);
        ImageButton addButton = view.findViewById(R.id.add_button);

        // RecyclerView Ayarları
        eventsList = getDummyEvents();
        adapter = new EventAdapter(getContext(), eventsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // "+" Butonuyla EventAddFragment'e Geçiş
        addButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new EventAddFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    // Örnek Event Verileri
    private List<Event> getDummyEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("Campus Party", "2025-01-20", "100 TL", "0123456789"));
        events.add(new Event("Android Workshop", "2025-02-10", "200 TL", "9876543210"));
        return events;
    }
}
