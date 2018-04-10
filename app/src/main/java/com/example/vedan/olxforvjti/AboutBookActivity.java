package com.example.vedan.olxforvjti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AboutBookActivity extends AppCompatActivity {

    private TextView namebook,descbook,autherbook,subbook,edibook,pricebook,condbook,sembook;
    private Button userprofile;
    private ImageView imageView;
    private static String link = "https://firebasestorage.googleapis.com/v0/b/olxforvjti.appspot.com/o/Photos%2Fbookimagenotavailable.png?alt=media&token=58ed9e71-3018-4e92-9514-d14ef948b038";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_book);
        intialize_view();

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent intent = getIntent();
        String name_book = intent.getExtras().getString("Name");
        String desc_book = intent.getExtras().getString("Desc");
        String auth_book = intent.getExtras().getString("Author");
        String edi_book = intent.getExtras().getString("Edition");
        String sub_book = intent.getExtras().getString("Subject");
        String price_book = intent.getExtras().getString("Price");
        String cond_book = intent.getExtras().getString("Condition");
        String sem_book = intent.getExtras().getString("Sem");
        String image_book = intent.getExtras().getString("Imagelink").trim();
        final String uid = intent.getExtras().getString("User UID").trim();
        Log.d("TAKING USER ID",uid+"");


        //set view
        namebook.setText(name_book);
        autherbook.setText(auth_book);
        sembook.setText("Sem - "+sem_book);
        descbook.setText("Description - "+desc_book);
        subbook.setText("Subject - "+sub_book);
        edibook.setText("Edition - "+edi_book);
        pricebook.setText(price_book);
        condbook.setText("Condition - "+cond_book);
        if(!image_book.equals("null")) Picasso.get().load(image_book).fit().centerCrop().into(imageView);
        else {
            link.trim();
            Picasso.get().load(link).fit().centerCrop().into(imageView);
        }

        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //transfer to user profile page
                Intent intent = new Intent(AboutBookActivity.this,ContactSellerActivity.class);
                intent.putExtra("user uid",uid);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)finish();
        return super.onOptionsItemSelected(item);
    }


    public void intialize_view(){
        namebook = findViewById(R.id.name_book);
        autherbook = findViewById(R.id.author_book);
        descbook = findViewById(R.id.desc_book);
        subbook = findViewById(R.id.subject_book);
        edibook = findViewById(R.id.edition_book);
        pricebook = findViewById(R.id.price_book);
        condbook = findViewById(R.id.condition_book);
        sembook = findViewById(R.id.semester_book);
        imageView = findViewById(R.id.image_book);
        userprofile = findViewById(R.id.contact_seller);
    }

}
