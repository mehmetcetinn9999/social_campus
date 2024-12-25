package com.example.proje11;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proje11.PostAdapter;
import com.example.proje11.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ListView postListView;
    PostAdapter postAdapter;
    ArrayList<Post> postList;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        postListView = findViewById(R.id.post_list);
        dbHelper = new DBHelper(this);
        postList = new ArrayList<>();

        loadPosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_post) {
            Intent intent = new Intent(this, NewPostActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPosts() {
        postList.clear();
        postList.addAll(dbHelper.getAllPosts());
        postAdapter = new PostAdapter(this, postList);
        postListView.setAdapter(postAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPosts();
    }
}
