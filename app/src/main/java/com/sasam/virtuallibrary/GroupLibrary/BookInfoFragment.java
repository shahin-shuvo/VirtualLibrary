package com.sasam.virtuallibrary.GroupLibrary;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sasam.virtuallibrary.Books.Book;
import com.sasam.virtuallibrary.R;
import com.sasam.virtuallibrary.Util.GlideApp;

// Book book = (Book) getIntent().getSerializableExtra("selected");

public class BookInfoFragment extends Fragment {

    private String groupId;
    private FirebaseDatabase mdatabase;
    private FirebaseStorage storage;
    private FirebaseUser user;
    private StorageReference storageReference;
    private DatabaseReference ref;
    private ImageView imageView;

    private TextView bookName, authorName, bookDescription, bookEdition, bookPublisher, ownerName, bookStatus;
    private Button requestBooks;
    public BookInfoFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_book_info, container, false);

        Bundle bundle = getArguments();

   //     groupId = bundle.getString("groupID");
        final Book book = (Book) bundle.getSerializable("selected");

    //
    //        Toast.makeText(getContext(), book.getBookName() + " = ggggg", Toast.LENGTH_LONG).show();


//
        bookName = view.findViewById(R.id.bookName);
        authorName = view.findViewById(R.id.bookAuthors);
        bookDescription = view.findViewById(R.id.bookDescription);
        bookEdition = view.findViewById(R.id.bookEdition);
        bookPublisher = view.findViewById(R.id.bookPublisher);
        ownerName = view.findViewById(R.id.bookOwner);
        bookStatus = view.findViewById(R.id.bookStatus);
        requestBooks = view.findViewById(R.id.removeBooks);

        requestBooks.setText("Request book");


        imageView = view.findViewById(R.id.bookCover);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        ref = mdatabase.getReference();
        ref.child("UserInfo").child(user.getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String txt = (String) dataSnapshot.getValue();
                //  Toast.makeText(BookDetails.this, txt, Toast.LENGTH_LONG).show();

                ownerName.setText(txt);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        GlideApp.with(this).load(book.getCoverUrl()).apply(new RequestOptions().override(100, 130)).into(imageView);




        populateData(book);


        requestBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(book.getCurrentOwner().equals("-1")==false){
                    Toast.makeText(getContext(), "Book Not available", Toast.LENGTH_SHORT).show();
                }
                else{

                    Toast.makeText(getContext(), "Book available", Toast.LENGTH_SHORT).show();
                  //  requestThisBook(book);
                }
            }
        });







        return view;
    }

    private void requestThisBook(Book mBook){

    }

    private void populateData(Book mBook){
        bookName.setText(mBook.getBookName());
        authorName.setText(mBook.getAuthors());
        bookDescription.setText(mBook.getDescription());
        bookEdition.setText(mBook.getEdition());
        bookPublisher.setText(mBook.getPublisher());
        bookStatus.setText("Available");
    }

}
