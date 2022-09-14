package de.ur.mi.android.booktrackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchBook extends AppCompatActivity {

//    private final String url = "https://www.googleapis.com/books/v1/volumes?q=";
//    private final String appid = "8137adceb3a078f5ae6ed79708493712";

    private RecyclerView recyclerViewSearchBook;
    private ImageView buttonSearch;
    private ProgressBar progressBar;
    private EditText bookInput;

    private ArrayList<BookItemModel> bookItemsList;
    private ArrayList<String> titles, authors, covers;
    private BookTrackerAdapter adapter;
    private BookItemModel bookItemModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        progressBar = findViewById(R.id.progressBar);
        bookInput = findViewById(R.id.editText_search_book);
        buttonSearch = findViewById(R.id.search_btn);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                getBookInfos(bookInput.getText().toString());
            }
        });
    }

    private void getBookInfos(String query) {
        bookItemsList = new ArrayList<>();

        String tempUrl = "";
        query = bookInput.getText().toString().trim();

        if (query.equals("")){
            Toast.makeText(this, "City field can not be empty!", Toast.LENGTH_SHORT).show();
        } else {
            tempUrl = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray itemsArray = jsonResponse.getJSONArray("items");

                        for (int i = 0; i < itemsArray.length(); i++) {
                            JSONObject itemsObj = itemsArray.getJSONObject(i);

                            JSONObject jsonObjVolume = itemsObj.getJSONObject("volumeInfo");

                            String title = jsonObjVolume.optString("title");
                            JSONArray authorsArray = jsonObjVolume.getJSONArray("authors");
                            String author = (String) authorsArray.get(0);

                            JSONObject imageLinks = jsonObjVolume.optJSONObject("imageLinks");
                            String cover = imageLinks.optString("thumbnail");

                            float rating = (float) jsonObjVolume.getDouble("averageRating");
                            int numPages = jsonObjVolume.optInt("pageCount");
                            String language = jsonObjVolume.optString("language");
//                            ArrayList<String> authorsArrayList = new ArrayList<>();
//                            if (authorsArray.length() != 0) {
//                                for (int j = 0; j < authorsArray.length(); j++) {
//                                    authorsArrayList.add(authorsArray.optString(i));
//                                }
//                            }
                        bookItemModel = new BookItemModel(title, author, cover, rating, numPages, language);
                        bookItemsList.add(bookItemModel);
                        adapter = new BookTrackerAdapter(this, bookItemsList);

                            LinearLayoutManager linearLayoutManager =
                                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                            recyclerViewSearchBook = findViewById(R.id.recyclerView_search_book);
                            recyclerViewSearchBook.setLayoutManager(linearLayoutManager);
                            recyclerViewSearchBook.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}