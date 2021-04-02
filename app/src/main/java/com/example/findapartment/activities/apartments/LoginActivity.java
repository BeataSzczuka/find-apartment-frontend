package com.example.findapartment.activities.apartments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.findapartment.R;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.clients.UserClient;
import com.example.findapartment.helpers.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private UserClient userClient;
    private UserSession userSession;

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userClient = new UserClient();
        userSession = new UserSession(LoginActivity.this);

        email = findViewById(R.id.loginEmailET);
        password = findViewById(R.id.loginPasswordET);
    }

    public void onLoginClick(View view) throws JSONException {
        JSONObject loginData = new JSONObject();
        loginData.put("email", email.getText().toString());
        loginData.put("password", password.getText().toString());
        userClient.login(loginData, getApplicationContext(), new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                JSONObject data = null;
                if (response != null) {
                    try {
                        data = response.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");
                        userSession.createSession(user.getString("email"), data.getString("accessToken"), user.getString("role"));
                        Intent i=new Intent(getBaseContext(), ApartmentListActivity.class);
                        startActivity(i);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onError(String result) throws Exception {
                Log.e("error", result);
            }
        });
    }
}