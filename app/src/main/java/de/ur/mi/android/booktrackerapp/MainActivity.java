package de.ur.mi.android.booktrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button continueBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        continueBtn = findViewById(R.id.btn_continue);
        continueBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ShowAllBooks.class);
            startActivity(intent);
        });
    }
}