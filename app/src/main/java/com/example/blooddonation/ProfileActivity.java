package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;

    int ageint;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;


    TextInputEditText updateacc_fullname,updateacc_email,updateacc_password,updateacc_phone,birth_date;
    ImageView back;
    AutoCompleteTextView auto_complete_txt,gender_auto_complete_txt;
    String[] items = {"A+","B+","AB+","O+","A-","B-","AB-","O-"};
    String[] genders = {"Male","Female","Other"};
    ArrayAdapter<String> adapterItems,genderadapterItems;
    String item = null;
    String genderstr1 = null;
    Button submit;

    String Usernamesp;

    String ageStr;
    String namestr,usernamestr,birthdateStr,emailstr,genderstr,bloodgroupstr,monostr,passwordStr;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ImageView genderImage = findViewById(R.id.gender_image);
        TextView name = findViewById(R.id.namePf1);
        TextView username = findViewById(R.id.usernamePf1);
        TextView gender = findViewById(R.id.genderPf1);
        TextView bloodGroup = findViewById(R.id.bloodGroupPf1);
        TextView agetv = findViewById(R.id.agePf1);
        TextView email = findViewById(R.id.emailPf1);
        TextView mono = findViewById(R.id.monoPf1);
        Button create_camp = findViewById(R.id.create_campPf);
        Button update_profile = findViewById(R.id.update_profilePf);
        FrameLayout frameLayout = findViewById(R.id.framelayout_id);



        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        Usernamesp = sharedPreferences.getString("value","");

        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState,0);




        // Display the age in the TextView



        create_camp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreateCamp.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(ProfileActivity.this)
                        .setExpanded(true, 1800)
                        .setContentHolder(new ViewHolder(R.layout.update_profile))
                        .create();
                dialogPlus.show();
                View myview = dialogPlus.getHolderView();


                auto_complete_txt = findViewById(R.id.auto_complete_txt);
                gender_auto_complete_txt = findViewById(R.id.gender_auto_complete_txt);
                updateacc_fullname = (TextInputEditText) findViewById(R.id.updateacc_fullname);
                updateacc_email = (TextInputEditText) findViewById(R.id.updateacc_email);
                updateacc_password = (TextInputEditText) findViewById(R.id.updateacc_password);
                updateacc_phone = (TextInputEditText) findViewById(R.id.updateacc_phone);
                submit = findViewById(R.id.btnUpdate);
                birth_date = findViewById(R.id.age_date);

                TextInputLayout passwordlayout = findViewById(R.id.passwordlayout);
                passwordlayout.setPasswordVisibilityToggleEnabled(true);
//                passwordlayout.setPasswordVisibilityToggleTintList(new ColorStateList(s, com.google.android.material.R.attr.colorPrimary));

                auto_complete_txt.setText(bloodgroupstr);
                updateacc_fullname.setText(namestr);
                updateacc_email.setText(emailstr);
                updateacc_password.setText(passwordStr);
                updateacc_phone.setText(monostr);
                birth_date.setText(birthdateStr);
                gender_auto_complete_txt.setText(genderstr);

                final Context dialogluscontext = dialogPlus.getHolderView().getContext();

                adapterItems = new ArrayAdapter<String>(dialogluscontext,R.layout.list_item,items);
                auto_complete_txt.setAdapter(adapterItems);
                auto_complete_txt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //item contain dropdown list data
                        item = parent.getItemAtPosition(position).toString();

                    }
                });

                genderadapterItems = new ArrayAdapter<String>(dialogluscontext,R.layout.list_item,genders);
                gender_auto_complete_txt.setAdapter(genderadapterItems);
                gender_auto_complete_txt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //item contain dropdown list data
                        genderstr1 = parent.getItemAtPosition(position).toString();

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

                        birth_date.setText(sdf.format(myCalendar.getTime()));
                    }
                };

                birth_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(dialogluscontext,
                                date,
                                myCalendar.get(Calendar.YEAR),
                                myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if((namechange() | phonenumberChange() | emailChange() | dobChange() | passwordChange() | genderChange() | bloodGroupChange())){
                                    Toast.makeText(dialogluscontext, "Account Updated Successfully", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(Usernamesp)){
                                                namestr =snapshot.child(Usernamesp).child("Full_name").getValue(String.class);
                                                bloodgroupstr = snapshot.child(Usernamesp).child("BloodGroup").getValue(String.class);
                                                birthdateStr = snapshot.child(Usernamesp).child("Birth_date").getValue(String.class);
                                                passwordStr = snapshot.child(Usernamesp).child("Password").getValue(String.class);
                                                emailstr = snapshot.child(Usernamesp).child("EmailId").getValue(String.class);
                                                genderstr = snapshot.child(Usernamesp).child("Gender").getValue(String.class);
                                                monostr = snapshot.child(Usernamesp).child("Phone_no").getValue(String.class);


                                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
                                                LocalDate birthdate = LocalDate.parse(birthdateStr, formatter);

                                                // Calculate the user's age
                                                Period age = Period.between(birthdate, LocalDate.now());

                                                if (age.getYears()<0){
                                                    ageint = Math.abs(age.getYears());
                                                    ageint = 100 - (ageint % 100);
                                                    ageStr = String.valueOf(ageint);
                                                }
                                                else {
                                                    // Format the age as a string
                                                    ageStr = String.format("%d", age.getYears());
                                                }

                                                name.setText(namestr);
                                                username.setText(Usernamesp);
                                                bloodGroup.setText(bloodgroupstr);
                                                agetv.setText(ageStr);
                                                email.setText(emailstr);
                                                gender.setText(genderstr);
                                                mono.setText(monostr);
                                                if(genderstr.equals("Male")){
                                                    genderImage.setImageResource(R.drawable.man_profile);
                                                } else if (genderstr.equals("Female")) {
                                                    genderImage.setImageResource(R.drawable.women_profile);
                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }else{
                                    Toast.makeText(dialogluscontext, "You Did not made any changes", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                }
//                                databaseReference.child("Users").child(Usernamesp).child("Full_name").setValue(updateacc_fullname.getText().toString().trim());
//                                databaseReference.child("Users").child(Usernamesp).child("Password").setValue(updateacc_password.getText().toString().trim());
//                                databaseReference.child("Users").child(Usernamesp).child("EmailId").setValue(updateacc_email.getText().toString().trim());
//                                databaseReference.child("Users").child(Usernamesp).child("Phone_no").setValue(updateacc_phone.getText().toString().trim());
//                                databaseReference.child("Users").child(Usernamesp).child("Birth_date").setValue(birth_date.getText().toString().trim());
//                                databaseReference.child("Users").child(Usernamesp).child("Gender").setValue(gender_auto_complete_txt.getText().toString().trim());
//                                databaseReference.child("Users").child(Usernamesp).child("BloodGroup").setValue(auto_complete_txt.getText().toString().trim());


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.Camp:
                    final Intent intent4 = new Intent(getApplicationContext(),CampActivity.class);
                    closeDrawer();
                    startActivity(intent4);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.BloodBank:
                    final Intent intent2 = new Intent(getApplicationContext(),BloodBankActivity.class);
                    closeDrawer();
                    startActivity(intent2);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.Organ:
                    final Intent intent3 = new Intent(getApplicationContext(),OrganActivity.class);
                    closeDrawer();
                    startActivity(intent3);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.Profile:
                    closeDrawer();
                    break;

                case R.id.log_out:
                    SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("flag",false);
                    editor.apply();

                    Intent i = new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    finishAffinity();
                    break;

                case R.id.share:
                    String message = "Heyy friends check this Helpful app for Blood Donation: " +
                            "https://drive.google.com/drive/folders/1O08qxFz5j3RUCz1KZs94Dwp4pf7R5NWu?usp=sharing";
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);

                    break;

                case R.id.exit:
                    showExitDialog();
                    break;
//
//            case R.id.nav_settings_id:
//                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new SettingsFragment())
//                        .commit();
////                deSelectCheckedState();
//                closeDrawer();
//                break;
            }
            return true;
        });

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(Usernamesp)){
                    namestr =snapshot.child(Usernamesp).child("Full_name").getValue(String.class);
                    bloodgroupstr = snapshot.child(Usernamesp).child("BloodGroup").getValue(String.class);
                    birthdateStr = snapshot.child(Usernamesp).child("Birth_date").getValue(String.class);
                    passwordStr = snapshot.child(Usernamesp).child("Password").getValue(String.class);
                    emailstr = snapshot.child(Usernamesp).child("EmailId").getValue(String.class);
                    genderstr = snapshot.child(Usernamesp).child("Gender").getValue(String.class);
                    monostr = snapshot.child(Usernamesp).child("Phone_no").getValue(String.class);


                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
                    LocalDate birthdate = LocalDate.parse(birthdateStr, formatter);

                    // Calculate the user's age
                    Period age = Period.between(birthdate, LocalDate.now());
                    if (age.getYears()<0){
                        ageint = Math.abs(age.getYears());
                        ageint = 100 - (ageint % 100);
                        ageStr = String.valueOf(ageint);
                    }
                    else {
                        // Format the age as a string
                        ageStr = String.format("%d", age.getYears());
                    }
                    name.setText(namestr);
                    username.setText(Usernamesp);
                    bloodGroup.setText(bloodgroupstr);
                    agetv.setText(ageStr);
                    email.setText(emailstr);
                    gender.setText(genderstr);
                    mono.setText(monostr);
                    if(genderstr.equals("Male")){
                        genderImage.setImageResource(R.drawable.man_profile);
                    } else if (genderstr.equals("Female")) {
                        genderImage.setImageResource(R.drawable.women_profile);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean bloodGroupChange() {

        if(!bloodgroupstr.equals(auto_complete_txt.getText().toString())){
            databaseReference.child("Users").child(Usernamesp).child("BloodGroup").setValue(auto_complete_txt.getText().toString());
            return true;
        }else{
            return false;
        }

    }
    private boolean genderChange() {

        if(!genderstr.equals(gender_auto_complete_txt.getText().toString())){
            databaseReference.child("Users").child(Usernamesp).child("Gender").setValue(gender_auto_complete_txt.getText().toString());
            return true;
        }else{
            return false;
        }

    }

    private boolean passwordChange() {

        if(!passwordStr.equals(updateacc_password.getText().toString())){
            databaseReference.child("Users").child(Usernamesp).child("Password").setValue(updateacc_password.getText().toString());
            return true;
        }else{
            return false;
        }

    }

    private boolean dobChange() {

        if(!birthdateStr.equals(birth_date.getText().toString())){
            databaseReference.child("Users").child(Usernamesp).child("Birth_date").setValue(birth_date.getText().toString());
            return true;
        }else{
            return false;
        }

    }

    private boolean phonenumberChange() {

        if(!monostr.equals(updateacc_phone.getText().toString())){
            databaseReference.child("Users").child(Usernamesp).child("Phone_no").setValue(updateacc_phone.getText().toString());
            return true;
        }else{
            return false;
        }
    }

    private boolean emailChange(){

        if(!emailstr.equals(updateacc_email.getText().toString())){
            databaseReference.child("Users").child(Usernamesp).child("EmailId").setValue(updateacc_email.getText().toString());
            return true;
        }else{
            return false;
        }

    }
    private boolean namechange() {
        if(!namestr.equals(updateacc_fullname.getText().toString())){
            databaseReference.child("Users").child(Usernamesp).child("Full_name").setValue(updateacc_fullname.getText().toString());
            return true;
        }else{
            return false;
        }
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_id);
        frameLayout = findViewById(R.id.framelayout_id);
        navigationView = findViewById(R.id.navigationview_id);

        navigationView.setCheckedItem(R.id.Profile);
    }

    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex){
        if (savedInstanceState == null){
//            MenuItem menuItem = navigationView.getMenu().getItem(itemIndex).setChecked(true);
//            onOptionsItemSelected(menuItem);
        }
    }

    private void toggleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            super.onBackPressed();
        }
    }

    private void closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit the app?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}