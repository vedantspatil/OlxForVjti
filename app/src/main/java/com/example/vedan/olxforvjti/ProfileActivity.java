package com.example.vedan.olxforvjti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private ImageView profilepic;
    private TextView profilename, profileemail,profilephone,profilebranch,profileyear;
    private Button profileeditButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;
    private StorageReference filepath;
    private String imagelink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        instantiate_profile();

        firebaseAuth =FirebaseAuth.getInstance();
        firebaseDatabase =FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        final DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        final DatabaseReference databaseReference1 = firebaseDatabase.getReference("User Images");



        profileeditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
            }
        });

        databaseReference.child(firebaseAuth.getUid()).child("Personal Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Registration_details registration_details = dataSnapshot.getValue(Registration_details.class);
                profilename.setText(registration_details.getName());
                profileemail.setText(registration_details.getEmail());
                profilephone.setText(registration_details.getPhone());
                profilebranch.setText(registration_details.getBranch()+" Engineering");
                profileyear.setText(registration_details.getYear()+" Year");

              }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(ProfileActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseReference1.child(firebaseAuth.getUid()).child("Profile Picture").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String downloadlink = dataSnapshot.getValue(String.class);
                if(!downloadlink.equals("null"))Picasso.get().load(downloadlink).fit().centerCrop().into(profilepic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)finish();
        return super.onOptionsItemSelected(item);
    }


    private void instantiate_profile(){
        profilepic = findViewById(R.id.profile_userimage);
        profilebranch = findViewById(R.id.profile_branchview);
        profilename = findViewById(R.id.profile_name);
        profileemail = findViewById(R.id.profile_emailview);
        profilephone = findViewById(R.id.profile_phoneview);
        profileyear = findViewById(R.id.profile_yearview);
        profileeditButton = findViewById(R.id.profile_editButton);
    }
}
