package com.appdesigndm.sharedlists.Login;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.appdesigndm.sharedlists.Helpers.NotesApp;
import com.appdesigndm.sharedlists.Main.PrincipalActivity;
import com.appdesigndm.sharedlists.R;

public class SplashActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);

        //Init variables.
        init();

        //Variable static is NotesApp.currentUserApp is true.
        if (startedSession() && NotesApp.currentUserApp != null) {

            //A la espera de hacer la conexión para recuperar los datos.
            //openConectionFirebase();
            openMainActivity();

        } else {

            viewSplash();
        }
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

                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean startedSession() {
        return NotesApp.userLogged;
    }

}
