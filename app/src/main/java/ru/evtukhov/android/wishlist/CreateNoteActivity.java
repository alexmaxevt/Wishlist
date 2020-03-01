package ru.evtukhov.android.wishlist;

import android.app.DatePickerDialog;
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

import androidx.appcompat.app.ActionBar;
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
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    private NoteRepository noteRepository = App.getNoteRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        this.setTitle(R.string.app_newNotesTitle);
        initView();
        initBundle();
        getHomeButton();
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
                selectDate();
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
        String txtTitle = editTextName.getText().toString();
        String txtBody = editTextBody.getText().toString();
        String txtDate = editTextDate.getText().toString();
        boolean isCheck = checkBoxSelect.isChecked();
        if (id == R.id.action_note) {
            if ("".equals(txtTitle) && "".equals(txtBody) && !"".equals(txtDate) && !isCheck) {
                emptyFields();
            } else if ("".equals(txtTitle) && "".equals(txtBody) && "".equals(txtDate) && !isCheck) {
                emptyFields();
            } else if ("".equals(txtTitle) && "".equals(txtBody) && !"".equals(txtDate) && isCheck) {
                emptyFields();
            } else if ("".equals(txtTitle) && "".equals(txtBody) && "".equals(txtDate) && isCheck) {
                emptyFields();
            } else {
                prepareInfoForSaving();
            }
            return false;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void selectDate() {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String formatDay = "" + dayOfMonth;
                String formatMonth = "" + (month + 1);
                if (dayOfMonth < 10) {
                    formatDay = "0" + dayOfMonth;
                }
                if (month < 10) {
                    formatMonth = "0" + (month + 1);
                }
                String formatDate = formatDay + "." + formatMonth + "." + year;
                editTextDate.setText(formatDate);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
        checkBoxSelect.setChecked(true);
    }

    private void emptyFields() {
        Toast.makeText(this, R.string.app_noteEmpty, Toast.LENGTH_SHORT).show();
        setListActivity();
    }

    private void getHomeButton() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
