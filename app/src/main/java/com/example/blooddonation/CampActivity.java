package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CampActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;

    String voiceresult;
    SearchView searchView;

    RecyclerView recview1;
    MyCampAdapter adapter1;

    FloatingActionButton voice1;
    FloatingActionButton createCamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camp);

        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState,0);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Camp:
                        closeDrawer();
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
                        ApplicationInfo app = getApplicationContext().getApplicationInfo();
                        String filePath = app.sourceDir;

                        Intent intent = new Intent(Intent.ACTION_SEND);

                        // MIME of .apk is "application/vnd.android.package-archive".
                        // but Bluetooth does not accept this. Let's use "*/*" instead.
                        intent.setType("*/*");

                        // Append file and send Intent
                        File originalApk = new File(filePath);

                        try {
                            //Make new directory in new location=
                            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
                            //If directory doesn't exists create new
                            if (!tempFile.isDirectory())
                                if (!tempFile.mkdirs())
                                    break;
                            //Get application's name and convert to lowercase
                            tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ","").toLowerCase() + ".apk");
                            //If file doesn't exists create new
                            if (!tempFile.exists()) {
                                if (!tempFile.createNewFile()) {
                                    break;
                                }
                            }
                            //Copy file to new location
                            InputStream in = new FileInputStream(originalApk);
                            OutputStream out = new FileOutputStream(tempFile);

                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                            in.close();
                            out.close();
                            System.out.println("File copied.");
                            //Open share dialog
//          intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
                            Uri photoURI = FileProvider.getUriForFile(CampActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", tempFile);
//          intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
                            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                            startActivity(Intent.createChooser(intent, "Share app via"));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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

        recview1 = (RecyclerView) findViewById(R.id.recview_camp);
        recview1.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<ModelCamp> options1 =
                new FirebaseRecyclerOptions.Builder<ModelCamp>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("camp").child("approved"), ModelCamp.class)
                        .build();
        adapter1=new MyCampAdapter(options1);
        recview1.setAdapter(adapter1);
//        adapter1.startListening();

        voice1 = findViewById(R.id.voice_floatingbtn);
        createCamp = findViewById(R.id.create_camp);

        voice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Search By Pin Code:", Toast.LENGTH_LONG).show();
                openVoiceDialog();
            }
        });

        createCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Enter Details to create camp", Toast.LENGTH_LONG).show();
                Intent cc = new Intent(CampActivity.this,CreateCamp.class);
                startActivity(cc);
            }
        });
    }

    private void openVoiceDialog() {

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(i,200);


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

    private void processsearch(String s) {

        FirebaseRecyclerOptions<ModelCamp> options1 =
                new FirebaseRecyclerOptions.Builder<ModelCamp>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("camp").child("approved").orderByChild("pin").startAt(s).endAt(s+"\uf8ff"), ModelCamp.class)
                        .build();
        adapter1 = new MyCampAdapter(options1);
        adapter1.startListening();
        recview1.setAdapter(adapter1);

    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle("Blood Donation App");
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_id);
        frameLayout = findViewById(R.id.framelayout_id);
        navigationView = findViewById(R.id.navigationview_id);

        navigationView.setCheckedItem(R.id.Camp);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.mainmenu,menu);
            getMenuInflater().inflate(R.menu.searchmenu, menu);

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

    private void closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}