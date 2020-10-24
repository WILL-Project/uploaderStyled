package com.plll.myapplication;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;


public class QuestionAdpter extends RecyclerView.Adapter<QuestionAdpter.ImageHolder> {
    private Context mContext;
    private List<TestClass> mUploads;
    private OnItemClickListener mListener;
    public QuestionAdpter(Context context, List<TestClass> uploads){
        mContext= context;
        mUploads=uploads;
    }
    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.question_item,parent,false);
        return new ImageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        TestClass uploadCurrent= mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getmQuestion());
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageHolder extends  RecyclerView.ViewHolder implements View.OnClickListener ,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName;
        public ImageView imageView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            textViewName= itemView.findViewById(R.id.text);

            textViewName.setOnClickListener(this);
            textViewName.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("select Action");
            MenuItem doWhatever = menu.add(Menu.NONE,1,1,"do what ever");
            MenuItem delete = menu.add(Menu.NONE,2,2,"delete");
            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            } return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick (int position);
        void onWhatEverClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public void onDeleteClick(int position) {
        TestClass selectedItem=mUploads.get(position);
        final String selectedkEY= selectedItem.getMkey();
         DatabaseReference mDatabaseRef= FirebaseDatabase.getInstance().getReference()
                .child("uploadsForTest").child(selectedkEY);
        mDatabaseRef.removeValue();
        Toast.makeText(mContext, "done", Toast.LENGTH_SHORT).show();

    }

}
