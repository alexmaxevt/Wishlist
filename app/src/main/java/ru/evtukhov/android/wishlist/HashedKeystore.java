package ru.evtukhov.android.wishlist;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class HashedKeystore implements Keystore {
    private final Context context;
    private final static String FILE_NAME = "keystore";

    HashedKeystore(Context context) {
        this.context = context;
    }

    @Override
    public void setPin(@NonNull final String pin) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)))) {
            final String hashPin = Hash.md5Custom(pin);
            bufferedWriter.write(hashPin);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPin() {
        try {
            final InputStreamReader inputStreamReader = new InputStreamReader(context.openFileInput(FILE_NAME));
            final BufferedReader reader = new BufferedReader(inputStreamReader);
            return reader.readLine();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean isPinCodeSet() {
        final File file = new File(context.getFilesDir(), FILE_NAME);
        return file.exists();
    }
}
