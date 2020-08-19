package com.softhub.vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddNewCustomer extends AppCompatActivity {

    private Button reg_register;
    private EditText reg_email;
    private EditText reg_password;
    private EditText reg_fname;
    private EditText reg_lname;
    private EditText reg_number;
    private int viewCount=1;

    private String s_email="NA";
    private String s_password;
    private String s_fname;
    private String s_lname;
    private String s_number;

    private Session session;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        getSupportActionBar().hide();

        session = new Session(getApplicationContext());
        progressDialog = new ProgressDialog(AddNewCustomer.this);

        reg_email = (EditText) findViewById(R.id.reg_email);
        reg_password = (EditText) findViewById(R.id.reg_password);
        reg_fname = (EditText) findViewById(R.id.reg_fname);
        reg_lname = (EditText) findViewById(R.id.reg_lname);
        reg_number = (EditText) findViewById(R.id.reg_number);
        reg_register = (Button) findViewById(R.id.reg_register);
        final ImageView view = (ImageView) findViewById(R.id.view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewCount==0){
                    view.setImageResource(R.drawable.ic_action_visible);
                    viewCount=1;
                    reg_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    view.setImageResource(R.drawable.ic_action_visiblenot);
                    viewCount=0;
                    reg_password.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });

        reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Toast.makeText(Register.this, session.prefs.getString("Tkn",null), Toast.LENGTH_SHORT).show();
            if(reg_password.getText().toString().equals("") ||
                    reg_fname.getText().toString().equals("") ||
                    reg_lname.getText().toString().equals("") ||
                    reg_number.getText().toString().equals("")){
                Toast.makeText(AddNewCustomer.this, "Fill all * fields", Toast.LENGTH_LONG).show();
            }else{
                s_password = reg_password.getText().toString();
                s_fname= reg_fname.getText().toString();
                s_lname = reg_lname.getText().toString();
                s_number = reg_number.getText().toString();

                if(s_password.length()<6){
                    reg_password.setError("6 Characters Minimum");
                }else{
                    reg_password.setError("");
                    if(s_email.equals("")){
                        s_email = "NA";
                    }else{
                        s_email = reg_email.getText().toString();
                    }
                    registerUser();
                }
            }
            }
        });
    }

    private void registerUser() {

            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            String uri = getResources().getString(R.string.server_name) + "addNewCustomer.php";
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject JO = new JSONObject(s);
                                String result = JO.getString("data_code");
                                if(result.equals("200")){
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            // Toast.makeText(getApplicationContext(), "Registration.Error!",Toast.LENGTH_SHORT).show();

                        }
                    }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();

                    params.put("first_name", s_fname);
                    params.put("last_name", s_lname);
                    params.put("mobile", s_number);
                    params.put("email", s_email);
                    params.put("password", s_password);
                    params.put("client", session.prefs.getString("prefix", null));
                    params.put("token", "NULL");

                    return params;
                }
            };
            queue.add(stringRequest);
    }

}