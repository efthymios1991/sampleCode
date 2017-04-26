package eu.applogic.onlinealb.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.core.deps.guava.base.Charsets;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

import eu.applogic.onlinealb.Adapters.ChannelsAdapter;
import eu.applogic.onlinealb.Adapters.SelectedChannelAdapter;
import eu.applogic.onlinealb.Application.AppController;
import eu.applogic.onlinealb.HelperClasses.AppConfig;
import eu.applogic.onlinealb.HelperClasses.BuildConfig;
import eu.applogic.onlinealb.HelperClasses.DebugLogger;
import eu.applogic.onlinealb.HelperClasses.DividerItemDecoration;
import eu.applogic.onlinealb.HelperClasses.ParsingFunctions;
import eu.applogic.onlinealb.HelperClasses.RecyclerTouchListener;
import eu.applogic.onlinealb.Objects.RssFeedObject;
import eu.applogic.onlinealb.R;

/**
 * Created by makis on 4/4/2017.
 */

public class SelectedChannelActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ParsingFunctions parse;
    private SelectedChannelAdapter mAdapter;

    private ArrayList<RssFeedObject> mChannels = new ArrayList<>();
    private RssFeedObject selectedChannel;

    private String dataURL;
    private String authToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getSelectedChannel();
        parse = new ParsingFunctions(this);
        
        setContentView(R.layout.selected_channel_activity);
        setToolbar();
        setLayout();
    }

    private void setLayout() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new SelectedChannelAdapter(this, mChannels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.custom_list_divider, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                RssFeedObject channel = mChannels.get(position);

                if(channel!=null){
                    DebugLogger.debug("Url is: "+channel.getLink()+""+authToken);
                    String [] uris = new String[1];
                    uris[0] = channel.getLink()+""+authToken;
                    Intent intent = new Intent(SelectedChannelActivity.this, PlayerActivity.class);
                    intent.putExtra(PlayerActivity.URI_LIST_EXTRA, uris);
                    intent.setAction(PlayerActivity.ACTION_VIEW_LIST);
                    startActivity(intent);
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
                        getDataForToken();
                        getChannelsList();
                    }
                }
        );
    }

    private String genToken(String remoteip) throws UnsupportedEncodingException {
        String today = DateTimeFormat.forPattern("M/d/y h:m:s a").withZone(DateTimeZone.UTC).withLocale(Locale.US).print(new DateTime(DateTimeZone.UTC));
        String ip = remoteip;
        String validminutes = "360";
        return "?wmsAuthSign=" + Base64.encodeToString(("server_time=" + today + "&hash_value=" + Base64.encodeToString(DigestUtils.md5((ip + "123qwerty" + today + validminutes).getBytes(Charsets.UTF_8)), 2) + "&validminutes=" + validminutes).getBytes(Charsets.UTF_8), 2);
    }

    @Override
    public void onRefresh() {
        getChannelsList();
    }

    private void getSelectedChannel() {
        Intent i = getIntent();
        selectedChannel = (RssFeedObject) i.getSerializableExtra(AppConfig.channel);

        if(selectedChannel == null){
            Toast.makeText(this, "An error occured", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_right);
        }
    }

    private void getChannelsList() {
        swipeRefreshLayout.setRefreshing(true);

        String tag_string_req = "request_specific_channels";
        StringRequest strReq = new StringRequest(Request.Method.GET, selectedChannel.getServiceLink(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DebugLogger.debug("Response value is: "+response);

                mChannels.clear();

                if(response != null && response.trim().length()>0){
                    mChannels = parse.parseSpecificChannels(response);
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
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        }) {

        };
        int socketTimeout = 10000;//Response timeout
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 1, 1);
        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getDataForToken() {
        String tag_string_req = "request_helper_data";
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.helperURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DebugLogger.debug("Helper url - Response value is: "+response);

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if(jObj.has("data")){
                        dataURL = jObj.getString("data");
                        DebugLogger.debug("Value of data in response is "+dataURL);
                        authToken = genToken(dataURL);
                        DebugLogger.debug("Value of genereted token is: "+authToken);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLogger.debug("Error value is: "+error.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {

        };
        int socketTimeout = 10000;//Response timeout
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 1, 1);
        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(selectedChannel.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_right);
        }

        return super.onOptionsItemSelected(item);
    }
}
