package com.appdesigndm.sharedlists.Helpers;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;

public class NotesApp extends Application {

    public static String email_user;
    public static String password_user;
    public static boolean userLogged = false;
    public static FirebaseUser currentUserApp;
}

