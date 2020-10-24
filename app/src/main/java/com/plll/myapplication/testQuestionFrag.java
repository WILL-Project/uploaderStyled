package com.plll.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class testQuestionFrag extends Fragment {
    public static final int PICK_IMAGE_REQUEST=1;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFilename;
    private ProgressBar mProgressBar;
    private DatabaseReference mDatabaseRef;

    public testQuestionFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_question, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mButtonUpload= view.findViewById(R.id.button3);
        mTextViewShowUploads= view.findViewById(R.id.textView3);
        mEditTextFilename= view.findViewById(R.id.editText3);
        mProgressBar= view.findViewById(R.id.progress_bar3);

        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploadsForTest");//database name

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String hold= mEditTextFilename.getText().toString();
                if (hold.matches("")) {
                    Toast.makeText(getActivity(), "please enter a question", Toast.LENGTH_SHORT).show();
               }
               else {
                    uploadfile();
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
    public void uploadfile(){
        String key=mDatabaseRef.child("uploadsForTest").push().getKey();
        TestClass upload = new TestClass(mEditTextFilename.getText().toString(),key);
        mDatabaseRef.child(key).setValue(upload);
        Toast.makeText(getActivity(), "uploaded test", Toast.LENGTH_SHORT).show();
    }
    private void opemImageView() {
        Intent intent = new Intent(getActivity(), questions1.class);
        startActivity(intent);

    }
}
