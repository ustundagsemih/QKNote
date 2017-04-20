package com.saportif.qknote.model;

import android.graphics.Color;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Semih on 13.09.2016.
 */
public class Note extends RealmObject {

    @PrimaryKey
    private String id;
    private String title;
    private String description;

    public int getNotePosition() {
        return notePosition;
    }

    public void setNotePosition(int notePosition) {
        this.notePosition = notePosition;
    }

    private int notePosition;

    public Note() {}

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
