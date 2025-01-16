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

public class ClubAddFragment extends Fragment {

    private EditText clubName, clubDescription, clubContact;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_club_add, container, false);

        clubName = view.findViewById(R.id.club_name);
        clubDescription = view.findViewById(R.id.club_description);
        clubContact = view.findViewById(R.id.club_contact);
        Button saveButton = view.findViewById(R.id.save_club_button);

        saveButton.setOnClickListener(v -> saveClub());

        return view;
    }

    private void saveClub() {
        String name = clubName.getText().toString();
        String description = clubDescription.getText().toString();
        String contact = clubContact.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(contact)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(), "Club added: " + name, Toast.LENGTH_SHORT).show();
    }
}
