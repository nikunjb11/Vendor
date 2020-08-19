package com.softhub.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FetchUserDetails> userDetails;
    private ImageView backButton;
    private ProgressDialog progressDialog;
    private Session session;
    private String prefixString;


    private Button addNewCustomer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().hide();

        session = new Session(getApplicationContext());
        prefixString = session.prefs.getString("prefix", null);

        progressDialog = new ProgressDialog(UserActivity.this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        backButton = findViewById(R.id.backButton);
        addNewCustomer = (Button) findViewById(R.id.addNewCustomer);

        addNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddNewCustomer.class);
                startActivity(i);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        userDetails = new ArrayList<>();
        adapter = new AdapterUserDetails(userDetails, getApplicationContext());
        recyclerView.setAdapter(adapter);

    }

    private void fetchUsers() {

        //String uri = "https://meradaftar.com/demoVendor/user_list.php";

        String uri = getResources().getString(R.string.server_name) + "user_list.php";
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

                                userDetails.clear();

                                JSONArray JA = JO.getJSONArray("res");
                                for (int i = 0; i < JA.length(); i++) {
                                    JSONObject JO1 = JA.getJSONObject(i);

                                    FetchUserDetails users = new FetchUserDetails(JO1.getString("id"),
                                            JO1.getString("first_name"),
                                            JO1.getString("email"),
                                            JO1.getString("mobile"),
                                            JO1.getString("address1")
                                    );
                                    userDetails.add(users);
                                }
                                adapter = new AdapterUserDetails(userDetails, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(UserActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UserActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserActivity.this, "Volley error" + error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("prefix", prefixString);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        fetchUsers();
    }
}
