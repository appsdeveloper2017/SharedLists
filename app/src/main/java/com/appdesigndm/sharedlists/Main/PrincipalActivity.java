package com.appdesigndm.sharedlists.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.appdesigndm.sharedlists.Helpers.NotesApp;
import com.appdesigndm.sharedlists.Login.LoginActivity;
import com.appdesigndm.sharedlists.Login.SplashActivity;
import com.appdesigndm.sharedlists.R;
import com.appdesigndm.sharedlists.Settings.SettingsApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Toast toast = Toast.makeText(this, getResources().getString(R.string.recovering_notes), Toast.LENGTH_SHORT);
            toast.show();
        } else {
            logout();
        }
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
        }
        if (id == R.id.policy) {
            Toast toast = Toast.makeText(this, "Policy", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.logout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        FirebaseAuth.getInstance().signOut();

        NotesApp.userLogged = false;
        NotesApp.email_user = null;
        NotesApp.password_user = null;

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void openSettingsApp() {
        Intent intent = new Intent(getApplicationContext(), SettingsApp.class);
        startActivity(intent);
    }
}

