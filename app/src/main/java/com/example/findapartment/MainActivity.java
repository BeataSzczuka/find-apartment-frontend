package com.example.findapartment;

import android.os.Bundle;

import com.example.findapartment.request.IRequestCallback;
import com.example.findapartment.request.RequestService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "tag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                String url = "http://10.0.2.2:3000/api/apartments";
                final TextView txtView = (TextView) findViewById(R.id.textView);
                assert txtView != null;
                RequestService.makeGetRequest(url, getApplicationContext(), new IRequestCallback(){
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.e("tag", "onSuccess from MainActivity");
                        try {
                            txtView.setText("Response is: " + result.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Snackbar.make(view, "On success", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    @Override
                    public void onError(String result) throws Exception {
                        Log.e("tag", "onError from MainActivity");
                        Snackbar.make(view, result, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}