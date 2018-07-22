package com.example.karan.digitalattendancesystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class StudentLoginActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner_course;
    EditText edittxt_rollno, edittxt_password;
    Button btn_login;
    TextView txt_newuser, txt_fgetpass;

    String course_selected, rollno, password;
    private static final String URL = Constants.ROOT_URL+"login_request_handler.php";
    private static final String ENTITY = "student";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        edittxt_password = findViewById(R.id.login_edit_txt_password);
        edittxt_rollno = findViewById(R.id.login_edit_txt_rollno);
        btn_login = findViewById(R.id.btn_login);
        txt_fgetpass = findViewById(R.id.txt_fgetpass);
        txt_newuser = findViewById(R.id.txt_newuser);
        spinner_course = findViewById(R.id.login_spin_course);

        course_selected = null;
        rollno = null;
        password = null;

        btn_login.setOnClickListener(this);
        txt_fgetpass.setOnClickListener(this);
        txt_newuser.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.courses,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_course.setAdapter(adapter);

        // when user interact with spinner
        spinner_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               course_selected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onClick(View v)
    {
        if(v == btn_login)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("connecting to server...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            // this is real code
            rollno = edittxt_rollno.getText().toString().trim().toUpperCase();
            password = edittxt_password.getText().toString().trim();

            if (rollno.equals("") || password.equals(""))
            {
                Toast.makeText(getApplicationContext(),"please enter rollno or password",Toast.LENGTH_LONG).show();

            }
            else
            {
                Log.d("karan",rollno);
                Log.d("karan",password);
                Log.d("karan",course_selected);

                RequestHandler requestHandler = RequestHandler.getInstance(this);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("login response ",response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("error") == false){
                                // move to student dashboard as login is success
                                Intent i = new Intent(StudentLoginActivity.this,StudentDashboardActivity.class);
                                i.putExtra("course",course_selected);
                                i.putExtra("rollno",rollno);
                                StudentLoginActivity.this.startActivity(i);
                                StudentLoginActivity.this.finish();
                                finish();
                            }
                            else{
                                // login unsuccessful
                            }
                            Toast.makeText(getApplicationContext(),object.getString("message"),Toast.LENGTH_LONG).show();
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
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("rollno", rollno);
                        params.put("password", password);
                        params.put("course", course_selected);
                        params.put("entity",ENTITY);

                        return params;
                    }
                };
                // finally adding request to request queue using RequestHandler Object.
                requestHandler.addToRequestQueue(stringRequest);
            }

        }
        else if (v == txt_fgetpass)
        {
                Toast.makeText(this, "sorry, this feature is not available now", Toast.LENGTH_SHORT).show();
        }
        else
        {
                // new user
                Intent i = new Intent(this, StudentRegistrationActivity.class);
                startActivity(i);
                finish();
            }


    }
}
