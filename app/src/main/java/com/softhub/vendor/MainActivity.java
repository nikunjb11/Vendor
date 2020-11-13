package com.softhub.vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FetchPackageDetails> packDetails;
    private ProgressDialog progressDialog;
    private Session session;
    private String prefixString, userNameString;
    private TextView totalCollection, totalCustomer, expiredRenewal;
    private TextView userName, userMobileNumber;

    private RecyclerView recyclerViewB;
    private RecyclerView.Adapter adapterB;
    private List<FetchedListOfImages> listItemsB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewB = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewB.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewB.setLayoutManager(horizontalLayoutManager);
        listItemsB = new ArrayList<>();
        adapterB = new ContactAdapterBanner(listItemsB,getApplicationContext());
        recyclerViewB.setAdapter(adapter);

        totalCollection = (TextView) findViewById(R.id.total_collection);
        totalCustomer = (TextView) findViewById(R.id.total_customer);
        expiredRenewal = (TextView) findViewById(R.id.expired_renewal);

        session = new Session(MainActivity.this);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        prefixString = session.prefs.getString("prefix",null);
        userNameString = session.prefs.getString("username",null);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nView = navigationView.inflateHeaderView(R.layout.nav_header);

        Menu menuNav = navigationView.getMenu();
        MenuItem login = menuNav.findItem(R.id.nav_login);
        MenuItem logout = menuNav.findItem(R.id.nav_logout);

        userName = (TextView) nView.findViewById(R.id.userName);
        userMobileNumber = (TextView) nView.findViewById(R.id.userMobileNumber);

        if(session.loggedIn()){

            logout.setVisible(true);
            login.setVisible(false);
            userName.setText(session.prefs.getString("username",null));
        }


//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        packDetails = new ArrayList<>();
//        adapter = new AdapterPackageDetails(packDetails,getApplicationContext());
//        recyclerView.setAdapter(adapter);

        new A().execute();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.nav_custList) {
            Intent i = new Intent(MainActivity.this,UserActivity.class);
            startActivity(i);
        }

        else if(id == R.id.nav_packList){
            Intent i = new Intent(MainActivity.this,MasterPackage.class);
            startActivity(i);
        }

        else if(id == R.id.nav_subscriptionList){
            Intent i = new Intent(MainActivity.this,UserSubscriptionActivity.class);
            startActivity(i);
        }

        else if(id == R.id.nav_addCategory){
            Intent i = new Intent(MainActivity.this,AddCatagoryActivity.class);
            startActivity(i);
        }

        else if(id == R.id.nav_createItem){
            Intent i = new Intent(MainActivity.this,CreateItem.class);
            startActivity(i);
        }

        else if(id == R.id.nav_addPackage){
            Intent i = new Intent(MainActivity.this,AddPackageActivity.class);
            startActivity(i);
        }

        else if(id == R.id.nav_address){
            Intent i = new Intent(MainActivity.this, AddAddressActivity.class);
            startActivity(i);
        }

        else if(id == R.id.subnav_CODRequest){
            Intent i = new Intent(MainActivity.this,CODRequsetActivity.class);
            startActivity(i);
        }

        else if(id == R.id.subnav_CODApprove){
            Intent i = new Intent(MainActivity.this,CODApproveActivity.class);
            startActivity(i);
        }

        else if(id == R.id.subnav_onlineRequest){
            Intent i = new Intent(MainActivity.this,OnlineRequestActivity.class);
            startActivity(i);
        }

        else if(id == R.id.subnav_onlineApprove){
            Intent i = new Intent(MainActivity.this,OnlineApproveActivity.class);
            startActivity(i);
        }

        else if(id == R.id.nav_login){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }

        else if(id == R.id.nav_logout){
            logOut();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    class A extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {
            fetchPackages();
            fetchBanner();
            return null;
        }
    }

    private void fetchBanner() {

        String uri = getResources().getString(R.string.server_name)+"vendor_banner.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    String path;
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject JO = new JSONObject(s);
                            String code = JO.getString("data_code");
                            if(code.equals("200")){
                                JSONArray JA = JO.getJSONArray("res1");
                                for(int i=0; i<JA.length(); i++){
                                    JSONObject JO1 = JA.getJSONObject(i);
                                    FetchedListOfImages flp = new FetchedListOfImages(JO1.getString("image_path"),
                                            JO1.getString("description"));
                                    listItemsB.add(flp);

                                }

                                adapterB = new ContactAdapterBanner(listItemsB,getApplicationContext());
                                recyclerViewB.setAdapter(adapterB);
                                if(listItemsB.size()>1){
                                    scrollItems();
                                }

                            }else{}


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //   Toast.makeText(getApplicationContext(), "Registration.Error!",Toast.LENGTH_SHORT).show();

                    }
                }){@Override
        public Map<String, String> getParams() {
            Map<String, String> params = new HashMap<>();
            params.put("prefix", prefixString);

            return params;
        }
        };
        queue.add(stringRequest);
    }

    private void scrollItems() {

        final int speedScroll = 4000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;
            @Override
            public void run() {
                if(count < adapterB.getItemCount()){
                    if(count==adapterB.getItemCount()-1){
                        flag = false;
                    }else if(count == 0){
                        flag = true;
                    }
                    if(flag) count++;
                    else count--;

                    recyclerViewB.smoothScrollToPosition(count);
                    handler.postDelayed(this,speedScroll);
                }
            }
        };

        handler.postDelayed(runnable,speedScroll);

    }


    private void logOut(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(session.loggedIn()){
                    session.setLoggedIn(false);

                    Intent intentLogout = new Intent(getApplicationContext(),LoginActivity.class);
                    intentLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentLogout);
                }

                else{
                    Toast.makeText(MainActivity.this, "You are not logged in!", Toast.LENGTH_SHORT).show();
                }
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void fetchPackages(){

      //  String uri = "https://meradaftar.com/demoVendor/summary.php";

        String uri = getResources().getString(R.string.server_name) + "summary.php";
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

                                    totalCollection.setText("Rs. " + JO1.getString("total_collection"));
                                    totalCustomer.setText(JO1.getString("total_customer"));
                                    expiredRenewal.setText(JO1.getString("expired_renewal"));

                                }

                                progressDialog.dismiss();
                            }
                            else {
                                Toast.makeText(MainActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }


                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            Log.d("VENDOR----------",e.toString());
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("VENDOR ------",error.toString());
                Toast.makeText(MainActivity.this, "Volley error" + error, Toast.LENGTH_SHORT).show();

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


