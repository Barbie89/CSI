package barbapplications.sft;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExistingCases extends AppCompatActivity implements CaseListAdapter.AdapterCallback {
    public final static String KEY_CASE_NUMBER = "KEY_CASE_NUMBER";
    private CaseListAdapter adapter;
    private String badgeNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_cases);
        badgeNo = getIntent().getStringExtra("kBadgeNo");

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, badgeNo, Toast.LENGTH_SHORT).show();
        getCaseIDs();
    }

    @Override
    public void onImageButtonClicked(int caseNumber) {
        Intent imagePreviewIntent = new Intent(this, ImagePreviewActivity.class);
        imagePreviewIntent.putExtra("case_id", Integer.toString(caseNumber));
        startActivity(imagePreviewIntent);
    }

    @Override
    public void onSendButtonClicked(int caseNumber, int badgeNumber) {
//        send(1035, 99999); //DEBUG
        Toast.makeText(this, Integer.toString(caseNumber), Toast.LENGTH_SHORT).show();
        send(caseNumber, badgeNumber);
    }

    private void showCases(ArrayList<CaseID> caseIDs){
        if(adapter == null) adapter = new CaseListAdapter(caseIDs, this);
        RecyclerView recyclerView = findViewById(R.id.caseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<CaseID> getCaseIDs(){
        final ArrayList<CaseID> cases = new ArrayList<>();
        String url = "https://csi445.ddns.net/csi/get_cases.php";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try {
                        Log.i("CASE IDs", response);
                        JSONArray jsonArray= new JSONArray(response);
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject= jsonArray.getJSONObject(i);
                            String caseID = jsonObject.getString("cid");
                            cases.add(new CaseID(Integer.parseInt(caseID)));
                        }
                        showCases(cases);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error "+ error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("badgeNo", badgeNo); //
                return params;
            }
        };
        requestQueue.add(stringRequest);
        return cases;
    }

    private void send(final int cid, final int badgeNumber){
        String url="https://csi445.ddns.net/csi/send.php";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CID", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error "+ error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("cid", Integer.toString(cid)); /// key must match in php
                params.put("badgeNo", Integer.toString(badgeNumber));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
