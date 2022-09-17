package de.ur.mi.android.booktrackerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.ur.mi.android.booktrackerapp.R;

public class ShowDetailBook extends AppCompatActivity {

    private String title, author, coverLink, language, status, note;
    private int numPages, currPage;
    private double rating;

    private TextView tvTitleBookInfos, tvAuthorBookInfos;
    private ImageView ivCoverBookInfos;
    private TextView tvNumRating, tvNumPages, tvLang, tvCurrStatus;
    private Button btnUpdate, btnDelete, btnLaunchMap;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_book);

        getAndSetData();

        btnLaunchMap.setOnClickListener(view -> launchGoogleMap());
        btnUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(ShowDetailBook.this, UpdateBookActivity.class);

            intent.putExtra("title", title);
            intent.putExtra("numPages", numPages);
            intent.putExtra("status", status);
            intent.putExtra("currPage", currPage);

            startActivity(intent);
        });

    }

    private void getAndSetData() {
        ivCoverBookInfos = findViewById(R.id.imageView_cover_book_infos);
        seekBar = findViewById(R.id.seekBar);

        tvTitleBookInfos = findViewById(R.id.tv_title_book_infos);
        tvAuthorBookInfos = findViewById(R.id.tv_author_book_infos);
        tvNumRating = findViewById(R.id.num_rating);
        tvNumPages = findViewById(R.id.num_pages);
        tvLang = findViewById(R.id.lang_content);
        tvCurrStatus = findViewById(R.id.tv_curr_status_book_infos);

        btnLaunchMap = findViewById(R.id.btn_launch_map);
        btnUpdate = findViewById(R.id.btn_update_book_infos);
        btnDelete = findViewById(R.id.btn_delete);

        getIntentData();
        setUpData();
        setUpSeekBar();

    }

    private void getIntentData() {
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
    }

    private void setUpSeekBar() {
        seekBar.setMax(100);
        if (numPages!=0){
            int progress = (currPage / numPages) * 100;
            seekBar.setProgress(progress);
        } else {
            return;
        }

    }

    private void launchGoogleMap() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:49.013432,12.101624"));
        Intent chooser = Intent.createChooser(intent, "Launch Map");
        startActivity(chooser);
    }
}