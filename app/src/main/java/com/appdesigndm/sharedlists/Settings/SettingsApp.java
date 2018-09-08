package com.appdesigndm.sharedlists.Settings;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appdesigndm.sharedlists.Helpers.NotesApp;
import com.appdesigndm.sharedlists.Login.SplashActivity;
import com.appdesigndm.sharedlists.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SettingsApp extends AppCompatActivity {

    //View components.
    private ProgressBar progressBar;
    private EditText newEmail, newPass, actualPassword;
    private TextView confirmEmail, confirmPass, confirmDelete;
    private LinearLayout linearLayout;
    private View view;
    private String email;
    private String password;

    //Declaration firebase.
    private FirebaseAuth mAuth;

    //Requeriments of email and pass.
    private final Pattern hasUppercase = Pattern.compile("[A-Z]");
    private final Pattern hasLowercase = Pattern.compile("[a-z]");
    private final Pattern hasNumber = Pattern.compile("\\d");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings_app);

        mAuth = FirebaseAuth.getInstance();
        init();

        //Listeners.
        confirmEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchEmailConfirmations();
            }
        });
        confirmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPasswordConfirmation();
            }
        });
        confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount(view);
            }
        });
    }

    private void init() {
        //Editable text
        newEmail = (EditText) findViewById(R.id.mail);
        newPass = (EditText) findViewById(R.id.password);
        actualPassword = (EditText) findViewById(R.id.password_for_delete);
        //Textview for onClickListeners.
        confirmEmail = (TextView) findViewById(R.id.change_email);
        confirmPass = (TextView) findViewById(R.id.change_pass);
        confirmDelete = (TextView) findViewById(R.id.delete_account);
        //Linearlayout components.
        linearLayout = (LinearLayout) findViewById(R.id.linear_settings);
        //Progressbar
        progressBar = (ProgressBar) findViewById(R.id.settings_progress);
    }

    //Change email in firebase.
    private void changeEmail(String email) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            closeInputManager(newEmail);
            progressBar.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.change_made), Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_change_email), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }
    }

    //Change pass in firebase.
    private void changePassword(final String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            closeInputManager(newEmail);
            progressBar.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        NotesApp.password_user = password;
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.change_pass), Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_change_pass), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }
    }

    //Delete account in firebase.
    public void deleteAccount(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && actualPassword.getText().toString().equals(NotesApp.password_user)) {
            linearLayout.setVisibility(View.GONE);
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    NotesApp.userLogged = false;
                    NotesApp.email_user = null;
                    NotesApp.password_user = null;

                    mAuth.signOut();

                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    startActivity(intent);
                }

            });
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.equals_pass), Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    private void launchEmailConfirmations() {
        // Check for a valid email address changes.
        email = newEmail.getText().toString();

        if (TextUtils.isEmpty(email)) {
            newEmail.setError(getString(R.string.error_field_required));
        } else if (!isValidEmail(email)) {
            newEmail.setError(getString(R.string.error_invalid_email));
        } else {
            changeEmail(email);
        }
    }

    private void launchPasswordConfirmation() {

        password = newPass.getText().toString();

        // Check for a valid password, if the user entered one.
        if (password.length() < 6) {
            newPass.setError(getString(R.string.error_invalid_password));
        }
        // Has a upper case in password.
        if (!hasUppercase.matcher(password).find()) {
            newPass.setError(getString(R.string.error_capital_letter));
        }
        //Has a lower case in password.
        if (!hasLowercase.matcher(password).find()) {
            newPass.setError(getString(R.string.error_lower_case));
        }
        //Has a number in password.
        if (!hasNumber.matcher(password).find()) {
            newPass.setError(getString(R.string.error_number));
        }
        if (isValidPass(password)) {
            changePassword(password);
        }
    }

    //Close input manager.
    private void closeInputManager(EditText editable) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editable.getWindowToken(), 0);
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPass(String password) {
        return password.length() >= 6 && hasUppercase.matcher(password).find() && hasLowercase.matcher(password).find() &&
                hasNumber.matcher(password).find();

    }
}

