package com.sasam.virtuallibrary.Books;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.sasam.virtuallibrary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


interface RepositoryObserver {
    void onUserDataChanged(ArrayList<Book> list);
}

interface Subject{  // Observables will implement it
    void registerObserver(RepositoryObserver repositoryObserver);
    void removeObserver(RepositoryObserver repositoryObserver);
    void notifyObservers();
}


class UserDataRepository implements Subject {   // Observables, will notify when data is loaded

    private static UserDataRepository INSTANCE = null;
    private FirebaseDatabase mdatabase;
    private FirebaseUser user;


    ArrayList<Book> bookList;

    private ArrayList<RepositoryObserver> mObservers;

    private UserDataRepository() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance();
        mObservers = new ArrayList<>();
        bookList = new ArrayList<>();
        getNewDataFromRemote();
    }


    // Fetching data from database

    private void getNewDataFromRemote() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                final ArrayList<Book> list = new ArrayList<>();
                DatabaseReference ref = mdatabase.getReference();
                ref.child("UserInfo").child(user.getUid()).child("BookList").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                            Book book = bookSnapshot.getValue(Book.class);
                            list.add(book);
                            //   Log.e("ttt", book.getBookName());
                        }
                        setUserData(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }, 10);
    }

    // Creates a Singleton of the class
    public static UserDataRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserDataRepository();
        }
        return INSTANCE;
    }

    @Override
    public void registerObserver(RepositoryObserver repositoryObserver) {
        if (!mObservers.contains(repositoryObserver)) {
            mObservers.add(repositoryObserver);
        }
    }

    @Override
    public void removeObserver(RepositoryObserver repositoryObserver) {
        if (mObservers.contains(repositoryObserver)) {
            mObservers.remove(repositoryObserver);
        }
    }

    @Override
    public void notifyObservers() {
        for (RepositoryObserver observer : mObservers) {
            observer.onUserDataChanged(bookList);
        }
    }

    public void setUserData(ArrayList<Book> list) {
        Log.e("Notifying now", "***");
        this.bookList = list;
        notifyObservers();
    }

    public ArrayList<Book> getUserData() {
        return bookList;
    }
}


public class BookListActivity extends AppCompatActivity implements RepositoryObserver {


    private ArrayList<HashMap<String, String>> list = new ArrayList<>();
    private SimpleAdapter simpleAdapter;
    private HashMap<String, String> item;
    private Subject mUserDataRepository;
    private ArrayList<Book> myLibrary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("My Library");
        setContentView(R.layout.activity_book_list);

        mUserDataRepository = UserDataRepository.getInstance();
        mUserDataRepository.registerObserver(this);


        FloatingActionButton fab = findViewById(R.id.addBookFab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookListActivity.this, BookAdder.class);
                startActivity(intent);
            }
        });

    }



    @Override
    public void onUserDataChanged(ArrayList<Book> list) {
        populateBookList(list);
    }


    public void populateBookList(final ArrayList<Book> bookList) {

        myLibrary = new ArrayList<>();

        for (int i = 0; i < bookList.size(); i++) {

            myLibrary.add(bookList.get(i));
            item = new HashMap<String, String>();
            item.put("line1", bookList.get(i).getBookName());
            item.put("line2", bookList.get(i).getAuthors());
            list.add(item);
        }
        simpleAdapter = new SimpleAdapter(this, list,
                R.layout.twolines,
                new String[]{"line1", "line2"},
                new int[]{R.id.line_a, R.id.line_b});

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(simpleAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Book book = myLibrary.get(position);
                //  String bookName = book.getBookName();
                // Toast.makeText(BookListActivity.this, bookName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BookListActivity.this, BookDetails.class);
                intent.putExtra("selected", book);
                startActivity(intent);
            }
        });
    }
}