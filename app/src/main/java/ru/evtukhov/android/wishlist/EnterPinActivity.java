package ru.evtukhov.android.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

public class EnterPinActivity extends AppCompatActivity {

    private String enterPin = "";
    private int countpush = 0;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btn0;
    private ImageButton btnDel;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private Keystore keystore = App.getKeystore();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);
        verifyFile();
        initViews();
        enterPinCode();
    }

    private void initViews() {
        btn1 = findViewById(R.id.number1);
        btn2 = findViewById(R.id.number2);
        btn3 = findViewById(R.id.number3);
        btn4 = findViewById(R.id.number4);
        btn5 = findViewById(R.id.number5);
        btn6 = findViewById(R.id.number6);
        btn7 = findViewById(R.id.number7);
        btn8 = findViewById(R.id.number8);
        btn9 = findViewById(R.id.number9);
        btn0 = findViewById(R.id.number0);
        btnDel = findViewById(R.id.btnDelete);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
    }

    private void setColorForCircle() {
        switch (countpush) {
            case 0:
                imageView1.setImageResource(R.drawable.shape);
                imageView2.setImageResource(R.drawable.shape);
                imageView3.setImageResource(R.drawable.shape);
                imageView4.setImageResource(R.drawable.shape);
                break;
            case 1:
                imageView1.setImageResource(R.drawable.shape_entered);
                imageView2.setImageResource(R.drawable.shape);
                imageView3.setImageResource(R.drawable.shape);
                imageView4.setImageResource(R.drawable.shape);
                break;
            case 2:
                imageView1.setImageResource(R.drawable.shape_entered);
                imageView2.setImageResource(R.drawable.shape_entered);
                imageView3.setImageResource(R.drawable.shape);
                imageView4.setImageResource(R.drawable.shape);
                break;
            case 3:
                imageView1.setImageResource(R.drawable.shape_entered);
                imageView2.setImageResource(R.drawable.shape_entered);
                imageView3.setImageResource(R.drawable.shape_entered);
                imageView4.setImageResource(R.drawable.shape);
                break;
            case 4:
                imageView1.setImageResource(R.drawable.shape_entered);
                imageView2.setImageResource(R.drawable.shape_entered);
                imageView3.setImageResource(R.drawable.shape_entered);
                imageView4.setImageResource(R.drawable.shape_entered);
                break;
        }
    }

    private void verifyCountNumber() {
        int lengthPin = 4;
        if (countpush == lengthPin) {
            verifyPin();
        }
    }

    private void enterPinCode() {
        btn1.setOnClickListener(getNumberButtonClickListener(R.string.app_enterPinNum1));
        btn2.setOnClickListener(getNumberButtonClickListener(R.string.app_enterPinNum2));
        btn3.setOnClickListener(getNumberButtonClickListener(R.string.app_enterPinNum3));
        btn4.setOnClickListener(getNumberButtonClickListener(R.string.app_enterPinNum4));
        btn5.setOnClickListener(getNumberButtonClickListener(R.string.app_enterPinNum5));
        btn6.setOnClickListener(getNumberButtonClickListener(R.string.app_enterPinNum6));
        btn7.setOnClickListener(getNumberButtonClickListener(R.string.app_enterPinNum7));
        btn8.setOnClickListener(getNumberButtonClickListener(R.string.app_enterPinNum8));
        btn9.setOnClickListener(getNumberButtonClickListener(R.string.app_enterPinNum9));
        btn0.setOnClickListener(getNumberButtonClickListener(R.string.app_enterPinNum0));
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countpush > 0) {
                    countpush--;
                    enterPin = enterPin.substring(0, enterPin.length() - 1);
                    setColorForCircle();
                }
            }
        });
    }

    private void verifyFile() {
        if (!keystore.isPinCodeSet()) {
            Intent intent = new Intent(EnterPinActivity.this, NewPinActivity.class);
            startActivity(intent);
        }
    }


    private void verifyPin() {
        final String pinStr = keystore.getPin();
        final String getPinTxt = Hash.md5Custom(enterPin);
        if (getPinTxt.equals(pinStr)) {
            countpush = 0;
            enterPin = "";
            Intent intent = new Intent(EnterPinActivity.this, NoteListActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(EnterPinActivity.this, R.string.app_enterPinError, Toast.LENGTH_LONG).show();
            countpush = 0;
            enterPin = "";
        }
    }

    private View.OnClickListener getNumberButtonClickListener(@StringRes final int numberResource) {
        final String number = getString(numberResource);
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterPin = enterPin + number;
                countpush++;
                verifyCountNumber();
                setColorForCircle();
            }
        };
    }
}
