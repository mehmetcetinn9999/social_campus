package com.example.t1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EventAddFragment extends Fragment {

    private EditText eventName, eventDate, eventPrice, eventContact;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_add, container, false);

        eventName = view.findViewById(R.id.event_name);
        eventDate = view.findViewById(R.id.event_date);
        eventPrice = view.findViewById(R.id.event_price);
        eventContact = view.findViewById(R.id.event_contact);
        Button saveButton = view.findViewById(R.id.save_event_button);

        saveButton.setOnClickListener(v -> saveEvent());

        return view;
    }

    private void saveEvent() {
        String name = eventName.getText().toString();
        String date = eventDate.getText().toString();
        String price = eventPrice.getText().toString();
        String contact = eventContact.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(date) || TextUtils.isEmpty(price) || TextUtils.isEmpty(contact)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(), "Event added: " + name, Toast.LENGTH_SHORT).show();
    }
}
