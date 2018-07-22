package com.example.karan.digitalattendancesystem;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by karan on 8/4/18.
 */

public class RequestHandler  {



        private static RequestHandler mInstance;
        private RequestQueue mRequestQueue;

        private static Context mCtx;

        private RequestHandler(Context context) {
            mCtx = context;
            mRequestQueue = getRequestQueue();


        }

        public static synchronized RequestHandler getInstance(Context context) {
            if (mInstance == null)
            {
                mInstance = new RequestHandler(context);
            }
            return mInstance;
        }

        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                // getApplicationContext() is key, it keeps you from leaking the
                // Activity or BroadcastReceiver if someone passes one in.
                mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            }
            return mRequestQueue;
        }

        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }



}
