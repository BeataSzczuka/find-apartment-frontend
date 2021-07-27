package com.example.findapartment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.findapartment.R;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.clients.UserClient;
import com.example.findapartment.fragments.NavigationbarFragment;
import com.example.findapartment.fragments.ToolbarFragment;
import com.example.findapartment.helpers.AppViewNames;
import com.example.findapartment.helpers.ToastService;
import com.example.findapartment.helpers.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private UserClient userClient;
    private UserSession userSession;

    private EditText email;
    private EditText password;
    private ProgressBar progressBar;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userClient = new UserClient();
        userSession = new UserSession(LoginActivity.this);
        NavigationbarFragment navigationbarfragment = (NavigationbarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationbar);
        navigationbarfragment.setTitle("Zaloguj się");

        ToolbarFragment toolbarFragment = (ToolbarFragment) getSupportFragmentManager().findFragmentById(R.id.menuFragment);
        toolbarFragment.setImageTint(AppViewNames.MY_ACCOUNT);

        email = findViewById(R.id.loginEmailET);
        password = findViewById(R.id.loginPasswordET);
        progressBar = findViewById(R.id.loginActivityProgressBar);
        loginBtn = findViewById(R.id.loginBtn);

        setLoginButtonEnabled(false);
        setNotEmptyValidator(email);
        setNotEmptyValidator(password);

    }

    public void onLoginClick(View view) throws JSONException {
        progressBar.setVisibility(View.VISIBLE);
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

                        // navigate to main activity
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
                ToastService.showErrorMessage("Nie zostałeś zalogowany. Spróbuj ponownie.", getApplicationContext());
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void navigateToCreateAccount(View view) {
        Intent i=new Intent(getBaseContext(), AddUserActivity.class);
        startActivity(i);
    }

    public void onCancelClick(View view) {
        this.finish();
    }

    public void setNotEmptyValidator(EditText editText) {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (email.getText().toString().length() == 0) {
                    email.setError("To pole nie może być puste");
                    setLoginButtonEnabled(false);
                } else {
                    email.setError(null);
                    if (password.getText().toString().length() > 0) {
                        setLoginButtonEnabled(true);
                    }
                }

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().toString().length() == 0) {
                    password.setError("To pole nie może być puste");
                    setLoginButtonEnabled(false);
                } else {
                    password.setError(null);
                    if (email.getText().toString().length() > 0) {
                        setLoginButtonEnabled(true);
                    }
                }

            }
        });
    }

    public void setLoginButtonEnabled(boolean enabled) {
        if (enabled) {
            loginBtn.setEnabled(true);
            loginBtn.setAlpha((float) 1);
        } else {
            loginBtn.setEnabled(false);
            loginBtn.setAlpha((float) 0.6);
        }
    }
}