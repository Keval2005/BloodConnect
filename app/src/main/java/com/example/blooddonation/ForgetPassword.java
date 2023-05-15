package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgetPassword extends AppCompatActivity {
    private EditText otpBox1, otpBox2, otpBox3, otpBox4;

    TextInputEditText username,password,confirmpassword;

    String dbpass;

//    TextInputEditText mono;
    private static final int SMS_PERMISSION_REQUEST_CODE = 1;
    Button getOTP,verifyOTP,updatePassword;



    String otpverify,monostr,usernamestr;

    LinearLayout layouttop,layoutbottom,layoutpassword;

    RelativeLayout relativeLayout;
    int otpsend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        relativeLayout = findViewById(R.id.forgotpasswordlayout);

        otpBox1 = findViewById(R.id.otp_box_1);
        otpBox2 = findViewById(R.id.otp_box_2);
        otpBox3 = findViewById(R.id.otp_box_3);
        otpBox4 = findViewById(R.id.otp_box_4);

        password = findViewById(R.id.forgotpassword1);
        confirmpassword = findViewById(R.id.forgotconfirmpassword1);
        updatePassword = findViewById(R.id.updatepassowrdforgot);

        layoutbottom = findViewById(R.id.layoutbottom);
        layouttop = findViewById(R.id.layouttop);
        layoutpassword = findViewById(R.id.passwordlayout);

        layouttop.setVisibility(View.VISIBLE);
        layoutbottom.setVisibility(View.GONE);
        layoutpassword.setVisibility(View.GONE);

        username = findViewById(R.id.usernameforget1);
//        mono = findViewById(R.id.monoforgot1);

        getOTP = findViewById(R.id.btngetotp);
        verifyOTP = findViewById(R.id.btnVerify);

        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(username.getText().toString().trim()))
                        {
                            usernamestr = username.getText().toString().trim();
                            monostr = snapshot.child(usernamestr).child("Phone_no").getValue(String.class);
//                            if(monostr.
//                                    equals(mono.getText().toString().trim())){

                                usernamestr = username.getText().toString().trim();
                                Random random = new Random();
                                otpsend = 1000 + random.nextInt(8999);
                                // Check if the SMS permission is already granted.
                                if (ContextCompat.checkSelfPermission(ForgetPassword.this, android.Manifest.permission.SEND_SMS)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    // Permission is already granted, do the work that requires this permission.
                                    // ...
                                    Toast.makeText(ForgetPassword.this, String.valueOf(otpsend), Toast.LENGTH_SHORT).show();
                                    // Get an instance of the SmsManager class
                                    SmsManager smsManager = SmsManager.getDefault();

                                    // Define the destination phone number and the message text
                                    String phoneNumber = monostr;
                                    String message = "Hey "+usernamestr+" Your OTP for forget password in Blood Donation App is: "+String.valueOf(otpsend);

                                    // Use the SmsManager to send the SMS message
                                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);

                                    layoutbottom.setVisibility(View.VISIBLE);
                                    layouttop.setVisibility(View.GONE);
                                    Snackbar snackbar = Snackbar.make(relativeLayout, "OTP sended to your Registered Mobile No.",Snackbar.LENGTH_LONG);
                                    snackbar.setAction("OK", v1 -> {
                                        snackbar.dismiss(); // Dismiss the Snackbar
                                    });
                                    snackbar.show();
//                                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                                intent.setData(Uri.parse("mailto:"+monostr));
//                                intent.putExtra(Intent.EXTRA_SUBJECT, "OTP verification");
//                                intent.putExtra(Intent.EXTRA_TEXT, "This is otp for your Forget Password in Blood Donation App"+String.valueOf(otpsend));
//                                startActivity(Intent.createChooser(intent, "Send mono"));
                                } else {
                                    // Permission is not granted yet, request the permission.
                                    Snackbar snackbar = Snackbar.make(relativeLayout, "Please allow us to send SMS to send OTP",Snackbar.LENGTH_LONG);
                                    snackbar.setAction("OK", v1 -> {
                                        snackbar.dismiss(); // Dismiss the Snackbar
                                    });
                                    snackbar.show();


                            }
//                            else{
//                                Toast.makeText(ForgetPassword.this, "Entered Mobile No is MisMatched with Profile", Toast.LENGTH_SHORT).show();
//                            }
                        }
                        else{
                            Toast.makeText(ForgetPassword.this, "Entered Valid Username", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });


        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otpverify.equals(String.valueOf(otpsend))){
                    layoutpassword.setVisibility(View.VISIBLE);
                    layoutbottom.setVisibility(View.GONE);
                    Toast.makeText(ForgetPassword.this, "OTP Verified", Toast.LENGTH_SHORT).show();

                    databaseReference.child("Users").child(usernamestr).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                           dbpass =  snapshot.child("Password").getValue(String.class);
                           password.setText(dbpass);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(ForgetPassword.this, "OTP is Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().trim().equals(confirmpassword.getText().toString().trim()))
                {
                    databaseReference.child("Users").child(usernamestr).child("Password").setValue(password.getText().toString().trim());
                    Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_LONG).show();
                    SmsManager smsManager = SmsManager.getDefault();

                    // Define the destination phone number and the message text
                    String phoneNumber = monostr;
                    String message = usernamestr+", Your Password is updated now ";

                    // Use the SmsManager to send the SMS message
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);

                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Confirm Password doesn't match with password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Add TextWatcher to each of the EditText views
        otpBox1.addTextChangedListener(new OTPTextWatcher(otpBox1, otpBox2));
        otpBox2.addTextChangedListener(new OTPTextWatcher(otpBox2, otpBox3));
        otpBox3.addTextChangedListener(new OTPTextWatcher(otpBox3, otpBox4));
        otpBox4.addTextChangedListener(new OTPTextWatcher(otpBox4, null));
    }
    private class OTPTextWatcher implements TextWatcher {
        private EditText currentView, nextView;

        private OTPTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1 && nextView != null) {
                nextView.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Merge OTP from all EditText views into a String
            otpverify = otpBox1.getText().toString()
                    + otpBox2.getText().toString()
                    + otpBox3.getText().toString()
                    + otpBox4.getText().toString();

            // Use the merged OTP String in your app's logic
        }
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission is granted, do the work that requires this permission.
//                // ...
//            } else {
//                // Permission is denied, show a message to inform the user.
//                Toast.makeText(this, "SMS permission is required to send messages.",
//                        Toast.LENGTH_SHORT).show();
//                // Show an explanation to the user asynchronously.
//                new AlertDialog.Builder(this)
//                        .setMessage("This app requires the SMS permission to send messages.")
//                        .setTitle("SMS Permission Required")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // Request permission again.
//                                ActivityCompat.requestPermissions(ForgetPassword.this,
//                                        new String[]{android.Manifest.permission.SEND_SMS},
//                                        SMS_PERMISSION_REQUEST_CODE);
//                            }
//                        })
//                        .create()
//                        .show();
//            }
//        }
//    }

}