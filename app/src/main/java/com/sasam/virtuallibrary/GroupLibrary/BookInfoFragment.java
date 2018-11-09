package com.sasam.virtuallibrary.GroupLibrary;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
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
import com.sasam.virtuallibrary.BookRequest.BookRequestData;
import com.sasam.virtuallibrary.Books.Book;
import com.sasam.virtuallibrary.ChatRoom.ChatListActivity;
import com.sasam.virtuallibrary.R;
import com.sasam.virtuallibrary.Util.GlideApp;

import cn.pedant.SweetAlert.SweetAlertDialog;

// Book book = (Book) getIntent().getSerializableExtra("selected");

public class BookInfoFragment extends AppCompatActivity {

    private String groupId;
    private FirebaseDatabase mdatabase;
    public DatabaseReference mDatabase, status;
    private FirebaseStorage storage;
    private FirebaseUser user;
    private StorageReference storageReference;
    private DatabaseReference ref;
    private ImageView imageView;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    private TextView bookName, authorName, bookDescription, bookEdition, bookPublisher, ownerName, bookStatus;
    private Button requestBooks;
    String txt,email;
    public BookInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_book_info);


        this.setTitle("Book Details");
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final Book book =  (Book) getIntent().getSerializableExtra("selected");
        groupId = getIntent().getExtras().getString("groupID");


        bookName = findViewById(R.id.bookName);
        authorName = findViewById(R.id.bookAuthors);
        bookDescription = findViewById(R.id.bookDescription);
        bookEdition = findViewById(R.id.bookEdition);
        bookPublisher = findViewById(R.id.bookPublisher);
        ownerName = findViewById(R.id.bookOwner);
        bookStatus = findViewById(R.id.bookStatus);
        requestBooks = findViewById(R.id.removeBooks);

        requestBooks.setText("Request book");
        imageView = findViewById(R.id.bookCover);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        ref = mdatabase.getReference();
        ref.child("UserInfo").child(book.getOwnerID()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                txt = (String) dataSnapshot.getValue();
                ownerName.setText(txt);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref.child("UserInfo").child(book.getOwnerID()).child("email").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                email = (String) dataSnapshot.getValue();
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
                if(book.getOwnerID().equals(user.getUid()))
                {
                    Toast.makeText(getApplication(), "This is your own book!", Toast.LENGTH_SHORT).show();
                }
                else if(book.getCurrentOwner().equals("-1")==false){
                    Toast.makeText(getApplication(), "Book Not available", Toast.LENGTH_SHORT).show();
                }
                else{
                    View menuItemView = findViewById(R.id.removeBooks);
                    final PopupMenu popupMenu = new PopupMenu(BookInfoFragment.this,menuItemView);
                    popupMenu.getMenuInflater().inflate(R.menu.book_req_menu,popupMenu.getMenu());
                    popupMenu.setGravity(Gravity.END);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            {
                                int id = item.getItemId();
                                if(id ==  R.id.msgReq){
                                    myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                    myClip = ClipData.newPlainText("text", email);
                                    myClipboard.setPrimaryClip(myClip);
                                    new SweetAlertDialog(BookInfoFragment.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                            .setCustomImage(R.drawable.chating)
                                            .setTitleText("Use this email for sending message to the owner")
                                            .setContentText(email)
                                            .setConfirmText("Copy Adress & Chat")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    Intent intent = new Intent(BookInfoFragment.this, ChatListActivity.class);
                                                    startActivity(intent);
                                                    Toast.makeText(getApplicationContext(), "Adress Copied to clipboard",Toast.LENGTH_SHORT).show();

                                                }
                                            })
                                            .show();


                                }



                                if(id== R.id.sendReq){
                                    requestThisBook(book);
                                    Toast.makeText(getApplicationContext(), "Request sent",Toast.LENGTH_SHORT).show();



                                }

                            }
                            return false;
                        }
                    });
                    popupMenu.show();

                }
            }
        });

    }

    private void requestThisBook(Book book){

        ref = mdatabase.getReference();
        String reqId = ref.push().getKey();
        assert reqId != null;
        BookRequestData bookRequestData = new BookRequestData(reqId,book.getBookName(),book.getBookId(),user.getUid(),user.getDisplayName());
      //  BookRequestData bookRequestData = new BookRequestData(reqId,"Misir Ali","-LQoLXXpDjM_rceljZUK","XO2KIbQ96te2qkLfrEiK7KM9OSD3","Shahrear Bin Amin");
        ref.child("Users").child(book.getOwnerID()).child("pendingRequest").child(reqId).setValue(bookRequestData);

    }

    private void populateData(Book mBook){
        bookName.setText(mBook.getBookName());
        authorName.setText(mBook.getAuthors());
        bookDescription.setText(mBook.getDescription());
        bookEdition.setText(mBook.getEdition());
        bookPublisher.setText(mBook.getPublisher());
        if(mBook.getCurrentOwner().equals("-1")) {
            bookStatus.setText("Available");
        }
        else  bookStatus.setText("Unavailable");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
           finish();
        }
        return true;
    }


}
