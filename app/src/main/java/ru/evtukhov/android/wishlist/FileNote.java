package ru.evtukhov.android.wishlist;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FileNote implements NoteRepository {

    private static final String FILE_NAME_NOTES = "notes";
    private static Gson gson = new Gson();

    static void exportToJSON(Context context, Note note) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit()
                .putString(note.getDate().toString(), gson.toJson(note))
                .apply();
    }

    static void remove(Context context, Note note) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit()
                .remove(note.getDate().toString())
                .apply();
    }

    static List<Note> importFromJSON(Context context) {
        SharedPreferences preferences = getPreferences(context);
        Map<String, ?> all = preferences.getAll();
        List<Note> result = new ArrayList<>(all.size());
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            String noteSerialized = (String) entry.getValue();
            result.add(gson.fromJson(noteSerialized, Note.class));
        }
        Collections.sort(result, new Comparator<Note>() {
            @Override
            public int compare(Note thisNote, Note anotherNote) {
                if (anotherNote.getTextDateNote() != null) {
                    return anotherNote.getTextDateNote().compareTo(thisNote.getTextDateNote()) + anotherNote.getDate().compareTo(thisNote.getDate());
                }
                return anotherNote.getDate().compareTo(thisNote.getDate());
            }
        });
        return result;
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(FILE_NAME_NOTES, MODE_PRIVATE);
    }

    @Override
    public Note getNoteById(String id) {
        return null;
    }

    @Override
    public List<Note> getNotes() {
        return null;
    }

    @Override
    public void saveNote(Note note) {

    }

    @Override
    public void deleteById(String id) {

    }
}
