package com.example.karan.digitalattendancesystem;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class StudentDashboardActivity extends AppCompatActivity implements View.OnClickListener
{

    Button btn_requestattendance, btn_viewattendance, btn_refresh;
    private static final String URL = Constants.ROOT_URL+"student_dashboard_request_handler.php";
    private String course, rollno;
    private int greyColor;
    TextView txt_requeststatus, txt_currentLecture, txt_currentTeacher;
    RequestHandler requestHandler;

    private String teacher,subject;

    BroadcastReceiver broadcastReceiver;
    private Double longitude,latitude;

    StudentLocationService studentLocationService;
    Intent serviceIntent;
    ProgressDialog locationProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        course = getIntent().getStringExtra("course");
        rollno = getIntent().getStringExtra("rollno");

        txt_requeststatus = findViewById(R.id.stud_dash_txt_reqstatus);
        btn_requestattendance = findViewById(R.id.dash_btn_requestattendance);
        btn_viewattendance = findViewById(R.id.dash_btn_attendancerecord);
        btn_refresh = findViewById(R.id.dash_btn_refresh);
        txt_currentLecture = findViewById(R.id.dash_txt_currentlecture);
        txt_currentTeacher = findViewById(R.id.dash_txt_currentteacher);

        btn_requestattendance.setOnClickListener(this);
        btn_viewattendance.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);

        requestHandler = RequestHandler.getInstance(this);

        // checking if teacher has enabled the attendance or not

        checkLectureAndTeacher();


    }

    private void checkLectureAndTeacher()
    {
        // tested ok
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading current lecture from server...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    Toast.makeText(StudentDashboardActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();

                    if(!object.getBoolean("error"))
                    {
                        subject = object.getString("subject");
                        teacher = object.getString("teacher");
                        txt_currentLecture.setText("Lecture : "+subject);
                        txt_currentTeacher.setText("Teacher : "+teacher);


                        checkIfTeacherEnabledAttendance();
                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(StudentDashboardActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String > params = new HashMap<>();
                params.put("request_type","get_lecture_info");
                params.put("rollno",rollno);
                params.put("course",course);
                return params;
            }
        };

        requestHandler.addToRequestQueue(request);
    }


    private void checkIfAlreadySubmitted()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("checking if you have already submitted attendance...");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    Toast.makeText(StudentDashboardActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();

                    if(!object.getBoolean("error"))
                    {
                        if(object.getBoolean("isrequested"))greenBtn();
                        else enableButton();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(StudentDashboardActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String > params = new HashMap<>();
                params.put("request_type","check_if_already_requested");
                params.put("rollno",rollno);
                params.put("course",course);
                params.put("subject",subject);
                return params;
            }
        };

        requestHandler.addToRequestQueue(request);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v)
    {


       if(v== btn_requestattendance)
        {

            //requestAttendance();
            if(studentLocationService==null)
            {
                studentLocationService = new StudentLocationService();
                serviceIntent = new Intent(this,StudentLocationService.class);
                startService(serviceIntent);

                // starting a progressdialog to notify the user about searching for location
                locationProgress = new ProgressDialog(this);
                locationProgress.setMessage("set back and wait untill your location is gathered...");
                locationProgress.setCancelable(false);
                locationProgress.show();
            }


        }
        else if(v == btn_viewattendance)
        {
            Toast.makeText(this, "sorry this feature is under development", Toast.LENGTH_SHORT).show();
        }
        else if(v == btn_refresh)
       {
           checkLectureAndTeacher();
       }

    }

    private void checkIfTeacherEnabledAttendance()
    {

       final ProgressDialog progressDialog = new ProgressDialog(this);
       progressDialog.setMessage("checking whether you teacher has enabled the attendance or not...");
       progressDialog.dismiss();
         final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();

                try {
                    JSONObject object = new JSONObject(response);

                    if(!object.getBoolean("error"))
                    {
                        checkIfAlreadySubmitted();

                        txt_requeststatus.setText("Status : teacher has enabled the attendance");
                    }
                    else
                    {

                    }

                    Toast.makeText(StudentDashboardActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(StudentDashboardActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String > params = new HashMap<>();
                params.put("request_type","check_if_attendance_enabled");
                params.put("course",course);
                params.put("subject",subject);
                return params;
            }
        };


        requestHandler.addToRequestQueue(stringRequest);

    }




    private void enableButton()
    {
        //Toast.makeText(StudentDashboardActivity.this,"button should be enabled now",Toast.LENGTH_SHORT).show();
        greyColor = btn_requestattendance.getCurrentTextColor();
        StudentDashboardActivity.this.btn_requestattendance.setEnabled(true);
        StudentDashboardActivity.this.btn_requestattendance.setBackground(getDrawable(R.drawable.btn_primary_dark_design));
        StudentDashboardActivity.this.btn_requestattendance.setTextColor(Color.WHITE);
        StudentDashboardActivity.this.btn_requestattendance.setText(R.string.enablebtn);
    }

    private void disableButton()
    {
        StudentDashboardActivity.this.btn_requestattendance.setEnabled(false);
        StudentDashboardActivity.this.btn_requestattendance.setBackground(getDrawable(R.drawable.btn_grey_design));
        StudentDashboardActivity.this.btn_requestattendance.setTextColor(greyColor);
        StudentDashboardActivity.this.btn_requestattendance.setText(R.string.disablebtn);
    }

    private void  greenBtn(){
        StudentDashboardActivity.this.btn_requestattendance.setEnabled(false);
        StudentDashboardActivity.this.btn_requestattendance.setBackground(getDrawable(R.drawable.btn_green_design));
        StudentDashboardActivity.this.btn_requestattendance.setTextColor(Color.WHITE);
        StudentDashboardActivity.this.btn_requestattendance.setText(R.string.successbtn);
    }

    private void requestAttendance()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("sending request...");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    Toast.makeText(StudentDashboardActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();

                    if(!object.getBoolean("error"))greenBtn();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(StudentDashboardActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String > params = new HashMap<>();
                params.put("request_type","request_attendance");
                params.put("rollno",rollno);
                params.put("course",course);
                params.put("subject",subject);
                params.put("longitude",longitude+"");
                params.put("latitude",latitude+"");
                return params;
            }
        };

        requestHandler.addToRequestQueue(request);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(broadcastReceiver == null)
        {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                        locationProgress.dismiss(); // dismissing the location progress

                        longitude=intent.getDoubleExtra("longitude",0);
                        latitude= intent.getDoubleExtra("latitude",0);
                        requestAttendance();

                }
            };

            registerReceiver(broadcastReceiver,new IntentFilter("receive_location_update"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}





