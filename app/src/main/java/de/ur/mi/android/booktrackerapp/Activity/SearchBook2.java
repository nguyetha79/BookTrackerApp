package de.ur.mi.android.booktrackerapp.Activity;

import android.content.Intent;
import android.os.Bundle;
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

import de.ur.mi.android.booktrackerapp.Adapter.SearchBookAdapter2;
import de.ur.mi.android.booktrackerapp.Model.BookItemModel;
import de.ur.mi.android.booktrackerapp.R;

public class SearchBook2 extends AppCompatActivity {

    private RecyclerView recyclerViewSearchBook;
    private ImageView buttonSearch;
    private ProgressBar progressBar;
    private EditText bookInput;

    private ArrayList<BookItemModel> bookItemsList;
    private SearchBookAdapter2 adapter;
    private BookItemModel bookItemModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        progressBar = findViewById(R.id.progressBar);
        bookInput = findViewById(R.id.editText_search_book);
        buttonSearch = findViewById(R.id.search_btn);

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
                                JSONArray authorsArray = jsonObjVolume.getJSONArray("authors");
                                author = (String) authorsArray.get(0);
                            }

                            JSONObject imageLinks;
                            String cover = "";
                            if (!jsonObjVolume.has("imageLinks")){
                                cover += "No cover";
                            } else {
                                imageLinks = jsonObjVolume.getJSONObject("imageLinks");
                                cover = imageLinks.getString("thumbnail");
                            }

                            double rating;
                            int numPages;
                            if (!jsonObjVolume.has("averageRating")){
                                rating = 0;
                                numPages = 0;
                            } else {
                                rating = jsonObjVolume.getDouble("averageRating");
                                numPages = jsonObjVolume.getInt("pageCount");
                            }

                            String language = jsonObjVolume.getString("language");

                            bookItemModel = new BookItemModel(title, author, cover, rating, numPages, language);
                            bookItemsList.add(bookItemModel);
                            adapter = new SearchBookAdapter2(this, bookItemsList);

                            LinearLayoutManager linearLayoutManager =
                                    new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                            recyclerViewSearchBook = findViewById(R.id.recyclerView_search_book);
                            recyclerViewSearchBook.setLayoutManager(linearLayoutManager);
                            recyclerViewSearchBook.setAdapter(adapter);

                            adapter.setOnItemClickListener(pos -> {
                                Intent intent = new Intent(SearchBook2.this, AddBookActivity.class);

                                  intent.putExtra("title", bookItemsList.get(pos).getTitle());
                                  intent.putExtra("author",  bookItemsList.get(pos).getAuthor());
                                  intent.putExtra("cover",  bookItemsList.get(pos).getCover());
                                  intent.putExtra("numPages",  bookItemsList.get(pos).getNumPages());
                                  intent.putExtra("rating",  bookItemsList.get(pos).getRating());
                                  intent.putExtra("language",  bookItemsList.get(pos).getLanguage());
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//    private void getBookInfos(String query) {
//        // creating a new array list.
//        bookItemsList = new ArrayList<>();
//
//        // below line is use to initialize
//        // the variable for our request queue.
//        mRequestQueue = Volley.newRequestQueue(this);
//
//        // below line is use to clear cache this
//        // will be use when our data is being updated.
//        mRequestQueue.getCache().clear();
//
//        // below is the url for getting data from API in json format.
//        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
//
//        // below line we are  creating a new request queue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//
//        // below line is use to make json object request inside that we
//        // are passing url, get method and getting json object. .
//        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                response -> {
//            progressBar.setVisibility(View.GONE);
//            // inside on response method we are extracting all our json data.
//            try {
//                JSONArray itemsArray = response.getJSONArray("items");
//                for (int i = 0; i < itemsArray.length(); i++) {
//                    JSONObject itemsObj = itemsArray.getJSONObject(i);
//                    JSONObject jsonObjVolume = itemsObj.getJSONObject("volumeInfo");
//
//
//                    String title = jsonObjVolume.optString("title");
//                    JSONArray authorsArray = jsonObjVolume.getJSONArray("authors");
//                    String author = (String) authorsArray.get(0);
//
//                    JSONObject imageLinks = jsonObjVolume.getJSONObject("imageLinks");
//                    String cover = imageLinks.getString("thumbnail");
//
//                     double rating = jsonObjVolume.getDouble("ratingsCount");
//                    int numPages = jsonObjVolume.getInt("pageCount");
//                    String language = jsonObjVolume.getString("language");
//
//                    // after extracting all the data we are
//                    // saving this data in our modal class.
//                    BookItemModel bookItemModel = new BookItemModel(title, author, cover, rating, numPages, language);
//
//                    // below line is use to pass our modal
//                    // class in our array list.
//                    bookItemsList.add(bookItemModel);
//
//                    // below line is use to pass our
//                    // array list in adapter class.
//                    adapter = new BookTrackerAdapter(SearchBook.this, bookItemsList);
//
//                    // below line is use to add linear layout
//                    // manager for our recycler view.
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchBook.this, RecyclerView.VERTICAL, false);
//                    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_search_book);
//
//                    // in below line we are setting layout manager and
//                    // adapter to our recycler view.
//                    mRecyclerView.setLayoutManager(linearLayoutManager);
//                    mRecyclerView.setAdapter(adapter);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                // displaying a toast message when we get any error from API
//                Toast.makeText(this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // also displaying error message in toast.
//                Toast.makeText(SearchBook.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
//            }
//        });
//        // at last we are adding our json object
//        // request in our request queue.
//        queue.add(booksObjrequest);
//    }
}