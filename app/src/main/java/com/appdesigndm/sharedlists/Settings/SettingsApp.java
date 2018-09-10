package com.appdesigndm.sharedlists.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appdesigndm.sharedlists.Helpers.NotesApp;
import com.appdesigndm.sharedlists.Login.LoginActivity;
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
    private EditText newEmail, newPass, actualPassword, old_password;
    private TextView confirmEmail, confirmPass, confirmDelete;
    private LinearLayout linearLayout;
    private String email;
    private String password;

    //Comppnents profile user cardview.
    private CardView cardView;
    private ImageView imageViewUser;
    private TextView nameUser;
    private TextView mailUser;

    //Declaration firebase.
    private FirebaseAuth mAuth;

    //Array spinner options settings account user.
    private String[] myArrayAccount = new String[4];
    private Spinner spinnerSettings;

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

        //Firebase connection and status.
        mAuth = FirebaseAuth.getInstance();

        //User status.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

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

        //Init elements.
        init();

        //Item selected for spinner.
        spinnerSettings.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myArrayAccount));
        spinnerSettings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                noVisibleElements(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                alertDeleteAccount();
            }
        });
    }

    //Init elements.
    private void init() {
        //Spinner.
        spinnerSettings = (Spinner) findViewById(R.id.spinner_settings);
        //Array Spinner.
        myArrayAccount[0] = getResources().getString(R.string.select_item);
        myArrayAccount[1] = getResources().getString(R.string.quetion_email);
        myArrayAccount[2] = getResources().getString(R.string.question_pass);
        myArrayAccount[3] = getResources().getString(R.string.question_delete);

        //Cardview elements.
        cardView = (CardView) findViewById(R.id.cardUser);
        imageViewUser = (ImageView) findViewById(R.id.person_photo);
        nameUser = (TextView) findViewById(R.id.user_card_name);
        mailUser = (TextView) findViewById(R.id.user_card_email);

        //Editable text
        newEmail = (EditText) findViewById(R.id.mail);
        newPass = (EditText) findViewById(R.id.password);
        old_password = (EditText) findViewById(R.id.password_old);
        actualPassword = (EditText) findViewById(R.id.password_for_delete);
        //Textview for onClickListeners.
        confirmEmail = (TextView) findViewById(R.id.change_email);
        confirmPass = (TextView) findViewById(R.id.change_pass);
        confirmDelete = (TextView) findViewById(R.id.delete_account);
        //Linearlayout components.
        linearLayout = (LinearLayout) findViewById(R.id.linear_settings);
        //Progressbar
        progressBar = (ProgressBar) findViewById(R.id.settings_progress);
        noVisibleElements(0);
    }

    //Atributes for visibility elements.
    private void noVisibleElements(int position) {
        switch (position) {
            case 0:
                newEmail.setVisibility(View.GONE);
                confirmEmail.setVisibility(View.GONE);

                old_password.setVisibility(View.GONE);
                newPass.setVisibility(View.GONE);
                confirmPass.setVisibility(View.GONE);

                actualPassword.setVisibility(View.GONE);
                confirmDelete.setVisibility(View.GONE);
                break;
            case 1:
                newEmail.setVisibility(View.VISIBLE);
                confirmEmail.setVisibility(View.VISIBLE);

                old_password.setVisibility(View.GONE);
                newPass.setVisibility(View.GONE);
                confirmPass.setVisibility(View.GONE);

                actualPassword.setVisibility(View.GONE);
                confirmDelete.setVisibility(View.GONE);
                break;
            case 2:
                newEmail.setVisibility(View.GONE);
                confirmEmail.setVisibility(View.GONE);

                old_password.setVisibility(View.VISIBLE);
                newPass.setVisibility(View.VISIBLE);
                confirmPass.setVisibility(View.VISIBLE);

                actualPassword.setVisibility(View.GONE);
                confirmDelete.setVisibility(View.GONE);
                break;
            case 3:
                newEmail.setVisibility(View.GONE);
                confirmEmail.setVisibility(View.GONE);

                old_password.setVisibility(View.GONE);
                newPass.setVisibility(View.GONE);
                confirmPass.setVisibility(View.GONE);

                actualPassword.setVisibility(View.VISIBLE);
                confirmDelete.setVisibility(View.VISIBLE);
                break;
        }
    }

    //Change email in firebase.
    private void changeEmail(String email) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
                        noVisibleElements(0);
                        alertVerifyEmail();
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

    //Alert Dialog info.
    private void alertVerifyEmail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string.title))
                .setMessage(getResources().getString(R.string.validate_other_account))
                .setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                                finish();
                            }
                        });
        builder.show();
    }

    //Alert Dialog delete account..
    private void alertDeleteAccount() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alert = builder.show();

        builder.setTitle(getResources().getString(R.string.title_delete))
                .setMessage(getResources().getString(R.string.message_delete))
                .setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAccount();
                                finish();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        actualPassword.setText("");
                        alert.dismiss();
                    }
                });
        builder.show();
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
                        noVisibleElements(0);
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
    public void deleteAccount() {
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

    //Method for validate email.
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

    //Check attributes for password.
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
        if (isValidPass(password) && old_password.getText().toString().equals(NotesApp.password_user) && isValidPass(old_password.getText().toString())) {
            changePassword(password);
        } else if (!old_password.getText().toString().equals(NotesApp.password_user)) {
            Toast.makeText(this, getResources().getString(R.string.does_no_match), Toast.LENGTH_SHORT).show();
        }
    }

    //Close input manager.
    private void closeInputManager(EditText editable) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editable.getWindowToken(), 0);
    }

    //Input valid email.
    private boolean isValidEmail(String email) {
        return email.contains("@") && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //Input valid password.
    private boolean isValidPass(String password) {
        return password.length() >= 6 && hasUppercase.matcher(password).find() && hasLowercase.matcher(password).find() &&
                hasNumber.matcher(password).find();

    }

    //Disconnect user to app.
    private void logout() {

        FirebaseAuth.getInstance().signOut();

        NotesApp.userLogged = false;
        NotesApp.email_user = null;
        NotesApp.password_user = null;

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}


