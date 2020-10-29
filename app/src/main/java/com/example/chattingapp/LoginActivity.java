package com.example.chattingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText name;
    EditText password;
    TextView signUp;
    Button login;
    LoadingDialog alertDialog=new LoadingDialog(LoginActivity.this);
    QBUser qbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name=findViewById(R.id.name);
        password=findViewById(R.id.password);

        signUp=findViewById(R.id.signUp);
        login=findViewById(R.id.login_button);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        alertDialog.startDialog();
        switch (v.getId()){
            case R.id.login_button:
                final QBUser qbUser = new QBUser();
                qbUser.setLogin(name.getText().toString());
                qbUser.setPassword(password.getText().toString());
                final QBChatService chatService = QBChatService.getInstance();
                QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
                    @Override
                    public void onSuccess(QBSession qbSession, Bundle bundle) {
                        chatService.login(qbUser,new QBEntityCallback<QBUser>() {
                            @Override
                            public void onSuccess(final QBUser user, Bundle args) {
                                SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                int userId=qbUser.getId();
                                editor.putInt("userId",userId);
                                editor.commit();
                                Intent intent=new Intent(getBaseContext(),ConnectionsActivity.class);
                                alertDialog.dismissDialog();
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onError(QBResponseException e) {
                                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                alertDialog.dismissDialog();

                            }
                        });
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        alertDialog.dismissDialog();
                    }
                });
                break;

            case R.id.signUp:
                startActivity(new Intent(getBaseContext(),SignUpActivity.class));
                finish();
                break;
        }
    }


}