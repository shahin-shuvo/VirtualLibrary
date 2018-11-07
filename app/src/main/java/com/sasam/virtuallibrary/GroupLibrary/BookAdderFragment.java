package com.sasam.virtuallibrary.GroupLibrary;


import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sasam.virtuallibrary.Books.Book;
import com.sasam.virtuallibrary.R;

import java.util.ArrayList;

public class BookAdderFragment extends Fragment {

    private Context context;
    private RecyclerViewAdapter adapter;
    private ArrayList<String> arrayList;
    private CheckBox selectButton;
    private ArrayList<Book> bookList;
    private FirebaseDatabase mdatabase;
    private FirebaseUser user;
    private String groupId;
    private String userId;

    public BookAdderFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_adder, null);
        groupId = getArguments().getString("groupID");


        return view;


    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectButton = (CheckBox) view.findViewById(R.id.select_button);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        mdatabase = FirebaseDatabase.getInstance();
        bookList = new ArrayList<>();


        fetchMyBooks(new MyCallback() {
            @Override
            public void onCallback(ArrayList<Book> list) {
                for (int i = 0; i < list.size(); i++) bookList.add(list.get(i));
                //   Collections.copy(bookList, list);
                populateRecyclerView(view);
            }
        });


        onClickEvent(view);
    }

    private void populateRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_add_to_group);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();

        for(int i = 0; i<bookList.size(); i++){
            arrayList.add("" + i);
        }

        adapter = new RecyclerViewAdapter(context, bookList);

        recyclerView.setAdapter(adapter);
    }

    private void fetchMyBooks(final MyCallback myCallback) {
        DatabaseReference ref = mdatabase.getReference();
        ref.child("Books").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Book> list = new ArrayList<>();

                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if(book.getOwnerID().equals(userId)){
                        list.add(book);
                    }
                    //   Log.e("ttt", book.getBookName());
                }

                myCallback.onCallback(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void onClickEvent(View view) {
        view.findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Integer> indices = new ArrayList<>();

                SparseBooleanArray selectedRows = adapter.getSelectedIds();
                if (selectedRows.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < selectedRows.size(); i++) {
                        if (selectedRows.valueAt(i)) {
                            String selectedRowLabel = arrayList.get(selectedRows.keyAt(i));
                            indices.add(Integer.parseInt(selectedRowLabel));
                            stringBuilder.append(selectedRowLabel + "\n");
                        }
                    }
                 //   Toast.makeText(context, "Selected Rows\n" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                        AddToThisGroup(indices);
                }

            }
        });

//        view.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SparseBooleanArray selectedRows = adapter.getSelectedIds();
//                if (selectedRows.size() > 0) {
//                    for (int i = (selectedRows.size() - 1); i >= 0; i--) {
//                        if (selectedRows.valueAt(i)) {
//                            arrayList.remove(selectedRows.keyAt(i));
//                        }
//                    }
//                    adapter.removeSelection();
//                }
//            }
//        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectButton.isChecked()) {
                    applyAll(true);
                } else {
                    applyAll(false);
                }
            }
        });


    }

    private void applyAll(boolean flag) {
        if (flag == true) {
            for (int i = 0; i < arrayList.size(); i++)
                adapter.checkCheckBox(i, true);
        } else {
            adapter.removeSelection();
        }
    }


    private interface MyCallback {
        void onCallback(ArrayList<Book> list);
    }


    private void AddToThisGroup(ArrayList<Integer> indices){

        DatabaseReference ref = mdatabase.getReference().child("Books");

        StringBuilder stringBuilder = new StringBuilder();


        for(int i = 0; i<indices.size(); i++){
            Book b = bookList.get(indices.get(i));

          //  ref.child(b.getBookId()).child("groupID").setValue(groupId);

            stringBuilder.append(b.getBookName());
            stringBuilder.append("\n");
        }

        Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_LONG).show();

    }




}