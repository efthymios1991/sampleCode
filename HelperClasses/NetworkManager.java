
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by makis on 1/12/2017.
 */

public class NetworkManager {

    private static final String TAG = NetworkManager.class.getSimpleName();

    private static NetworkManager instance = null;

    private Context mContext;
    private RequestQueue requestQueue;

    private NetworkManager(Context context) {
        this.mContext = context;
        requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
    }

    public static synchronized NetworkManager getInstance(Context context){
        if (null == instance){
            instance = new NetworkManager(context);
        }
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized NetworkManager getInstance() {
        if (null == instance) {
            throw new IllegalStateException(NetworkManager.class.getSimpleName() + " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void customNetworkRequest(String requestTag, int requestMethod, String url,
                                               final Map<String, String> headers, final Map<String, String> params, int timeOut,
                                               final ResponseListener<String> responseListener, final ErrorListener<VolleyError> errorListener) {

        StringRequest strReq = new StringRequest(requestMethod, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                responseListener.getResult(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                errorListener.getError(error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(timeOut, 1, 1);
        strReq.setRetryPolicy(policy);
        strReq.setTag(requestTag);
        getRequestQueue().add(strReq);
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return requestQueue;
    }

    public interface ResponseListener<T> {
        public void getResult(T object);
    }

    public interface ErrorListener<T> {
        public void getError(T object);
    }
}
