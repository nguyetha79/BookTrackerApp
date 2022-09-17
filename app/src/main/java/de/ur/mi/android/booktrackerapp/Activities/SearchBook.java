package de.ur.mi.android.booktrackerapp.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.ur.mi.android.booktrackerapp.Adapter.SearchBookAdapter;
import de.ur.mi.android.booktrackerapp.Model.BookItemModel;
import de.ur.mi.android.booktrackerapp.R;

public class SearchBook extends AppCompatActivity {

    private RecyclerView recyclerViewSearchBook;
    private ImageView buttonSearch;
    private ProgressBar progressBar;
    private EditText bookInput;

    private ArrayList<BookItemModel> bookItemsList;
    private SearchBookAdapter adapter;
    private BookItemModel bookItemModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        progressBar = findViewById(R.id.progressBar);
        bookInput = findViewById(R.id.editText_search_book);
        buttonSearch = findViewById(R.id.search_btn);

        initButtonSearch();
    }

    private void initButtonSearch() {
        buttonSearch.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

            if (bookInput.getText().toString().isEmpty()) {
                bookInput.setError("Please enter search query");
                return;
            }
            getBookInfos(bookInput.getText().toString());
        });
    }

    private void getBookInfos(String query) {
        bookItemsList = new ArrayList<>();

        String tempUrl = "";
        query = bookInput.getText().toString().trim();

        if (query.equals("")){
            Toast.makeText(this, "This field can not be empty!", Toast.LENGTH_SHORT).show();
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

                            String title = jsonObjVolume.getString("title");

                            String author = "";
                            if (!jsonObjVolume.has("authors")){
                                author += "No author";
                            } else {
                                author = getAuthor(jsonObjVolume);
                            }

                            JSONObject imageLinks;
                            String cover = "";
                            if (!jsonObjVolume.has("imageLinks")){
                                cover += "No cover";
                            } else {
                                imageLinks = jsonObjVolume.getJSONObject("imageLinks");
                                cover = imageLinks.getString("thumbnail");
                            }

                            Double rating;
                            int numPages;

                            rating = jsonObjVolume.has("averageRating")
                                   ? jsonObjVolume.getDouble("averageRating") : 0;

                            numPages = jsonObjVolume.has("pageCount")
                                    ? jsonObjVolume.getInt("pageCount") : 0;

                            String language = jsonObjVolume.getString("language");

                            bookItemModel = new BookItemModel(title, author, cover, rating, numPages, language);
                            bookItemsList.add(bookItemModel);

                            setUpAdapterSearchBook();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setUpAdapterSearchBook() {
        adapter = new SearchBookAdapter(this, bookItemsList);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);
        recyclerViewSearchBook = findViewById(R.id.recyclerView_search_book);
        recyclerViewSearchBook.setLayoutManager(linearLayoutManager);
        recyclerViewSearchBook.setAdapter(adapter);
    }

    private static String getAuthor(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray("authors");
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for (int i = 0; i < numAuthors; ++i) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }
}