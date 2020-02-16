package ru.evtukhov.android.wishlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Keystore {
    void setPin(@NonNull String pin);

    @Nullable
    String getPin();

    boolean isPinCodeSet();
}
