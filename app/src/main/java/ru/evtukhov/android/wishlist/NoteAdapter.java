package ru.evtukhov.android.wishlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends BaseAdapter {
    private List<Note> notesList;
    private LayoutInflater layoutInFlater;
    private Context myContext;
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    private NoteRepository noteRepository = App.getNoteRepository();

    NoteAdapter(Context context, List<Note> notesList) {
        myContext = context;
        this.notesList = notesList;
        layoutInFlater = LayoutInflater.from(context);

    }

    void addItem(Note item) {
        this.noteRepository.saveNote(item);
        notifyDataSetChanged();
    }

    private void removeItem(int position) {
        Note note = notesList.get(position);
        notesList.remove(position);
        try {
            noteRepository.deleteById(note);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (notesList == null) return 0;
        return notesList.size();
    }

    @Override
    public Object getItem(int position) {
        return notesList.get(position);
    }

    private Note getNote(int position) {
        return notesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInFlater.inflate(R.layout.item_note, parent, false);
        }
        final Note notePosition = getNote(position);
        TextView textViewName = view.findViewById(R.id.textViewHead);
        TextView textViewBody = view.findViewById(R.id.textViewBody);
        TextView textViewDate = view.findViewById(R.id.textViewDeadLineDay);
        String textName = notePosition.getTextNameNote();
        String textBody = notePosition.getTextBodyNote();
        if ("".equals(textName)) {
            textViewName.setVisibility(View.GONE);
        } else {
            textViewName.setVisibility(View.VISIBLE);
            textViewName.setText(textName);
        }
        if ("".equals(textBody)) {
            if ("".equals(textName) && notePosition.getDeadlineDate() == null) {
                textViewBody.setVisibility(View.VISIBLE);
            } else {
                textViewBody.setVisibility(View.GONE);
            }
        } else {
            textViewBody.setVisibility(View.VISIBLE);
            textViewBody.setText(textBody);
        }
        textViewBody.setText(textBody);
        if (notePosition.getDeadlineDate() == null) {
            textViewDate.setVisibility(View.GONE);
        }
        else {
            textViewDate.setVisibility(View.VISIBLE);
            textViewDate.setText(format.format(notePosition.getDeadlineDate()));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note editNote = new Note(notePosition.getId(), notePosition.getTextNameNote(),
                        notePosition.getTextBodyNote(), notePosition.isCheckIsChecked(),
                        notePosition.getDeadlineDate(), notePosition.getLastModifiedDate());
                Intent intent = new Intent(myContext, CreateNoteActivity.class);
                intent.putExtra(Note.class.getSimpleName(), editNote);
                myContext.startActivity(intent);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                builder.setTitle(R.string.app_deleteDialogTitle)
                        .setMessage(R.string.app_deleteDialogMessage)
                        .setIcon(R.drawable.ic_trash)
                        .setCancelable(true)
                        .setPositiveButton(R.string.app_deleteDialogPositiveButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeItem(position);
                            }
                        })
                        .setNegativeButton(R.string.app_deleteDialogNegativeButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        });
        return view;
    }
}
