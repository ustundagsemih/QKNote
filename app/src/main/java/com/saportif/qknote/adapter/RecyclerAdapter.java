package com.saportif.qknote.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saportif.qknote.R;
import com.saportif.qknote.model.Note;

import java.util.Collections;
import java.util.List;

/**
 * Created by Semih on 22.09.2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        public TextView title;
        public TextView description;

        public ViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.tv_note_title);
            description = (TextView) itemView.findViewById(R.id.tv_note_description);
        }

        //OnClickListener
        public void bind(final Note note) {
            title.setText(note.getTitle());
            description.setText(note.getDescription());
        }
    }

    private List<Note> mData = Collections.emptyList();
    private List<Note> mNotes;
    private Context mContext;

    public RecyclerAdapter(Context context, List<Note> notes) {
        mContext = context;
        mNotes = notes;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View noteView = inflater.inflate(R.layout.note_cardview, parent, false);

        final ViewHolder viewHolder = new ViewHolder(noteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = mNotes.get(position);

        TextView tvTitle = holder.title;
        tvTitle.setText(note.getTitle());
        TextView tvDescription = holder.description;
        tvDescription.setText(note.getDescription());

        /*if((position % 2) == 1) {
            holder.cv.setBackgroundColor(Color.RED);
        }*/
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateList(List<Note> data) {
        mData = data;
        notifyDataSetChanged();
    }
}
