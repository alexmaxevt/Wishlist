package ru.evtukhov.android.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private List<Note> notes = new ArrayList<>();
    private NoteRepository noteRepository = App.getNoteRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        this.setTitle(R.string.app_newNotesTitle);
        getNotes();
        initView();
    }

    private void getNotes() {
        try {
            notes = noteRepository.getNotes();
        } catch (EmptyStackException e) {
            e.getMessage();
        }
        if (notes != null) {
            BaseAdapter adapter = new NoteAdapter(this, notes);
            ListView listView = findViewById(R.id.listViewNotes);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, R.string.app_notesAddDataDonOpen, Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        FloatingActionButton buttonAddNewNote = findViewById(R.id.fab);
        buttonAddNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteListActivity.this, CreateNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(NoteListActivity.this, SettingActivity.class);
            startActivity(intent);
            return false;
        }

        return true;
    }
}
