package eu.applogic.onlinealb.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.arlib.floatingsearchview.FloatingSearchView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import eu.applogic.onlinealb.Activity.RadioPlayerActivity;
import eu.applogic.onlinealb.Adapters.NewsRssAdapter;
import eu.applogic.onlinealb.Adapters.RadiosAdapter;
import eu.applogic.onlinealb.Application.AppController;
import eu.applogic.onlinealb.HelperClasses.AppConfig;
import eu.applogic.onlinealb.HelperClasses.DebugLogger;
import eu.applogic.onlinealb.HelperClasses.DividerItemDecoration;
import eu.applogic.onlinealb.HelperClasses.ParsingFunctions;
import eu.applogic.onlinealb.HelperClasses.RecyclerTouchListener;
import eu.applogic.onlinealb.Objects.NewsRssFeedObject;
import eu.applogic.onlinealb.Objects.RadioStationObject;
import eu.applogic.onlinealb.R;

/**
 * Created by makis on 4/6/2017.
 */

public class RadioStationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private FloatingSearchView mSearch;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Activity activity;
    private ParsingFunctions parse;
    private RadiosAdapter mAdapter;

    private ArrayList<RadioStationObject> mRadio = new ArrayList<>();

    public static RadioStationsFragment newInstance() {
        RadioStationsFragment f = new RadioStationsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.radio_content_main, container, false);
        activity = getActivity();

        /**
         * Initialize helpers classes
         */
        parse = new ParsingFunctions(activity);

        setLayout();
        getRadioStations();
        return view;
    }

    private void setLayout() {
        mSearch = (FloatingSearchView) view.findViewById(R.id.mSearch);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.RED, Color.RED, Color.RED);
        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new RadiosAdapter(activity, mRadio);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, R.drawable.custom_list_divider, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(activity, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                RadioStationObject radio = mRadio.get(position);
                Intent intent = new Intent(activity, RadioPlayerActivity.class);
                intent.putExtra(AppConfig.radio, (Serializable) radio);
                activity.startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getRadioStations();
                    }
                }
        );

        mSearch.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                DebugLogger.debug("On Search Text Changed called - params "+oldQuery+" - "+newQuery);

                ArrayList<RadioStationObject> tempData;
                tempData = (ArrayList<RadioStationObject>)mRadio.clone();
                mAdapter.setAdapterData(tempData);

                newQuery = newQuery.toLowerCase();
                final ArrayList<RadioStationObject> filteredModelList = new ArrayList<>();
                DebugLogger.debug("Size of mData: "+tempData.size()+" - "+mRadio.size());
                for (RadioStationObject model : tempData) {
                    final String text = model.getName().toLowerCase();
                    if (text.contains(newQuery)) {
                        filteredModelList.add(model);
                    }
                }
                DebugLogger.debug("Size of filtered list is: "+filteredModelList.size());

                mAdapter.setAdapterData(filteredModelList);
                mAdapter.notifyDataSetChanged();

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        });
    }

    @Override
    public void onRefresh() {
        getRadioStations();
    }

    private void getRadioStations(){
        swipeRefreshLayout.setRefreshing(true);
        String radioURL = "http://escucharlaradio.net/servicio_radios/stations2.php?cc=AL&lastUpdate=2000-01-01";
        String tag_string_req = "request_radio";
        StringRequest strReq = new StringRequest(Request.Method.GET, radioURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DebugLogger.longInfo("Response value is: ",response);
                mRadio.clear();

                if(response!=null && response.trim().length()>0){
                    mRadio = parse.parseRadioStations(response);
                    DebugLogger.debug("Size of mRadio is: "+mRadio.size());

                    mAdapter.setAdapterData(mRadio);
                    mAdapter.notifyDataSetChanged();

                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    layoutManager.scrollToPositionWithOffset(0, 0);

                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLogger.debug("Error value is: "+error.getLocalizedMessage());
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + Base64.encodeToString("radios2016:JK4y#HMTwNUP".getBytes(), 2));
                headers.put("Accept", "application/json");
                return headers;
            }

        };
        int socketTimeout = 10000;//Response timeout
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 1, 1);
        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
