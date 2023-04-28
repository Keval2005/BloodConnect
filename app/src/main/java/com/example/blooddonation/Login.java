package com.example.blooddonation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

//    EditText username;
//    EditText password;

    TextInputEditText username,password;

//    FirebaseAuth mAuth;

    Button btnLogin;
    Button signup,fp;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl
//            ("https://finalproject1-c0216-default-rtdb.firebaseio.com");



//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent iHome = new Intent(Login.this,HomeActivity.class);
//            startActivity(iHome);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
        Boolean logedIn = sp.getBoolean("flag",false);
        if(logedIn==true){
            Intent logedinIntent = new Intent(getApplicationContext(),ProfileActivity.class);
            startActivity(logedinIntent);
        }

        btnLogin = findViewById(R.id.btnLogin);
        signup = findViewById(R.id.signup);

        //mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        fp = findViewById(R.id.fp);


        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ForgetPassword.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // code for varifiaction

//                SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//
//                editor.putBoolean("flag",true);
//                editor.apply();



                final String usernamestr,passwordstr;
                usernamestr = username.getText().toString().trim();
                passwordstr = password.getText().toString().trim();
//                passwordstr = getMdhash(password.getText().toString().trim());

                if(TextUtils.isEmpty(usernamestr) || TextUtils.isEmpty(passwordstr)){
                    Toast.makeText(Login.this,
                            "Enter Username and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(usernamestr)){

                                final String getpassword = snapshot.child(usernamestr).child("Password").getValue(String.class);

                                if (getpassword.equals(passwordstr)){
                                    Toast.makeText(Login.this,
                                            "Logged In Successfully", Toast.LENGTH_SHORT).show();

                                    share();


                                    Intent intent = new Intent(getApplicationContext(),OnBoarding.class);
                                    startActivity(intent);
                                    finish();

                                }
                                else{
                                    // for password
                                    Toast.makeText(Login.this,
                                            "Your Password Is Incorrect", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                // for username
                                Toast.makeText(Login.this,
                                        "Your Username Or Password Is Incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }


//                mAuth.signInWithEmailAndPassword(usernamestr, passwordstr)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    // Sign in success, update UI with the signed-in user's information
//                                    Toast.makeText(getApplicationContext(), "Login Successful",
//                                            Toast.LENGTH_SHORT);
//                                    Intent iHome = new Intent(Login.this,HomeActivity.class);
//                                    startActivity(iHome);
//                                    finish();
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    Toast.makeText(Login.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });



            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // code for varifiaction


                Intent iHome = new Intent(Login.this,Signup.class);
                startActivity(iHome);

            }
        });
    }

//    private String getMdhash(String toString) {
//
//        String MD5 = "MD5";
//        try {
//            MessageDigest digest = MessageDigest.getInstance(MD5);
//            digest.update(toString.getBytes());
//
//            byte messageDigest[] = digest.digest();
//            StringBuilder hexstring = new StringBuilder();
//
//            for(byte aMsgDigest : messageDigest) {
//
//                String h = Integer.toHexString(0xFF & aMsgDigest);
//
//                while (h.length() < 2)
//                    h = "0" + h;
//                hexstring.append(h);
//            }
//            return hexstring.toString();
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return "";
//
//
//    }

    private void share() {
//        SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putBoolean("flag",true);
//        editor.apply();

        SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPref.edit();
        editor1.putString("value",username.getText().toString().trim());
        editor1.apply();


    }
}