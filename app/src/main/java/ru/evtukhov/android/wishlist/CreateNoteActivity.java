package ru.evtukhov.android.wishlist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextBody;
    private EditText editTextDate;
    private CheckBox checkBoxSelect;

    private String textName;
    private String textBody;
    private String textDate;
    private String textDateOfCreate;
    private boolean checkIsChecked;

    private Note getNote;
    private Bundle bundle;

    final Calendar dateDeadLine = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        this.setTitle(R.string.app_newNotesTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            getNote = (Note) bundle.getSerializable(Note.class.getSimpleName());
            textDateOfCreate = getNote.getDate().toString();
            editTextName.setText(getNote.getTextNameNote());
            editTextBody.setText(getNote.getTextBodyNote());
            editTextDate.setText(getNote.getTextDateNote());
            checkBoxSelect.setChecked(getNote.isCheckIsChecked());
        }
    }

    private void initView() {
        editTextName = findViewById(R.id.editTextHead);
        editTextBody = findViewById(R.id.editTextBody);
        editTextDate = findViewById(R.id.editTextDeadLine);
        checkBoxSelect = findViewById(R.id.checkBoxHasDeadLine);
        checkBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if ("".equals(editTextDate.getText().toString())) {
                        setDate();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.app_noteDontDeadline, Toast.LENGTH_SHORT).show();
                    String convertText = editTextDate.getText().toString();
                    editTextDate.setHint(convertText);
                    editTextDate.getText().clear();
                }
            }
        });
        ImageButton buttonDate = findViewById(R.id.btnSelectDeadLine);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
    }

    public void setDate() {
        new DatePickerDialog(CreateNoteActivity.this, date,
                dateDeadLine.get(Calendar.YEAR),
                dateDeadLine.get(Calendar.MONTH),
                dateDeadLine.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateDeadLine.set(Calendar.YEAR, year);
            dateDeadLine.set(Calendar.MONTH, monthOfYear);
            dateDeadLine.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    private void setInitialDate() {
        editTextDate.setText(DateUtils.formatDateTime(this,
                dateDeadLine.getTimeInMillis(),
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
        checkBoxSelect.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_note) {
            Toast.makeText(CreateNoteActivity.this, R.string.app_pinButton, Toast.LENGTH_LONG).show();
            saveInfoNote();
            return false;
        } else if (id == android.R.id.home) {
            Intent intent = new Intent(CreateNoteActivity.this, NoteListActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void saveInfoNote() {
        if (bundle != null) {
            FileNote.remove(this, getNote);
        }
        textName = editTextName.getText().toString();
        textBody = editTextBody.getText().toString();
        textDate = editTextDate.getText().toString();
        checkIsChecked = checkBoxSelect.isChecked();
        Note noteNewInfo = new Note(textName, textBody, textDate, checkIsChecked);
        try {
            FileNote.exportToJSON(this, noteNewInfo);
            Toast.makeText(this, R.string.app_createNoteSuccess, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CreateNoteActivity.this, NoteListActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.app_createNoteDontSuccess, Toast.LENGTH_SHORT).show();
        }
    }
}
