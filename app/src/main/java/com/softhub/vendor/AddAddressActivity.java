package com.softhub.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class AddAddressActivity extends AppCompatActivity {

    private ImageView backButton;
    private Spinner mAddressGroupSpinner;

    private ArrayList<FetchGroup> groupArrayList;
    private AdapterGroup gAdapter;
    private ProgressDialog dialog;
    private String prefixString;
    private Session session;
    private String groupId, groupName, addressString, preferenceString;
    private Button mSubmit;
    private EditText address, preference;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter glAdapter;
    private List<FetchGroupList> fetchGroupLists;


    private ArrayList<String> groupListName;
    private ArrayList<String> groupListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getSupportActionBar().hide();


        groupListName = new ArrayList<String>();
        groupListId = new ArrayList<String>();

        dialog = new ProgressDialog(AddAddressActivity.this);
        session = new Session(AddAddressActivity.this);
        prefixString = session.prefs.getString("prefix",null);

        backButton = (ImageView) findViewById(R.id.backButton);
        mSubmit = (Button) findViewById(R.id.submit);
        address = (EditText) findViewById(R.id.address_name);
        preference = (EditText) findViewById(R.id.preference_name);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mAddressGroupSpinner = (Spinner) findViewById(R.id.spinner_Address);

        groupArrayList = new ArrayList<>();
        groupArrayList.add(new FetchGroup("","Choose Group"));

        gAdapter = new AdapterGroup(this,groupArrayList);
        mAddressGroupSpinner.setAdapter(gAdapter);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        fetchGroupLists = new ArrayList<>();
        glAdapter = new AdapterGroupList(fetchGroupLists,getApplicationContext());
        recyclerView.setAdapter(glAdapter);

        chooseAddressGroup();

        mAddressGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    groupId = groupListId.get(i);
                    if(!groupId.equals("0")){
                        loadGroupList();
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressString = address.getText().toString();
                preferenceString = preference.getText().toString();
                if(!addressString.equals("") || !preferenceString.equals("")){
                    addGroupAddress();
                }else{
                    if(addressString.equals("")){
                        address.setError("Add Address");
                    }
                    if(preferenceString.equals("")){
                        preference.setError("Add Preference");
                    }
                }

            }
        });

    }

        private void chooseAddressGroup(){
            dialog.setMessage("Loading....");
            dialog.show();
            groupListId.clear();
            groupListId.add("0");
            groupListName.clear();
            groupListName.add("Select");
            String uri = getResources().getString(R.string.server_name)+"choose_groupaddress.php";
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

                                    JSONArray JA = JO.getJSONArray("res");

                                    for(int i = 0; i<JA.length();i++) {
                                        JSONObject JO1 = JA.getJSONObject(i);

                                        FetchGroup fetchGroup = new FetchGroup(JO1.getString("id"),
                                                JO1.getString("group_name")
                                        );
                                        groupArrayList.add(fetchGroup);
                                        groupListId.add(JO1.getString("id"));
                                        groupListName.add(JO1.getString("group_name"));
                                    }
                                    setAdapter();
                                    dialog.dismiss();
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                                Toast.makeText(AddAddressActivity.this, "Json error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddAddressActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("prefix",prefixString);

                    return params;
                }
            };
            queue.add(stringRequest);

        }

    private void setAdapter() {

        ArrayAdapter<String> countListAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, groupListName);
        countListAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mAddressGroupSpinner.setAdapter(countListAdapter);

    }

    private void addGroupAddress() {


            dialog.setMessage("Loading...");
            dialog.show();

            String uri = getResources().getString(R.string.server_name)+"add_addressGroup.php";
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
                                    Toast.makeText(AddAddressActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                    loadGroupList();
                                    dialog.dismiss();

                                }else {
                                    Toast.makeText(AddAddressActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(AddAddressActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Toast.makeText(AddAddressActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("prefix",prefixString);
                    params.put("groupId",groupId);
                    params.put("address",addressString);
                    params.put("preference",preferenceString);

                    return params;
                }
            };
            queue.add(stringRequest);
        }

        private void loadGroupList(){

            fetchGroupLists.clear();

            dialog.setMessage("Loading...");
            dialog.show();

            String uri = getResources().getString(R.string.server_name) + "group_address_list.php";
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

                                        FetchGroupList groupList = new FetchGroupList(JO1.getString("address"),
                                                JO1.getString("pref")
                                        );
                                        fetchGroupLists.add(groupList);
                                    }
                                    glAdapter = new AdapterGroupList(fetchGroupLists,getApplicationContext());
                                    recyclerView.setAdapter(glAdapter);
                                    dialog.dismiss();
                                }

                                else
                                {
                                    Toast.makeText(AddAddressActivity.this, JO.getString("Message"), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }


                            }catch (JSONException e){
                                e.printStackTrace();
                                Toast.makeText(AddAddressActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddAddressActivity.this, "Volley error" + error, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("prefix",prefixString);
                    params.put("groupId",groupId);
                    return params;
                }
            };
            queue.add(stringRequest);
        }
}


