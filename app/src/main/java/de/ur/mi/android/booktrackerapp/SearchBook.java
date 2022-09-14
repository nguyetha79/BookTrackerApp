package de.ur.mi.android.booktrackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SearchBook extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView buttonSearch;
    private ProgressBar progressBar;
    private EditText editTextSearchBook;

    private MyDatabaseHelper myDB;
    private ArrayList<BookItemModel> bookItemModels;
    private ArrayList<String> titles, authors, covers;
    private BookTrackerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        progressBar = findViewById(R.id.progressBar);
        editTextSearchBook = findViewById(R.id.editText_search_book);
        buttonSearch = findViewById(R.id.search_btn);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                if (editTextSearchBook.getText().toString().isEmpty()) {
                    editTextSearchBook.setError("Please enter search query");
                    return;
                }

                getBooksInfo(editTextSearchBook.getText().toString());
            }
        });
    }
}