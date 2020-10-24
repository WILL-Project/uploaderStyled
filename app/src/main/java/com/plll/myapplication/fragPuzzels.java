package com.plll.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class fragPuzzels extends Fragment{
    public static final int PICK_IMAGE_REQUEST=1;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFilename;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri=null;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    StorageReference fileReference;
    private ImageAdpter mAdapter;
    boolean checker= false;
    public fragPuzzels() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_puzzels, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonChooseImage= view.findViewById(R.id.button1);
        mButtonUpload= view.findViewById(R.id.button21);
        mTextViewShowUploads= view.findViewById(R.id.textView1);
        mEditTextFilename= view.findViewById(R.id.editText1);
        mImageView= view.findViewById(R.id.image_view1);
        mProgressBar= view.findViewById(R.id.progress_bar1);
        mStorageRef= FirebaseStorage.getInstance().getReference("uploadsForPuzzel");//folder name
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploadsForPuzzel");//database name

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String holder =mEditTextFilename.getText().toString();

                if (holder.matches("") || checker==false){
                    Toast.makeText(getActivity(), "select an image or enter a tital", Toast.LENGTH_SHORT).show();
                }
                else {
                   uploadfile();
                    mImageView.setImageResource(android.R.color.transparent);
                    mEditTextFilename.setText("");
                    mImageUri=null;
                    checker=false;
                }

            }
        });
        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opemImageView();
            }
        });
    }

    private void opemImageView() {
        Intent intent = new Intent(getActivity(), images.class);
        startActivity(intent);

    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST
                && data!=null && data.getData()!=null){
            mImageUri=data.getData();
            Picasso.get().load(mImageUri).into(mImageView);
            checker=true;

        }
    }
    private  void uploadfile(){

            fileReference = mStorageRef.child("uploadsForPuzzel").child(Uri.parse(mImageUri.toString()).getLastPathSegment());

            fileReference.putFile(Uri.parse(mImageUri.toString())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uris) {
                            Uri downloadUrl =uris ;
                            String key=mDatabaseRef.child("uploadsForPuzzel").push().getKey();
                            Upload upload = new Upload(mEditTextFilename.getText().toString(),
                                    uris.toString(),key);
                            mDatabaseRef.child(key).setValue(upload);
                        }
                    });

                }
            })


                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "failed to upload "+e, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress =(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int)progress);
                }
            });



        Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();


    }

}
