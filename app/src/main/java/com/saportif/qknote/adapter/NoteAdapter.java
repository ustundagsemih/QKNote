package com.saportif.qknote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.saportif.qknote.R;
import com.saportif.qknote.model.Note;

import java.util.ArrayList;


/**
 * Created by Semih on 13.09.2016.
 */
public class NoteAdapter extends ArrayAdapter<Note>  {

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }

    /*public NoteAdapter(Context context, RealmResults<Note> realmResults) {
        super(context, realmResults);
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Note note = getItem(position);

        if(convertView == null)  {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_note_title);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tv_note_description);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(note.getTitle());
        viewHolder.tvDescription.setText(note.getDescription());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
    }
}
