package com.example.karan.digitalattendancesystem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentRequestListActivity extends AppCompatActivity {

    private String course, subject;
    ArrayList<StudentDetails> studentlist;
    RecyclerView recyclerView_studentlist;
    MyAdapter adapter;

    JsonArray removedstudentarray = new JsonArray();

    String checkURL = Constants.ROOT_URL+"teacher_dashboard_request_handler.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_request_list);

        course = getIntent().getStringExtra("course");
        subject = getIntent().getStringExtra("subject");
        recyclerView_studentlist = findViewById(R.id.studentlist);

        recyclerView_studentlist.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_studentlist.setHasFixedSize(true);

        studentlist = new ArrayList<>();


        requestAndCheckList();
        initSwipe();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_studentrequestlist,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mnu_refresh:

                if(studentlist!=null)studentlist.clear();
                for(int i = 0; i< removedstudentarray.size(); i++) removedstudentarray.remove(i);
                requestAndCheckList();
                return true;

            case R.id.mnu_send:
                sendAttendance();
                return true;

            case R.id.mnu_showonmap:
                if(studentlist.size()>0)
                {
                    Intent i = new Intent(StudentRequestListActivity.this,MapsActivity.class);
                    i.putExtra("list_of_students",studentlist);
                    startActivity(i);
                }
                return true;
        }
        return false;
    }

    private void sendAttendance()
    {
        if(!course.equals("N/A"))
        {
            Log.d("removedstudentlist", removedstudentarray.toString());
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("marking attendance \n please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

           /* // if approvedlist is empty then return
            if (studentlist.size() == 0) {
                progressDialog.dismiss();
                return;
            }*/

            // getting jsonarry from rlist
            Gson gson = new Gson();
            final JsonArray jsonarray = new JsonArray();
            for (StudentDetails i : studentlist)
            {
                String jsonobj = gson.toJson(i);
                jsonarray.add(jsonobj);
            }
            Log.d("karan", jsonarray.toString());

            StringRequest request = new StringRequest(Request.Method.POST, checkURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("sendattendance response", response);
                    progressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (!object.getBoolean("error")) {
                            //do whatever you want to do man :D
                            studentlist.clear();
                            adapter.notifyItemRangeChanged(0, 0);
                        }
                        Toast.makeText(StudentRequestListActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(StudentRequestListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("request_type", "mark_attendance");
                    params.put("subject", subject);
                    params.put("course", course);
                    params.put("approvedlist", jsonarray.toString());
                    params.put("removedlist", removedstudentarray.toString());
                    return params;
                }
            };

            RequestHandler requestHandler = RequestHandler.getInstance(this);
            requestHandler.addToRequestQueue(request);
        }
    }

    // this method checks at regular interval for to update student list
    private void requestAndCheckList()
    {
        if(!course.equals("N/A"))
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("getting students...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            final RequestHandler requestHandler = RequestHandler.getInstance(this);
            final Gson gson = new Gson();
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, checkURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (!object.getBoolean("error"))
                        {
                            Toast.makeText(StudentRequestListActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            // check here to update student list who requested for attendance

                            JSONArray jsonArraylist = object.getJSONArray("list");


                            if (!jsonArraylist.toString().equals("[[]]"))
                            {
                                for (int i = 0; i < jsonArraylist.length(); i++)
                                {
                                    StudentDetails details = gson.fromJson(jsonArraylist.get(i).toString(), StudentDetails.class);
                                    studentlist.add(details);
                                }

                            }
                            else Toast.makeText(StudentRequestListActivity.this, "NO request found", Toast.LENGTH_SHORT).show();


                            adapter = new MyAdapter(studentlist, StudentRequestListActivity.this);
                            recyclerView_studentlist.setAdapter(adapter);

                        } else {
                            // when some error occured at backend(php)
                            Toast.makeText(StudentRequestListActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(StudentRequestListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("course", course);
                    params.put("subject", subject);
                    params.put("request_type", "get_students");
                    return params;
                }
            };

            requestHandler.addToRequestQueue(stringRequest);

        }

    }


    private void initSwipe()
    {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @SuppressLint("MissingPermission")
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT)
                {
                    //delete user from list
                    AlertDialog alertDialog = new AlertDialog.Builder(StudentRequestListActivity.this)
                            .setMessage("Are you sure you want to remove this request")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final StudentDetails backedupitem = studentlist.get(position);
                                    studentlist.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    adapter.notifyItemRangeChanged(position, studentlist.size());

                                        //JsonObject object = new JsonObject();

                                    try {
                                        JSONObject object = new JSONObject();
                                        object.put("rollno",backedupitem.getRollno());
                                        object.put("subject",backedupitem.getSubject());
                                        removedstudentarray.add(object.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }



                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Do you want to undelete ?", Snackbar.LENGTH_SHORT);
                                    snackbar.setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            removedstudentarray.remove(removedstudentarray.size()-1);
                                            studentlist.add(position, backedupitem);
                                            adapter.notifyItemInserted(position);
                                        }
                                    });
                                    snackbar.show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    adapter.notifyItemRangeChanged(0,studentlist.size());
                                }
                            })
                            .setCancelable(false)
                            .create();
                    alertDialog.show();


                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
            {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView_studentlist);
    }

}
