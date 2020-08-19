package com.softhub.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddPackageActivity extends AppCompatActivity {

    private ImageView backButton;
    private Spinner spinner, spinner1, spinnerType;
    private ProgressDialog dialog;
    private List<String> categories, packageItems, type;
    private ArrayAdapter<String> dataAdapter, dataAdapter1, dataAdapterType;
    private Button mSave, mFinish;
    private ImageView mChooseImage;
    Uri ImageUri;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FetchPackageItems> packItems;
    private ProgressDialog progressDialog;
    private static final int PICK_IMAGE = 1;

    private EditText mPackageName, mPackageCoupon, mPackagePrice, mPackageCode, mQuantity;
    private String packageId, quentity, categoryItem, packageName, packageCoupon,
            packagePrice, packageCode, encoded, packageType, imageString;

    private TextView mImageText;

    private ArrayList<FetchCategories> categoriesArrayList;
    private AdapterCategory mAdapter;

    private ArrayList<String> itemsArrayList;
    private ArrayList<String> itemsArrayListId;
    private AdapterItem iAdapter;
    private String clickedCategoryId, clickedCategoryName, clickedItemName;
    private Session session;
    private String prefixString, userNameString;



    private ArrayList<String> categoryName;
    private ArrayList<String> categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);
        getSupportActionBar().hide();


        backButton = (ImageView) findViewById(R.id.backButton);
        spinner = (Spinner) findViewById(R.id.mSpinner);
        mPackageName = (EditText) findViewById(R.id.mPackageName);
        mPackageCoupon = (EditText) findViewById(R.id.mPackageCoupon);
        mPackagePrice = (EditText) findViewById(R.id.mPackagePrice);
        mPackageCode = (EditText) findViewById(R.id.mPackageCode);

        spinner1 = (Spinner) findViewById(R.id.mSpinnerItem);
        mSave = (Button) findViewById(R.id.save);
        mQuantity = (EditText) findViewById(R.id.quantity);
        mFinish = (Button) findViewById(R.id.submit);
        spinnerType = (Spinner) findViewById(R.id.mSpinnerType);
        mChooseImage = (ImageView) findViewById(R.id.mChooseImage);
        mImageText = (TextView) findViewById(R.id.mImageName);

        categoryName = new ArrayList<String>();
        categoryId = new ArrayList<String>();


        session = new Session(getApplicationContext());
        prefixString = session.prefs.getString("prefix", null);
        userNameString = session.prefs.getString("username", null);

        dialog = new ProgressDialog(AddPackageActivity.this);


        packageItems = new ArrayList<String>();
        type = new ArrayList<String>();

        packageItems.add("Select Item");
        packageItems.add("Jain Sabji");
        packageItems.add("Fulka");
        packageItems.add("Paneer");
        packageItems.add("Chapati");



        categoriesArrayList = new ArrayList<>();
        categoriesArrayList.add(new FetchCategories("", "Choose Categories"));

//        mAdapter = new AdapterCategory(categoriesArrayList,this);
//        spinner.setAdapter(mAdapter);

        itemsArrayList = new ArrayList<>();
        itemsArrayListId = new ArrayList<>();

        type.add("Select Type");
        type.add("Monthly");
        type.add("One Time");

        ArrayAdapter<String> countListAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, type);
        countListAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerType.setAdapter(countListAdapter);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        packItems = new ArrayList<>();
        adapter = new AdapterPackageItems(packItems, getApplicationContext());
        recyclerView.setAdapter(adapter);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quentity = mQuantity.getText().toString();
                if(clickedItemName.contains("Select")){
                    Toast.makeText(AddPackageActivity.this, "Select Item", Toast.LENGTH_SHORT).show();
                }else{
                    if(quentity.equals("")){
                        mQuantity.setError("Add Quantity");
                    }else{
                        addAndDisplay();
                    }
                }
            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (encoded == null) {
                    Toast.makeText(AddPackageActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                } else {
                    packageName = mPackageName.getText().toString();
                    packageCoupon = mPackageCoupon.getText().toString();
                    packagePrice = mPackagePrice.getText().toString();
                    packageCode = mPackageCode.getText().toString();
                    packageType = spinnerType.getSelectedItem().toString();
                    saveData();
                }
            }
        });

        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallary = new Intent();
                openGallary.setType("image/*");
                openGallary.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(openGallary, "Select Image"), PICK_IMAGE);
            }
        });

        mImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallary = new Intent();
                openGallary.setType("image/*");
                openGallary.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(openGallary, "Select Image"), PICK_IMAGE);
            }
        });

        addCategory();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                clickedCategoryId = categoryId.get(i);
                clickedCategoryName = categoryName.get(i);
                addItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clickedItemName = itemsArrayList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            ImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageUri);
                File file = new File(ImageUri.getPath());
                final String[] split = file.getPath().split("/");
                convertImagetoBase64(bitmap);
                mImageText.setText(split[2]);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void convertImagetoBase64(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Log.d("ENCODED IMAGE", encoded);
    }

    private void addCategory() {

        dialog.setMessage("Loading....");
        dialog.show();

        categoryName.clear();
        categoryId.clear();
        categoryId.add("0");
        categoryName.add("Select Category");
        String uri = getResources().getString(R.string.server_name) + "choose_category.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject JO = new JSONObject(response);
                            String data_code = JO.getString("data_code");

                            if (data_code.equals("200")) {

                                JSONArray JA = JO.getJSONArray("res");

                                for (int i = 0; i < JA.length(); i++) {
                                    JSONObject JO1 = JA.getJSONObject(i);
                                    categoryId.add(JO1.getString("id"));
                                    categoryName.add(JO1.getString("category_name"));
                                }
                                setAdapter();
                                dialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddPackageActivity.this, "Json error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPackageActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("prefix", prefixString);

                return params;
            }

        };
        queue.add(stringRequest);
    }

    private void setAdapter() {

        ArrayAdapter<String> countListAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, categoryName);
        countListAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(countListAdapter);

    }

    private void addAndDisplay() {

        dialog.setMessage("Loading....");
        dialog.show();

         //String uri = "https://meradaftar.com/demoVendor/add_and_fetch_packageItem.php";

        String uri = getResources().getString(R.string.server_name) + "add_and_fetch_packageItem.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject JO = new JSONObject(response);
                            String result = JO.getString("data_code");

                            if (result.equals("200")) {
                                JSONArray JA = JO.getJSONArray("res");

                                for (int i = 0; i < JA.length(); i++) {
                                    JSONObject JO1 = JA.getJSONObject(i);
                                    FetchPackageItems packagesItems = new FetchPackageItems(JO1.getString("id"),
                                            JO1.getString("item_name"),
                                            JO1.getString("quantity"));

                                    packItems.add(packagesItems);
                                }
                                adapter = new AdapterPackageItems(packItems, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                                dialog.dismiss();
                                mFinish.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(AddPackageActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddPackageActivity.this, "Json error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPackageActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
            }
        }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("categoryItemName", clickedItemName);
                params.put("categoryItemQuality", quentity);
                params.put("prefix", prefixString);

                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void saveData() {

        dialog.setMessage("Loading....");
        dialog.show();

        //String uri = "https://meradaftar.com/demoVendor/add_packageInfo.php";

        String uri = getResources().getString(R.string.server_name) + "add_packageInfo.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject JO = new JSONObject(response);
                            String result = JO.getString("data_code");

                            if (result.equals("200")) {
                                Toast.makeText(AddPackageActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                onBackPressed();

                            } else {
                                Toast.makeText(AddPackageActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddPackageActivity.this, "Json error", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPackageActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
            }
        }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("categoryName", clickedCategoryName);
                params.put("packageName", packageName);
                params.put("packageCoupon", packageCoupon);
                params.put("packagePrice", packagePrice);
                params.put("packageCode", packageCode);
                params.put("packageImage", encoded);
                params.put("packageType", packageType);
                params.put("prefix", prefixString);
                params.put("userName", userNameString);

                return params;
            }
        };
        queue.add(stringRequest);
    }


    private void addItem() {

        itemsArrayList.clear();
        itemsArrayListId.clear();
        itemsArrayList.add("Select Item");
        itemsArrayListId.add("0");

        dialog.setMessage("Loading....");
        dialog.show();

        //String uri = "https://meradaftar.com/demoVendor/choose_item.php";

        String uri = getResources().getString(R.string.server_name) + "choose_item.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject JO = new JSONObject(response);
                            String result = JO.getString("data_code");

                            if (result.equals("200")) {
                                JSONArray JA = JO.getJSONArray("res");

                                for (int i = 0; i < JA.length(); i++) {
                                    JSONObject JO1 = JA.getJSONObject(i);

                                    itemsArrayList.add(JO1.getString("item_name"));
                                    itemsArrayListId.add(JO1.getString("id"));

                                }
                                dialog.dismiss();
                                setItemAdapter();
                            } else {
                                Toast.makeText(AddPackageActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddPackageActivity.this, "Json error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPackageActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
            }
        }
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("categoryItemId", clickedCategoryId);
                params.put("prefix", prefixString);

                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void setItemAdapter() {

        ArrayAdapter<String> countListAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, itemsArrayList);
        countListAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner1.setAdapter(countListAdapter);

    }

}
