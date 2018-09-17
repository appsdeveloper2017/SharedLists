package com.appdesigndm.sharedlists.Helpers;

import android.app.Application;

import com.appdesigndm.sharedlists.Models.UserModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotesApp extends Application {

    // BBDD
    public final static String BBDD_USERS = "Users";
    public final static String BBDD_MAIN_LISTS = "Lists";

    public static String email_user;
    public static String password_user;
    public static boolean userLogged = false;
    public static FirebaseUser currentUserApp;

    public static UserModel currentUserModel;

    public static void addUser(UserModel user) {
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();

        dbr.child(BBDD_USERS).child(user.getUserEmail()).setValue(user);
    }

    public static void deleteUser(UserModel user) {
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();

        dbr.child(BBDD_USERS).child(user.getUserEmail()).removeValue();
    }

    public static void updateUser(UserModel user) {
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();

        dbr.child(BBDD_USERS).child(user.getUserEmail()).setValue(user);
    }
}

