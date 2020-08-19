package com.softhub.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
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

public class OnlineApproveActivity extends AppCompatActivity {

    private ImageView backButton;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FetchOnlineApprove> fetchOnlineApproves;

    private ProgressDialog progressDialog;
    private String prefixString ;
    private Session session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_approve);
        getSupportActionBar().hide();

        session = new Session(OnlineApproveActivity.this);
        prefixString = session.prefs.getString("prefix",null);

        progressDialog = new ProgressDialog(OnlineApproveActivity.this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();


        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        fetchOnlineApproves = new ArrayList<>();
        adapter = new AdapterOnlineApprove(fetchOnlineApproves,this);
        recyclerView.setAdapter(adapter);

        loadOnlineApprove();
    }

    private void loadOnlineApprove(){

     //   String uri = "https://meradaftar.com/demoVendor/online_approved_list.php";

        String uri = getResources().getString(R.string.server_name) + "online_approved_list.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject JO = new JSONObject(response);
                            String data_code = JO.getString("data_code");

                            if(data_code.equals("200")) {

                                JSONArray JA = JO.getJSONArray("res");
                                for(int i = 0; i<JA.length();i++) {
                                    JSONObject JO1 = JA.getJSONObject(i);

                                    FetchOnlineApprove fetchOnline = new FetchOnlineApprove(JO1.getString("id"),
                                            JO1.getString("username"),
                                            JO1.getString("order_id"),
                                            JO1.getString("meal_name"),
                                            JO1.getString("meal_time"),
                                            JO1.getString("meal_type"),
                                            JO1.getString("meal_count"),
                                            JO1.getString("price")
                                    );
                                    fetchOnlineApproves.add(fetchOnline);
                                }
                                adapter = new AdapterOnlineApprove(fetchOnlineApproves,getApplicationContext());
                                recyclerView.setAdapter(adapter);
                                progressDialog.dismiss();
                            }

                            else
                            {
                                Toast.makeText(OnlineApproveActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                            }


                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(OnlineApproveActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OnlineApproveActivity.this, "Volley error" + error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

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
}


