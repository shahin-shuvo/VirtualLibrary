package com.sasam.virtuallibrary.GroupLibrary;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sasam.virtuallibrary.Books.Book;
import com.sasam.virtuallibrary.R;


import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView label, author;
        private CheckBox checkBox;


        RecyclerViewHolder(View view) {
            super(view);
            label = (TextView) view.findViewById(R.id.label);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            author = (TextView) view.findViewById(R.id.label_author);


        }

    }

    private ArrayList<Book> bookList;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;
    private Boolean[] disabledBooks;


    public RecyclerViewAdapter(Context context, ArrayList<Book> bookList) {
        this.bookList = bookList;
        this.context = context;
        disabledBooks = new Boolean[bookList.size()];
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tester, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {
        holder.label.setText(bookList.get(i).getBookName());
        holder.checkBox.setChecked(mSelectedItemsIds.get(i));

        if (bookList.get(i).getGroupID().equals("-1") == false) {

            disabledBooks[i] = true;
            holder.label.setTextColor(Color.parseColor("#c0c0c0"));
            holder.author.setText("Already added to other groups");
            holder.checkBox.setEnabled(false);

        } else {
            disabledBooks[i] = false;
            holder.label.setTextColor(Color.parseColor("#0084FF"));
            holder.author.setText(bookList.get(i).getAuthors());
            holder.checkBox.setEnabled(true);
        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBox(i, !mSelectedItemsIds.get(i));
            }
        });

        holder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBox(i, !mSelectedItemsIds.get(i));
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != bookList ? bookList.size() : 0);
    }

    /**
     * Remove all checkbox Selection
     **/
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    /**
     * Check the Checkbox if not checked
     **/
    public void checkCheckBox(int position, boolean value) {

        if (disabledBooks[position] == true) return;
        if (value)
            mSelectedItemsIds.put(position, true);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


}