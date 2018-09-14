package com.appdesigndm.sharedlists.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mainRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.main_activity_title));
        setSupportActionBar(toolbar);

        mainRecycler = (RecyclerView) findViewById(R.id.main_recycler);
        MainRecyclerAdapter adapter = new MainRecyclerAdapter(NotesApp.user.getLists());
        mainRecycler.setAdapter(adapter);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private ArrayList<MainListsModel> fetchMainList() {
        ArrayList<MainListsModel> list = new ArrayList<>();
        for (int i=0; i<10; i++) {
            MainListsModel item = new MainListsModel();
            item.setListName("Title " + (i + 1));
            list.add(item);
        }
        return list;
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

