package com.example.vedan.olxforvjti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ContactSellerActivity extends AppCompatActivity {
    private TextView name,email,branch,year,phone;
    private ImageView image;
    private String downloadlink = "null",uid;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReference_image;
    private ProgressDialog progressDialog;
    private ImageButton hangouts,dailer;
    private Uri uri;
    String dail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_seller);
        name = findViewById(R.id.seller_name);
        email = findViewById(R.id.seller_emailview);
        branch = findViewById(R.id.seller_branchview);
        year = findViewById(R.id.seller_yearview);
        phone = findViewById(R.id.seller_phoneview);
        image = findViewById(R.id.seller_userimage);
        hangouts = findViewById(R.id.hangouts);
        dailer = findViewById(R.id.dailer);


        hangouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address",dail);
                smsIntent.putExtra("sms_body","Hey");
                startActivity(smsIntent);
            }
        });

        dailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dailintent = new Intent(Intent.ACTION_DIAL);
                dailintent.setData(Uri.parse("tel:" +dail));
                startActivity(dailintent);
            }
        });

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent intent = getIntent();
        uid = intent.getExtras().getString("user uid").trim();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Personal Info");
        databaseReference_image = FirebaseDatabase.getInstance().getReference("User Images").child(uid).child("Profile Picture");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Registration_details registration_details = dataSnapshot.getValue(Registration_details.class);
                name.setText(registration_details.getName());
                email.setText(registration_details.getEmail());
                phone.setText(registration_details.getPhone());
                dail = registration_details.getPhone().trim();
                branch.setText(registration_details.getBranch()+" Engineering");
                year.setText(registration_details.getYear()+" Year");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(ContactSellerActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });

        databaseReference_image.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                downloadlink = dataSnapshot.getValue(String.class);
                if(!downloadlink.equals("null")) Picasso.get().load(downloadlink).fit().centerCrop().into(image);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ContactSellerActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)finish();
        return super.onOptionsItemSelected(item);
    }


}
