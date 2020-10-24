package com.plll.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    UploadStoryForAdmin storyForAdmin;
    BlankFragment blankFragment;
    fragPuzzels FragPuzzels;
    testQuestionFrag TestQuestionFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.spinners);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.holder_array, R.layout.action_item_list);
        adapter.setDropDownViewResource(R.layout.action_item_list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        blankFragment= new BlankFragment();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {


            case 0:
                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flfragment, blankFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 1: //move to UploadStory
                storyForAdmin= new UploadStoryForAdmin();
                final FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                //frag1
                transaction2.replace(R.id.flfragment, storyForAdmin);
                transaction2.addToBackStack(null);
                transaction2.commit();
                break;
            case 2://move to Puzzels
                FragPuzzels= new fragPuzzels();
                final FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                //frag1
                transaction3.replace(R.id.flfragment, FragPuzzels);
                transaction3.addToBackStack(null);
                transaction3.commit();
                break;
            case 3: //move to Words
                TestQuestionFrag= new testQuestionFrag();
                final FragmentTransaction transaction4 = getSupportFragmentManager().beginTransaction();
                //frag1
                transaction4.replace(R.id.flfragment, TestQuestionFrag);
                transaction4.addToBackStack(null);
                transaction4.commit();
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

