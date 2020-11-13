package com.softhub.vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class SignUp extends AppCompatActivity {

    private Button reg_login;
    private ImageView log_closebtn;
    private EditText reg_number;
    private EditText reg_password;
    private EditText reg_full_name;
    private EditText reg_company_name;
    private EditText reg_email;
    private CheckBox check_tc;
    private Button reg_register;
    private EditText reg_otp;
    private LinearLayout reg_llOtp;
    private Button reg_verifyOtp;
    private Button reg_resendOtp;

    private ProgressDialog progressDialog;
    private Session session;
    private String clientCode="";
    private String userName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(SignUp.this);
        session = new Session(getApplicationContext());

        reg_login = (Button) findViewById(R.id.reg_login);
        log_closebtn = (ImageView) findViewById(R.id.log_closebtn);
        reg_number = (EditText) findViewById(R.id.reg_number);
        reg_password = (EditText) findViewById(R.id.reg_password);
        reg_full_name = (EditText) findViewById(R.id.reg_full_name);
        reg_company_name = (EditText) findViewById(R.id.reg_company_name);
        reg_email = (EditText) findViewById(R.id.reg_email);
        check_tc = (CheckBox) findViewById(R.id.check_tc);
        reg_register = (Button) findViewById(R.id.reg_register);
        reg_otp = (EditText) findViewById(R.id.reg_otp);
        reg_llOtp = (LinearLayout) findViewById(R.id.reg_llOtp);
        reg_verifyOtp = (Button) findViewById(R.id.reg_verifyOtp);
        reg_resendOtp = (Button) findViewById(R.id.reg_resendOtp);

        reg_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        log_closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reg_full_name.getText().toString().equals("")
                    || reg_number.getText().length()<10
                    || reg_company_name.getText().toString().equals("")
                    || reg_password.getText().length()<6
                    || !check_tc.isChecked()){
                    if(reg_full_name.getText().toString().equals("")){
                        reg_full_name.setError("Enter Full Name");
                    }else{reg_full_name.setError(null);}
                    if(reg_company_name.getText().toString().equals("")){
                        reg_company_name.setError("Enter Company Name");
                    }else{reg_company_name.setError(null);}
                    if(reg_number.getText().length()<10){
                        reg_number.setError("Invalid Number");
                    }else{reg_number.setError(null);}
                    if(reg_password.getText().length()<6){
                        reg_password.setError("Minimum 6 Characters");
                    }else{reg_password.setError(null);}
                    if(!check_tc.isChecked()){
                        check_tc.setError("Check T&C");
                    }else{check_tc.setError(null);}
                }else{
                    registerAdmin();
                }
            }
        });

        reg_verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reg_otp.getText().length()<4){
                    reg_otp.setError("Invalid OTP");
                }else{
                    reg_otp.setError(null);
                    verifyOTP();
                }
            }
        });

        reg_resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reg_full_name.getText().toString().equals("")
                        || reg_number.getText().length()<10
                        || reg_company_name.getText().toString().equals("")
                        || reg_password.getText().length()<6
                        || !check_tc.isChecked()){
                    if(reg_full_name.getText().toString().equals("")){
                        reg_full_name.setError("Enter Full Name");
                    }else{reg_full_name.setError(null);}
                    if(reg_company_name.getText().toString().equals("")){
                        reg_company_name.setError("Enter Company Name");
                    }else{reg_company_name.setError(null);}
                    if(reg_number.getText().length()<10){
                        reg_number.setError("Invalid Number");
                    }else{reg_number.setError(null);}
                    if(reg_password.getText().length()<6){
                        reg_password.setError("Minimum 6 Characters");
                    }else{reg_password.setError(null);}
                    if(!check_tc.isChecked()){
                        check_tc.setError("Check T&C");
                    }else{check_tc.setError(null);}
                }else{
                    registerAdmin();
                }
            }
        });

    }

    private void registerAdmin() {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        // String uri = "https://meradaftar.com/demoVendor/login.php";

        String uri = getResources().getString(R.string.server_name) + "merizeb_admin_register.php";
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
                                reg_llOtp.setVisibility(View.VISIBLE);
                                reg_otp.setVisibility(View.VISIBLE);
                                reg_register.setVisibility(View.GONE);
                                clientCode = JO.getString("client_code");
                                progressDialog.dismiss();
                                reg_otp.setText(JO.getString("Otp"));
                                Toast.makeText(getApplicationContext(),JO.getString("Message"),Toast.LENGTH_LONG).show();
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
                        Toast.makeText(SignUp.this, "Volley Error", Toast.LENGTH_SHORT).show();
                    }
                }
        ){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("mobile",reg_number.getText().toString());
                params.put("password",reg_password.getText().toString());
                params.put("first_name",reg_full_name.getText().toString());
                params.put("company_name",reg_company_name.getText().toString());
                params.put("email",reg_email.getText().toString());

                return params;
            }
        };
        queue.add(stringRequest);

    }


    private void verifyOTP() {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        // String uri = "https://meradaftar.com/demoVendor/login.php";

        String uri = getResources().getString(R.string.server_name) + "merizeb_admin_register_verify_otp.php";
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

                                progressDialog.dismiss();
                                userName = JO.getString("username");
                                Toast.makeText(getApplicationContext(),JO.getString("res"),Toast.LENGTH_LONG).show();
                                openApp();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),JO.getString("res"),Toast.LENGTH_LONG).show();
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
                        Toast.makeText(SignUp.this, "Volley Error", Toast.LENGTH_SHORT).show();
                    }
                }
        ){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("mobile",reg_number.getText().toString());
                params.put("otp",reg_otp.getText().toString());
                params.put("client_code",clientCode);

                return params;
            }
        };
        queue.add(stringRequest);

    }

    private void openApp(){

        session.setLoggedIn(true);
        session.setPrefix(clientCode);
        session.setUserName(userName);

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

}
