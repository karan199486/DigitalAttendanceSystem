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

public class StudentRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText_name, editText_phone, editText_email, editText_rollno, editText_password, editText_confirmpassword;
    TextView txt_login;
    Button btn_register;
    Spinner spinner_course;

    String url = Constants.ROOT_URL+"registration_request_handler.php";
    String name,password,confirmpassword,email,phoneno,rollno,course;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);
        editText_name = findViewById(R.id.reg_edit_txt_name);
        editText_phone = findViewById(R.id.reg_edit_txt_phoneno);
        editText_email = findViewById(R.id.reg_edit_txt_email);
        editText_password = findViewById(R.id.reg_edit_txt_password);
        editText_confirmpassword = findViewById(R.id.reg_edit_txt_confirmpassword);
        editText_rollno = findViewById(R.id.reg_edit_txt_rollno);
        txt_login = findViewById(R.id.reg_txt_login);
        btn_register = findViewById(R.id.btn_register);
        spinner_course = findViewById(R.id.reg_spin_course);

        btn_register.setOnClickListener(this);
        txt_login.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.courses,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_course.setAdapter(adapter);

        spinner_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 course = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == btn_register.getId())
        {
            name = editText_name.getText().toString().trim();
            rollno = editText_rollno.getText().toString().trim().toUpperCase();
            email = editText_email.getText().toString().trim();
            phoneno = editText_phone.getText().toString().trim();
            password = editText_password.getText().toString().trim();
            confirmpassword = editText_confirmpassword.getText().toString().trim();

            if(!isEmptyAnyField())
            {
                // if all fields are filled
                if (passwordMismatchCheck())
                {
                    // password = confirm password

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("please wait ...");
                    progressDialog.show();
                    // getting request queue
                    RequestHandler requestHandler = RequestHandler.getInstance(this);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            Log.d("response registration",response);
                            progressDialog.dismiss();
                            try {
                                JSONObject object = new JSONObject(response);
                                if(object.getBoolean("error") == false){
                                    // registration successful
                                    startActivity(new Intent(StudentRegistrationActivity.this,StudentLoginActivity.class));
                                    finish();
                                }
                                else{
                                    // registration unsuccessful

                                }
                                Toast.makeText(getApplicationContext(),object.getString("message"),Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
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
                        protected Map<String, String> getParams() throws AuthFailureError
                        {
                            Map<String, String> params = new HashMap<>();
                            params.put("name",name);
                            params.put("rollno", rollno);
                            params.put("phoneno",phoneno);
                            params.put("email",email);
                            params.put("password", password);
                            params.put("course", course);

                            return params;
                        }
                    };
                    // finally adding request to request queue using RequestHandler Object.
                    requestHandler.addToRequestQueue(stringRequest);
                }
                else
                {
                    // password != confirm password
                    Toast.makeText(getApplicationContext(),"password mismatch",Toast.LENGTH_LONG).show();
                }
            }
            else{
                // when some field are empty
                Toast.makeText(getApplicationContext(),"please fill all the fields",Toast.LENGTH_LONG).show();
            }
        }
        else if(v.getId() == txt_login.getId()){
            Intent i = new Intent(this,StudentLoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    private boolean isEmptyAnyField()
    {
        if(name.equals("") || password.equals("") || confirmpassword.equals("") || phoneno.equals("") || email.equals("") || rollno.equals(""))return true;
        else return false;
    }

    private boolean passwordMismatchCheck(){
        if(password.equals(confirmpassword))return true;
        else return false;
    }
}
