package com.example.wrapped;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;

public class SignedInUser {

    static public SignedInUser user;
    private final String email;
    private final String spotifyUser;
    private ArrayList<File> pastWrappeds;

    /**
     * Constructor for a signed in user. Private due to use of the singleton pattern
     * @param email User's email
     * @param spotifyUser User's spotify user name
     */
    private SignedInUser(String email, String spotifyUser) {
        this.email = email;
        this.spotifyUser = spotifyUser;
    }

    /**
     * Sets the signed in user to one with the paramaters as it's data
     * @param email       User's email
     * @param spotifyUser User's spotify user name
     */
    static public void setUser(String email, String spotifyUser) {
        if (email == null || spotifyUser == null) {
            return;
        }
        //Pull list of wrappeds from the database and set pastWrappeds to that
        user = new SignedInUser(email, spotifyUser);
    }

    /**
     * Ensures that user is not null, if so creates a new user with the current logged in user's data
     * Should be called at the beginning of any file that uses the user for anything
     */
    static public void validateCurrentUser() {
        if (user == null) {
            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
            assert fbUser != null;
            user = new SignedInUser(fbUser.getEmail(), fbUser.getDisplayName());
        }
    }

    /**
     * Sets user to null and updates the saved list of past wrappeds
     * Only used when user is signed out
     */
    static public void SignUserOut() {
        //Replace the list of saved wrappeds in the database with the current one
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(user.email);
        myRef.setValue(user.pastWrappeds);
        user = null;
    }

    /**
     * returns the current signed in user
     * @return Current signed in user
     */
    static public SignedInUser getCurrentUser() {
        return user;
    }

    /**
     * Returns the current signed in user's email
     * @return Current signed in user's email
     */
    static public String getCurrentUserEmail() {
        if (user == null) {
            return null;
        }
        return user.email;
    }

    /**
     * Returns the current signed in user's spotify user name
     * @return Current signed in user's spotify user name
     */
    static public String getCurrentUserSpotify() {
        if (user == null) {
            return null;
        }
        return user.spotifyUser;
    }

    /**
     * Add a wrapped file to the list of past wrappeds for an account
     * @param screenshot file of the current wrapped
     */
    static public void addWrapped(File screenshot) {
        if (user.pastWrappeds == null) {
            user.pastWrappeds = new ArrayList<File>();
        }
        user.pastWrappeds.add(screenshot);
    }

    /**
     * Getter for list of past wrappeds
     * @return List of past wrappeds
     */
    static public ArrayList<File> getPastWrappeds() {
        return user.pastWrappeds;
    }
}