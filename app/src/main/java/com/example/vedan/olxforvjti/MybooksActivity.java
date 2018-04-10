package com.example.vedan.olxforvjti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MybooksActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private CardView cardView;
    String key;
    DatabaseReference databaseReference,databaseReference_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybooks);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        cardView = findViewById(R.id.cardview_mybooks);
        recyclerView = findViewById(R.id.recyclerview_mybooks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid()).child("Books");
        databaseReference_image = FirebaseDatabase.getInstance().getReference("User Images").child(firebaseAuth.getUid()).child("Books");
        progressDialog = new ProgressDialog(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)finish();
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<book_details,recyclerviewadapter_mybooks.MyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<book_details, recyclerviewadapter_mybooks.MyViewHolder>(
                book_details.class,
                R.layout.cardview_mybooks,
                recyclerviewadapter_mybooks.MyViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final recyclerviewadapter_mybooks.MyViewHolder viewHolder, final book_details model, int position) {
                viewHolder.setBookname(model.getBook_Name());
                viewHolder.setBooksem(model.getBook_Semester());
                viewHolder.setBookprice(model.getBook_Price());
                viewHolder.setBookimage(getApplicationContext(),model.getImagelink());
                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MybooksActivity.this,EditBookActivity.class);
                        key = model.getPush_id();
                        Log.d("KEY",key);
                        intent.putExtra("Key",key);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}