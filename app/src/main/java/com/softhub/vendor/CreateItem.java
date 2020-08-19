package com.softhub.vendor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CreateItem extends AppCompatActivity {

    private Spinner mItemSpinner;
    private EditText mItemName;
    private Button mSubmit;
    private ImageView backButton;
    private ProgressDialog dialog;
    private Session session;
    private String category, item;
    private String prefixString;

    private ArrayList<String> categoryName;
    private ArrayList<String> categoryId;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FetchCategories> listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        getSupportActionBar().hide();

        session = new Session(getApplicationContext());
        prefixString = session.prefs.getString("prefix", null);
        dialog = new ProgressDialog(CreateItem.this);

        mItemSpinner = (Spinner) findViewById(R.id.spinner_Itemname);
        mItemName = (EditText) findViewById(R.id.item_name);
        mSubmit = (Button) findViewById(R.id.submit);
        backButton = (ImageView) findViewById(R.id.backButton);

        categoryName = new ArrayList<String>();
        categoryId = new ArrayList<String>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listItems = new ArrayList<>();
        adapter = new AdapterCategory(listItems,getApplicationContext());
        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addCategory();

        mItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = categoryId.get(i);
                if(!category.equals("0")){
                    loadRecyclerViewData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(category.equals("0")){
                    Toast.makeText(CreateItem.this, "Select Category", Toast.LENGTH_SHORT).show();
                }else{
                    item = mItemName.getText().toString();
                    if(item.equals("")){
                        mItemName.setError("Enter Item Name");
                    }else{
                        addItem();
                    }
                }
            }
        });

    }

    private void loadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(CreateItem.this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
        listItems.clear();

        String uri = getResources().getString(R.string.server_name)+"choose_item.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String data = "Y";
                        try {
                            JSONObject JO = new JSONObject(s);

                            if(JO.getString("data_code").equals("200")){
                                JSONArray JA = JO.getJSONArray("res");

                                for(int i=0; i<JA.length(); i++){
                                    JSONObject JO1 = JA.getJSONObject(i);

                                    FetchCategories flp = new FetchCategories(JO1.getString("id"),
                                            JO1.getString("item_name"));
                                    listItems.add(flp);
                                }
                                progressDialog.dismiss();
                                adapter = new AdapterCategory(listItems,getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), JO.getString("res"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //   Toast.makeText(getContext(), "Error.Response",Toast.LENGTH_SHORT).show();
                    }
                }){@Override
        public Map<String, String> getParams() {
            Map<String, String> params = new HashMap<>();
            params.put("categoryItemId", category);
            params.put("prefix", session.prefs.getString("prefix",null));
            return params;
        }
        };
        queue.add(stringRequest);
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
                            Toast.makeText(CreateItem.this, "Json error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreateItem.this, "Volley Error", Toast.LENGTH_SHORT).show();
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
        mItemSpinner.setAdapter(countListAdapter);

    }

    private void addItem() {
        dialog.setMessage("Loading");
        dialog.show();

        //String uri = "https://meradaftar.com/demoVendor/add_items.php";

        String uri = getResources().getString(R.string.server_name) + "add_items.php";
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
                                Toast.makeText(CreateItem.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                mItemSpinner.setSelection(0);
                                category="0";
                                mItemName.setText("");
                                listItems.clear();
                                adapter = new AdapterCategory(listItems,getApplicationContext());
                                recyclerView.setAdapter(adapter);

                            } else {
                                Toast.makeText(CreateItem.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CreateItem.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(CreateItem.this, "Volley Error", Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("itemName", item);
                params.put("categoryName", category);
                params.put("prefix", prefixString);

                return params;
            }
        };
        queue.add(stringRequest);
    }
}
