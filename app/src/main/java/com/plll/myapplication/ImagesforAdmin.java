package com.plll.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImagesforAdmin  extends AppCompatActivity implements ImageAdpter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ImageAdpter mAdapter;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    private ValueEventListener mDBListener;
    String TAG=" plz work";
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagesfor_admin);
        mRecyclerView =findViewById(R.id.recyclerViewForAdmin);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads= new ArrayList<>();
        mStorage=FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploadsForAdmin");
        mAdapter= new ImageAdpter(ImagesforAdmin.this,mUploads);
        mAdapter.setOnItemClickListener(ImagesforAdmin.this);
        mRecyclerView.setAdapter(mAdapter);
        mDBListener=mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mUploads.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){

                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setMkey(postSnapshot.getKey());
                    Log.i(TAG, "onDataChange: works" +upload.getName());
                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();


                if (mAdapter == null){
                    Toast.makeText(ImagesforAdmin.this, "no images added", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImagesforAdmin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "yeeet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "yeeet", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem=mUploads.get(position);
        final String selectedkEY= selectedItem.getMkey();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedkEY).removeValue();
                Toast.makeText(ImagesforAdmin.this, "image deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}
