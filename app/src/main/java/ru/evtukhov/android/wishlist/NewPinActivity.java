package ru.evtukhov.android.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class NewPinActivity extends AppCompatActivity {

    private Button btnSave;
    private EditText editPin;
    private int LEGTHPIN = 4;
    private Keystore keystore = App.getKeystore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pin);
        this.setTitle(R.string.app_settingTitle);
        getHomeButton();
        initViews();

    }

    private void getHomeButton() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {
        btnSave = findViewById(R.id.btnSave);
        editPin = findViewById(R.id.pinCode);
        savePin();
    }

    private void savePin() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pin = editPin.getText().toString();
                if (pin.length() == LEGTHPIN) {
                    keystore.setPin(pin);
                    Toast.makeText(NewPinActivity.this, R.string.app_savePinSuccess, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NewPinActivity.this, NoteListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(NewPinActivity.this, R.string.app_savePinDontSuccess, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_exit) {
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
