package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.chattingapp.adapter.ChatRecyclerAdapter;
import com.example.chattingapp.adapter.DialogRecyclerAdapter;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import static com.example.chattingapp.SignUpActivity.ACCOUNT_KEY;
import static com.example.chattingapp.SignUpActivity.APPLICATION_ID;
import static com.example.chattingapp.SignUpActivity.AUTH_KEY;
import static com.example.chattingapp.SignUpActivity.AUTH_SECRET;

public class ConnectionsActivity extends AppCompatActivity implements DialogRecyclerAdapter.ItemOnClickListner, Toolbar.OnMenuItemClickListener {
    RecyclerView mRecyclerView;
    DialogRecyclerAdapter mDialogRecyclerAdapter;
    LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(this);
    DialogRecyclerAdapter.ItemOnClickListner itemOnClickListner=this;
    ArrayList<QBChatDialog> qbDialogsArrayList;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);
        mRecyclerView=findViewById(R.id.recyclerView);
        mToolbar=findViewById(R.id.toolbar);
        mToolbar.setOnMenuItemClickListener(this);
        ImageButton imageButton=findViewById(R.id.fab);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),NewChatActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDialogs();
    }

    private void getDialogs() {
        final QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setLimit(50);

        QBRestChatService.getChatDialogs(null, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> result, Bundle params) {
                mDialogRecyclerAdapter=new DialogRecyclerAdapter(result,itemOnClickListner);
                mRecyclerView.setAdapter(mDialogRecyclerAdapter);
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                qbDialogsArrayList=result;
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void itemOnClick(int position) {
        Intent intent=new Intent(getBaseContext(),MassageActivity.class);
        String id=qbDialogsArrayList.get(position).getDialogId();
        intent.putExtra("dialogId",id);
        intent.putExtra("name",qbDialogsArrayList.get(position).getName());
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out:
                QBUsers.signOut().performAsync(new QBEntityCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid, Bundle bundle) {
                        getSharedPreferences("user",MODE_PRIVATE).edit().putInt("userId",0);
                        startActivity(new Intent(getBaseContext(),LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
                break;
        }
        return true;
    }
}