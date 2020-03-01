package ru.evtukhov.android.wishlist;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Note implements Serializable {
    private String textNameNote;
    private String textBodyNote;
    private boolean checkIsChecked;
    private String id;
    private Date deadlineDate;
    private Date lastModifiedDate;

    Note(String id, String textNameNote, String textBodyNote, boolean checkIsChecked, Date lastModifiedDate, Date deadlineDate) {
        this.id = id;
        this.textNameNote = textNameNote;
        this.textBodyNote = textBodyNote;
        this.checkIsChecked = checkIsChecked;
        this.lastModifiedDate = lastModifiedDate;
        this.deadlineDate = deadlineDate;
    }

    String getTextNameNote() {
        return textNameNote;
    }

    boolean isCheckIsChecked() {
        return checkIsChecked;
    }

    public void setCheckIsChecked(boolean checkIsChecked) {
        this.checkIsChecked = checkIsChecked;
    }

    public void setTextNameNote(String textNameNote) {
        this.textNameNote = textNameNote;
    }

    String getTextBodyNote() {
        return textBodyNote;
    }

    public void setTextBodyNote(String textBodyNote) {
        this.textBodyNote = textBodyNote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(textNameNote, note.textNameNote) &&
                Objects.equals(textBodyNote, note.textBodyNote) &&
                Objects.equals(deadlineDate, note.deadlineDate) &&
                Objects.equals(checkIsChecked, note.checkIsChecked) &&
                Objects.equals(lastModifiedDate, note.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, textNameNote, textBodyNote, checkIsChecked, lastModifiedDate, deadlineDate);
    }
}
