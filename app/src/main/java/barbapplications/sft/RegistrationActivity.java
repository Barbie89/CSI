package barbapplications.sft;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.HashMap;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    String fName, lName, email, cEmail, username, password;
    EditText etFirstName, etLastName, etEmail, etConfirmEmail, etUsername, etPassword;
    Button btnRegister;
    AlertDialog.Builder builder;
    String url="https://csi445.ddns.net/csi/new_user.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // initialize all edit text variables
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etConfirmEmail = (EditText) findViewById(R.id.etConfirmEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        // initialize button
        btnRegister = (Button) findViewById(R.id.btnRegister);
        // initialize builder
        builder = new AlertDialog.Builder(RegistrationActivity.this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fName= etFirstName.getText().toString();
                lName= etLastName.getText().toString();
                email=etEmail.getText().toString();
                cEmail=etConfirmEmail.getText().toString();
                username=etUsername.getText().toString();
                password= etPassword.getText().toString();

                if (fName.equals("") || lName.equals("") || email.equals("") || cEmail.equals("") || username.equals("") || password.equals("")) {
                    builder = new AlertDialog.Builder(RegistrationActivity.this);
                    builder.setTitle("Fields Missing:");
                    builder.setMessage("Fill in all fields.");
                    displayAlert("input_error");
                }

                else if (!(email.equals(cEmail))){
                    builder = new AlertDialog.Builder(RegistrationActivity.this);
                    builder.setTitle("E-mails Do Not Match:");
                    builder.setMessage("Renter Email");
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
                                builder.setTitle("Registration Status:");
                                builder.setMessage(message);
                                displayAlert(code);


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
                            params.put("fname", fName);
                            params.put("lname",lName);
                            params.put("email",email);
                            params.put("username",username);
                            params.put("password",password);
                            return params;
                        }
                    };
                    MySingleton.getInstance(RegistrationActivity.this).addToRequestque(stringRequest);
                    ////////

                }

            }
        });

    }
    public void openLogin(){
        Intent intent= new Intent(this,Login.class);
        startActivity(intent);
    }
    public void displayAlert(final String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(code.equals("reg_success")){
                    openLogin();
                    //finish();
                }
                else if (code.equals("reg_failed")){
                    etFirstName.setText("");
                    etLastName.setText("");
                    etUsername.setText("");
                    etPassword.setText("");
                    etEmail.setText("");
                    etConfirmEmail.setText("");

                }
                else if(code.equals("input_error"))

                etEmail.setText("");
                etConfirmEmail.setText("");
                dialog.dismiss();
            }

        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();

    }
}


