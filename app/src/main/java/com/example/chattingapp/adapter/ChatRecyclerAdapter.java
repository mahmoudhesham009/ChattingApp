package com.example.chattingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.R;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ChatViewHolder> {
    ArrayList<QBUser> qbUserArrayList;
    ItemOnClickListner mItemOnClickListner;

    public ChatRecyclerAdapter(ArrayList<QBUser> qbUserArrayList,ItemOnClickListner itemOnClickListner){
        this.qbUserArrayList=qbUserArrayList;
        this.mItemOnClickListner=itemOnClickListner;
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(v,mItemOnClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.name.setText(qbUserArrayList.get(position).getLogin());
    }

    @Override
    public int getItemCount() {
        return qbUserArrayList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView avatar;
        ItemOnClickListner itemOnClickListner;
        public ChatViewHolder(@NonNull View itemView , ItemOnClickListner itemOnClickListner) {
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
