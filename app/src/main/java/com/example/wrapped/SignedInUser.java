package com.example.wrapped;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignedInUser {

    static public SignedInUser user;
    private final String email;
    private final String spotifyUser;

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
     * Sets user to null
     * Only used when user is signed out
     */
    static public void SignUserOut() {
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
}