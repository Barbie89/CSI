package barbapplications.sft;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class NewCaseActivity extends AppCompatActivity {
    Button btnLaunchCase;
    EditText etCaseName,etBadgeNo,etSiteAddress,etCity,etState,etZipCode,etOtherInfo; //etOtherInfo= Location notes on U.I
    String caseName,badgeNo,siteAddress,city,state,zipCode,other_info;
    AlertDialog.Builder builder;
    String url="https://csi445.ddns.net/csi/new_case.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_case);
        // initialize button
        btnLaunchCase = (Button)findViewById(R.id.btnLaunchCase);
        // initialize all edit text variables
        etCaseName=(EditText) findViewById(R.id.etCaseName);
        etBadgeNo=(EditText) findViewById(R.id.etBadgeNo);
        etSiteAddress=(EditText) findViewById(R.id.etSiteAddress);
        etCity=(EditText) findViewById(R.id.etCity);
        etState=(EditText) findViewById(R.id.etState);
        etZipCode=(EditText) findViewById(R.id.etZipCode);
        etOtherInfo=(EditText) findViewById(R.id.etOtherInfo);
        // initialize builder
        builder = new AlertDialog.Builder(NewCaseActivity.this);

        btnLaunchCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch the info from the new case form
                caseName = etCaseName.getText().toString();
                badgeNo = etBadgeNo.getText().toString();
                siteAddress = etSiteAddress.getText().toString();
                city = etCity.getText().toString();
                state = etState.getText().toString();
                zipCode = etZipCode.getText().toString();
                other_info = etOtherInfo.getText().toString();

                //check required fields are filled
                if(caseName.equals("") || siteAddress.equals("") || city.equals("") || state.equals("") || zipCode.equals("")){
                    builder.setTitle("Fields Missing:");
                    builder.setMessage("Fill in Required Fields *");
                    displayAlert("input_error");

                }
                else{
                    StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray= new JSONArray(response);
                                JSONObject jsonObject= jsonArray.getJSONObject(0);
                                String code= jsonObject.getString("code");
                                String message= jsonObject.getString("message");
                                String case_id = jsonObject.getString("case_id");
                                builder.setTitle("Case Status:");
                                builder.setMessage(message);
                                //displayAlert(code);



                                Intent intent = new Intent(NewCaseActivity.this, ImagePreviewActivity.class);
                                intent.putExtra("case_id", case_id);
                                startActivity(intent);

                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params= new HashMap<>();
                            params.put("caseName", caseName);
                            params.put("badgeNo",badgeNo);
                            params.put("siteAddress",siteAddress);
                            params.put("city",city);
                            params.put("state",state);
                            params.put("zipCode",zipCode);
                            params.put("other_info",other_info);
                            return params;
                        }
                    };
                    MySingleton.getInstance(NewCaseActivity.this).addToRequestque(stringRequest);
                    ////////

                }

            }
        });

    }
    public void displayAlert(final String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(code.equals("case_created")){
                    //openCamera();
                    finish();
                }
                else if (code.equals("case_exists")){
                    etCaseName.setText("");
                    etBadgeNo.setText("");
                    etSiteAddress.setText("");
                    etCity.setText("");
                    etState.setText("");
                    etZipCode.setText("");
                    etOtherInfo.setText("");

                }
                else if(code.equals("input_error"))
                    dialog.dismiss();
            }

        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();

    }

}
