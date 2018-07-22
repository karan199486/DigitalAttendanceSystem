package com.example.karan.digitalattendancesystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeacherLoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editText_email, editText_pass;
    Button btn_login;
    TextView txt_fgetpass;
    Spinner spinner_teachername;

    private static final String URL = Constants.ROOT_URL+"login_request_handler.php";
    private static final String ENTITY = "teacher";
    private String email, password, name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        editText_email = findViewById(R.id.teacher_edittxt_email);
        editText_pass = findViewById(R.id.teacher_edittxt_password);
        btn_login = findViewById(R.id.teacher_btn_login);
        txt_fgetpass = findViewById(R.id.teacher_txt_fgetpass);
        spinner_teachername = findViewById(R.id.teachernames);

        btn_login.setOnClickListener(this);
        txt_fgetpass.setOnClickListener(this);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.teachernames,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner_teachername.setAdapter(arrayAdapter);

        spinner_teachername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v== btn_login)
        {
            // send login request to server
            email = editText_email.getText().toString().trim().toUpperCase();
            password = editText_pass.getText().toString().trim();

            if (email.equals("") || password.equals(""))
            {
                Toast.makeText(getApplicationContext(),"please fill all the fields",Toast.LENGTH_SHORT).show();

            }
            else {

                final ProgressDialog progressDialog = new ProgressDialog(TeacherLoginActivity.this);
                progressDialog.setMessage("Connecting to server\nplease wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                RequestHandler requestHandler = RequestHandler.getInstance(this);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            if (!object.getBoolean("error"))
                            {
                                // move to teacher's dashboard as login is successful
                                Intent i = new Intent(TeacherLoginActivity.this,TeacherDashboardActivity.class);
                                i.putExtra("teacher_name",name);
                                i.putExtra("teacher_email",email);
                                startActivity(i);
                                finish();

                            } else {
                                // login unsuccessful
                            }
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("name",name);
                        params.put("email", email);
                        params.put("password", password);
                        params.put("entity",ENTITY);

                        return params;
                    }
                };
                // finally adding request to request queue using RequestHandler Object.

                requestHandler.addToRequestQueue(stringRequest);
            }
        }
        else  if(v == txt_fgetpass){
            // send forgot password request
            Toast.makeText(this,"Sorry this feature is not available now",Toast.LENGTH_SHORT).show();
        }
    }

}
