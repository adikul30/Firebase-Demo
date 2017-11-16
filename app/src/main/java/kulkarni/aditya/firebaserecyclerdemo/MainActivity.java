package kulkarni.aditya.firebaserecyclerdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference storageRef;
    private StorageReference profilesRef;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Courses, MainActivity.CoursesViewHolder>
            mFirebaseAdapter;

    private static final int READ_REQUEST_CODE = 1337;
    private static final String TAG = "MainActivity";
    private static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageView profileImage;
    private Uri downloadUrl;


    public static class CoursesViewHolder extends RecyclerView.ViewHolder {

        TextView courseName;

        public CoursesViewHolder(View v) {
            super(v);
            courseName = (TextView) itemView.findViewById(R.id.course_name);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setReferences();

        profileImage = (ImageView) findViewById(R.id.profileImage);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, READ_REQUEST_CODE);

            }
        });

        mMessageRecyclerView = (RecyclerView) findViewById(R.id.course_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Courses, CoursesViewHolder>(Courses.class,R.layout.course_layout_item,CoursesViewHolder.class,mFirebaseDatabaseReference.child("courses")) {
            @Override
            protected void populateViewHolder(CoursesViewHolder viewHolder, Courses model, int position) {
                viewHolder.courseName.setText(model.name);
            }
        };

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

    }

    public void setReferences(){
        mUsername = ANONYMOUS;
        mFirebaseStorage = FirebaseStorage.getInstance();
        storageRef = mFirebaseStorage.getReference();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                //Toast.makeText(this,uri.toString(),Toast.LENGTH_LONG).show();

                uploadImage(uri);
                Toast.makeText(MainActivity.this,"Upload started !",Toast.LENGTH_SHORT).show();


            }
            else Toast.makeText(this,"Some Error !",Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImage(Uri uriFromFile){
        //Calculate hash for a unique value
//        int imageHash = uriFromFile.hashCode();
//        profilesRef = storageRef.child("profiles").child(String.valueOf(imageHash));

        profilesRef = storageRef.child("profiles");

        UploadTask uploadTask = profilesRef.putFile(uriFromFile);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("downloadUrl","unsuccessful");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d("downloadUrl",String.valueOf(downloadUrl));
                Toast.makeText(MainActivity.this,"Upload successful !",Toast.LENGTH_SHORT).show();
                downloadImage(downloadUrl);
            }
        });
    }

    public void downloadImage(Uri downloadUri){
        // Reference to an image file in Firebase Storage
        StorageReference httpsReference = mFirebaseStorage.getReferenceFromUrl(downloadUri.toString());

        // Load the image using Glide
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(httpsReference)
                .into(profileImage);
    }
}
