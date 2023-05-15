package com.example.blooddonation;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Signup extends AppCompatActivity {

    String[] items = {"A+","B+","AB+","O+","A-","B-","AB-","O-"};
    AutoCompleteTextView auto_complete_txt;
    ArrayAdapter<String> adapterItems;
    RadioGroup radio_group;
    TextInputEditText signup_fullname,signup_username,signup_email,signup_password,signup_phone,birth_date,filename_str;
    Button btnSignup,btnUpload;

    ImageButton back;
    String value = null;
    String item = null;

    String b_datereg;

    Uri filepath;

    DatabaseReference databaseReference;
    StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        back = findViewById(R.id.signup_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotologin = new Intent(getApplicationContext(),Login.class);
                startActivity(gotologin);
                overridePendingTransition(0, 0);
            }
        });

        auto_complete_txt = findViewById(R.id.auto_complete_txt);
        signup_fullname = (TextInputEditText) findViewById(R.id.signup_fullname);
        signup_username = (TextInputEditText) findViewById(R.id.signup_username);
        signup_email = (TextInputEditText) findViewById(R.id.signup_email);
        signup_password = (TextInputEditText) findViewById(R.id.signup_password);
        signup_phone = (TextInputEditText) findViewById(R.id.signup_phone);
        btnSignup = (Button)findViewById(R.id.btnSignup);
        btnUpload = (Button)findViewById(R.id.btnUpload);
        filename_str = findViewById(R.id.filename);

        birth_date = findViewById(R.id.age_date);

        // age value


        //gender value
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
//        selectedGender = findViewById(radio_group.getCheckedRadioButtonId());

        int checkedId = radio_group.getCheckedRadioButtonId();
        if(checkedId == -1){
            //no radio button select
            Toast.makeText(this, "please select gender", Toast.LENGTH_SHORT).show();
        }else{
            findRadioButton(checkedId);
        }

        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);
        auto_complete_txt.setAdapter(adapterItems);
        auto_complete_txt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //item contain dropdown list data
                item = parent.getItemAtPosition(position).toString();

            }
        });

        Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
            private void updateDate() {
                String myFormat = "dd/MM/yy"; //put your date format in which you need to display
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                b_datereg =  sdf.format(myCalendar.getTime());
                birth_date.setText(sdf.format(myCalendar.getTime()));
            }
        };

        birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Signup.this,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    uploadtofirebase();


            }
        });


        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                findRadioButton(checkedId);
            }
        });


        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("mydocuments");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext( getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select Pdf Files"),101);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });


    }

    private void uploadtofirebase() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setTitle("File Uploading....!!!");
        pd.show();
        auto_complete_txt = findViewById(R.id.auto_complete_txt);
        signup_fullname = (TextInputEditText) findViewById(R.id.signup_fullname);
        signup_username = (TextInputEditText) findViewById(R.id.signup_username);
        signup_email = (TextInputEditText) findViewById(R.id.signup_email);
        signup_password = (TextInputEditText) findViewById(R.id.signup_password);
        signup_phone = (TextInputEditText) findViewById(R.id.signup_phone);
        btnSignup = (Button)findViewById(R.id.btnSignup);
        btnUpload = (Button)findViewById(R.id.btnUpload);

        birth_date = findViewById(R.id.age_date);

        radio_group = (RadioGroup) findViewById(R.id.radio_group);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        final String fullname,username,email,password,phone,bloodgroup;

        password = signup_password.getText().toString().trim();

        fullname = String.valueOf(signup_fullname.getText());
        username = String.valueOf(signup_username.getText());
        email = String.valueOf(signup_email.getText());
        // password = String.valueOf(signup_password.getText());
        phone = String.valueOf(signup_phone.getText());
        bloodgroup = String.valueOf(item);

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(username)){
                    Toast.makeText(Signup.this, "Username is already registered",
                            Toast.LENGTH_SHORT).show();
                }
                else{

                    databaseReference.child("Users").child(username).child("Full_name").setValue(fullname);
                    databaseReference.child("Users").child(username).child("Password").setValue(password);
                    databaseReference.child("Users").child(username).child("EmailId").setValue(email);
                    databaseReference.child("Users").child(username).child("Phone_no").setValue(phone);
                    databaseReference.child("Users").child(username).child("Birth_date").setValue(b_datereg);
                    databaseReference.child("Users").child(username).child("Gender").setValue(value);
                    databaseReference.child("Users").child(username).child("BloodGroup").setValue(bloodgroup);



                    StorageReference reference=storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
                    reference.putFile(filepath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

//                                                    final String username1 = String.valueOf(signup_username.getText());

                                            databaseReference = FirebaseDatabase.getInstance().getReference();

                                            fileinfomodel obj=new fileinfomodel(uri.toString());
                                            databaseReference.child("Users").child(username).child("Donate_Eligble_Certi").setValue(obj);

                                            pd.dismiss();
                                            Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();
                                            Toast.makeText(getApplicationContext(), "User Registered Successfully",
                                                    Toast.LENGTH_LONG).show();

                                            Intent login = new Intent(getApplicationContext(),Login.class);
                                            startActivity(login);

//                                filelogo.setVisibility(View.INVISIBLE);
//                                cancelfile.setVisibility(View.INVISIBLE);
//                                imagebrowse.setVisibility(View.VISIBLE);
//                                filetitle.setText("");
                                        }
                                    });

                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                                    pd.setMessage("Uploaded :"+(int)percent+"%");
                                }
                            });



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            String filename = getFilenameFromUri(filepath);
            filename_str.setText(filename);
            Toast.makeText(getApplicationContext(),"File Selected Successfully",
                    Toast.LENGTH_SHORT).show();
//            filelogo.setVisibility(View.VISIBLE);
//            cancelfile.setVisibility(View.VISIBLE);
//            imagebrowse.setVisibility(View.INVISIBLE);
        }
    }

    private String getFilenameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }




    public void findRadioButton(int checkedId) {
        switch (checkedId) {
            case R.id.male:
                value = "Male";
                break;
            case R.id.female:
                value = "Female";
                break;
            case R.id.others:
                value = "Others";
                break;
        }
    }
//    //variables
//    ImageView backbtn;
//    Button next,login;
//    TextView titletext;
//    TextInputEditText fullName_signup1,username_signup1,email_signup1,password_signup1;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        //hooks for animation
//        backbtn = findViewById(R.id.signup_back_button);
//        next = findViewById(R.id.signup_next_button);
//        login = findViewById(R.id.signup_login_button);
//        titletext = findViewById(R.id.signup_title_text);
//
//        //HOOKS FOR GET DATA
//        fullName_signup1 = findViewById(R.id.full_name_signup1);
//        email_signup1 = findViewById(R.id.email_signup1);
//        username_signup1 = findViewById(R.id.username_signup1);
//        password_signup1 = findViewById(R.id.password_signup1);
//
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String usernamesignup1str,passwordsignup1str,emailsignup1str,fullnamesignup1str;
//                usernamesignup1str = String.valueOf(username_signup1.getText());
//                passwordsignup1str = String.valueOf(password_signup1.getText());
//                fullnamesignup1str = String.valueOf(fullName_signup1.getText());
//                emailsignup1str = String.valueOf(email_signup1.getText());
//
//                Intent intent = new Intent(Signup.this,SignUp2ndClass.class);
//                intent.putExtra("usernamesgn",usernamesignup1str);
//                intent.putExtra("passwordsgn",passwordsignup1str);
//                intent.putExtra("fullnamesgn", fullnamesignup1str);
//                intent.putExtra("emailsgn", emailsignup1str);
//                startActivity(intent);
//            }
//        });
//
//    }
//
////    public void callNextSignupScreen(View view){
////
//////        String usernamesignup1str,passwordsignup1str,emailsignup1str,fullnamesignup1str;
//////                usernamesignup1str = String.valueOf(username_signup1.getText());
//////                passwordsignup1str = String.valueOf(password_signup1.getText());
//////                fullnamesignup1str = String.valueOf(fullName_signup1.getText());
//////                emailsignup1str = String.valueOf(email_signup1.getText());
//////
//////                Intent intent = new Intent(Signup.this,SignUp2ndClass.class);
//////                intent.putExtra("usernamesgn",usernamesignup1str);
//////                intent.putExtra("passwordsgn",passwordsignup1str);
//////                intent.putExtra("fullnamesgn", fullnamesignup1str);
//////                intent.putExtra("emailsgn", emailsignup1str);
////
////        if (!validateFullName() | !validateUserName() | !validateEmail() | !validatePassword()){
////            return;
////        }
////        else if (validateFullName() | validateUserName() | validateEmail() | validatePassword()){
////            intent = new Intent(Signup.this,SignUp2ndClass.class);
////        }
////
////        //add transition
////        Pair[] pairs = new Pair[4];
////        pairs[0] = new Pair<View,String>(backbtn,"transition_back_arrow_btn");
////        pairs[1] = new Pair<View,String>(next,"transition_next_btn");
////        pairs[2] = new Pair<View,String>(login,"transition_login_btn");
////        pairs[3] = new Pair<View,String>(titletext,"transition_title_text");
////
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
////            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Signup.this,pairs);
////            startActivity(intent,options.toBundle());
////        }
////        else{
////            startActivity(intent);
////        }
////
////    }
////
////
////    //VALIDATION FUNCTION
////    private boolean validateFullName(){
////        String val = fullName_signup1.getText().toString().trim();
////        if(val.isEmpty()){
////            fullName_signup1.setError("This Field Can Not Be Empty");
////            return false;
////        } else {
////            fullName_signup1.setError(null);
//////            fullName_signup1.setErrorEnabled(false);
////            return true;
////        }
////    }
////
////    private boolean validateUserName(){
////        String val = username_signup1.getText().toString().trim();
////        String checkspaces = "\\A\\w{1,20}\\z";
////        if(val.isEmpty()){
////            username_signup1.setError("This Field Can Not Be Empty");
////            return false;
////        } else if(val.length()>20){
////            username_signup1.setError("Username Is Too Large");
////            return false;
////        } else if (!val.matches(checkspaces)){
////            username_signup1.setError("No White Space Is Allowed");
////            return false;
////        }
////        else {
////            username_signup1.setError(null);
//////            username_signup1.setErrorEnabled(false);
////            return true;
////        }
////    }
////
////    private boolean validateEmail(){
////        String val = email_signup1.getText().toString().trim();
////        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
////        if(val.isEmpty()){
////            email_signup1.setError("This Field Can Not Be Empty");
////            return false;
////        } else if (!val.matches(checkEmail)){
////            email_signup1.setError("Invalid Email..");
////            return false;
////        }
////        else {
////            email_signup1.setError(null);
//////            email_signup1.setErrorEnabled(false);
////            return true;
////        }
////    }
////
////    private boolean validatePassword(){
////        String val = password_signup1.getText().toString().trim();
////        String checkPassword = "^"+
////                //"(?=.*[0-9])" +  //at least one digit
////                // "(?=.*[a-z])" +  //at least one lowercase letter
////                // "(?=.*[A-Z])" +  //at least one uppercase letter
////                 "(?=.*[a-zA-Z])" +  //any letter
////                "(?=.*[@#$%^&+=])" +  //at least one special character
////                "(?=\\S+$)" +  //no white spaces
////                ".{8,}" +      //at least 8 characters
////                "$";
////
////        if(val.isEmpty()){
////            password_signup1.setError("This Field Can Not Be Empty");
////            return false;
////        } else if (!val.matches(checkPassword)){
////            password_signup1.setError("Password Should Contain 8 Characters!");
////            return false;
////        }
////        else {
////            password_signup1.setError(null);
//////            password_signup1.setErrorEnabled(false);
////            return true;
////        }
////
////
////    }

}