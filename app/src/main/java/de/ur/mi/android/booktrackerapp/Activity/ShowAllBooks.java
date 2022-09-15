package de.ur.mi.android.booktrackerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.ur.mi.android.booktrackerapp.R;

public class ShowAllBooks extends AppCompatActivity {

    private FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_books);

        fabBtn = findViewById(R.id.add_fab_btn);
        fabBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ShowAllBooks.this, SearchBook.class);
            startActivity(intent);
        });
    }
}