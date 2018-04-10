package com.example.vedan.olxforvjti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class FilterActivity extends AppCompatActivity {

    EditText bookname,author,sem;
    String book_name,author_name,semester,price;
    Button filter;
    SeekBar seekBar;
    int progress = 2000;
    TextView pricerange;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        initialize_view();

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        progressDialog = new ProgressDialog(this);
        seekBar.setMax(2000);
        seekBar.setProgress(progress);
        pricerange.setText("Maximum Price Range: ₹" + progress);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress_this, boolean fromUser) {
                progress = progress_this;
                pricerange.setText("Maximum Price Range: ₹" + progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pricerange.setText("Maximum Price Range: " + progress);

            }
        });


        progressDialog.setMessage("Filtering Results");
        filter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressDialog.show();
                book_name = bookname.getText().toString().trim();
                author_name = author.getText().toString().trim();
                semester = sem.getText().toString().trim();
                price = String.valueOf(progress).trim();
                if(isValid(semester)){
                    Intent i = new Intent(FilterActivity.this, HomeActivity.class);
                    i.putExtra("Price", price);
                    i.putExtra("BookName", book_name);
                    i.putExtra("Author", author_name);
                    i.putExtra("Sem", semester);
                    i.putExtra("Parent", "FilterActivity");
                    startActivity(i);
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(FilterActivity.this, "Invalid Semester !", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private boolean isValid(String semester) {
        if(semester.isEmpty()|semester.equals("I")| semester.equals("II")| semester.equals("III")| semester.equals("IV")| semester.equals("V")| semester.equals("VI")| semester.equals("VII")| semester.equals("VIII"))
            return true;
        if(semester.equals("1")){
            this.semester = "I";
            return true;
        }
        if(semester.equals("2")){
            this.semester = "II";
            return true;
        }
        if(semester.equals("3")){
            this.semester = "III";
            return true;
        }
        if(semester.equals("4")){
            this.semester = "IV";
            return true;
        }
        if(semester.equals("5")){
            this.semester = "V";
            return true;
        }
        if(semester.equals("6")){
            this.semester = "VI";
            return true;
        }
        if(semester.equals("7")){
            this.semester = "VII";
            return true;
        }
        if(semester.equals("8")){
            this.semester = "VIII";
            return true;
        }

        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)finish();
        return super.onOptionsItemSelected(item);
    }
    private void initialize_view(){
        bookname = findViewById(R.id.filter_bookname);
        pricerange = findViewById(R.id.filter_priceview);
        author = findViewById(R.id.filter_authorname);
        seekBar = findViewById(R.id.filter_seekbar);
        sem = findViewById(R.id.filter_sem);
        pricerange = findViewById(R.id.filter_priceview);
        filter = findViewById(R.id.filter_button);
    }
}
