package barbapplications.sft;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import com.android.volley.*;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button btnRegisterHere, btnLogin;
    EditText etbadge,etpassWord;
    String carryBadgeNo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //
        etbadge= (EditText) findViewById(R.id.etbadge);
        etpassWord= (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegisterHere = (Button) findViewById(R.id.btnRegisterHere);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btnRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                openRegistration();
            }
        });
    }

    public void openReceiverActivity(){
        Intent intent= new Intent(this,ReceiverActivity.class);
        startActivity(intent);
    }
    public void openHomeScreen(){
       //uncomment to revert Intent intent= new Intent(this,HomeScreenActivity.class);

        // new stuff
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.putExtra("kBadgeNo", carryBadgeNo);


        //new stuff


        startActivity(intent);
    }
    public void openRegistration(){
        Intent intent= new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }
    private void login(){
        String url="https://csi445.ddns.net/csi/login_type_id.php";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if( response.trim().equals("unsuccessful") ){Toast.makeText(getApplicationContext(),"Login Failed", Toast.LENGTH_SHORT).show();}
                else{
                try {
                    JSONArray jsonArray= new JSONArray(response);
                    JSONObject jsonObject= jsonArray.getJSONObject(0);
                    String userType= jsonObject.getString("userType");
                    carryBadgeNo= jsonObject.getString("badgeNo");
                    Toast.makeText(getApplicationContext(),"Logged In", Toast.LENGTH_SHORT).show();

                    if(userType.equalsIgnoreCase("Sender")){
                    openHomeScreen();}
                    else{
                        openReceiverActivity();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }

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
                params.put("badgeNo", etbadge.getText().toString().trim()); /// key must match in php
                params.put("password", etpassWord.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);



    }
}


