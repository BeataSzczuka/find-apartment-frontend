package com.example.findapartment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.findapartment.R;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.clients.UserClient;
import com.example.findapartment.helpers.ToastService;
import com.example.findapartment.helpers.UserSession;

import org.json.JSONObject;

public class MenuActivity extends AppCompatActivity {

    private UserClient userClient;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        userSession = new UserSession(MenuActivity.this);
        userClient = new UserClient();
        hideButtons();
    }

    private void hideButtons() {
        if (userSession.isLoggedIn()) {
            findViewById(R.id.menuLoginBtn).setVisibility(View.GONE);
            findViewById(R.id.menuAddUserBtn).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.menuHelloUserTV)).setText("Hello, "+userSession.getLoggedInUserEmail());
        } else {
            findViewById(R.id.menuLogoutBtn).setVisibility(View.GONE);
            findViewById(R.id.menuMyAccountBtn).setVisibility(View.GONE);
            findViewById(R.id.menuMyApartmentsBtn).setVisibility(View.GONE);
            findViewById(R.id.menuAddApartmentBtn).setVisibility(View.GONE);
        }
    }

    public void onLogoutButtonClick(View view) {
        userClient.logout(getApplicationContext(), new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null) {
                        userSession.deleteSession();
                        Intent i=new Intent(getBaseContext(), ApartmentListActivity.class);
                        startActivity(i);
                        ToastService.showSuccessMessage("Zostałeś wylogowany.", getApplicationContext());
                }
            }
            @Override
            public void onError(String result) throws Exception {
                ToastService.showErrorMessage("Wystąpił błąd podczas wylogowywania.", getApplicationContext());
            }
        });
    }

    public void navigateToAddUser(View view) {
        Intent i=new Intent(getBaseContext(),AddUserActivity.class);
        startActivity(i);
    }
    public void navigateToAllApartments(View view) {
        Intent i=new Intent(getBaseContext(), ApartmentListActivity.class);
        startActivity(i);
    }
    public void navigateToAddApartment(View view) {
        Intent i=new Intent(getBaseContext(), AddApartmentActivity.class);
        startActivity(i);
    }
    public void navigateToLogin(View view) {
        Intent i=new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
    }
}