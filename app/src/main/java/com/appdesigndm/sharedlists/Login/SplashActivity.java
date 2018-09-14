package com.appdesigndm.sharedlists.Login;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.appdesigndm.sharedlists.Helpers.NotesApp;
import com.appdesigndm.sharedlists.Main.MainActivity;
import com.appdesigndm.sharedlists.Models.MainListsModel;
import com.appdesigndm.sharedlists.Models.SecondaryListModel;
import com.appdesigndm.sharedlists.Models.UserModel;
import com.appdesigndm.sharedlists.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private TextView textView;
    private FirebaseDatabase fdb;
    private DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);

        //Init variables.
        init();

        //Variable static is NotesApp.currentUserApp is true.
//        if (startedSession() && NotesApp.currentUserApp != null) {
//
//            A la espera de hacer la conexión para recuperar los datos.
//            openConectionFirebase();
//            openMainActivity();
//
//        } else {
//
//            viewSplash();
//        }
        fillDataInFirebase();
        fetchData();
    }

    private void fillDataInFirebase() {
        fdb = FirebaseDatabase.getInstance();
        dbr = fdb.getReference();

        MainListsModel listsModel1 = new MainListsModel();
        listsModel1.setListName("Primera lista");
        listsModel1.addItemToSecondaryList(new SecondaryListModel("Primer item de la primera lista"));
        listsModel1.addItemToSecondaryList(new SecondaryListModel("Segundo item de la primera lista"));
        UserModel user = new UserModel();
        user.setUserName("Usuario 1");
        user.setUserEmail("email 1");
        user.addNewList(listsModel1);
        MainListsModel listsModel2 = new MainListsModel();
        listsModel2.setListName("Segunda lista");
        listsModel2.addItemToSecondaryList(new SecondaryListModel("Primer item de la segunda lista"));
        listsModel2.addItemToSecondaryList(new SecondaryListModel("Segundo item de la segunda lista"));
        user.addNewList(listsModel2);

        Task task = dbr.setValue(user);
        if (!task.isSuccessful() || !task.isComplete()){
            Toast.makeText(SplashActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchData() {
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NotesApp.user = dataSnapshot.getValue(UserModel.class);
                openMainActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SplashActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        textView = (TextView) findViewById(R.id.textSplash);
    }

    private void viewSplash() {

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        textView.startAnimation(myanim);

        myanim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean startedSession() {
        return NotesApp.userLogged;
    }

}
