package de.ur.mi.android.booktrackerapp.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import de.ur.mi.android.booktrackerapp.R;
import de.ur.mi.android.booktrackerapp.SQLite.MyDatabaseHelper;

public class AddBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String title, author, cover, language, status;
    private int numPages;
    private int currPageAdd;
    private double rating;

    private TextView tvTitleAdd;
    private Spinner spinnerAdd;
    private EditText currPageInputAdd;
    private Button addButton;
    private LinearLayout linearLayoutCurrPageAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        linearLayoutCurrPageAdd = findViewById(R.id.Llayout_curr_page_add);
        tvTitleAdd =findViewById(R.id.tv_title_content_add);
        spinnerAdd = findViewById(R.id.spinner_add);
        currPageInputAdd = findViewById(R.id.editText_curr_page_add);
        addButton = findViewById(R.id.btn_add);

        getIntentData();
        initSpinner();

        addButton.setOnClickListener(view -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(AddBookActivity.this);
            myDB.addData(title, author, cover, rating, numPages, language, status, currPageAdd);
        });
    }


    private int getValueFromEditText(EditText editText) {
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
            Number value = format.parse(editText.getText().toString());
            if (value != null) {
                return value.intValue();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) 0d;
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter =
                ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdd.setAdapter(spinnerAdapter);
        spinnerAdd.setOnItemSelectedListener(this);
    }

    private void getIntentData() {
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            cover = getIntent().getStringExtra("cover");
            rating = getIntent().getDoubleExtra("rating", 0.0);
            numPages = getIntent().getIntExtra("numPages", 0);
            language = getIntent().getStringExtra("language");

            tvTitleAdd.setText(title);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        status = parent.getItemAtPosition(position).toString();

        TextView tvStatus = ((TextView) parent.getChildAt(0));
        tvStatus.setTextColor(Color.parseColor("#8D4C2E"));

        Typeface typeface = getResources().getFont(R.font.poppins_medium);
        tvStatus.setTypeface(typeface);
        tvStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);

        switch (status) {
            case "To read":
                linearLayoutCurrPageAdd.setVisibility(View.GONE);
                currPageAdd = 0;
                break;
            case "Reading":
                linearLayoutCurrPageAdd.setVisibility(View.VISIBLE);
                currPageAdd = getValueFromEditText(currPageInputAdd);
                break;
            case "Read":
                linearLayoutCurrPageAdd.setVisibility(View.GONE);
                currPageAdd = numPages;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}