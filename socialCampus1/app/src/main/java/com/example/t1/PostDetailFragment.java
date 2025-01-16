package com.example.t1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class PostDetailFragment extends DialogFragment {

    private TextView textName, textSurname, textUsername, textEmail, textDepartment;
    private FirebaseFirestore db;

    public PostDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        textName = view.findViewById(R.id.textName);
        textSurname = view.findViewById(R.id.textSurname);
        textUsername = view.findViewById(R.id.textUsername);
        textEmail = view.findViewById(R.id.textEmail);
        textDepartment = view.findViewById(R.id.textDepartment);

        db = FirebaseFirestore.getInstance();

        // Kullanıcı adı argüman olarak alınıyor
        String username = getArguments() != null ? getArguments().getString("username") : null;

        if (username != null) {
            db.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                textName.setText(document.getString("name"));
                                textSurname.setText(document.getString("surname"));
                                textUsername.setText(document.getString("username"));
                                textEmail.setText(document.getString("email"));
                                textDepartment.setText(document.getString("department"));
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
