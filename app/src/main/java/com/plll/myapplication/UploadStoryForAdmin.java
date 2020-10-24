package com.plll.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.content.ContentResolver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;


public class UploadStoryForAdmin extends Fragment implements ImageAdpter.OnItemClickListener {
    public static final int PICK_IMAGE_REQUEST=1;
    private Button mButtonChooseImage;
    private Button mButtonUpload,mlist;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFilename;
    private RecyclerView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    StorageReference fileReference;
    private ImageAdpter mAdapter;
    private List<Upload> mUploads;
    boolean checker = false;

    public UploadStoryForAdmin() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_story_for_admin, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mlist=view.findViewById(R.id.list);
        mButtonChooseImage= view.findViewById(R.id.button);
        mButtonUpload= view.findViewById(R.id.button2);
        mTextViewShowUploads= view.findViewById(R.id.textView);
        mEditTextFilename= view.findViewById(R.id.editText);
        mImageView= view.findViewById(R.id.image_view);
        mImageView.setHasFixedSize(true);
        mImageView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUploads = new ArrayList<>();
        mProgressBar= view.findViewById(R.id.progress_bar);
        mStorageRef= FirebaseStorage.getInstance().getReference("uploadsForAdmin");//folder name
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploadsForAdmin");//database name

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        mlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hold =mEditTextFilename.getText().toString();
                if (hold.matches("") || checker==false){
                    Toast.makeText(getActivity(), "add image and words", Toast.LENGTH_SHORT).show();
                }
                else{
                    addImage();
                    mEditTextFilename.setText("");
                    mImageUri=null;
                    checker=false;
                }

            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadfile();
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
        Intent intent = new Intent(getActivity(), ImagesforAdmin.class);
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
            checker=true;
        }
    }
    private String getFileExtension(Uri uri){//might delete
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cR.getType(uri));

    }


    private void displayImages(){
        mAdapter = new ImageAdpter(getActivity(), mUploads);

        if (mAdapter == null) {
            Toast.makeText(getActivity(), "pry", Toast.LENGTH_SHORT).show();
        }
        mAdapter.setOnItemClickListener(this);
        mImageView.setAdapter(mAdapter);
    }
    private void addImage(){
        if (mImageUri!= null) {
            Upload holder= new Upload(mEditTextFilename.getText().toString().trim(),mImageUri.toString(),"");
            Toast.makeText(getActivity(), "image added", Toast.LENGTH_SHORT).show();
            mUploads.add(holder);
            displayImages();
        }

        else{
            Toast.makeText(getActivity(), "no pic", Toast.LENGTH_SHORT).show();
        }
    }

    private  void uploadfile() {
        if (mUploads.size()!=0){
        for (final Upload list : mUploads) {
            fileReference = mStorageRef.child("test1").child(Uri.parse(list.getImageUrl()).getLastPathSegment());

            fileReference.putFile(Uri.parse(list.getImageUrl())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uris) {
                            Uri downloadUrl = uris;
                            String key = mDatabaseRef.child("uploadsForAdmin").push().getKey();
                            Upload upload = new Upload(list.getName(),
                                    uris.toString(), key);
                            mDatabaseRef.child(key).setValue(upload);
                        }
                    });

                }
            })


                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "failed to upload " + e, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int) progress);
                }
            });


        }
        Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();

    }
        else{
            Toast.makeText(getActivity(), " please enter data un to the list", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        mUploads.remove(position);
        displayImages();

    }
}
