package com.sasam.virtuallibrary.GroupLibrary;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;


public class LibraryFragment extends Fragment {

    String groupId;
    private SearchView searchView;

    private ListView listView;
    private ArrayList<HashMap<String, String>> list = new ArrayList<>();
    private SimpleAdapter simpleAdapter;
    private HashMap<String, String> item;
    private FirebaseDatabase mdatabase;
    private FirebaseUser user;
    private ArrayList<Book> bookList;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_library, container, false);
        groupId = getArguments().getString("groupID");
        //    Toast.makeText(getActivity(), groupId, Toast.LENGTH_SHORT).show();
        //  System.out.println(groupId);
        //  Toast.makeText(getActivity(),"groupId! = " + groupId,Toast.LENGTH_LONG).show();

//
        SearchView searchView = view.findViewById(R.id.my_search_view);
        listView = view.findViewById(R.id.my_list_view);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance();
        bookList = new ArrayList<>();


   //     Toast.makeText(getActivity(), "sz = " + bookList.size(), Toast.LENGTH_SHORT).show();


        fetchData(new MyCallback() {
            @Override
            public void onCallback(ArrayList<Book> myList) {
                for (int i = 0; i < myList.size(); i++) {
                    bookList.add(myList.get(i));
                    item = new HashMap<String, String>();
                    item.put("line1", myList.get(i).getBookName());
                    item.put("line2", myList.get(i).getAuthors());
                    list.add(item);

                }

                populateList();
                //    Toast.makeText(getActivity(), "sz = " + myList.size(), Toast.LENGTH_SHORT).show();

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (list.contains(query)) {
                    simpleAdapter.getFilter().filter(query);
                } else {
                    Toast.makeText(getContext(), "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                simpleAdapter.getFilter().filter(newText);
                return false;
            }
        });


        FloatingActionButton fab = view.findViewById(R.id.addBookToLibrary);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //          Toast.makeText(getActivity(), "groupId! = " + groupId, Toast.LENGTH_LONG).show();
                BookAdderFragment fragment = new BookAdderFragment();
                Bundle bundle = new Bundle();
                bundle.putString("groupID", groupId);

                fragment.setArguments(bundle);

                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.bottomTabGroupPage, fragment);
                fragmentTransaction.commit();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Book book = bookList.get(position);

                //  String bookName = book.getBookName();
                // Toast.makeText(getActivity(), book.getBookName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),BookInfoFragment.class);

              //  BookInfoFragment fragment = new BookInfoFragment();
                Bundle bundle = new Bundle();
              //  bundle.putString("groupID", groupId);
                //bundle.putSerializable("selected", book);
                intent.putExtra("selected", book);
                intent.putExtra("groupID", groupId);
                startActivity(intent);

             /*   fragment.setArguments(bundle);

                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.bottomTabGroupPage, fragment);
                fragmentTransaction.commit();*/
            }
        });

        return view;
    }

    private void populateList() {
        simpleAdapter = new SimpleAdapter(getActivity(), list,
                R.layout.twolines,
                new String[]{"line1", "line2"},
                new int[]{R.id.line_a, R.id.line_b});

        listView.setAdapter(simpleAdapter);
    }


    private void fetchData(final MyCallback myCallback) {
        DatabaseReference ref = mdatabase.getReference();
        ref.child("Books").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Book> myList = new ArrayList<>();

                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);

                    if (book.getGroupID().equals(groupId)) {
                        myList.add(book);
                    }
                }

                myCallback.onCallback(myList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private interface MyCallback {
        void onCallback(ArrayList<Book> list);
    }


}
