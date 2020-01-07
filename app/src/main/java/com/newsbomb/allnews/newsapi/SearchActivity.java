package com.newsbomb.allnews.newsapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newsbomb.allnews.newsapi.Model.Articles;
import com.newsbomb.allnews.newsapi.Model.Users;
import com.google.gson.Gson;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    NavigationView navigationView;
    DrawerLayout drawerLayout;

    SwipeRefreshLayout swipeRefreshLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;


    private static String searchurl;

    public static String countrys="de";

    private static final String base="https://newsapi.org/v2/top-headlines";

    public static final String cate="general";



    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public static  String url;

    RelativeLayout layout_internet;
    // private static final String url=base+country+api_key;
    FrameLayout frameLayout;
    RecyclerView recycle;
    List<Articles> movieList = new ArrayList<>();
    RequestQueue requestQueue;
    LottieAnimationView mProgressBar;
    String preferenceCountry;

    Toolbar toolbar;
    Button refresh;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity);
        layout_internet = findViewById(R.id.layout_internet);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        url = intent.getStringExtra("searchurl");
        onLoadingSwipeRefresh(url);

        swipeRefreshLayout.setProgressViewOffset(false, 150, 210);
        //swipeRefreshLayout.setSize(15);
        refresh = (Button) findViewById(R.id.refresh);

        LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        refresh.setVisibility(View.INVISIBLE);
        if (!isNetworkAvailable()) {
            layout_internet.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            Alerter.create(this).setTitle("No Internet Connection").setText("No Internet Connect. Please Check Your Internet Connection.")
                    .setBackgroundColorRes(R.color.colorPrimaryDark).setDuration(6000).show();
            refresh.setVisibility(View.VISIBLE);
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(getIntent());
                }
            });
        }
        preferenceCountry = pref.getString("storedCountry", null);
        if (preferenceCountry == null) {
            preferenceCountry = "de";
        }



        //this code is for changing the color of title in navigation drawer
      //  navigationView = (NavigationView) findViewById(R.id.navigation_menu);
      // // Menu menu = navigationView.getMenu();
       // MenuItem toolss = menu.findItem(R.id.tools);
       /// SpannableString ss = new SpannableString(toolss.getTitle());
       // ss.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44s), 0, ss.length(), 0);
      //  toolss.setTitle(ss);
       // MenuItem toolsss = menu.findItem(R.id.toolss);
      //  SpannableString sss = new SpannableString(toolsss.getTitle());
       // sss.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, sss.length(), 0);
       // toolsss.setTitle(sss);
        // ok


        recycle = (RecyclerView) findViewById(R.id.recyclerView);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        mProgressBar = (LottieAnimationView) findViewById(R.id.progressBar);


    }

        //Navigation Drawer Corner

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    private void request(String url){
        swipeRefreshLayout.setRefreshing(true);


        final StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressBar.setVisibility(View.INVISIBLE);

                Log.v("Response", response.toString());
                Gson gson = new Gson();
                Users listModel = gson.fromJson(response.toString(), Users.class);
                movieList=listModel.getArticles();
                Adapter adapter = new Adapter(movieList, SearchActivity.this);
                recycle.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue=Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }




    @Override
    public void onRefresh() {

        request(url);

    }
    private void onLoadingSwipeRefresh(final String hello){

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                request(hello);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}



