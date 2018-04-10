package com.example.vedan.olxforvjti;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "TAG";
    private EditText login_email;
    private EditText login_password;
    private Button login_button;
    private TextView signup_link;
    private TextView Login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initiailize_view();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //This code allows user to directly enter the app if he have
        //logged in before

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin(v);
            }
        });
            if(user!=null){

            finish();
            startActivity(new Intent(MainActivity.this , HomeActivity.class));
        }
 //
        signup_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this ,Main2Activity.class);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed() {
        //Adding a alert dialog box

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Exit App?");
        builder.setCancelable(true);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void Initiailize_view(){
        firebaseAuth = FirebaseAuth.getInstance();
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login);
        signup_link = findViewById(R.id.signup_link);
      //  Login = findViewById(R.id.login_text);


    }
    protected void onLogin(View view){
        progressDialog.setMessage("Verifying Login Details");
        progressDialog.show();
        String email = login_email.getText().toString();
        String password = login_password.getText().toString();
        if(validate(email,password))
        {
            Log.d( TAG ,"BAR DISPLAYED");
            firebaseAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Checks if login was successful
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Login Successful !", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(MainActivity.this , HomeActivity.class));

                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Login Failed !", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            progressDialog.dismiss();
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG).show();
        }

    }
    private boolean validate(String email , String password)
    {
        if (email.isEmpty() | password.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

}
