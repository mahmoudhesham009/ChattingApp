package com.example.chattingapp.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.MassageActivity;
import com.example.chattingapp.R;
import com.quickblox.auth.QBAuth;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.MessageViewHolder> {
    ArrayList<QBChatMessage> qbChatMessageArrayList;
    ItemOnClickListner mItemOnClickListner;
    Context context;
    SharedPreferences sharedPreferences;

    public MessageRecyclerAdapter(ArrayList<QBChatMessage> qbChatMessageArrayList, ItemOnClickListner itemOnClickListner){
        this.qbChatMessageArrayList=qbChatMessageArrayList;
        this.mItemOnClickListner=itemOnClickListner;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.massage_item, parent, false);
        context=parent.getContext();
        sharedPreferences=context.getSharedPreferences("user",Context.MODE_PRIVATE);
        return new MessageViewHolder(v,mItemOnClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.messageBody.setText(qbChatMessageArrayList.get(position).getBody());
        int userId = sharedPreferences.getInt("userId",0);
        if(qbChatMessageArrayList.get(position).getSenderId().equals(userId)){
            holder.messageBody.setTextColor(Color.WHITE);
            holder.messageBody.setBackgroundColor(Color.BLACK);
            holder.linearLayout.setGravity(Gravity.RIGHT);

        }else {
            holder.messageBody.setTextColor(Color.BLACK);
            holder.messageBody.setBackgroundColor(Color.WHITE);
            holder.linearLayout.setGravity(Gravity.LEFT);
        }

    }

    @Override
    public int getItemCount() {
        return qbChatMessageArrayList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView messageBody;
        LinearLayout linearLayout;
        ItemOnClickListner itemOnClickListner;
        public MessageViewHolder(@NonNull View itemView , ItemOnClickListner itemOnClickListner) {
            super(itemView);
            messageBody=itemView.findViewById(R.id.messag_body);
            linearLayout=itemView.findViewById(R.id.linear_layout);
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
