package com.example.sandhyayalla.firebaseexample;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class ExpenseActivity extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    EditText overlattext;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);


        /*Log.d("demo","expense started ");
       // FirebaseStorage storage = FirebaseStorage.getInstance("firebaseexample");
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://my-custom-bucket");
        if(storage!=null
                ) {
            Log.d("demo", "storage" + storage.toString());
        }

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Create a child reference
// imagesRef now points to "images"
        StorageReference imagesRef = storageRef.child("images");

// spaceRef now points to "images/space.jpg
// imagesRef still points to "images"
        StorageReference spaceRef = storageRef.child("images/space.jpg");

        //getParent allows us to move our reference to a parent node
// imagesRef now points to 'images'
                imagesRef = spaceRef.getParent();


                // getRoot allows us to move all the way back to the top of our bucket
// rootRef now points to the root
        StorageReference rootRef = spaceRef.getRoot();

        ImageView imageView=(ImageView)findViewById(R.id.imageView);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        // Create a storage reference from our app
        StorageReference storageRef1 = storage.getReference();

// Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef1.child("mountains.jpg");

// Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef1.child("images/mountains.jpg");
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
        Log.d("demo","not able to upload pic"+e);
            }
        });
        */


    }
}
