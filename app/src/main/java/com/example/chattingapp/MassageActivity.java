package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.adapter.MessageRecyclerAdapter;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import java.util.ArrayList;

public class MassageActivity extends AppCompatActivity implements MessageRecyclerAdapter.ItemOnClickListner{
    Toolbar toolbar;
    EditText mEditText;
    ImageButton mImageButton;
    RecyclerView mRecyclerView;

    QBChatDialog mQbChatDialog;
    ArrayList<QBChatMessage> mQbChatMessageArrayList;

    MessageRecyclerAdapter mMessageRecyclerAdapter;
    LinearLayoutManager mLinearLayoutManager;
    MessageRecyclerAdapter.ItemOnClickListner itemOnClickListner=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("name"));
        mEditText=findViewById(R.id.message_edit_text);
        mImageButton=findViewById(R.id.imageButton2);
        mRecyclerView=findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        getDialog();



    }

    private void getMassageHistory() {
        QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
        messageGetBuilder.setLimit(100);
        QBRestChatService.getDialogMessages(mQbChatDialog, messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(final ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                mQbChatMessageArrayList=qbChatMessages;
                mMessageRecyclerAdapter=new MessageRecyclerAdapter(mQbChatMessageArrayList,itemOnClickListner);
                mRecyclerView.setAdapter(mMessageRecyclerAdapter);
                setMessageListner();
                mRecyclerView.scrollToPosition(mQbChatMessageArrayList.size() - 1);

            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setMessageListner() {
        mQbChatDialog.addMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String dialogId, QBChatMessage qbChatMessage, Integer senderId) {
                mQbChatMessageArrayList.add(qbChatMessage);
                mMessageRecyclerAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mQbChatMessageArrayList.size() - 1);

            }

            @Override
            public void processError(String dialogId, QBChatException e, QBChatMessage qbChatMessage, Integer senderId) {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDialog() {
        String id=getIntent().getStringExtra("dialogId");
        QBRestChatService.getChatDialogById(id).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                mQbChatDialog=qbChatDialog;
                getMassageHistory();
                setOnSendClick();
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOnSendClick() {
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mEditText.getText().toString());
                mEditText.setText("");
            }
        });
    }

    private void sendMessage(String message){
        final QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody(message);
        chatMessage.setSaveToHistory(true);
        chatMessage.setSenderId(getSharedPreferences("user",Context.MODE_PRIVATE).getInt("userId",0));

        mQbChatDialog.sendMessage(chatMessage, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                mQbChatMessageArrayList.add(chatMessage);
                mMessageRecyclerAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mQbChatMessageArrayList.size() - 1);
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void itemOnClick(int position) {

    }
}