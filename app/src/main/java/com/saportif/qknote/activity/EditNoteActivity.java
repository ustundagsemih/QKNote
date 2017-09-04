package com.saportif.qknote.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.saportif.qknote.model.Note;
import com.saportif.qknote.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class EditNoteActivity extends AppCompatActivity {

    EditText et_note_title, et_note_description;
    String noteId;
    Toolbar toolbar;
    Note note;

    Realm realm;

    int notePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
//      getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Düzenle");


        et_note_title = (EditText) findViewById(R.id.et_note_title);
        et_note_description = (EditText) findViewById(R.id.et_note_description);

        Intent editNoteIntent = getIntent();
        String title = editNoteIntent.getStringExtra("noteTitle");
        String description = editNoteIntent.getStringExtra("noteDescription");
        notePosition = editNoteIntent.getIntExtra("notePosition", -1);
        noteId = editNoteIntent.getStringExtra("noteId");

        et_note_title.setText(title);
        et_note_description.setText(description);

        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onBackPressed() {
        updateNote();
    }

    private void updateNote() {
        note = new Note();
        note.setNotePosition(notePosition);

        //realm.beginTransaction();
        String newTitle = et_note_title.getText().toString();
        String newDesc = et_note_description.getText().toString();

        if(noteId == null) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
        else {
            note.setId(noteId);
            note.setTitle(newTitle);
            note.setDescription(newDesc);
            note.setNotePosition(notePosition);

            Intent editIntent = new Intent();
            editIntent.putExtra("noteTitle", note.getTitle());
            editIntent.putExtra("noteDescription", note.getDescription());
            editIntent.putExtra("noteIdBack", note.getId());
            editIntent.putExtra("notePosition", note.getNotePosition());

            editIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            setResult(RESULT_OK, editIntent);
            finish();

            //realm.copyToRealmOrUpdate(note);
            //realm.commitTransaction();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.delete) {
            //TODO: implement delete function
        }

        return super.onOptionsItemSelected(item);
    }
}
