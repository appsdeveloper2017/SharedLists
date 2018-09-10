package com.appdesigndm.sharedlists.Main;

import android.content.Intent;
import android.net.Uri;
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

        //Status firebase connection.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            Toast toast = Toast.makeText(this, getResources().getString(R.string.recovering_notes), Toast.LENGTH_SHORT);
            toast.show();
            // Name, email address, and profile photo Url
            String nameUser = user.getDisplayName();
            String emailUser = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

//             The user's ID, unique to the Firebase project. Do NOT use this value to
//             authenticate with your backend server, if you have one. Use
//             FirebaseUser.getToken() instead.
            String uid = user.getUid();
        } else {
            logout();
        }
    }

    //Menu bar options inflater.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Actions menu bar.
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

    //Quick logout aplication and reset variables and finish activity.
    private void logout() {

        FirebaseAuth.getInstance().signOut();

        NotesApp.userLogged = false;
        NotesApp.email_user = null;
        NotesApp.password_user = null;

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //Open Settings activity.
    private void openSettingsApp() {
        Intent intent = new Intent(getApplicationContext(), SettingsApp.class);
        startActivity(intent);
    }
}

