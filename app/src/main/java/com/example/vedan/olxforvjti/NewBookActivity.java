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
import android.widget.AdapterViewAnimator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewBookActivity extends AppCompatActivity {

    boolean task1,task2;
    private EditText book_desc, book_name ,book_author,book_edi ,book_sub ,book_price;
    private  Spinner book_cond ,book_sem;
    private String bookname, bookdesc, bookauth,pushLink = "null", bookedi, bookprice,booksub, bookcond ,booksem,imagelink="null",userUID;
    private String key;
    private ImageView book_image;
    ArrayAdapter<CharSequence> cond_adapter , sem_adapter;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference,filepath;
    private FirebaseDatabase firebaseDatabase;
    private Uri imageholder = null,downloadlink = null;
    DatabaseReference databaseReference , databaseReference1,databaseReference3;
    private static final int GALLARY_REQUEST_CODE = 2;
    final String TAG = "TAG";
    private Button addbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
        initialize_view();
        Initialize_spinner();

        firebaseAuth =FirebaseAuth.getInstance();
        userUID = firebaseAuth.getUid().toString().trim();
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        book_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image = new Intent(Intent.ACTION_PICK);
                image.setType("image/*");
                startActivityForResult(image,GALLARY_REQUEST_CODE);
            }
        });

        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Adding book");
                progressDialog.show();
                if(validate()) {
                    firebaseDatabase = FirebaseDatabase.getInstance();

                    databaseReference3 = firebaseDatabase.getReference("All Books");
                    databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Books");
                    databaseReference1 = firebaseDatabase.getReference("User Images").child(firebaseAuth.getUid()).child("Books");
                    filepath = storageReference.child("Photos").child(firebaseAuth.getUid()).child("Books/");
                    final book_details book_details = new book_details(bookname, bookdesc, bookauth, bookedi, bookprice, booksub, bookcond, booksem,imagelink,pushLink);
                    final Allbooks allbooks = new Allbooks(bookname, bookdesc, bookauth, bookedi, bookprice, booksub, bookcond, booksem,imagelink,userUID);

                    key = databaseReference3.push().getKey();
                    databaseReference3.child(key).setValue(allbooks).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                databaseReference.child(key).setValue(book_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            task1 = true;
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(NewBookActivity.this, "Failed to add Book!", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    });

                    //Adding Book Details


                    //Adding Image
                    if (imageholder != null) {
                        filepath.putFile(imageholder).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                downloadlink = taskSnapshot.getDownloadUrl();
                                imagelink = downloadlink.toString().trim();
                                databaseReference1.child(key).child("image").setValue(imagelink);
                                databaseReference1.child(key).child("push_id").setValue(key);
                                databaseReference.child(key).child("push_id").setValue(key);
                                databaseReference.child(key).child("imagelink").setValue(imagelink);
                                databaseReference3.child(key).child("book_Image").setValue(imagelink);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                task2 = false;
                                Toast.makeText(NewBookActivity.this, "Failed to Update storage! ", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    } else {
                        imagelink = "null";
                        databaseReference1.child(key).child("image").setValue(imagelink);
                        databaseReference1.child(key).child("push_id").setValue(key);
                        databaseReference.child(key).child("push_id").setValue(key);
                        databaseReference.child(key).child("imagelink").setValue(imagelink);
                    }


                    Log.d(TAG, "Before if else: " + task1 + ".." + task2);
                  // Add if else and solve the multithreading problem
                  //   if (task1 == true & task2 == true) {


                    //        Log.d(TAG, "Inside Dismiss");
                            progressDialog.dismiss();
                            Toast.makeText(NewBookActivity.this, "Book Added!", Toast.LENGTH_SHORT).show();
                            finish();

                    //    }
                  /* else {
                            Log.d(TAG, "Inside Dismiss Else");
                            progressDialog.dismiss();
                            Toast.makeText(NewBookActivity.this, "Failed To Upload...Try Again", Toast.LENGTH_SHORT).show();

                        }*/


                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(NewBookActivity.this, "Fill the required fields!", Toast.LENGTH_LONG).show();

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
            book_image.setImageURI(imageholder);

        }
    }

    private void Initialize_spinner(){

        cond_adapter = ArrayAdapter.createFromResource(NewBookActivity.this , R.array.bookcondition , android.R.layout.simple_spinner_item);
        cond_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        book_cond.setAdapter(cond_adapter);
        book_cond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookcond = parent.getItemAtPosition(position).toString();
                Log.d(TAG , bookcond);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bookcond = parent.getItemAtPosition(0).toString();
                Log.d(TAG , bookcond);

            }
        });
        sem_adapter = ArrayAdapter.createFromResource(NewBookActivity.this , R.array.booksemester , android.R.layout.simple_spinner_item);
        sem_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        book_sem.setAdapter(sem_adapter);
        book_sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                booksem = parent.getItemAtPosition(position).toString();
                Log.d(TAG , booksem);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                booksem = parent.getItemAtPosition(0).toString();
                Log.d(TAG , booksem);

            }
        });

    }
    private void initialize_view(){

        book_desc = findViewById(R.id.book_editdesc);
        book_name = findViewById(R.id.book_editname);
        book_author = findViewById(R.id.book_editauthor);
        book_edi = findViewById(R.id.book_editedi);
        book_price = findViewById(R.id.book_editprice);
        book_sub = findViewById(R.id.book_editsub);
        book_cond = findViewById(R.id.book_spincond);
        book_image = findViewById(R.id.book_img);
        addbook = findViewById(R.id.addbook);
        book_sem = findViewById(R.id.book_spinsem);

    }
    private boolean validate(){
        bookname = book_name.getText().toString();
        bookauth = book_author.getText().toString();
        booksub = book_sub.getText().toString();
        bookprice = book_price.getText().toString();
        bookedi = book_edi.getText().toString();
        if(bookedi.isEmpty())
            bookedi = "No Information";
        bookdesc = book_desc.getText().toString();
        if(bookdesc.isEmpty())
            bookdesc = "No Description";

        if(bookprice.isEmpty()| booksub.isEmpty()| bookname.isEmpty() | bookauth.isEmpty())
            return false;
        else return true;

    }

}
