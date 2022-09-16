package de.ur.mi.android.booktrackerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import de.ur.mi.android.booktrackerapp.Adapter.ShowAllBooksAdapter;
import de.ur.mi.android.booktrackerapp.Model.BookItemModel;
import de.ur.mi.android.booktrackerapp.R;
import de.ur.mi.android.booktrackerapp.SQLite.MyDatabaseHelper;

public class ShowAllBooks extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private Button btnDetail;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MyDatabaseHelper myDB;
    private ArrayList<BookItemModel> bookItemsList;
    private ArrayList<String> ids, titles, authors, covers, ratings, numPages, languages, status, currPages, notes;
    private ShowAllBooksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_books);

        btnDetail = findViewById(R.id.btn_detail);
        recyclerView = findViewById(R.id.recyclerView_show_books);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_show_books);
        initFloatingActionButton();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            myDB = new MyDatabaseHelper(ShowAllBooks.this);
            initArrayList();
            setUpBookItemsList();
            setUpAdapterShowAllBooks();
            swipeRefreshLayout.setRefreshing(false);
        });

    }

    private void initFloatingActionButton() {
        floatingActionButton = findViewById(R.id.add_fab_btn);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(ShowAllBooks.this, SearchBook.class);
            startActivity(intent);
        });
    }

    private void initArrayList() {
        bookItemsList = new ArrayList<>();
        ids = new ArrayList<>();
        titles = new ArrayList<>();
        authors = new ArrayList<>();
        covers = new ArrayList<>();
        ratings = new ArrayList<>();
        numPages = new ArrayList<>();
        languages = new ArrayList<>();
        status = new ArrayList<>();
        currPages = new ArrayList<>();
        notes = new ArrayList<>();
    }

    private void setUpBookItemsList() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                ids.add(cursor.getString(0));
                titles.add(cursor.getString(1));
                authors.add(cursor.getString(2));
                covers.add(cursor.getString(3));
                ratings.add(cursor.getString(4));
                numPages.add(cursor.getString(5));
                languages.add(cursor.getString(6));
                status.add(cursor.getString(7));
                currPages.add(cursor.getString(8));
                notes.add(cursor.getString(9));
            }
        }

        for (int i = 0; i < titles.size() ; i++) {
            BookItemModel bookItem = new BookItemModel(
                    Integer.parseInt(ids.get(i)),
                    titles.get(i),
                    authors.get(i),
                    covers.get(i),
                    Double.parseDouble(ratings.get(i)),
                    Integer.parseInt(numPages.get(i)),
                    languages.get(i),
                    status.get(i),
                    Integer.parseInt(currPages.get(i)),
                    notes.get(i));
            bookItemsList.add(bookItem);
        }
    }

    private void setUpAdapterShowAllBooks() {
        int numCols = 2;
        adapter = new ShowAllBooksAdapter(ShowAllBooks.this, bookItemsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numCols));
    }


}