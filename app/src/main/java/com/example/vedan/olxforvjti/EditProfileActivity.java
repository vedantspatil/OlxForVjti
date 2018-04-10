package com.example.vedan.olxforvjti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG ="TAG";
    private int didimagechange = 0;
    private ImageView edit_image;
    private EditText edit_name, edit_email, edit_phone;
    private Spinner edit_branch, edit_year;
    private Button edit_save,edit_changepass;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference,filepath;
    private FirebaseDatabase firebaseDatabase;
    private Uri imageholder=null,downloadlink;
    private static final int GALLARY_REQUEST_CODE = 2;
    private String image_dbname,name,email,phone,branch,year,password ,branch_preset, year_preset , email_preset,imagelink_present,imagename;
    private ProgressDialog progressDialog;
    private int branchPosition,yearPosition;
    ArrayAdapter<CharSequence> adapter_branch , adapter_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initialize_view();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //spinner
        //set adapter and intialize spinner

        adapter_branch = ArrayAdapter.createFromResource(EditProfileActivity.this , R.array.branches , android.R.layout.simple_spinner_item);
        adapter_year = ArrayAdapter.createFromResource(EditProfileActivity.this, R.array.Year , android.R.layout.simple_spinner_item);
        adapter_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        edit_branch.setAdapter(adapter_branch);
        edit_year.setAdapter(adapter_year);

        //now get spinner click listener and set the values of branch and year
        Initialize_spinner();


        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progressDialog = new ProgressDialog(this);
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Personal Info");
        final DatabaseReference databaseReference1 = firebaseDatabase.getReference("User Images").child(firebaseAuth.getUid()).child("Profile Picture");
        filepath = storageReference.child("Photos").child(firebaseAuth.getUid()).child("Profile Picture");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Registration_details registration_details = dataSnapshot.getValue(Registration_details.class);
                edit_name.setText(registration_details.getName());
                edit_email.setText(registration_details.getEmail());
                edit_phone.setText(registration_details.getPhone());
                email_preset = registration_details.getEmail().trim();
                year_preset = registration_details.getYear().trim();
                branch_preset = registration_details.getBranch().trim();
                branchPosition = adapter_branch.getPosition(branch_preset.trim());
                yearPosition = adapter_year.getPosition(year_preset.trim());
                edit_branch.setSelection(branchPosition);
                edit_year.setSelection(yearPosition);
                password = registration_details.getPassword().trim();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(EditProfileActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String downloadlink = dataSnapshot.getValue(String.class);
                imagelink_present = downloadlink;
                if(!downloadlink.equals("null"))Picasso.get().load(downloadlink).fit().centerCrop().into(edit_image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });





        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image = new Intent(Intent.ACTION_PICK);
                image.setType("image/*");
                startActivityForResult(image,GALLARY_REQUEST_CODE);
            }
        });

        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Updating profile");
                progressDialog.show();
                if(validate()){
                    name = edit_name.getText().toString();
                    email = edit_email.getText().toString();
                    phone = edit_phone.getText().toString();
                    Registration_details registration_details = new Registration_details(name,email,password,phone,branch,year);
                    databaseReference.setValue(registration_details);

                    if(didimagechange != 0) {
                        if (imageholder != null) {
                            filepath = filepath.child(imageholder.getLastPathSegment());
                            filepath.putFile(imageholder).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    downloadlink = taskSnapshot.getDownloadUrl();
                                    imagelink_present = downloadlink.toString().trim();
                                    databaseReference1.setValue(imagelink_present);
                                }
                            });

                        } else {
                            databaseReference.child(firebaseAuth.getUid()).child("Profile Picture").setValue("null");
                        }
                    }
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    //set measures when verification in play
                    if (!email_preset.equals(email)){
                        progressDialog.dismiss();
                        firebaseUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Log.d(TAG,"email changed");
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfileActivity.this, "Updation Failed !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }

                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();

                }
                finish();
            }
        });


    } @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)finish();
        return super.onOptionsItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLARY_REQUEST_CODE && resultCode == RESULT_OK) {
            imageholder = data.getData();
            edit_image.setImageURI(imageholder);
            didimagechange = 1;
            image_dbname = imageholder.getLastPathSegment();

        }
    }
    private void Initialize_spinner(){

          edit_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                branch = parent.getItemAtPosition(branchPosition).toString();


            }
        });
        edit_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                year = parent.getItemAtPosition(yearPosition).toString();
            }
        });


    }
    private void initialize_view(){

        edit_image = findViewById(R.id.edit_profileimage);
        edit_name = findViewById(R.id.edit_profilename);
        edit_email = findViewById(R.id.edit_profileemail);
        edit_phone = findViewById(R.id.edit_profilephone);
        edit_branch = findViewById(R.id.edit_spinbranch);
        edit_year = findViewById(R.id.edit_spinyear);
        edit_save = findViewById(R.id.edit_save);
        edit_changepass = findViewById(R.id.edit_changepass);

    }
    private boolean validate(){
        name = edit_name.getText().toString();
        email = edit_email.getText().toString();
        phone = edit_phone.getText().toString();

        if(name.isEmpty() | email.isEmpty() | phone.isEmpty()){
            return false;
        }else {return true;
        }
    }

}
