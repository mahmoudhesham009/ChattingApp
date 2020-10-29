package com.example.chattingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.R;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class DialogRecyclerAdapter extends RecyclerView.Adapter<DialogRecyclerAdapter.dialogViewHolder> {
    ArrayList<QBChatDialog> qbUserArrayList;
    DialogRecyclerAdapter.ItemOnClickListner mItemOnClickListner;

    public DialogRecyclerAdapter(ArrayList<QBChatDialog> qbUserArrayList, DialogRecyclerAdapter.ItemOnClickListner itemOnClickListner){
        this.qbUserArrayList=qbUserArrayList;
        this.mItemOnClickListner=itemOnClickListner;
    }


    @NonNull
    @Override
    public DialogRecyclerAdapter.dialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new DialogRecyclerAdapter.dialogViewHolder(v,mItemOnClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogRecyclerAdapter.dialogViewHolder holder, int position) {
        holder.name.setText(qbUserArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return qbUserArrayList.size();
    }

    class dialogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView avatar;
        DialogRecyclerAdapter.ItemOnClickListner itemOnClickListner;
        public dialogViewHolder(@NonNull View itemView , DialogRecyclerAdapter.ItemOnClickListner itemOnClickListner) {
            super(itemView);
            name=itemView.findViewById(R.id.name_text_view);
            avatar=itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            this.itemOnClickListner=itemOnClickListner;
        }

        @Override
        public void onClick(View v) {
            itemOnClickListner.itemOnClick(getAdapterPosition());
        }
    }

    public interface ItemOnClickListner{
        void itemOnClick(int position);
    }
}
