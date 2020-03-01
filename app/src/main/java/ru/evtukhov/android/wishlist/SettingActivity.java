package ru.evtukhov.android.wishlist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class SettingActivity extends AppCompatActivity {

    private EditText oldPin;
    private EditText newPin;
    private Button save;
    private int LEGTHPIN = 4;
    private Keystore keystore = App.getKeystore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.setTitle(R.string.app_pinEditTitle);
        getHomeButton();
        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getHomeButton() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        oldPin = findViewById(R.id.oldCode);
        newPin = findViewById(R.id.newPinCode);
        save = findViewById(R.id.btnSaveNewPin);
        saveNewPin();
    }

    private void saveNewPin() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pinOld = oldPin.getText().toString();
                final String pinNew = newPin.getText().toString();
                final String pinStr = keystore.getPin();
                final String getOldPinTxt = Hash.md5Custom(pinOld);
                final String getNewPinTxt = Hash.md5Custom(pinNew);
                if (pinOld.length() == LEGTHPIN & pinNew.length() == LEGTHPIN) {
                    if (getOldPinTxt.equals(pinStr)) {
                        if (getNewPinTxt.equals(pinStr)) {
                            Toast.makeText(SettingActivity.this, R.string.app_pinEquals, Toast.LENGTH_LONG).show();
                            oldPin.setText("");
                            newPin.setText("");
                        } else {
                            try {
                                keystore.setPin(pinNew);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(SettingActivity.this, R.string.app_savePinSuccess, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SettingActivity.this, NoteListActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(SettingActivity.this, R.string.app_pinOldEqualsError, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SettingActivity.this, R.string.app_pinLenError, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
