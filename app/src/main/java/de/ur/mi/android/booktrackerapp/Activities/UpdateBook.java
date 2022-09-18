package de.ur.mi.android.booktrackerapp.Activities;

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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import de.ur.mi.android.booktrackerapp.R;
import de.ur.mi.android.booktrackerapp.SQLite.MyDatabaseHelper;

public class UpdateBook extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int id;
    private String title;
    private String statusUpdate;
    private String noteUpdate;
    private int  currPageUpdate, numPages;

    private TextView tvTitleBookUpdate, tvStatusUpdate;
    private Spinner spinnerUpdate;
    private LinearLayout linearLayoutCurrPageUpdate;
    private EditText currPageInputUpdate;
    private EditText noteInputUpdate;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        getAndSetData();
        initSpinner();

        updateBtn.setOnClickListener(view -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateBook.this);

            statusUpdate = tvStatusUpdate.getText().toString();
            currPageUpdate = getValueFromEditText(currPageInputUpdate);
            noteUpdate = noteInputUpdate.getText().toString();

            myDB.updateData(id, statusUpdate, currPageUpdate, noteUpdate);
        });
    }

    private void getAndSetData() {
        tvTitleBookUpdate = findViewById(R.id.tv_title_content_update);
        spinnerUpdate = findViewById(R.id.spinner_update);
        linearLayoutCurrPageUpdate = findViewById(R.id.Llayout_curr_page_update);
        currPageInputUpdate = findViewById(R.id.tv_num_curr_page_update);
        noteInputUpdate = findViewById(R.id.editText_note_content);
        updateBtn = findViewById(R.id.btn_update_book);

        id = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");
        numPages = getIntent().getIntExtra("numPages", 0);
        statusUpdate = getIntent().getStringExtra("status");
        currPageUpdate = getIntent().getIntExtra("currPage", 0);

        tvTitleBookUpdate.setText(title);
        noteUpdate = noteInputUpdate.getText().toString();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter =
                ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUpdate.setAdapter(spinnerAdapter);
        spinnerUpdate.setOnItemSelectedListener(this);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        statusUpdate = parent.getItemAtPosition(position).toString();

        tvStatusUpdate = (TextView) parent.getChildAt(0);
        tvStatusUpdate.setTextColor(Color.parseColor("#8D4C2E"));

        Typeface typeface = getResources().getFont(R.font.poppins_medium);
        tvStatusUpdate.setTypeface(typeface);
        tvStatusUpdate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);


        switch (statusUpdate) {
            case "To read":
                linearLayoutCurrPageUpdate.setVisibility(View.GONE);
                currPageUpdate = 0;
                break;
            case "Reading":
                linearLayoutCurrPageUpdate.setVisibility(View.VISIBLE);
                currPageUpdate = getValueFromEditText(currPageInputUpdate);
                break;
            case "Read":
                linearLayoutCurrPageUpdate.setVisibility(View.GONE);
                currPageUpdate = numPages;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
}