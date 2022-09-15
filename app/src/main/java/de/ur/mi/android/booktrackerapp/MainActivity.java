package de.ur.mi.android.booktrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import de.ur.mi.android.booktrackerapp.Activity.ShowAllBooks;

public class MainActivity extends AppCompatActivity {
    private Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        continueBtn = findViewById(R.id.btn_continue);
        continueBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ShowAllBooks.class);
            startActivity(intent);
        });
    }
}