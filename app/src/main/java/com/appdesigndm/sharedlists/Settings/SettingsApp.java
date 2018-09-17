package com.appdesigndm.sharedlists.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appdesigndm.sharedlists.Helpers.NotesApp;
import com.appdesigndm.sharedlists.Login.LoginActivity;
import com.appdesigndm.sharedlists.Login.SplashActivity;
import com.appdesigndm.sharedlists.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsApp extends AppCompatActivity {

    private String TAG = "Error";
    //View components.
    private ProgressBar progressBar;
    private EditText newEmail, newPass, actualPassword, old_password;
    private TextView optionMail, optionPassword, optionDelete, confirmEmail, confirmPass, confirmDelete;
    private LinearLayout alternativeLinear;
    private LinearLayout layoutEmailVisible, layoutPasswordVisible, layoutDeleteVisible;
    private String email;
    private String password;

    //Components profile user cardview.
    private boolean invisibleElementsEmail;
    private boolean isInvisibleElementsPass;
    private boolean isInvisibleElementsDelete;
    private RelativeLayout relativeLayoutUser;
    private CircleImageView imageViewUser;
    private TextView mailUser;
    private TextView notes, sharedNotes, votes;

    //Declaration firebase.
    private FirebaseAuth mAuth;

    //Storage Firebase.
    private StorageReference mStorageRef;
    private Uri path;
    private Uri photoUrl;

    //Requeriments of email and pass.
    private final Pattern hasUppercase = Pattern.compile("[A-Z]");
    private final Pattern hasLowercase = Pattern.compile("[a-z]");
    private final Pattern hasNumber = Pattern.compile("\\d");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.user_settings_app);

        //Firebase auth connection and status.
        mAuth = FirebaseAuth.getInstance();

        //Declaration storage firebase.
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Init elements.
        init();

        //User status.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Validate email.
        boolean emailVerified = user.isEmailVerified();

        if (user != null && emailVerified) {

            mailUser.setText(user.getEmail());

            photoUrl = user.getPhotoUrl();

            if (photoUrl == null) {

                imageViewUser.setImageResource(R.drawable.perfil);

            } else {

                try {

                    new DownloadImageTask(imageViewUser).execute(photoUrl.toString());

                } catch (Exception e) {

                    imageViewUser.setImageResource(R.drawable.perfil);
                }
            }
            //             The user's ID, unique to the Firebase project. Do NOT use this value to
//             authenticate with your backend server, if you have one. Use
//             FirebaseUser.getToken() instead.
//                String uid = user.getUid();
        } else {
            logout();
        }

        imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });
        optionMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (invisibleElementsEmail) {
                    noVisibleElements(2);
                    invisibleElementsEmail = false;
                } else {
                    noVisibleElements(0);
                    closeInputManager(newEmail);
                    clearText();
                    invisibleElementsEmail = true;
                }
            }
        });
        optionPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInvisibleElementsPass) {
                    noVisibleElements(3);
                    isInvisibleElementsPass = false;
                } else {
                    noVisibleElements(0);
                    closeInputManager(newPass);
                    clearText();
                    isInvisibleElementsPass = true;
                }
            }
        });
        optionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInvisibleElementsDelete) {
                    noVisibleElements(4);
                    isInvisibleElementsDelete = false;
                } else {
                    noVisibleElements(0);
                    closeInputManager(actualPassword);
                    clearText();
                    isInvisibleElementsDelete = true;
                }
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

        //Content for elements.
        relativeLayoutUser = (RelativeLayout) findViewById(R.id.rLayoutUser);
        alternativeLinear = (LinearLayout) findViewById(R.id.alternative_linear);

        //Cardview elements.
        imageViewUser = (CircleImageView) findViewById(R.id.cardPhotoUser);
        imageViewUser.setImageResource(R.drawable.perfil);
        mailUser = (TextView) findViewById(R.id.cardMailUser);
        notes = (TextView) findViewById(R.id.item_list);
        sharedNotes = (TextView) findViewById(R.id.item_shared);
        votes = (TextView) findViewById(R.id.item_votes);

        //Layout Visible or Gone componets.
        layoutEmailVisible = (LinearLayout) findViewById(R.id.vistaEmailVisible);
        layoutPasswordVisible = (LinearLayout) findViewById(R.id.vistaPassVisible);
        layoutDeleteVisible = (LinearLayout) findViewById(R.id.vistaDeleteVisible);

        invisibleElementsEmail = true;
        isInvisibleElementsPass = true;
        isInvisibleElementsDelete = true;

        optionMail = (TextView) findViewById(R.id.desplegableEmail);
        optionPassword = (TextView) findViewById(R.id.desplegablePass);
        optionDelete = (TextView) findViewById(R.id.desplegableDeleteAccount);

        //Editable text
        newEmail = (EditText) findViewById(R.id.mail);
        newPass = (EditText) findViewById(R.id.password);
        old_password = (EditText) findViewById(R.id.password_old);
        actualPassword = (EditText) findViewById(R.id.password_for_delete);

        //Textview for onClickListeners.
        confirmEmail = (TextView) findViewById(R.id.change_email);
        confirmPass = (TextView) findViewById(R.id.change_pass);
        confirmDelete = (TextView) findViewById(R.id.delete_account);

        //Progressbar
        progressBar = (ProgressBar) findViewById(R.id.settings_progress);

        noVisibleElements(0);
    }

    //Atributes for visibility elements.
    private void noVisibleElements(int position) {
        switch (position) {
            case 0:
                relativeLayoutUser.setVisibility(View.VISIBLE);
                alternativeLinear.setVisibility(View.VISIBLE);
                layoutEmailVisible.setVisibility(View.GONE);
                layoutPasswordVisible.setVisibility(View.GONE);
                layoutDeleteVisible.setVisibility(View.GONE);
                break;
            case 1:
                alternativeLinear.setVisibility(View.GONE);
                relativeLayoutUser.setVisibility(View.GONE);
                break;
            case 2:
                relativeLayoutUser.setVisibility(View.VISIBLE);
                alternativeLinear.setVisibility(View.VISIBLE);

                layoutEmailVisible.setVisibility(View.VISIBLE);
                layoutPasswordVisible.setVisibility(View.GONE);
                layoutDeleteVisible.setVisibility(View.GONE);
                break;
            case 3:
                relativeLayoutUser.setVisibility(View.VISIBLE);
                alternativeLinear.setVisibility(View.VISIBLE);

                layoutEmailVisible.setVisibility(View.GONE);
                layoutPasswordVisible.setVisibility(View.VISIBLE);
                layoutDeleteVisible.setVisibility(View.GONE);
                break;
            case 4:
                relativeLayoutUser.setVisibility(View.VISIBLE);
                alternativeLinear.setVisibility(View.VISIBLE);

                layoutEmailVisible.setVisibility(View.GONE);
                layoutPasswordVisible.setVisibility(View.GONE);
                layoutDeleteVisible.setVisibility(View.VISIBLE);
                break;
        }
    }

    //Change email in firebase.
    private void changeEmail(String email) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            closeInputManager(newEmail);
            progressBar.setVisibility(View.VISIBLE);
            noVisibleElements(1);
            user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        noVisibleElements(0);
                        alertVerifyEmail();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        noVisibleElements(0);
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

                                alert.dismiss();
                                deleteImageFirebase();

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
            noVisibleElements(1);
            user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        NotesApp.password_user = password;
                        progressBar.setVisibility(View.GONE);
                        relativeLayoutUser.setVisibility(View.VISIBLE);
                        layoutPasswordVisible.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.change_pass), Toast.LENGTH_SHORT);
                        toast.show();
                        noVisibleElements(0);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        relativeLayoutUser.setVisibility(View.VISIBLE);
                        layoutPasswordVisible.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_change_pass), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }
    }

    //Delete account in firebase.
    public void deleteAccount() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            progressBar.setVisibility(View.VISIBLE);
            noVisibleElements(1);
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    //Reset global variables.
                    NotesApp.userLogged = false;
                    NotesApp.email_user = null;
                    NotesApp.password_user = null;

                    //Exit and finish activity.
                    mAuth.signOut();
                    finishAffinity();

                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT);
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

    //Clear all editText.
    private void clearText() {
        newEmail.setText("");
        old_password.setText("");
        newPass.setText("");
        actualPassword.setText("");
    }

    //Search image in resoures of phone.
    private void loadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Select program"), 10);
    }

    //Override method onActivityResult for request image.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            //Directory for photo.
            path = data.getData();
            //Upload photo to firebase storage.
            uploadFile(File.separator + "ImageProfile" + File.separator + NotesApp.email_user);
        }
    }

    //Upload photo to firebase storage.
    private void uploadFile(final String pathRoute) {

        if (path != null) {

            noVisibleElements(1);
            progressBar.setVisibility(View.VISIBLE);

            final StorageReference ref = mStorageRef.child(pathRoute);
            ref.putFile(path)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            noVisibleElements(0);
                            progressBar.setVisibility(View.GONE);
                            //Get url from image.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //Method updateProfile.
                            updateProfile(downloadUrl);
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_succeful), Toast.LENGTH_LONG);
                            toast.show();
                            new DownloadImageTask(imageViewUser).execute(downloadUrl.toString());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            noVisibleElements(0);
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_failed), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_failed), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    //Update profile user.
    public void updateProfile(final Uri downloadUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                //No use this line for name for user.
                // .setDisplayName("Jane Q. User")
                .setPhotoUri(downloadUrl)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    //Download image for circle imageView with AsyncTask.
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        CircleImageView bmImage;

        public DownloadImageTask(CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            imageViewUser.setImageBitmap(result);
        }
    }
    //When delete profile, delete photo porfile with firebase.
    public void deleteImageFirebase(){

        if (actualPassword.getText().toString().equals(NotesApp.password_user)) {

            noVisibleElements(1);
            progressBar.setVisibility(View.VISIBLE);
            // Create a storage reference from our app
            FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance().getReference().getStorage();

            // Create a reference to the file to delete
            StorageReference desertRef = mFirebaseStorage.getReferenceFromUrl(photoUrl.toString());

            // Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    noVisibleElements(0);
                    progressBar.setVisibility(View.GONE);

                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.delete_image_succeful), Toast.LENGTH_LONG);
                    toast.show();
                    //When finish action deleteImage go deleteAccount.
                    deleteAccount();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.delete_image_error), Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        }else {
            actualPassword.setText("");
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.equals_pass), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //Listener for error delete image.
    class MyFailureListener implements OnFailureListener {
        @Override
        public void onFailure(@NonNull Exception exception) {
            int errorCode = ((StorageException) exception).getErrorCode();
            String errorMessage = exception.getMessage();
            // test the errorCode and errorMessage, and handle accordingly
            System.out.println(errorMessage);
        }
    }
}

//Se detecta un error que ocurre en algunas ocasiones en el metodo de borrar imagen. Próximo dia prueba de try/catch en el método.