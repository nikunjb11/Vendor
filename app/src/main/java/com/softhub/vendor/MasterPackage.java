package com.softhub.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterPackage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FetchPackageDetails> packDetails;
    private ProgressDialog progressDialog;
    private Session session;
    private String prefixString, userNameString;
    private ImageView backButton;
    private Button addPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_package);
        getSupportActionBar().hide();

        addPackage = (Button) findViewById(R.id.addNewPackage);

        session = new Session(MasterPackage.this);
        progressDialog = new ProgressDialog(MasterPackage.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        prefixString = session.prefs.getString("prefix",null);
        userNameString = session.prefs.getString("username",null);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        packDetails = new ArrayList<>();
        adapter = new AdapterPackageDetails(packDetails,getApplicationContext());
        recyclerView.setAdapter(adapter);

        backButton = (ImageView) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddPackageActivity.class);
                startActivity(i);
            }
        });

    }

    private void fetchPackages(){

      //  String uri = "https://meradaftar.com/demoVendor/package_list.php";
        packDetails.clear();

        String uri = getResources().getString(R.string.server_name) + "package_list.php";
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
                                JSONArray JA = JO.getJSONArray("res1");

                                for(int i = 0; i<JA.length();i++) {
                                    JSONObject JO1 = JA.getJSONObject(i);

                                    FetchPackageDetails packages = new FetchPackageDetails(JO1.getString("package_id"),
                                            JO1.getString("package_name"),
                                            JO1.getString("coupon"),
                                            JO1.getString("price"),
                                            JO1.getString("meal_type"));
                                    packDetails.add(packages);
                                }
                                adapter = new AdapterPackageDetails(packDetails,getApplicationContext());
                                recyclerView.setAdapter(adapter);
                                progressDialog.dismiss();
                            }
                            else {
                                Toast.makeText(MasterPackage.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }


                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(MasterPackage.this, "Error", Toast.LENGTH_SHORT).show();
                            Log.d("VENDOR----------",e.toString());
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("VENDOR ------",error.toString());
                Toast.makeText(MasterPackage.this, "Volley error" + error, Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("prefix",prefixString);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        fetchPackages();
    }
}
