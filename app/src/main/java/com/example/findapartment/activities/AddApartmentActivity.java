package com.example.findapartment.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.findapartment.R;
import com.example.findapartment.adapters.UploadedImagesAdapter;
import com.example.findapartment.clients.ApartmentClient;
import com.example.findapartment.clients.ApiConfig;
import com.example.findapartment.clients.AppConfig;
import com.example.findapartment.clients.ServerResponse;
import com.example.findapartment.helpers.ToastService;
import com.example.findapartment.helpers.TransactionTypeEnum;
import com.example.findapartment.helpers.UserSession;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddApartmentActivity extends AppCompatActivity {

    int SELECT_PICTURE = 200;
    private GridView uploadedImagesListView;
    private UploadedImagesAdapter uploadedImagesAdapter;
    private RadioGroup transactionTypeRadio;
    private EditText propertySizeEditText;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private ProgressBar progressBar;

    private ApartmentClient apartmentClient;
    private UserSession userSession;

    List<String> imagesPaths = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apartment);

        this.apartmentClient = new ApartmentClient();
        this.userSession = new UserSession(AddApartmentActivity.this);

        uploadedImagesListView = (GridView) findViewById(R.id.uploadedImagesListView);
        ArrayList<Uri> uploadedImages = new ArrayList<Uri>();
        uploadedImagesAdapter = new UploadedImagesAdapter(this, uploadedImages);
        uploadedImagesListView.setAdapter(uploadedImagesAdapter);

        transactionTypeRadio = findViewById(R.id.transactionTypeRadio);
        priceEditText = findViewById(R.id.priceEditText);
        propertySizeEditText = findViewById(R.id.propertySizeEditText);
        locationEditText = findViewById(R.id.locationEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);

        progressBar = findViewById(R.id.addApartmentActivityProgressBar);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.e("E", "Permission granted");
        } else requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        Log.e("E", "after");
    }

    public void uploadImage(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 0);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                uploadedImagesAdapter.add(selectedImageUri);
                uploadedImagesAdapter.notifyDataSetChanged();


                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagesPaths.add(cursor.getString(columnIndex));

                cursor.close();

            }
        }
    }

    public void onAddApartmentClick(View view) throws JSONException {
        uploadMultipleFiles();
    }


    private void uploadMultipleFiles() throws JSONException {
        progressBar.setVisibility(View.VISIBLE);

        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[imagesPaths.size()];

        for (int i = 0; i < imagesPaths.size(); i++) {
            File file = new File(imagesPaths.get(i));

            RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("uploads", file.getName(), requestBody1);

            surveyImagesParts[i] = fileToUpload1;
        }

        JSONObject newApartment = new JSONObject();
        if (transactionTypeRadio.getCheckedRadioButtonId() == R.id.transactionSale) {
            newApartment.put("transactionType", TransactionTypeEnum.SALE);
        } else {
            newApartment.put("transactionType", TransactionTypeEnum.RENT);
        }
        newApartment.put("price", priceEditText.getText().toString());
        newApartment.put("propertySize", propertySizeEditText.getText().toString());
        newApartment.put("location", locationEditText.getText().toString());
        newApartment.put("description", descriptionEditText.getText().toString());

        RequestBody name = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), newApartment.toString());

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> call = getResponse.uploadMulFile(userSession.getLoggedInUserToken(), surveyImagesParts, name);
        call.enqueue(new Callback< ServerResponse >() {
            @Override
            public void onResponse(Call < ServerResponse > call, Response < ServerResponse > response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.v("Response", serverResponse.toString());
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call < ServerResponse > call, Throwable t) {

                ToastService.showErrorMessage("Wystąpił błąd podczas dodawania ogłoszenia.", getApplicationContext());
                progressBar.setVisibility(View.GONE);
            }
        });
    }


}