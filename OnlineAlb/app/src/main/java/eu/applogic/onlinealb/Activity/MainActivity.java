package eu.applogic.onlinealb.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.applogic.onlinealb.Adapters.ChannelsAdapter;
import eu.applogic.onlinealb.Application.AppController;
import eu.applogic.onlinealb.Fragments.ChannelsFragment;
import eu.applogic.onlinealb.Fragments.NewsFragment;
import eu.applogic.onlinealb.Fragments.RadioStationsFragment;
import eu.applogic.onlinealb.HelperClasses.AppConfig;
import eu.applogic.onlinealb.HelperClasses.DebugLogger;
import eu.applogic.onlinealb.HelperClasses.DividerItemDecoration;
import eu.applogic.onlinealb.HelperClasses.ParsingFunctions;
import eu.applogic.onlinealb.HelperClasses.RecyclerTouchListener;
import eu.applogic.onlinealb.Objects.RssFeedObject;
import eu.applogic.onlinealb.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public ViewPagerAdapter adapter;

    private int[] tabIcons = {
            R.drawable.ic_television_white_48dp,
            R.drawable.ic_newspaper_white_48dp,
            R.drawable.ic_radio_white_48dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        setLayout();
    }

    public static String getMetadata(String key, Activity context) {
        String value = null;
        try {
            value = String.valueOf(context.getPackageManager().getApplicationInfo(context.getPackageName(), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS).metaData.get(key));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("GET META DATA", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e2) {
            Log.e("GET META DATA", "Failed to load meta-data, NullPointer: " + e2.getMessage());
        }
        return value;
    }

    private void setLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        viewPager.setOffscreenPageLimit(3);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ChannelsFragment(), "TV");
        adapter.addFragment(new NewsFragment(), "News");
        adapter.addFragment(new RadioStationsFragment(), "Radio");
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return ChannelsFragment.newInstance();
                case 1:
                    return NewsFragment.newInstance();
                case 2:
                    return RadioStationsFragment.newInstance();
                default:
                    return ChannelsFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return mFragmentTitleList.get(position);
            return null;
        }
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.logo_2));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        TabLayout.Tab tab;
        if(id == R.id.nav_tv){
            tab = tabLayout.getTabAt(0);
            tab.select();
        }else if(id == R.id.nav_news){
            tab = tabLayout.getTabAt(1);
            tab.select();
        }else if(id == R.id.nav_radio){
            tab = tabLayout.getTabAt(2);
            tab.select();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
