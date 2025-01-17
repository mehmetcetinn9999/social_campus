package com.example.t1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClubDetailFragment extends DialogFragment {

    private TextView textClubName, textClubContent, textClubOwner;
    private ImageView imageClub;
    private FirebaseFirestore db;

    public ClubDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_club_detail, container, false);

        textClubName = view.findViewById(R.id.textClubName);
        textClubContent = view.findViewById(R.id.textClubContent);
        textClubOwner = view.findViewById(R.id.textClubOwner);
        imageClub = view.findViewById(R.id.imageClub);

        db = FirebaseFirestore.getInstance();

        String clubId = getArguments() != null ? getArguments().getString("clubId") : null;

        if (clubId != null) {
            db.collection("clubs").document(clubId)
                    .get()
                    .addOnSuccessListener(document -> {
                        if (document.exists()) {
                            textClubName.setText(document.getString("clubName"));
                            textClubContent.setText(document.getString("content"));
                            textClubOwner.setText(document.getString("username"));

                            String imageUri = document.getString("imageUri");
                            if (imageUri != null && !imageUri.isEmpty()) {
                                Glide.with(requireContext()).load(imageUri).into(imageClub);
                            }
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
