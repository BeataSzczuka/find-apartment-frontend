package com.example.findapartment.activities.apartments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.findapartment.R;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.clients.UserClient;
import com.example.findapartment.helpers.UserSession;
import com.example.findapartment.models.Apartment;

import org.json.JSONException;
import org.json.JSONObject;

public class AddUserActivity extends AppCompatActivity {

    private UserClient userClient;
    private UserSession userSession;

    private EditText email;
    private EditText phoneNumber;
    private EditText password;
    private EditText repeatedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        userClient = new UserClient();
        userSession = new UserSession(AddUserActivity.this);

        email = findViewById(R.id.addUserEmailET);
        phoneNumber = findViewById(R.id.addUserPhoneET);
        password = findViewById(R.id.addUserPasswordET);
        repeatedPassword = findViewById(R.id.addUserRepeatPasswordET);
    }

    public void onAddUserClick(View view) throws JSONException {
        if (!password.getText().toString().equals(repeatedPassword.getText().toString())) {
            Log.e("tag", "passwords should be equal");
            return;
        }
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
                        Intent i=new Intent(getBaseContext(), ApartmentListActivity.class);
////                        i.putExtra("TOKEN", data.getString("accessToken"));
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