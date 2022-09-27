package de.ur.mi.android.booktrackerapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.Slider;
import com.squareup.picasso.Picasso;

import de.ur.mi.android.booktrackerapp.R;
import de.ur.mi.android.booktrackerapp.SQLite.MyDatabaseHelper;

public class ShowDetailBook extends AppCompatActivity {

    private String title, author, coverLink, language, status, note;
    private int id, numPages, currPage;
    private double rating, latitude, longtitude;
    private LocationRequest locationRequest;

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
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        btnLaunchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(ShowDetailBook.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED){

                        if (isGPSEnabled()) {

                            LocationServices.getFusedLocationProviderClient(ShowDetailBook.this)
                                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                                        @Override
                                        public void onLocationResult(@NonNull LocationResult locationResult) {
                                            super.onLocationResult(locationResult);

                                            LocationServices.getFusedLocationProviderClient(ShowDetailBook.this)
                                                    .removeLocationUpdates(this);

                                            if (locationResult != null && locationResult.getLocations().size() > 0){
                                                int index = locationResult.getLocations().size() -1;
                                                latitude = locationResult.getLocations().get(index).getLatitude();
                                                longtitude = locationResult.getLocations().get(index).getLongitude();

                                                launchGoogleMap();
                                            }
                                        }
                                    }, Looper.getMainLooper());
                        }else {
                            turnOnGPS();
                        }
                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }
            }
        });
    }

    private boolean isGPSEnabled(){
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }

    private void launchGoogleMap() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:"+ latitude +"," + longtitude));
        Intent chooser = Intent.createChooser(intent, "Launch Map");
        startActivity(chooser);
    }

    private void turnOnGPS() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result
                = LocationServices.getSettingsClient(getApplicationContext())
                                  .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {

            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                Toast.makeText(ShowDetailBook.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

            } catch (ApiException e) {

                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(ShowDetailBook.this, 2);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
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