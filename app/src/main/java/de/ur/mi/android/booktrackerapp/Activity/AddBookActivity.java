package de.ur.mi.android.booktrackerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private int currPage;
    private double rating;

    private TextView tvTitleAdd;
    private Spinner spinnerAdd;
    private EditText currPageInput;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        tvTitleAdd =findViewById(R.id.tv_title_content_add);
        spinnerAdd = findViewById(R.id.spinner_add);
        currPageInput = findViewById(R.id.editText_curr_page_add);
        addButton = findViewById(R.id.btn_add);

        getIntentData();
        initSpinner();
        currPage = getValueFromEditText(currPageInput);

        addButton.setOnClickListener(view -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(AddBookActivity.this);
            myDB.addData(title, author, cover, rating, numPages, language, status, currPage);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
            status = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), status, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}