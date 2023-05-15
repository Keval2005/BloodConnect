package com.example.blooddonation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ngoActivity extends AppCompatActivity {
    Toolbar toolbar;
    String voiceresult;
    SearchView searchView;

    RecyclerView recview1;
    MyNgoAdapter adapter1;

    FloatingActionButton voice1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo);

        initializeViews();

        recview1 = (RecyclerView) findViewById(R.id.recview_ngo);
        recview1.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<ModelNGO> options1 =
                new FirebaseRecyclerOptions.Builder<ModelNGO>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("NGO"), ModelNGO.class)
                        .build();
        adapter1=new MyNgoAdapter(options1);
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

    private void processsearch(String s) {

        FirebaseRecyclerOptions<ModelNGO> options1 =
                new FirebaseRecyclerOptions.Builder<ModelNGO>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("NGO").orderByChild("pin").startAt(s).endAt(s+"\uf8ff"), ModelNGO.class)
                        .build();

        adapter1 = new MyNgoAdapter(options1);
        adapter1.startListening();
        recview1.setAdapter(adapter1);

    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle("Helper NGOs");
        setSupportActionBar(toolbar);
    }

}