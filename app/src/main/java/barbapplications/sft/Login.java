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
                Login();
            }
        });
        btnRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                openRegistration();
            }
        });
    }

    public void openHomeScreen(){
        Intent intent= new Intent(this,HomeScreenActivity.class);
        startActivity(intent);
    }
    public void openRegistration(){
        Intent intent= new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }
    private void Login(){
        String url="https://csi445.ddns.net/csi/login.php";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if( response.trim().equals("success") ){
                    Toast.makeText(getApplicationContext(),"Logged In", Toast.LENGTH_SHORT).show();
                    openHomeScreen();
                }
                else
                    Toast.makeText(getApplicationContext(),"Login Failed", Toast.LENGTH_SHORT).show();

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


