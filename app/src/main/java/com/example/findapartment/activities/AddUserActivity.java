package com.example.findapartment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.findapartment.R;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.clients.UserClient;
import com.example.findapartment.fragments.NavigationbarFragment;
import com.example.findapartment.helpers.ToastService;
import com.example.findapartment.helpers.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

public class AddUserActivity extends AppCompatActivity {

    private UserClient userClient;
    private UserSession userSession;

    private EditText email;
    private EditText phoneNumber;
    private EditText password;
    private EditText repeatedPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        userClient = new UserClient();
        userSession = new UserSession(AddUserActivity.this);


        NavigationbarFragment navigationbarfragment = (NavigationbarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationbar);
        navigationbarfragment.setTitle("Załóż konto");

        email = findViewById(R.id.addUserEmailET);
        phoneNumber = findViewById(R.id.addUserPhoneET);
        password = findViewById(R.id.addUserPasswordET);
        repeatedPassword = findViewById(R.id.addUserRepeatPasswordET);
        progressBar = findViewById(R.id.addUserActivityProgressBar);
    }

    public void onAddUserClick(View view) throws JSONException {
        if (!password.getText().toString().equals(repeatedPassword.getText().toString())) {
            ToastService.showErrorMessage("Te hasła nie pasują do siebie. Spróbuj ponownie. ", getApplicationContext());
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        JSONObject newUser = new JSONObject();
        newUser.put("email", email.getText().toString());
        newUser.put("phoneNumber", phoneNumber.getText().toString());
        newUser.put("password", password.getText().toString());
        userClient.register(newUser, getApplicationContext(), new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                JSONObject data = null;
                if (response != null) {
                    try {
                        data = response.getJSONObject("data");
                        userSession.createSession(data.getString("email"), data.getString("accessToken"), data.getString("role"));
                        Intent i=new Intent(getBaseContext(), MyAccountActivity.class);
                        startActivity(i);

                        ToastService.showSuccessMessage("Zostałeś zalogowany.", getApplicationContext());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onError(String result) throws Exception {
                ToastService.showErrorMessage("Nie można utworzyć konta. Spróbuj ponownie.", getApplicationContext());
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }
}