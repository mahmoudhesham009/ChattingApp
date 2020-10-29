package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    static final String APPLICATION_ID = "86543";
    static final String AUTH_KEY = "9UxA3Rakparzftu";
    static final String AUTH_SECRET = "3aR-UeH7peDva-M";
    static final String ACCOUNT_KEY = "yYezjogW-nRxYeub-vBy";

    EditText name;
    EditText password;
    EditText Email;
    Button signUp;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QBSettings.getInstance().init(getApplicationContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        boolean isLoggedIn = QBSessionManager.getInstance().getSessionParameters() != null;
        if(isLoggedIn){
            Intent intent=new Intent(getBaseContext(),ConnectionsActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_signup);
        name=findViewById(R.id.name);
        Email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        signUp=findViewById(R.id.signUp);
        login=findViewById(R.id.login);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUp:
                final QBUser user = new QBUser();
                user.setLogin(name.getText().toString());
                user.setPassword(password.getText().toString());
                user.setEmail(Email.getText().toString());

                QBUsers.signUp(user).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        Toast.makeText(getBaseContext(),"Done",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getBaseContext(),LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.d("Erorr",e.toString());
                        Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.login:
                startActivity(new Intent(getBaseContext(),LoginActivity.class));
                finish();
                break;
        }
    }
}