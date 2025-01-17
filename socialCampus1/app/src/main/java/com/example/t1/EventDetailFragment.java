package com.example.t1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventDetailFragment extends DialogFragment {

    private TextView textEventName, textEventDate, textEventPrice;
    private FirebaseFirestore db;

    public EventDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);

        textEventName = view.findViewById(R.id.textEventName);
        textEventDate = view.findViewById(R.id.textEventDate);
        textEventPrice = view.findViewById(R.id.textEventPrice);

        db = FirebaseFirestore.getInstance();

        String eventId = getArguments() != null ? getArguments().getString("eventId") : null;

        if (eventId != null) {
            db.collection("events").document(eventId)
                    .get()
                    .addOnSuccessListener(document -> {
                        if (document.exists()) {
                            textEventName.setText(document.getString("content"));
                            textEventDate.setText(document.getString("eventDate"));
                            textEventPrice.setText(document.getString("price"));
                        }
                    });
        }

        return view;
    }

    @Override
    public int getTheme() {
        return R.style.CustomDialogTheme;
    }
}
