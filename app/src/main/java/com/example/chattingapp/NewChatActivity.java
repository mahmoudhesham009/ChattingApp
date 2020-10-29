package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.chattingapp.adapter.ChatRecyclerAdapter;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class NewChatActivity extends AppCompatActivity implements ChatRecyclerAdapter.ItemOnClickListner {
    RecyclerView mRecyclerView;
    ChatRecyclerAdapter mChatRecyclerAdapter;
    LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(this);
    ChatRecyclerAdapter.ItemOnClickListner itemOnClickListner=this;
    QBUser qbUser=new QBUser();
    ArrayList<QBUser> qbUsersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        mRecyclerView=findViewById(R.id.recyclerView);
        getUsers();
    }

    @Override
    public void itemOnClick(int position) {
        ArrayList<Integer> occupantIdsList = new ArrayList<Integer>();
        occupantIdsList.add(qbUsersArrayList.get(position).getId());

        QBChatDialog dialog = new QBChatDialog();
        dialog.setType(QBDialogType.PRIVATE);
        dialog.setOccupantsIds(occupantIdsList);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle params) {

            }

            @Override
            public void onError(QBResponseException responseException) {

            }
        });
    }

    private void getUsers() {
        QBUsers.getUsers(new QBPagedRequestBuilder()).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUserArrayList, Bundle bundle) {
                for(QBUser user:qbUserArrayList){
                    if(user.getId().equals(getSharedPreferences("user", Context.MODE_PRIVATE).getInt("userId",0))){
                        qbUserArrayList.remove(user);
                        break;
                    }
                }
                mChatRecyclerAdapter=new ChatRecyclerAdapter(qbUserArrayList,itemOnClickListner);
                mRecyclerView.setAdapter(mChatRecyclerAdapter);
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                qbUsersArrayList=qbUserArrayList;
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }
}