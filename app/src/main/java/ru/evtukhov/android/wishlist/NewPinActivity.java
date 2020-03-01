package ru.evtukhov.android.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

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
        initViews();
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
                    try {
                        keystore.setPin(pin);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
