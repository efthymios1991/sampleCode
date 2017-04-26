package eu.applogic.onlinealb.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.Serializable;
import java.util.ArrayList;

import eu.applogic.onlinealb.Activity.MainActivity;
import eu.applogic.onlinealb.Activity.SelectedChannelActivity;
import eu.applogic.onlinealb.Adapters.ChannelsAdapter;
import eu.applogic.onlinealb.Application.AppController;
import eu.applogic.onlinealb.HelperClasses.AppConfig;
import eu.applogic.onlinealb.HelperClasses.DebugLogger;
import eu.applogic.onlinealb.HelperClasses.DividerItemDecoration;
import eu.applogic.onlinealb.HelperClasses.ParsingFunctions;
import eu.applogic.onlinealb.HelperClasses.RecyclerTouchListener;
import eu.applogic.onlinealb.Objects.RssFeedObject;
import eu.applogic.onlinealb.R;

/**
 * Created by makis on 4/5/2017.
 */

public class ChannelsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Activity activity;
    private ParsingFunctions parse;
    private ChannelsAdapter mAdapter;

    private ArrayList<RssFeedObject> mChannels = new ArrayList<>();

    public static ChannelsFragment newInstance() {
        ChannelsFragment f = new ChannelsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_main, container, false);
        activity = getActivity();

        /**
         * Initialize helpers classes
         */
        parse = new ParsingFunctions(activity);

        setLayout();
        return view;
    }

    private void setLayout() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.RED, Color.RED, Color.RED);
        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new ChannelsAdapter(activity, mChannels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, R.drawable.custom_list_divider, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(activity, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                RssFeedObject channel = mChannels.get(position);

                if(channel!=null){
                    Intent intent = new Intent(activity, SelectedChannelActivity.class);
                    intent.putExtra(AppConfig.channel, (Serializable) channel);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
                }
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
                        getChannelsList();
                    }
                }
        );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRefresh() {
        // swipe refresh is performed, fetch the messages again
        getChannelsList();
    }

    private void getChannelsList() {
        swipeRefreshLayout.setRefreshing(true);

        String tag_string_req = "request_channels";
        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.channelsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DebugLogger.debug("Response value is: "+response);

                mChannels.clear();

                if(response != null && response.trim().length()>0){
                    mChannels = parse.parseChannels(response);
                    DebugLogger.debug("Size of parsed array list with channels: "+mChannels.size());

                    mAdapter.setAdapterData(mChannels);
                    mAdapter.notifyDataSetChanged();
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

        };
        int socketTimeout = 10000;//Response timeout
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 1, 1);
        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
