package de.ur.mi.android.booktrackerapp.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.slider.Slider;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.ur.mi.android.booktrackerapp.R;
import de.ur.mi.android.booktrackerapp.SQLite.MyDatabaseHelper;

public class ShowDetailBook extends AppCompatActivity {

    private String title, author, coverLink, language, status, note;
    private int id, numPages, currPage;
    private double rating, latitude, longtitude;
    private LocationRequest locationRequest;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 1;

    private TextView tvTitleBookInfos, tvAuthorBookInfos, tvNoteContentBookInfos;
    private ImageView ivCoverBookInfos;
    private TextView tvNumRating, tvNumPages, tvLang, tvCurrStatus;
    private Button btnUpdate, btnDelete, btnLaunchMap;
    private Slider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_book);

        getAndSetData();

        initSlider();
        initBtnLaunchMap();
        initBtnUpdate();
        initBtnDelete();
    }

    private void getAndSetData() {
        ivCoverBookInfos = findViewById(R.id.imageView_cover_book_infos);
        slider = findViewById(R.id.slider);

        tvTitleBookInfos = findViewById(R.id.tv_title_book_infos);
        tvAuthorBookInfos = findViewById(R.id.tv_author_book_infos);
        tvNumRating = findViewById(R.id.num_rating);
        tvNumPages = findViewById(R.id.num_pages);
        tvLang = findViewById(R.id.lang_content);
        tvCurrStatus = findViewById(R.id.tv_curr_status_book_infos);
        tvNoteContentBookInfos = findViewById(R.id.tv_note_content_book_infos);

        btnLaunchMap = findViewById(R.id.btn_launch_map);
        btnUpdate = findViewById(R.id.btn_update_book_infos);
        btnDelete = findViewById(R.id.btn_delete);

        getIntentData();
        setUpData();
    }

    private void getIntentData() {

        id = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("author");
        coverLink = getIntent().getStringExtra("cover");
        rating = getIntent().getDoubleExtra("rating", 0.0);
        numPages = getIntent().getIntExtra("numPages", 0);
        language = getIntent().getStringExtra("language");
        status = getIntent().getStringExtra("status");
        currPage = getIntent().getIntExtra("currPage", 0);
        note = getIntent().getStringExtra("note");
    }

    private void setUpData() {
        tvTitleBookInfos.setText(title);
        tvAuthorBookInfos.setText(author);

        if (coverLink.equals("No cover")) {
            String uri = "@drawable/no_book_cover_available";
            int imageResource = ShowDetailBook.this.getResources()
                    .getIdentifier(uri, null, ShowDetailBook.this.getPackageName());
            Drawable res = ShowDetailBook.this.getResources().getDrawable(imageResource);
            ivCoverBookInfos.setImageDrawable(res);
        } else {
            Picasso.get().load(coverLink).into(ivCoverBookInfos);
        }

        String strRating = Double.toString(rating);
        tvNumRating.setText(strRating);

        String strNumPages = Integer.toString(numPages);
        tvNumPages.setText(strNumPages);

        tvLang.setText(language);
        tvCurrStatus.setText(status);

        tvNoteContentBookInfos.setText(note);

     }

    private void initSlider() {
        if (numPages!=0){
            int progress = (currPage / numPages) * 100;
            slider.setValue(progress);
        }
    }

    private void initBtnLaunchMap() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnLaunchMap.setOnClickListener(view -> getLastLocation());

    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(ShowDetailBook.this, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                latitude = addresses.get(0).getLatitude();
                                longtitude = addresses.get(0).getLongitude();
                                launchGoogleMap();
                                Log.d(String.valueOf(latitude), "getLastLocation");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(ShowDetailBook.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();

            } else {
                Toast.makeText(ShowDetailBook.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchGoogleMap() {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("geo:"+ latitude +"," + longtitude));
//        Intent chooser = Intent.createChooser(intent, "Launch Map");
//        startActivity(chooser);

        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longtitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    private void initBtnUpdate() {
        btnUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(ShowDetailBook.this, UpdateBook.class);

            intent.putExtra("title", title);
            intent.putExtra("numPages", numPages);
            intent.putExtra("status", status);
            intent.putExtra("currPage", currPage);

            startActivity(intent);
        });
    }

    private void initBtnDelete() {
        btnDelete.setOnClickListener(view -> {
            confirmDialog();
        });
    }

    private void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete this book ?");
        builder.setMessage("Are you sure that you want to delete " + title + " ?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(ShowDetailBook.this);
            myDB.deleteData(id);
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {

        });
        builder.create().show();
    }
}