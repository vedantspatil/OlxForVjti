package com.example.vedan.olxforvjti;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.support.v7.widget.AlertDialogLayout;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private recyclerviewadapter_homescreen recyclerview_filter;
    private CardView cardView;
    private static boolean decide = false;
    private DatabaseReference databaseReference, FilterQuery;
    private Query firebaseSearchQuery;
    private String caller = "Home";;
    private int count;
    private ImageView nav_head;
    private TextView nav_email;
    private List<Allbooks> books = new ArrayList<>();
    private List<Allbooks> books1 = new ArrayList<>();
    private TextView noresults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //setting navigation bar header {null point}
       // nav_head = findViewById(R.id.imageView_navprifile);
       // nav_email = findViewById(R.id.textView_navemail);
        noresults = findViewById(R.id.noresultsfound);
        cardView = findViewById(R.id.cardview_books);
        recyclerView = findViewById(R.id.recyclerview_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("All Books");
        FilterQuery = FirebaseDatabase.getInstance().getReference().child("All Books");
        firebaseSearchQuery = databaseReference.orderByChild("book_Name");
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        //find prifile image and email id using query and set on navigation bar when requires , however currently its throwing null point exception
      /*  Query findImage = FirebaseDatabase.getInstance().getReference().child("User Images").child(firebaseAuth.getCurrentUser().getUid()).child("Profile Picture");
        Query findEmail = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Personal Info").child("email");
        findImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String link = dataSnapshot.getValue(String.class);
                if(!link.equals("null") | !link.equals(null)){
                    Picasso.get().load(link).fit().centerCrop().into(nav_head);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });
        findEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mail = dataSnapshot.getValue(String.class).trim();
               nav_email.setText(mail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(HomeActivity.this,NewBookActivity.class));
            }
        });

        //for Filtering results
        Intent intent = getIntent();
        boolean hasExtra = intent.hasExtra("Parent");

        //Unfunctional Filter:
        if (hasExtra) {
            final String bookname = intent.getExtras().getString("BookName").trim();
            final String price = intent.getExtras().getString("Price").trim();
            final String sem = intent.getExtras().getString("Sem").trim();
            final String author = intent.getExtras().getString("Author").trim();
            filter_details(bookname, price, sem, author);
        }

    }
    private void filter_details ( final String bookname, final String price,final String sem, final String author){
        if (bookname.isEmpty())
            if (price.equals("2000"))
                if (sem.isEmpty())
                    if (author.isEmpty()) { ;//no filter
                    } else {
            //Auhtor name provided
                        FilterQuery.orderByChild("book_Author").startAt(author).endAt(author+"\uf88f").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                                        Allbooks allbooks = childSnapshot.getValue(Allbooks.class);
                                        books.add(allbooks);

                                }
                                if(books.size() == 0 ){
                                    noresults.setText("No Book Found!");
                                }
                                recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                recyclerView.setAdapter(h1);
                                decide = true;

                            }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                else {
                        if (author.isEmpty()) {
                            FilterQuery.orderByChild("book_Semester").equalTo(sem).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks allbooks = d.getValue(Allbooks.class);
                                        books.add(allbooks);
                                    }
                                    if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else {
                            FilterQuery.orderByChild("book_Semester").equalTo(sem).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks allbooks = d.getValue(Allbooks.class);
                                        Log.d("bookauthor",allbooks.getBook_Author());
                                        if (allbooks.getBook_Author().equalsIgnoreCase(author)) {
                                            books.add(allbooks);
                                        }
                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }
            else {
                    if (sem.isEmpty())
                        if (author.isEmpty()) {
                            FilterQuery.orderByChild("book_Price").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks allbooks = d.getValue(Allbooks.class);
                                        if(Integer.parseInt(allbooks.getBook_Price()) <= Integer.parseInt(price)){
                                            books.add(allbooks);
                                            }

                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                        else {
                            FilterQuery.orderByChild("book_Author").startAt(author).endAt(author+"\uf88f").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks allbooks = d.getValue(Allbooks.class);
                                        if (Integer.parseInt(allbooks.getBook_Price()) <= Integer.parseInt(price)) {
                                            books.add(allbooks);
                                            }
                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    else {
                        if (author.isEmpty()) {
                            FilterQuery.orderByChild("book_Semester").equalTo(sem).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks allbooks = d.getValue(Allbooks.class);
                                        if (Integer.parseInt(allbooks.getBook_Price()) <= Integer.parseInt(price)) {
                                            books.add(allbooks);
                                        }
                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else {
                            FilterQuery.orderByChild("book_Semester").equalTo(sem).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks allbooks = d.getValue(Allbooks.class);
                                        if (allbooks.getBook_Author().equalsIgnoreCase(author) & Integer.parseInt(allbooks.getBook_Price()) <= Integer.parseInt(price)){
                                            books.add(allbooks);
                                        }
                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                }
        else {
            if (price.equals("2000"))
                if (sem.isEmpty())
                    if (author.isEmpty()) {
                            FilterQuery.orderByChild("book_Name").startAt(bookname).endAt(bookname+"\uf88f").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks book = d.getValue(Allbooks.class);
                                        books.add(book);
                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    else {

                            FilterQuery.orderByChild("book_Name").startAt(bookname).endAt(bookname+"\uf88f").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks allbooks = d.getValue(Allbooks.class);
                                        if (allbooks.getBook_Author().equalsIgnoreCase(author)) {
                                            books.add(allbooks);
                                        }
                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                else {
                    if (author.isEmpty()) {
                            FilterQuery.orderByChild("book_Semester").equalTo(sem).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks book = d.getValue(Allbooks.class);
                                        if (book.getBook_Name().equalsIgnoreCase(bookname)) {
                                            books.add(book);
                                        }
                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    else {
                            FilterQuery.orderByChild("book_Semester").equalTo(sem).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks book = d.getValue(Allbooks.class);
                                        if (book.getBook_Name().equalsIgnoreCase(bookname) & book.getBook_Author().equalsIgnoreCase(author)) {
                                            books.add(book);

                                        }
                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }
            else {
                 if (sem.isEmpty())
                     if (author.isEmpty()) {
                            FilterQuery.orderByChild("book_Name").startAt(bookname).endAt(bookname+"\uf88f").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks allbooks = d.getValue(Allbooks.class);
                                        if (Integer.parseInt(allbooks.getBook_Price()) <= Integer.parseInt(price)) {
                                            books.add(allbooks);
                                        }
                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                     else {
                            FilterQuery.orderByChild("book_Author").startAt(bookname).endAt(bookname+"\uf88f").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks book = d.getValue(Allbooks.class);
                                        if (Integer.parseInt(book.getBook_Price()) <= Integer.parseInt(price) & book.getBook_Name().equalsIgnoreCase(bookname)) {
                                            books.add(book);
                                        }
                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                 else{
                     if (author.isEmpty()) {
                            FilterQuery.orderByChild("book_Semester").equalTo(sem).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks allbooks = d.getValue(Allbooks.class);
                                        if (allbooks.getBook_Name().equalsIgnoreCase(bookname) & Integer.parseInt(allbooks.getBook_Price()) <= Integer.parseInt(price)) {
                                            books.add(allbooks);

                                        }
                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");

                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                     else {
                            FilterQuery.orderByChild("book_Semester").equalTo(sem).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        Allbooks allbooks = d.getValue(Allbooks.class);
                                        if (allbooks.getBook_Author().equalsIgnoreCase(author) & Integer.parseInt(allbooks.getBook_Price()) <= Integer.parseInt(price) & allbooks.getBook_Name().equalsIgnoreCase(bookname)) {
                                            books.add(allbooks);
                                        }
                                    } if(books.size() == 0 ){
                                        noresults.setText("No Book Found!");
                                    }
                                    recyclerviewadapter_homescreen h1 = new recyclerviewadapter_homescreen(HomeActivity.this , books);
                                    recyclerView.setAdapter(h1);
                                    decide = true;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                }


            }

        }
    @Override
    protected void onStart () {
             decide = false;
             noresults.setText(null);
             super.onStart();
             FirebaseRecyclerAdapter<Allbooks, recyclerviewadapter_homescreen.MyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Allbooks, recyclerviewadapter_homescreen.MyViewHolder>(
                    Allbooks.class,
                    R.layout.cardview_books,
                    recyclerviewadapter_homescreen.MyViewHolder.class,
                    firebaseSearchQuery

            ) {
                @Override
                protected void populateViewHolder(recyclerviewadapter_homescreen.MyViewHolder viewHolder, final Allbooks model, final int position) {

                    viewHolder.setBookname(model.getBook_Name());
                    viewHolder.setBooksem(model.getBook_Semester());
                    viewHolder.setBookprice(model.getBook_Price());
                    viewHolder.setBookimage(getApplicationContext(), model.getBook_Image());
                    viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(HomeActivity.this, AboutBookActivity.class);
                            intent.putExtra("Name", model.getBook_Name());
                            intent.putExtra("Desc", model.getBook_Description());
                            intent.putExtra("Author", model.getBook_Author());
                            intent.putExtra("Subject", model.getBook_Subject());
                            intent.putExtra("Edition", model.getBook_Edition());
                            intent.putExtra("Price", model.getBook_Price());
                            intent.putExtra("Condition", model.getBook_Condition());
                            intent.putExtra("Sem", model.getBook_Semester());
                            intent.putExtra("Imagelink", model.getBook_Image());
                            intent.putExtra("User UID", model.getUSER_UID());
                            startActivity(intent);
                        }
                    });
                }
            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);

        }
    @Override
    public void onBackPressed () {
        noresults.setText(null);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else { //Adding a alert dialog box
                if(decide){finish();}
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setMessage("Exit App?");

                    builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finishAffinity();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            }

        }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        //  Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.home, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Book Name(Case Sensitive)");
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; }
                @Override
                public boolean onQueryTextChange(String newText) {
                    noresults.setText(null);
                    Log.d("TAG", "Implemented");
                    firebaseSearchQuery = databaseReference.orderByChild("book_Name").startAt(newText).endAt(newText + "\uf8ff");
                    FirebaseRecyclerAdapter<Allbooks, recyclerviewadapter_homescreen.MyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Allbooks, recyclerviewadapter_homescreen.MyViewHolder>(
                            Allbooks.class,
                            R.layout.cardview_books,
                            recyclerviewadapter_homescreen.MyViewHolder.class,
                            firebaseSearchQuery

                    ) {
                        @Override
                        protected void populateViewHolder(recyclerviewadapter_homescreen.MyViewHolder viewHolder, final Allbooks model, final int position) {

                            viewHolder.setBookname(model.getBook_Name());
                            viewHolder.setBooksem(model.getBook_Semester());
                            viewHolder.setBookprice(model.getBook_Price());
                            viewHolder.setBookimage(getApplicationContext(), model.getBook_Image());
                            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(HomeActivity.this, AboutBookActivity.class);
                                    intent.putExtra("Name", model.getBook_Name());
                                    intent.putExtra("Desc", model.getBook_Description());
                                    intent.putExtra("Author", model.getBook_Author());
                                    intent.putExtra("Subject", model.getBook_Subject());
                                    intent.putExtra("Edition", model.getBook_Edition());
                                    intent.putExtra("Price", model.getBook_Price());
                                    intent.putExtra("Condition", model.getBook_Condition());
                                    intent.putExtra("Sem", model.getBook_Semester());
                                    intent.putExtra("Imagelink", model.getBook_Image());
                                    intent.putExtra("User UID", model.getUSER_UID());
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    recyclerView.setAdapter(firebaseRecyclerAdapter);
                    return true;
                }
            });
            return true;
        }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){


        noresults.setText(null);

        int id = item.getItemId();

           //noinspection SimplifiableIfStatement
        if (id == R.id.action_faq) {
            startActivity(new Intent(HomeActivity.this, FaqActivity.class));
        } else {
                if (id == R.id.action_filter) {
                    startActivity(new Intent(HomeActivity.this, FilterActivity.class));

                }

            }

            return super.onOptionsItemSelected(item);
        }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected (MenuItem item){
        // Handle navigation view item clicks here.
        noresults.setText(null);
        int id = item.getItemId();
        progressDialog.setMessage("Logging Out");
        Intent intent;
            if (id == R.id.nav_profile) {
                // Handle the camera action
                progressDialog.dismiss();
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_MyBooks) {
                progressDialog.dismiss();
                intent = new Intent(this, MybooksActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_addbook) {
                progressDialog.dismiss();
                intent = new Intent(this, NewBookActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_logout) {
                progressDialog.show();
                firebaseAuth.signOut();
                finish();
                progressDialog.dismiss();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_share) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("Text/plain");
                String sub = "Vj Books";
                String body = "https://drive.google.com/open?id=1Tee5XGYHVPSeVL6xXpmSq1XVpdhdkAM3";
                i.putExtra(Intent.EXTRA_SUBJECT, sub);
                i.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(i, "Share Using.."));

            } else if (id == R.id.nav_info) {

                startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }


    }
