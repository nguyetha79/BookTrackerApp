package de.ur.mi.android.booktrackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShowAllBooks extends AppCompatActivity {

    private Button fabBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_books);

        fabBtn = findViewById(R.id.add_fab_btn);
        fabBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchBook.class);
            startActivity(intent);
        });
    }
}