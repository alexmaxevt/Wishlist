package ru.evtukhov.android.wishlist;

import android.app.Application;

public class App extends Application {
    private static Keystore keystore;
    private static NoteRepository noteRepository;

    public static Keystore getKeystore() {
        return keystore;
    }

    public static NoteRepository getNoteRepository() {
        return noteRepository;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        keystore = new HashedKeystore(this);
        FileNote fileNoteRepository = new FileNote();
    }
}
