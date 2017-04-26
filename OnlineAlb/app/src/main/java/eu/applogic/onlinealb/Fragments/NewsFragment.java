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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eu.applogic.onlinealb.Adapters.NewsRssAdapter;
import eu.applogic.onlinealb.Application.AppController;
import eu.applogic.onlinealb.HelperClasses.AppConfig;
import eu.applogic.onlinealb.HelperClasses.DebugLogger;
import eu.applogic.onlinealb.HelperClasses.DividerItemDecoration;
import eu.applogic.onlinealb.HelperClasses.ParsingFunctions;
import eu.applogic.onlinealb.HelperClasses.RecyclerTouchListener;
import eu.applogic.onlinealb.Objects.NewsRssFeedObject;
import eu.applogic.onlinealb.R;

/**
 * Created by makis on 4/5/2017.
 */

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private Spinner categorySpinner;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Activity activity;
    private ParsingFunctions parse;
    private NewsRssAdapter mAdapter;
    private int selectedCategory = 0;
    private boolean firstTime = true;

    private ArrayList<NewsRssFeedObject> mNews = new ArrayList<>();

    public static NewsFragment newInstance() {
        NewsFragment f = new NewsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_content_main, container, false);
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

        mAdapter = new NewsRssAdapter(activity, mNews);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, R.drawable.custom_list_divider, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(activity, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                NewsRssFeedObject newsObject = mNews.get(position);

                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(newsObject.getLink()));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        categorySpinner = (Spinner) view.findViewById(R.id.category_spinner);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int id, long l) {
                DebugLogger.debug("Selected item: "+id);
                selectedCategory = id;
                if(!firstTime){
                    getNewsList(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        firstTime = false;
                        getNewsList(selectedCategory);
                    }
                }
        );
    }

    @Override
    public void onRefresh() {
        getNewsList(selectedCategory);
    }

    private void getNewsList(int selectedCategory) {

        String newsUrl = getNewsUrl(selectedCategory);

        swipeRefreshLayout.setRefreshing(true);

        String tag_string_req = "request_news";
        StringRequest strReq = new StringRequest(Request.Method.GET, newsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DebugLogger.longInfo("Response value is: ",response);
                mNews.clear();

                if(response != null && response.trim().length()>0){
                    mNews = parse.parseRssNews(response);
                    DebugLogger.debug("Size of parsed array list with news: "+mNews.size());

                    mAdapter.setAdapterData(mNews);
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
                headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");
                headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                headers.put("Content-type", "text/html; charset=UTF-8");
                return headers;
            }
        };
        int socketTimeout = 10000;//Response timeout
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 1, 1);
        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private String getNewsUrl(int selectedCategory) {
        if(selectedCategory == 0){
            return "http://www.gazetaexpress.com/rss/ballina/?xml=1";
        }else if(selectedCategory == 1){
            return "http://www.gazetaexpress.com/rss/lajme/?xml=1";
        }else if(selectedCategory == 2){
            return "http://www.gazetaexpress.com/rss/lajme-nga-shqiperia/?xml=1";
        }else if(selectedCategory == 3){
            return "http://www.gazetaexpress.com/rss/lajme-nga-maqedonia/?xml=1";
        }else if(selectedCategory == 4){
            return "http://www.gazetaexpress.com/rss/sport/?xml=1";
        }else if(selectedCategory == 5){
            return "http://www.gazetaexpress.com/rss/roze/?xml=1";
        }else if(selectedCategory == 6){
            return "http://www.gazetaexpress.com/rss/shneta/?xml=1";
        }else if(selectedCategory == 7){
            return "http://www.gazetaexpress.com/rss/oped/?xml=1";
        }else if(selectedCategory == 8){
            return "http://www.gazetaexpress.com/rss/arte/?xml=1";
        }else if(selectedCategory == 9){
            return "http://www.gazetaexpress.com/rss/fun/?xml=1";
        }else if(selectedCategory == 10){
            return "http://www.gazetaexpress.com/rss/mistere/?xml=1";
        }else if(selectedCategory == 11){
            return "http://www.gazetaexpress.com/rss/auto-tech/?xml=1";
        }else{
            return "http://www.gazetaexpress.com/rss/ballina/?xml=1";
        }
    }
}
