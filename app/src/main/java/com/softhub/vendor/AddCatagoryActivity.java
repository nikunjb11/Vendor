package com.softhub.vendor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class AddCatagoryActivity extends AppCompatActivity {

    private ImageView backButton;
    private EditText mCategoryName;
    private String categoryName;
    private Button mSubmit;
    private Button mCancel;
    private ProgressDialog mDialog;
    private Session session;
    private String prefixString;

    private Button addNewCategory;
    private AlertDialog dialogBox;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FetchCategories> listItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_catagory);
        getSupportActionBar().hide();

        session = new Session(getApplicationContext());
        mDialog = new ProgressDialog(AddCatagoryActivity.this);

        backButton = (ImageView) findViewById(R.id.backButton);

        addNewCategory = (Button) findViewById(R.id.addNewCategory);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listItems = new ArrayList<>();
        adapter = new AdapterCategory(listItems,getApplicationContext());
        recyclerView.setAdapter(adapter);


        loadRecyclerViewData();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddCatagoryActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_category,null);
                mBuilder.setView(mView);
                dialogBox = mBuilder.create();
                dialogBox.show();
                mCategoryName = (EditText) mView.findViewById(R.id.catagory_name);
                mSubmit = (Button) mView.findViewById(R.id.submit);
                mCancel = (Button) mView.findViewById(R.id.cancel);

                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBox.dismiss();
                    }
                });
                mSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mCategoryName.getText().toString().trim().equals("")){
                            mCategoryName.setError("Enter Categroy");
                        }else{
                            categoryName = mCategoryName.getText().toString();
                            addCategory();
                        }

                    }
                });
            }
        });

    }

    private void addCategory(){
        mDialog.setMessage("Loading");
        mDialog.show();

        String uri = getResources().getString(R.string.server_name) + "add_category.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject JO = new JSONObject(response);
                            String data_code = JO.getString("data_code");

                            if(data_code.equals("200")){
                                Toast.makeText(AddCatagoryActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                                dialogBox.dismiss();
                                loadRecyclerViewData();

                            }else {
                                Toast.makeText(AddCatagoryActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddCatagoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                Toast.makeText(AddCatagoryActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("categoryName",categoryName);
                params.put("prefix",session.prefs.getString("prefix",null));

                return params;
            }
        };
        queue.add(stringRequest);

    }


    private void loadRecyclerViewData() {

        final ProgressDialog progressDialog = new ProgressDialog(AddCatagoryActivity.this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
        listItems.clear();

        String uri = getResources().getString(R.string.server_name)+"choose_category.php";
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
                                            JO1.getString("category_name"));
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
            params.put("prefix", session.prefs.getString("prefix",null));
            return params;
        }
        };
        queue.add(stringRequest);

    }


}
