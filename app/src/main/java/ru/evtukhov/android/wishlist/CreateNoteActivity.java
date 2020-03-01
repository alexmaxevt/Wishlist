package ru.evtukhov.android.wishlist;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextBody;
    private EditText editTextDate;
    private CheckBox checkBoxSelect;

    private String textName;
    private String textBody;
    private Date deadLineDateParse;

    private Note getNote;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    final Calendar dateDeadLine = Calendar.getInstance();
    private NoteRepository noteRepository = App.getNoteRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        this.setTitle(R.string.app_newNotesTitle);
        initView();
        initBundle();
    }

    private void setListActivity() {
        Intent intent = new Intent(CreateNoteActivity.this, NoteListActivity.class);
        startActivity(intent);
    }

    private void initBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.setTitle(R.string.app_noteEdit);
            getNote = (Note) bundle.getSerializable(Note.class.getSimpleName());
            if (getNote == null) {
                return;
            }
            editTextName.setText(getNote.getTextNameNote());
            editTextBody.setText(getNote.getTextBodyNote());
            if (getNote.getDeadlineDate() != null) {
                String deadLineDate = format.format(getNote.getDeadlineDate());
                editTextDate.setText(deadLineDate);
            } else {
                editTextDate.setText("");
            }
            checkBoxSelect.setChecked(getNote.isCheckIsChecked());
        }
    }

    private void initView() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        editTextName = findViewById(R.id.editTextHead);
        editTextBody = findViewById(R.id.editTextBody);
        editTextDate = findViewById(R.id.editTextDeadLine);
        checkBoxSelect = findViewById(R.id.checkBoxHasDeadLine);
        checkBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    Toast.makeText(getApplicationContext(), R.string.app_noteDontDeadline, Toast.LENGTH_SHORT).show();
                    editTextDate.getText().clear();
                    checkBoxSelect.setChecked(false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_note) {
            prepareInfoForSaving();
            return false;
        }
        return true;
    }

    private void saveInfoNote() {
        boolean checkIsChecked = checkBoxSelect.isChecked();
        Note noteNewInfo;
        if (getNote != null) {
            noteRepository.deleteById(getNote);
            noteNewInfo = new Note(getNote.getId(), textName, textBody, checkIsChecked, new Date(), deadLineDateParse);
        } else {
            noteNewInfo = NoteProd.createNote(textName, textBody, checkIsChecked, deadLineDateParse);
        }
        try {
            noteRepository.saveNote(noteNewInfo);
            Toast.makeText(this, R.string.app_createNoteSuccess, Toast.LENGTH_SHORT).show();
            setListActivity();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.app_createNoteDontSuccess, Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareInfoForSaving() {
        textName = editTextName.getText().toString();
        textBody = editTextBody.getText().toString();
        String textDate = editTextDate.getText().toString();
        if ("".equals(textDate)) {
            checkBoxSelect.setChecked(false);
            deadLineDateParse = null;
            saveInfoNote();
        } else {
            try {
                deadLineDateParse = format.parse(textDate);
            } catch (ParseException e) {
                deadLineDateParse = null;
            }
            checkBoxSelect.setChecked(true);
            saveInfoNote();
        }
    }

    public void setDate() {
        datePickerDialog = new DatePickerDialog(CreateNoteActivity.this, date,
                dateDeadLine.get(Calendar.YEAR),
                dateDeadLine.get(Calendar.MONTH),
                dateDeadLine.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(false);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.app_noteCancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datePickerDialog.dismiss();
                    }
                });
        datePickerDialog.show();
    }

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateDeadLine.set(Calendar.YEAR, year);
            dateDeadLine.set(Calendar.MONTH, monthOfYear);
            dateDeadLine.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    };
}
