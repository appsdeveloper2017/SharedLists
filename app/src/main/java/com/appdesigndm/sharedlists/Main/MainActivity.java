package com.appdesigndm.sharedlists.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.appdesigndm.sharedlists.Adapters.MainRecyclerAdapter;
import com.appdesigndm.sharedlists.Helpers.NotesApp;
import com.appdesigndm.sharedlists.Models.MainListsModel;
import com.appdesigndm.sharedlists.R;
import com.appdesigndm.sharedlists.Settings.SettingsApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mainRecycler;
    private MainRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        subscribeToUserListener();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.main_activity_title));
        setSupportActionBar(toolbar);

        mainRecycler = (RecyclerView) findViewById(R.id.main_recycler);
        mainRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new MainRecyclerAdapter(NotesApp.currentUserModel.getLists());
        mainRecycler.setAdapter(adapter);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewList();
            }
        });
    }

    private void subscribeToUserListener() {
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();
        dbr.child(NotesApp.BBDD_USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO: Cuando cambia la lista se debe actualizar. Recuperar los datos y refrescar el adapter del recycler
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addNewList() {
        // TODO: Acciones necesarias para añadir nueva lista, editText para escribir el nombre, actualizar la BBDD y refrescar el adapter del recycler
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            openSettingsApp();
        } else if (id == R.id.policy) {
            Toast toast = Toast.makeText(this, "Policy", Toast.LENGTH_SHORT);
            toast.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSettingsApp() {
        Intent intent = new Intent(getApplicationContext(), SettingsApp.class);
        startActivity(intent);
    }
}

