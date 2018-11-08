package com.sasam.virtuallibrary.Books;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sasam.virtuallibrary.R;

import java.io.IOException;
import java.util.UUID;

public class BookAdder extends AppCompatActivity {

    private FirebaseDatabase mdatabase;
    private FirebaseStorage storage;
    private FirebaseUser user;
    private StorageReference storageReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private Button dataUploader;
    private EditText nameField, authorField, descriptionField, editionField, publisherField;
    private ImageView coverPhoto;
    String downloadUrl="-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_adder);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        dataUploader = findViewById(R.id.adderUploadData);
        coverPhoto = findViewById(R.id.adderCoverPhoto);

        nameField = findViewById(R.id.adderBookName);
        editionField = findViewById(R.id.adderEdition);
        authorField = findViewById(R.id.adderAuthors);
        descriptionField = findViewById(R.id.adderDescription);
        publisherField = findViewById(R.id.adderPublisher);







        coverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        dataUploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

            }
        });

    }



    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                coverPhoto.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());


            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri.toString();
                            progressDialog.dismiss();

                           // Toast.makeText(BookAdder.this, downloadUrl, Toast.LENGTH_LONG).show();

                            Toast.makeText(BookAdder.this, downloadUrl, Toast.LENGTH_LONG).show();
                            uploadData();

                        }
                    });
                }
            }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(BookAdder.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void uploadData(){

        DatabaseReference ref = mdatabase.getReference().child("Books");
        String bookId = ref.push().getKey();

        Book book = new Book();

        book.setBookName(nameField.getText().toString().trim());
        book.setAuthors(authorField.getText().toString().trim());
        book.setDescription(descriptionField.getText().toString().trim());
        book.setEdition(editionField.getText().toString().trim());
        book.setPublisher(publisherField.getText().toString().trim());
        book.setOwnerID(user.getUid());
        book.setCurrentOwner("-1");
        book.setGroupID("-1");
        book.setStatus("Available");
        book.setCoverUrl(downloadUrl);
        book.setBookId(bookId);


        ref.child(bookId).setValue(book);



        //   this.finish();

    }
}
// request.auth != null