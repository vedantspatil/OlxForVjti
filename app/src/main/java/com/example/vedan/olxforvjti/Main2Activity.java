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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Main2Activity extends AppCompatActivity {
    String signup_branch , signup_year ,name , email , password ,phone,imagelink = "null";
    private ImageView signup_image;
    final String TAG = "TAG";
    private Button bsignup;
    private EditText signup_name,signup_email,signup_password,signup_phone;
    private TextView signupToLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference,filepath;
    private Uri imageholder = null;
    FirebaseDatabase firebaseDatabase;
    private Uri downloadlink = null;
    DatabaseReference databaseReference;
    private Spinner spinner_branch;
    private Spinner spinner_year;
    private static final int GALLARY_REQUEST_CODE = 2;
    ArrayAdapter<CharSequence> adapter_branch , adapter_year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initialize_signupView();
        Initialize_spinner();



      /*  signup_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(signup_password.getText().length() < 6){
                    signup_password.setError("Password too short!");


                }
            }
        });*/

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        firebaseAuth =FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();






        signup_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image = new Intent(Intent.ACTION_PICK);
                image.setType("image/*");
                startActivityForResult(image,GALLARY_REQUEST_CODE);
            }
        });

        signupToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(Main2Activity.this , MainActivity.class));
            }
        });


        bsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Creating New Account");
                progressDialog.show();
                if (validate()){
                    //transfer data to database and login again
                   String email = signup_email.getText().toString().trim();
                   String password = signup_password.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful()){
                              sendUserData();
                              sendImageData();
                              progressDialog.dismiss();
                              Toast.makeText(Main2Activity.this,"Successful Registration" , Toast.LENGTH_LONG).show();
                              finish();
                              startActivity(new Intent(Main2Activity.this , MainActivity.class));
                          }else{
                              progressDialog.dismiss();
                              Toast.makeText(Main2Activity.this,"Registration Failed !" , Toast.LENGTH_LONG).show();
                          }
                       }
                   });

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(Main2Activity.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLARY_REQUEST_CODE && resultCode == RESULT_OK) {

            imageholder = data.getData();
            signup_image.setImageURI(imageholder);

        }
    }
    private void Initialize_spinner(){

        adapter_branch = ArrayAdapter.createFromResource(Main2Activity.this , R.array.branches , android.R.layout.simple_spinner_item);
        adapter_year = ArrayAdapter.createFromResource(Main2Activity.this, R.array.Year , android.R.layout.simple_spinner_item);
        adapter_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_branch.setAdapter(adapter_branch);
        spinner_year.setAdapter(adapter_year);
        spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                signup_branch = parent.getItemAtPosition(position).toString();
                Log.d(TAG , signup_branch);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                signup_branch = parent.getItemAtPosition(0).toString();
                Log.d(TAG , signup_branch);


            }
        });
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                signup_year = parent.getItemAtPosition(position).toString();
                Log.d(TAG , signup_year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                signup_year = parent.getItemAtPosition(0).toString();
                Log.d(TAG , signup_year);
            }
        });


    }
    private void initialize_signupView(){

      bsignup = findViewById(R.id.signup);
      signup_image = findViewById(R.id.signup_imageView);
      spinner_branch = findViewById(R.id.spinner_branch);
      spinner_year = findViewById(R.id.spinner_year);
      signup_name = findViewById(R.id.signup_name);
      signup_email = findViewById(R.id.signup_email);
      signup_password = findViewById(R.id.signup_password);
      signup_phone = findViewById(R.id.signup_phone);
      signupToLogin = findViewById(R.id.signupToLogin);
    }
    private boolean validate(){
        name = signup_name.getText().toString();
        password = signup_password.getText().toString();
        email = signup_email.getText().toString();
        phone = signup_phone.getText().toString();

        if(name.isEmpty() | password.isEmpty() | email.isEmpty() | phone.isEmpty()){
            return false;
        }else {return true;
        }
    }
    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        Registration_details registration_details = new Registration_details(name, email, password, phone, signup_branch, signup_year);
        databaseReference.child(firebaseAuth.getUid()).child("Personal Info").setValue(registration_details);
    }
    private void sendImageData(){

        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("User Images");
        if(imageholder != null) {
            filepath = storageReference.child("Photos").child(firebaseAuth.getUid()).child("Profile Picture").child(imageholder.getLastPathSegment());
            filepath.putFile(imageholder).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadlink = taskSnapshot.getDownloadUrl();
                    imagelink = downloadlink.toString().trim();
                    databaseReference.child(firebaseAuth.getUid()).child("Profile Picture").setValue(imagelink);
                }
            });

        }else{
            databaseReference.child(firebaseAuth.getUid()).child("Profile Picture").setValue("null");

        }

    }

}


