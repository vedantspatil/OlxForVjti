package com.example.vedan.olxforvjti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class FaqActivity extends AppCompatActivity {

    TextView q1,q2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        q1 = findViewById(R.id.ans1);
        q2 = findViewById(R.id.ans2);
        q1.setText("->Any Number\nUser can upload any number of books as there is no such restrictions as long as they are valid. Any kind of spamming will be removed.");
        q2.setText("-> Once a book is sold the seller will manually remove the book so no book that is no longer available will be available is shown to you.");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)finish();
        return super.onOptionsItemSelected(item);
    }



}
