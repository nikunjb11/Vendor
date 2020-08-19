package com.softhub.vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button mLogIn;
    private TextView mobile, password;
    private ProgressDialog progressDialog;
    private String prefixString, userNameString;
    private Session session;
    private TextView log_new_account;
    private ImageView log_closebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(LoginActivity.this);
        session = new Session(getApplicationContext());

        log_closebtn = (ImageView) findViewById(R.id.log_closebtn);
        mLogIn = (Button) findViewById(R.id.log_loginbtn);
        mobile = (TextView) findViewById(R.id.log_mobileNumber);
        password = (TextView) findViewById(R.id.log_password);
        log_new_account = (TextView) findViewById(R.id.log_new_account);

        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            loginUser();
            }
        });

        log_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SignUp.class);
                startActivity(i);
            }
        });
        log_closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

        private void loginUser(){

            progressDialog.setMessage("Please wait...");
            progressDialog.show();
           // String uri = "https://meradaftar.com/demoVendor/login.php";

            String uri = getResources().getString(R.string.server_name) + "login.php";
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject JO = new JSONObject(response);
                                String result = JO.getString("data_code");

                                if(result.equals("200")){
                                    JSONArray JA = JO.getJSONArray("item_list");
                                    for(int i = 0 ; i<JA.length(); i++){
                                        JSONObject JO1 = JA.getJSONObject(i);
                                        prefixString = JO1.getString("prefix");
                                        userNameString = JO1.getString("username");
                                        openApp();

                                    }
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),JO.getString("Message"),Toast.LENGTH_LONG).show();
                                    openApp();
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),JO.getString("Message"),Toast.LENGTH_LONG).show();
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Log.d("Vendor----------",e.toString());
                                Toast.makeText(getApplicationContext(), "Error---" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.d("VEGGIE------","VOLLEY ERROR:- "+error);
                            Toast.makeText(LoginActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                        }
                    }
            ){

                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();

                    params.put("userName",mobile.getText().toString());
                    params.put("password",password.getText().toString());

                    return params;
                }
            };
            queue.add(stringRequest);
    }

         private void openApp(){

         session.setLoggedIn(true);
         session.setPrefix(prefixString);
         session.setUserName(userNameString);

         Intent i = new Intent(getApplicationContext(),MainActivity.class);
         i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(i);

    }

}
