package barbapplications.sft;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.content.Context;

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;
    // Constructor
    private MySingleton(Context context){
        mCtx=context;
        requestQueue= getRequestQueue();
    }


    public RequestQueue getRequestQueue(){
        if(requestQueue==null) {
            requestQueue= Volley.newRequestQueue(mCtx.getApplicationContext());// if request queue is empty create new one
        }
        return requestQueue;
    }

    // Returns instance of of Mysingleton
    public static synchronized MySingleton getInstance(Context context){
        if(mInstance==null){
            mInstance= new MySingleton(context);
        }
        return mInstance;
    }
    // This method adds request to request queue
    public <T>void addToRequestque(Request<T> request){
        requestQueue.add(request);
    }



}
