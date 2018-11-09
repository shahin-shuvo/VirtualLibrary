package com.sasam.virtuallibrary.Books;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.sasam.virtuallibrary.R;
import com.sasam.virtuallibrary.Util.GlideApp;


public class BookDetails extends AppCompatActivity {

    private FirebaseDatabase mdatabase;
    private FirebaseStorage storage;
    private FirebaseUser user;
    private StorageReference storageReference;
    private DatabaseReference ref;
    private ImageView imageView;

    TextView bookName, authorName, bookDescription, bookEdition, bookPublisher, ownerName, bookStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        this.setTitle("Book Details");
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Book book = (Book) getIntent().getSerializableExtra("selected");



        //  Toast.makeText(BookDetails.this, book.getBookName(), Toast.LENGTH_LONG).show();

        bookName = findViewById(R.id.bookName);
        authorName = findViewById(R.id.bookAuthors);
        bookDescription = findViewById(R.id.bookDescription);
        bookEdition = findViewById(R.id.bookEdition);
        bookPublisher = findViewById(R.id.bookPublisher);
        ownerName = findViewById(R.id.bookOwner);
        bookStatus = findViewById(R.id.bookStatus);
        populateData(book);


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









        imageView = findViewById(R.id.bookCover);


        GlideApp.with(this).load(book.getCoverUrl()).apply(new RequestOptions().override(100, 130)).into(imageView);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  removeBookFromLibrary(new Book());
                        dialog.cancel();
                    }
                });


        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        Button removeBooks = (Button) findViewById(R.id.removeBooks);

        removeBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });





    }

    public void removeBookFromLibrary(Book book){

    }

    public void populateData(Book book){
        bookName.setText(book.getBookName());
        authorName.setText(book.getAuthors());
        bookDescription.setText(book.getDescription());
        bookEdition.setText(book.getEdition());
        bookPublisher.setText(book.getPublisher());
        if(book.getCurrentOwner().equals("-1")) {
            bookStatus.setText("Available");
        }
        else  bookStatus.setText("Unavailable");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
            // this.getSupportFragmentManager().popBackStack();
        }
        return true;
    }


}