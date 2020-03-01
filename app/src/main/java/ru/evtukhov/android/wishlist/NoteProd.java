package ru.evtukhov.android.wishlist;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

class NoteProd {
    static Note createNote(@Nullable String textNameNote, @Nullable String textBodyNote, boolean checked, @Nullable Date deadLineDate) {
        return new Note(
                UUID.randomUUID().toString(),
                textNameNote,
                textBodyNote,
                checked,
                new Date(),
                deadLineDate
        );
    }
}
