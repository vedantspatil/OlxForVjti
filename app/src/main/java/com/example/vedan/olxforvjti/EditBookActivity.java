package com.example.vedan.olxforvjti;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditBookActivity extends AppCompatActivity {

    private static final String TAG ="TAG";
    private int didimagechange = 0;
    private ImageView edit_image;
    private EditText edit_bname, edit_bdesc, edit_bauthor ,edit_bsub,edit_bedi,edit_bprice;
    private Spinner edit_cond, edit_sem;
    private Button edit_save,delete;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference,filepath;
    private FirebaseDatabase firebaseDatabase;
    private Query query,query1,query2;
    private DatabaseReference databaseReference,databaseReference1,databaseReference2,temp,temp2;
    private Uri imageholder=null,downloadlink;
    private static final int GALLARY_REQUEST_CODE = 2;
    private String image_dbname,imagelink = "null",bname,bdesc,bauthor,bedi,bsem,bprice,bsub,bcond,imagelink_present = "null",Uid;
    private ProgressDialog progressDialog;
    private int cond_preset,sem_preset;
    ArrayAdapter<CharSequence> cond_adapter , sem_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        initialize_view();

        //intitalize spinners
        cond_adapter = ArrayAdapter.createFromResource(EditBookActivity.this , R.array.bookcondition , android.R.layout.simple_spinner_item);
        cond_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_cond.setAdapter(cond_adapter);

        sem_adapter = ArrayAdapter.createFromResource(EditBookActivity.this , R.array.booksemester , android.R.layout.simple_spinner_item);
        sem_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_sem.setAdapter(sem_adapter);

        //listen for clicks
        Initialize_spinner();



        Intent intent = getIntent();
        Uid = intent.getExtras().getString("Key").trim();
        Log.d("UID",Uid);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        progressDialog = new ProgressDialog(this);

        databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
        databaseReference1 = firebaseDatabase.getReference().child("User Images").child(firebaseAuth.getUid());
        databaseReference2 = firebaseDatabase.getReference().child("All Books").child(Uid);
        query =  databaseReference.child("Books").orderByChild("push_id").equalTo(Uid);
        query1 = databaseReference1.child("Books").orderByChild("push_id").equalTo(Uid);
        filepath = storageReference.child("Photos").child(firebaseAuth.getUid()).child("Books/");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                        book_details allbooks = childSnapshot.getValue(book_details.class);
                    edit_bname.setText(allbooks.getBook_Name().trim());
                    edit_bauthor.setText(allbooks.getBook_Author().trim());
                    edit_bdesc.setText(allbooks.getBook_Description().trim());
                    edit_bedi.setText(allbooks.getBook_Edition().trim());
                    edit_bprice.setText(allbooks.getBook_Price().trim());
                    edit_bsub.setText(allbooks.getBook_Subject().trim());
                    String condition = allbooks.getBook_Condition().trim();
                    String semester = allbooks.getBook_Semester().trim();
                    cond_preset = cond_adapter.getPosition(condition);
                    sem_preset = sem_adapter.getPosition(semester);
                    edit_cond.setSelection(cond_preset);
                    edit_sem.setSelection(sem_preset);
                    String downloadlink = allbooks.getImagelink();
                    imagelink_present = downloadlink;
                    if (!downloadlink.equals("null"))
                        Picasso.get().load(downloadlink).fit().centerCrop().into(edit_image);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(EditBookActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
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
                progressDialog.setMessage("Updating Book Info");
                progressDialog.show();
                if (validate()) {
                    bname = edit_bname.getText().toString();
                    bdesc = edit_bdesc.getText().toString();
                    bauthor = edit_bauthor.getText().toString();
                    bedi = edit_bedi.getText().toString();
                    bsub = edit_bsub.getText().toString();
                    bprice = edit_bprice.getText().toString();

                    Log.d("ImageBefore",imagelink);


                    final book_details bdetails= new book_details(bname,bdesc,bauthor,bedi,bprice,bsub,bcond, bsem,imagelink_present,Uid);
                    Allbooks allbooks = new Allbooks(bname,bdesc,bauthor,bedi,bprice,bsub,bcond, bsem,imagelink_present,firebaseAuth.getUid());

                    //uploading information

                    databaseReference.child("Books").orderByChild("push_id").equalTo(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                    temp = childSnapshot.getRef();
                                    temp.setValue(bdetails);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            Toast.makeText(EditBookActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    databaseReference1.child("Books").orderByChild("push_id").equalTo(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                    temp2 = childSnapshot.getRef();
                                    if(didimagechange != 0) {
                                        if (imageholder != null) {
                                            filepath = filepath.child(imageholder.getLastPathSegment());
                                            filepath.putFile(imageholder).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    downloadlink = taskSnapshot.getDownloadUrl();
                                                    imagelink_present = downloadlink.toString().trim();
                                                    temp2.child("image").setValue(imagelink_present);
                                                    temp.child("imagelink").setValue(imagelink_present);
                                                    databaseReference2.child("book_Image").setValue(imagelink_present);

                                                }
                                            });

                                        } else {
                                            imagelink_present = "null";
                                            temp2.child("image").setValue(imagelink_present);
                                            temp.child("imagelink").setValue(imagelink_present);
                                            databaseReference2.child("book_Image").setValue(imagelink_present);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(EditBookActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    databaseReference2.setValue(allbooks);
                    progressDialog.dismiss();


                } else{
                    progressDialog.dismiss();
                    Toast.makeText(EditBookActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();

                }
                finish();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Deleting Book");
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditBookActivity.this);
                builder.setMessage("Delete Book?");

                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //delete books from all database
                        progressDialog.show();

                        databaseReference.child("Books").orderByChild("push_id").equalTo(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                        temp = childSnapshot.getRef();
                                        temp.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                   Log.d("DELETE","BOOK DELETED FROM USERS");
                                                    // Toast.makeText(EditBookActivity.this, "Book Deleted ! ", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                                Toast.makeText(EditBookActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        databaseReference1.child("Books").orderByChild("push_id").equalTo(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                        temp2 = childSnapshot.getRef();
                                        temp2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d("DELETE","BOOK DELETED FROM USERS IMAGES");
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(EditBookActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        databaseReference2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("DELETE","BOOK DELETED FROM ALLBOOOKS");
                                Toast.makeText(EditBookActivity.this, "Book Deleted!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                        progressDialog.dismiss();

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });





    }

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


        edit_cond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bcond = parent.getItemAtPosition(position).toString();
                Log.d(TAG , bcond);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bcond = parent.getItemAtPosition(0).toString();
                Log.d(TAG , bcond);

            }
        });

          edit_sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bsem = parent.getItemAtPosition(position).toString();
                Log.d(TAG , bsem);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bsem = parent.getItemAtPosition(0).toString();
                Log.d(TAG , bsem);

            }
        });

    }
    private boolean validate(){
        bname = edit_bname.getText().toString();
        bdesc = edit_bdesc.getText().toString();
        bauthor = edit_bauthor.getText().toString();
        bedi = edit_bedi.getText().toString();
        bsub = edit_bsub.getText().toString();
        bprice = edit_bprice.getText().toString();
        if(bedi.isEmpty())
            bedi = "No Information";
        if(bdesc.isEmpty())
            bdesc = "No Description";
        if(bname.isEmpty() | bauthor.isEmpty() | bprice.isEmpty() | bsub.isEmpty()){
            return false;
        }else {return true;
        }
    }
    private void initialize_view() {
        edit_image = findViewById(R.id.editbook_img);
        edit_bname = findViewById(R.id.editbook_editname);
        edit_bdesc = findViewById(R.id.editbook_editdesc);
        edit_bauthor = findViewById(R.id.editbook_editauthor);
        edit_bsub = findViewById(R.id.editbook_editsub);
        edit_bedi = findViewById(R.id.editbook_editedi);
        edit_cond = findViewById(R.id.editbook_spincond);
        edit_sem = findViewById(R.id.editbook_spinsem);
        edit_bprice = findViewById(R.id.editbook_editprice);
        edit_save = findViewById(R.id.editsavebook);
        delete = findViewById(R.id.deletebook);

    }
}
