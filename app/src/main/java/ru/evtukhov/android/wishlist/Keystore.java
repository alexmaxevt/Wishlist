package ru.evtukhov.android.wishlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

public interface Keystore {
    void setPin(@NonNull String pin) throws IOException;

    @Nullable
    String getPin();

    boolean isPinCodeSet();
}
