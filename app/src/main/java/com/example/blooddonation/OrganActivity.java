package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class OrganActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;

    String voiceresult;
    SearchView searchView;

    RecyclerView recview1;
    MyOrganAdapter adapter1;

    FloatingActionButton voice1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_organ);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState,0);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Camp:
                        final Intent intent3 = new Intent(getApplicationContext(),CampActivity.class);
                        closeDrawer();
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.BloodBank:
                        final Intent intent2 = new Intent(getApplicationContext(),BloodBankActivity.class);
                        closeDrawer();
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.Organ:
                        closeDrawer();
                        break;
                    case R.id.Profile:
                        final Intent intent4 = new Intent(getApplicationContext(),ProfileActivity.class);
                        closeDrawer();
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
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
            }
        });

        recview1 = (RecyclerView) findViewById(R.id.recview_organ);
        recview1.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<ModelOrgan> options1 =
                new FirebaseRecyclerOptions.Builder<ModelOrgan>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Organ_Hospital"), ModelOrgan.class)
                        .build();
        adapter1=new MyOrganAdapter(options1);
        recview1.setAdapter(adapter1);
//        adapter1.startListening();

        voice1 = findViewById(R.id.voice_floatingbtn);

        voice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Search By Pin Code:", Toast.LENGTH_LONG).show();
                openVoiceDialog();
            }
        });
    }


    private void openVoiceDialog() {

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(i,200);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200 && resultCode == RESULT_OK)
        {
            ArrayList<String> arrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            voiceresult = arrayList.get(0);
            searchView.onActionViewExpanded();
            searchView.setQuery(voiceresult,false);

        }
        else{
            Toast.makeText(this, "something was wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
//        getMenuInflater().inflate(R.menu.mainmenu,menu);
        getMenuInflater().inflate(R.menu.searchmenu,menu);

        MenuItem item = menu.findItem(R.id.search);
        searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Pin Code");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public void onStart() {
        super.onStart();
        adapter1.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter1.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter1.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter1.notifyDataSetChanged();
    }

    private void processsearch(String s) {

        FirebaseRecyclerOptions<ModelOrgan> options1 =
                new FirebaseRecyclerOptions.Builder<ModelOrgan>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Organ_Hospital").orderByChild("pin").startAt(s).endAt(s+"\uf8ff"), ModelOrgan.class)
                        .build();

        adapter1 = new MyOrganAdapter(options1);
        adapter1.startListening();
        recview1.setAdapter(adapter1);

    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle("Hospitals");
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_id);
        frameLayout = findViewById(R.id.framelayout_id);
        navigationView = findViewById(R.id.navigationview_id);

        navigationView.setCheckedItem(R.id.Organ);
    }

    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex){
        if (savedInstanceState == null){
//            MenuItem menuItem = navigationView.getMenu().getItem(itemIndex).setChecked(true);
//            onNavigationItemSelected(menuItem);
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