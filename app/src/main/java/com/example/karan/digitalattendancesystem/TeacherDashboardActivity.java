package com.example.karan.digitalattendancesystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_enableattendance, btn_viewstudentrequests, btn_disableattendance;
    TextView txt_course, txt_lecture;
    String name, email, attendancecount="1";
    String subject = null, course = null;
    Spinner spinner_attendancecount;
    ArrayAdapter<CharSequence> adapter;


    private  static final String URL = Constants.ROOT_URL+"teacher_dashboard_request_handler.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        btn_enableattendance = findViewById(R.id.teach_dash_btn_enableattendance);
        btn_viewstudentrequests = findViewById(R.id.teach_dash_btn_viewstudentrequest);
        btn_disableattendance = findViewById(R.id.teach_dash_btn_disableattendance);
        txt_course = findViewById(R.id.teacher_dash_txt_currentcourse);
        txt_lecture = findViewById(R.id.teacher_dash_txt_currentlecture);
        spinner_attendancecount = findViewById(R.id.tech_dash_spin_attendancecount);

        email = getIntent().getStringExtra("teacher_email");
        name = getIntent().getStringExtra("teacher_name");

        btn_enableattendance.setOnClickListener(this);

        btn_viewstudentrequests.setOnClickListener(this);

        btn_disableattendance.setOnClickListener(this);

        adapter = ArrayAdapter.createFromResource(this,R.array.attendancecount,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_attendancecount.setAdapter(adapter);

        spinner_attendancecount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               attendancecount =  parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                attendancecount = parent.getItemAtPosition(0).toString();
            }
        });

        getCurrentLectureInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_teacher_dashboard,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.mnu_teacher_refresh:
                  getCurrentLectureInfo();
                return true;

            case R.id.mnu_teacher_logout:
                startActivity(new Intent(this,TeacherLoginActivity.class));
                finish();
                return true;

            default:return false;
        }
    }

    private void getCurrentLectureInfo()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting info...");
        progressDialog.setCanceledOnTouchOutside(false);
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
                        subject = object.getString("lecture");
                        course = object.getString("course");
                        txt_course.setText("Lecture : "+subject);
                        txt_lecture.setText("Class : "+course);

                        if(!subject.equals("N/A"))checkIfAttendanceEnabled();
                    }
                    else
                    {

                        // when some error occured at backend(php)

                    }
                    Toast.makeText(TeacherDashboardActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(TeacherDashboardActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("request_type","get_current_lecture");
                params.put("name",name);
                return params;
            }
        };
        // finally adding request to request queue using RequestHandler Object.

        requestHandler.addToRequestQueue(stringRequest);
    }


    private void checkIfAttendanceEnabled()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("contacting server...");
        progressDialog.setCanceledOnTouchOutside(false);
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

                        Toast.makeText(TeacherDashboardActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        // disable Enable attendance button
                        if(object.getBoolean("isenabled"))btn_enableattendance.setEnabled(false);
                        else btn_enableattendance.setEnabled(true);

                        // check here to update student list who requested for attendance

                    }
                    else
                    {
                        // when some error occured at backend(php)
                        Toast.makeText(TeacherDashboardActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(TeacherDashboardActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("request_type","check_if_attendance_enabled");
                params.put("course",course);
                params.put("lecture",subject);
                return params;
            }
        };
        // finally adding request to request queue using RequestHandler Object.

        requestHandler.addToRequestQueue(stringRequest);

    }



    private void enableAttendance()
    {

        if(!course.equals("N/A") && !subject.equals("N/A"))
        {
            final ProgressDialog progressDialog = new ProgressDialog(TeacherDashboardActivity.this);
            progressDialog.setMessage("Contacting server to enable the attendance ...");
            progressDialog.show();

            RequestHandler requestHandler = RequestHandler.getInstance(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (!object.getBoolean("error")) {

                            // disable button
                            //btn_enableattendance.setEnabled(false);
                            // check here to update student list who requested for attendance


                        } else {
                            // when some error occured at backend(php)
                        }
                        Toast.makeText(TeacherDashboardActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(TeacherDashboardActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("request_type", "enable_attendance");
                    params.put("lecture", subject);
                    params.put("course", course);
                    params.put("attendance_count", attendancecount);
                    return params;
                }
            };
            // finally adding request to request queue using RequestHandler Object.

            requestHandler.addToRequestQueue(stringRequest);
        }
        else Toast.makeText(this,"NO lecture found",Toast.LENGTH_SHORT).show();
    }


    private void disableAttendance()
    {
        if(!course.equals("N/A") && !subject.equals("N/A"))
        {
            final ProgressDialog progressDialog = new ProgressDialog(TeacherDashboardActivity.this);
            progressDialog.setMessage("Contacting server to enable the attendance ...");
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
                            // disable button
                            btn_enableattendance.setEnabled(true);
                            //btn_disableattendance.setEnabled(false);
                            // check here to update student list who requested for attendance

                        } else
                            {
                            // when some error occured at backend(php)
                        }
                        Toast.makeText(TeacherDashboardActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(TeacherDashboardActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("request_type", "disable_attendance");
                    params.put("lecture", subject);
                    params.put("course", course);
                    return params;
                }
            };
            // finally adding request to request queue using RequestHandler Object.

            requestHandler.addToRequestQueue(stringRequest);
        }
        else Toast.makeText(this,"NO lecture found",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        if(v ==btn_enableattendance)
        {
            if(!txt_lecture.getText().toString().equals("Lecture : N/A"))enableAttendance();
            else Toast.makeText(this,"cannot enable the attendance as there is no lecture",Toast.LENGTH_SHORT).show();
        }
        else if(v == btn_viewstudentrequests)
        {
            Intent i = new Intent(TeacherDashboardActivity.this,StudentRequestListActivity.class);
            i.putExtra("course",course);
            i.putExtra("subject",subject);
            startActivity(i);
        }
        else if(v == btn_disableattendance)
        {
            //getCurrentLectureInfo();
            disableAttendance();
        }
    }
}


