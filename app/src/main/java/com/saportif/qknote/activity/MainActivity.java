package com.saportif.qknote.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.saportif.qknote.adapter.DividerItemDecoration;
import com.saportif.qknote.adapter.RecyclerAdapter;
import com.saportif.qknote.adapter.RecyclerTouchListener;
import com.saportif.qknote.model.Note;
import com.saportif.qknote.adapter.NoteAdapter;
import com.saportif.qknote.R;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;


import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    //TODO: Check Update Listview
    Toolbar toolbar;
    TextView addNote;

    ArrayList<Note> notes;
    NoteAdapter adapter;

    private Realm realm;
    RecyclerView rvNotes;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addNote = (TextView) findViewById(R.id.addNote);
        rvNotes = (RecyclerView) findViewById(R.id.rvNotes);

        notes = new ArrayList<>();
        adapter = new NoteAdapter(this, notes);
        recyclerAdapter = new RecyclerAdapter(this, notes);
        rvNotes.setAdapter(recyclerAdapter);
        rvNotes.setHasFixedSize(true);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.addItemDecoration(new  DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));


        rvNotes.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvNotes, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent editNoteIntent = new Intent(MainActivity.this, EditNoteActivity.class);
                int notePos = position;

                editNoteIntent.putExtra("notePosition", notePos);
                editNoteIntent.putExtra("noteTitle", notes.get(position).getTitle());
                editNoteIntent.putExtra("noteDescription", notes.get(position).getDescription());
                editNoteIntent.putExtra("noteId", notes.get(position).getId());
                editNoteIntent.putExtra("noteIndex", String.valueOf(notes.get(position)));
                editNoteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(editNoteIntent, 2);
            }

            @Override
            public void onLongClick(View view, final int position) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                // set title
                alertDialogBuilder.setTitle("Silmek İstediğinizden Emin Misiniz?");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Click yes to exit!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Note item = notes.get(position);
                                String noteId = item.getId();
                                realm.beginTransaction();
                                RealmResults<Note> row = realm.where(Note.class).equalTo("id" ,noteId).findAll();
                                row.deleteAllFromRealm();
                                realm.commitTransaction();

                                notes.remove(position);
                                rvNotes.removeViewAt(position);
                                recyclerAdapter.notifyItemRemoved(position);
                                recyclerAdapter.notifyItemRangeChanged(position, notes.size());
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).create().show();

            }
        }));

        realm = Realm.getDefaultInstance();
        updateRV();

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNoteIntent = new Intent(getApplicationContext(), AddNoteActivity.class);
                addNoteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(addNoteIntent, 1);
            }
        });
    }

    public void updateRV() {
        RealmResults<Note> results = realm.where(Note.class).findAll();
        for(Note n : results) {
            notes.add(0, n);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String noteTitle = data.getStringExtra("noteTitle");
                String noteContent = data.getStringExtra("noteContent");
                String noteId = data.getStringExtra("noteId");

                Note newNote = new Note();
                newNote.setTitle(noteTitle);
                newNote.setDescription(noteContent);
                newNote.setId(noteId);

                notes.add(0, newNote);
                recyclerAdapter.notifyDataSetChanged();
            }
        }

        if(requestCode == 2) {
            if(resultCode == RESULT_OK) {
                realm.beginTransaction();
                int notePositionFromEdit = data.getIntExtra("notePosition", -1);
                Note noteEdit;
                noteEdit = notes.get(notePositionFromEdit);


                String updatedNoteTitle = data.getStringExtra("noteTitle");
                String updatedNoteDescription = data.getStringExtra("noteDescription");
                String noteId = data.getStringExtra("noteIdBack");

                noteEdit.setId(noteId);
                noteEdit.setTitle(updatedNoteTitle);
                noteEdit.setDescription(updatedNoteDescription);

                realm.copyToRealmOrUpdate(noteEdit);
                realm.commitTransaction();

                //recyclerAdapter.updateList(notes);

            }

            if(resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    protected void onDestroy () {
            super.onDestroy();
            realm.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerAdapter.notifyItemRangeChanged(0, recyclerAdapter.getItemCount());
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private void addSampleData() {
        Note note = new Note("Title", "Description");
        adapter.add(note);
        note = new Note("Title", "Description2Description2Description2Description2D");
        adapter.add(note);
        note = new Note("Title", "Description2Description2Description2Description2D");
        adapter.add(note);
        note = new Note("Title", "Description2Description2Description2Description2D");
        adapter.add(note);
        note = new Note("Title", "Description2Description2Description2Description2D");
        adapter.add(note);
        note = new Note("Title", "Description2Description2Description2Description2D");
        adapter.add(note);
        note = new Note("Title", "Description2Description2Description2Description2D");
        adapter.add(note);
    }

}
