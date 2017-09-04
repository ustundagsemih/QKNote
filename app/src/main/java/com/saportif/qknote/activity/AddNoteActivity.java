package com.saportif.qknote.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saportif.qknote.model.Note;
import com.saportif.qknote.adapter.NoteAdapter;
import com.saportif.qknote.R;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;

public class AddNoteActivity extends AppCompatActivity {

    EditText et_note_title, et_note_description;
    Toolbar toolbar;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et_note_title = (EditText) findViewById(R.id.et_note_title);
        et_note_description = (EditText) findViewById(R.id.et_note_description);

        et_note_description.requestFocus();
        final InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onBackPressed() {
        saveToDb();
    }

    public void saveToDb() {
        realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Note note = realm.createObject(Note.class, UUID.randomUUID().toString());
        String note_title = et_note_title.getText().toString().trim();
        String note_description = et_note_description.getText().toString().trim();

        if(note_description.isEmpty()) {
            realm.cancelTransaction();
            finish();
        }
        else {
            note.setTitle(note_title);
            note.setDescription(note_description);
            //note.setId(UUID.randomUUID().toString());

            Intent intent = new Intent();
            intent.putExtra("noteTitle", note.getTitle());
            intent.putExtra("noteContent", note.getDescription());
            intent.putExtra("noteId", note.getId());
            setResult(RESULT_OK, intent);
            finish();
            realm.copyToRealmOrUpdate(note);
            realm.commitTransaction();
        }
    }
}
