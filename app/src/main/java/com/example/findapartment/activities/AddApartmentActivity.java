package com.example.findapartment.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findapartment.R;
import com.example.findapartment.adapters.UploadedImagesAdapter;
import com.example.findapartment.clients.ApartmentClient;
import com.example.findapartment.clients.ApiConfig;
import com.example.findapartment.clients.AppConfig;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.clients.ServerResponse;
import com.example.findapartment.fragments.AddApartmentStep1Fragment;
import com.example.findapartment.fragments.AddApartmentStep2Fragment;
import com.example.findapartment.fragments.AddApartmentStep3Fragment;
import com.example.findapartment.fragments.AddApartmentStep4Fragment;
import com.example.findapartment.fragments.NavigationbarFragment;
import com.example.findapartment.fragments.ToolbarFragment;
import com.example.findapartment.helpers.AppViewNames;
import com.example.findapartment.helpers.SetupHelpers;
import com.example.findapartment.helpers.ToastService;
import com.example.findapartment.helpers.TransactionTypeEnum;
import com.example.findapartment.helpers.UserSession;
import com.example.findapartment.models.Apartment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private Button addApartmentBtn;

    private AddApartmentStep1Fragment addApartmentStep1Fragment;
    private AddApartmentStep2Fragment addApartmentStep2Fragment;
    private AddApartmentStep3Fragment addApartmentStep3Fragment;
    private AddApartmentStep4Fragment addApartmentStep4Fragment;

    private ApartmentClient apartmentClient;
    private UserSession userSession;

    private ArrayList<Uri> uploadedImages;

    private ArrayList<String> unchangedImages = new ArrayList<String>();

    private String editedApartmentId;

    private TextView imagesMessage;

    List<String> imagesPaths = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apartment);

        this.apartmentClient = new ApartmentClient();
        this.userSession = new UserSession(AddApartmentActivity.this);


        imagesMessage = findViewById(R.id.addImagesMessage);

        ToolbarFragment toolbarFragment = (ToolbarFragment) getSupportFragmentManager().findFragmentById(R.id.menuFragment);
        toolbarFragment.setImageTint(AppViewNames.ADD_APARTMENT);

        NavigationbarFragment navigationbarfragment = (NavigationbarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationbar);

        addApartmentStep1Fragment = (AddApartmentStep1Fragment) getSupportFragmentManager().findFragmentById(R.id.fragmentStep1);
        addApartmentStep2Fragment = (AddApartmentStep2Fragment) getSupportFragmentManager().findFragmentById(R.id.fragmentStep2);
        getSupportFragmentManager().beginTransaction().hide(addApartmentStep2Fragment).commit();
        addApartmentStep3Fragment = (AddApartmentStep3Fragment) getSupportFragmentManager().findFragmentById(R.id.fragmentStep3);
        getSupportFragmentManager().beginTransaction().hide(addApartmentStep3Fragment).commit();
        addApartmentStep4Fragment = (AddApartmentStep4Fragment) getSupportFragmentManager().findFragmentById(R.id.fragmentStep4);
        getSupportFragmentManager().beginTransaction().hide(addApartmentStep4Fragment).commit();

        uploadedImagesListView = (GridView) findViewById(R.id.uploadedImagesListView);
        uploadedImages = new ArrayList<Uri>();
        uploadedImagesAdapter = new UploadedImagesAdapter(this, uploadedImages);
        uploadedImagesListView.setAdapter(uploadedImagesAdapter);
        uploadedImagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                uploadedImages.remove(uploadedImagesAdapter.getItem(position));
                unchangedImages.remove(imagesPaths.get(position));
                imagesPaths.remove(position);
                uploadedImagesAdapter.notifyDataSetChanged();

                if (uploadedImages.size() > 0) {
                    imagesMessage.setVisibility(View.GONE);
                } else {
                    imagesMessage.setVisibility(View.VISIBLE);
                }
            }
        });

        transactionTypeRadio = findViewById(R.id.transactionTypeRadio);
        priceEditText = findViewById(R.id.priceEditText);
        propertySizeEditText = findViewById(R.id.propertySizeEditText);
        locationEditText = findViewById(R.id.locationEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        addApartmentBtn = findViewById(R.id.addApartmentBtn);

        progressBar = findViewById(R.id.addApartmentActivityProgressBar);



        loadDataIfInEditMode();
        if (editedApartmentId != null) {
            navigationbarfragment.setTitle("Edytuj ogłoszenie");
            addApartmentBtn.setText("Zapisz zmiany");

        } else {
            navigationbarfragment.setTitle("Dodaj ogłoszenie");
        }

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
        } else requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        SetupHelpers.setKeyboardHideListener(this);
    }

    public void loadDataIfInEditMode() {
        Intent intentNow = getIntent();
        editedApartmentId = intentNow.getStringExtra("editedApartmentId");
        if (editedApartmentId != null && editedApartmentId.length() > 0) {
            fetchApartmentToEdit();
        }
    }

    private void fetchApartmentToEdit() {
        progressBar.setVisibility(View.VISIBLE);
        apartmentClient.getApartment(getApplicationContext(), editedApartmentId, new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = null;
                    if (response != null) {
                        data = response.getJSONObject("data");
                        Apartment apartment = Apartment.fromJSON(data);

                        propertySizeEditText.setText(formatNumber(apartment.getPropertySize().toString()));
                        locationEditText.setText(apartment.getLocation());
                        descriptionEditText.setText(apartment.getDescription());
                        priceEditText.setText(formatNumber(apartment.getPrice().toString()));
                        if (apartment.getTransactionType() == TransactionTypeEnum.SALE.name()) {
                            transactionTypeRadio.check(R.id.transactionSale);
                        } else {
                            transactionTypeRadio.check(R.id.transactionRent);
                        }
                        if (apartment.getImages().size() > 0) {
                            imagesMessage.setVisibility(View.GONE);
                        }
                        for (int j = 0; j < apartment.getImages().size(); j++) {
                            imagesPaths.add(apartment.getImage(j));
                            uploadedImages.add(Uri.parse(apartment.getImage(j)));
                            uploadedImagesAdapter.notifyDataSetChanged();
                            unchangedImages.add(apartment.getImage(j));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onError(String result) throws Exception {
                ToastService.showErrorMessage(result, findViewById(R.id.rootView));
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void uploadImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(galleryIntent, 1);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount() && i < 6; i++) {
                    Uri selectedImageUri = clipData.getItemAt(i).getUri();
                    uploadedImagesAdapter.add(selectedImageUri);

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagesPaths.add(cursor.getString(columnIndex));

                    cursor.close();
                }
            } else if(data.getData() != null) {
                Uri selectedImageUri = data.getData();
                uploadedImagesAdapter.add(selectedImageUri);

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagesPaths.add(cursor.getString(columnIndex));
                cursor.close();
            }
        }
        if (uploadedImages.size() > 0) {
            imagesMessage.setVisibility(View.GONE);
        } else {
            imagesMessage.setVisibility(View.VISIBLE);
        }
    }

    private float getNumber(EditText editText) {
        String text = editText.getText().toString();
        text = text.replaceAll("\\s", "");
        text = text.replaceAll(",", ".");
        return Float.parseFloat(text);
    }

    public void onAddApartmentClick() throws JSONException {
        SetupHelpers.hideSoftKeyboard(this);
        progressBar.setVisibility(View.VISIBLE);
        addApartmentBtn.setEnabled(false);

        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[imagesPaths.size()];

        for (int i = 0; i < imagesPaths.size(); i++) {
            String imagePath = imagesPaths.get(i);
            if (!unchangedImages.contains(imagePath)) {
                File file = new File(imagePath);

                RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
                MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("uploads", file.getName(), requestBody1);

                surveyImagesParts[i] = fileToUpload1;
            }
        }

        JSONObject newApartment = new JSONObject();
        if (transactionTypeRadio.getCheckedRadioButtonId() == R.id.transactionSale) {
            newApartment.put("transactionType", TransactionTypeEnum.SALE);
        } else {
            newApartment.put("transactionType", TransactionTypeEnum.RENT);
        }
        newApartment.put("price", getNumber(priceEditText));
        newApartment.put("propertySize", getNumber(propertySizeEditText));
        newApartment.put("location", locationEditText.getText().toString());
        newApartment.put("description", descriptionEditText.getText().toString());
        newApartment.put("unchangedImages", new JSONArray(unchangedImages));


        RequestBody name = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), newApartment.toString());

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> call;
        if (editedApartmentId != null && editedApartmentId.length() > 0) {
            call = getResponse.updateApartment(userSession.getLoggedInUserToken(), editedApartmentId, surveyImagesParts, name);
        } else {
            call = getResponse.uploadMulFile(userSession.getLoggedInUserToken(), surveyImagesParts, name);
        }
        call.enqueue(new Callback< ServerResponse >() {
            @Override
            public void onResponse(Call < ServerResponse > call, Response < ServerResponse > response) {
                progressBar.setVisibility(View.GONE);
                addApartmentBtn.setEnabled(true);
                ToastService.showSuccessMessage(response.body().getMessage(), getApplicationContext());
                Intent i=new Intent(getBaseContext(), ApartmentListActivity.class);
                startActivity(i);
            }
            @Override
            public void onFailure(Call < ServerResponse > call, Throwable t) {

                ToastService.showErrorMessage("Wystąpił błąd podczas dodawania ogłoszenia.", findViewById(R.id.rootView));
                progressBar.setVisibility(View.GONE);
                addApartmentBtn.setEnabled(true);
            }
        });
    }


    private String formatNumber(String content) {
        if (content.length() > 0) {
            String cleanString = content.replaceAll(",", ".");
            cleanString = cleanString.replaceAll("\\s", "");
            double parsed = Double.parseDouble(cleanString);
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance();
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
            symbols.setCurrencySymbol("");
            formatter.setDecimalFormatSymbols(symbols);
            return formatter.format(parsed);
        }
        return "";
    }


}