package com.newsbomb.allnews.newsapi;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;
import com.newsbomb.allnews.newsapi.Model.Articles;
import com.newsbomb.allnews.newsapi.Model.Users;

import com.tapadoo.alerter.Alerter;

import java.lang.reflect.Field;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    StringRequest stringRequest;

    SwipeRefreshLayout swipeRefreshLayout;


    Context context;

    private static String searchurl;

    public static String countrys="de";

    private static final String base="http://newsapi.org/v2/top-headlines";

    public static final String cate="general";

    private static String urlss="";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public static  String urls;

    RelativeLayout layout_internet;
   // private static final String url=base+country+api_key;
    FrameLayout frameLayout;
    RecyclerView recycle;
    List<Articles> movieList = new ArrayList<>();
    RequestQueue requestQueue;

    String preferenceCountry;
    String preferenceCategory;
    TextView textheadline;
    Button refresh;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       layout_internet = findViewById(R.id.layout_internet);



        textheadline=(TextView) findViewById(R.id.textheadline);




             frameLayout=(FrameLayout) findViewById(R.id.frame);
             swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
             swipeRefreshLayout.setOnRefreshListener(this);

        FontsOverride.setDefaultFont(MainActivity.this, "MONOSPACE", "fonts/ac.ttf");



        swipeRefreshLayout.setProgressViewOffset (false,10, 150);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE);
                //swipeRefreshLayout.setSize(15);
        setuptoolbar();
        refresh=(Button) findViewById(R.id.refresh);

        textheadline.setVisibility(View.INVISIBLE);

        LinearLayout linear=(LinearLayout) findViewById(R.id.linear);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
       // refresh.setVisibility(View.INVISIBLE);
        if (!isNetworkAvailable()) {
            layout_internet.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            Alerter.create(this).setTitle("No Internet Connection").setText("No Internet Connect. Please Check Your Internet Connection.")
                    .setBackgroundColorRes(R.color.colorPrimaryDark).setDuration(25000).show();
            refresh.setVisibility(View.VISIBLE);
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
                }
            });
        }
        preferenceCountry=pref.getString("storedCountry",null);
       if(preferenceCountry==null){
           preferenceCountry="us";
       }
        preferenceCategory=pref.getString("storedCategory",null);
        if(preferenceCategory==null){
            preferenceCategory="general";
        }

        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";

        onLoadingSwipeRefresh(urlss);

        //this code is for changing the color of title in navigation drawer
        navigationView=(NavigationView) findViewById(R.id.navigation_menu);
        View headerView = navigationView.getHeaderView(0);
       // TextView navUsername = (TextView) headerView.findViewById(R.id.nda);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ac.ttf");
      //  navUsername.setTypeface(typeface);

        Menu menu = navigationView.getMenu();
        MenuItem toolss= menu.findItem(R.id.tools);
        SpannableString ss = new SpannableString(toolss.getTitle());
        ss.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44s), 0, ss.length(), 0);
        toolss.setTitle(ss);
        MenuItem toolsss= menu.findItem(R.id.toolss);
        SpannableString sss = new SpannableString(toolsss.getTitle());
        sss.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, sss.length(), 0);
        toolsss.setTitle(sss);
        // ok
        recycle= (RecyclerView) findViewById(R.id.recyclerView);
        recycle.setLayoutManager(new LinearLayoutManager(this));



        //Navigation Drawer Corner
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final String[] url = {null};
                switch (item.getItemId()){
                    case R.id.home:


                        preferenceCategory="business";
                        editor.putString("storedCategory", preferenceCategory); // Storing string
                        editor.apply();
                        //  Toast.makeText(MainActivity.this, "Clicked ", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        // editor.putString("storedCountry", countrys); // Storing string
                        // editor.apply();
                       onLoadingSwipeRefresh(urlss);

                        break;

                    case  R.id.entertainment:
                        preferenceCategory="entertainment";
                        editor.putString("storedCategory", preferenceCategory); // Storing string
                        editor.apply();
                        drawerLayout.closeDrawer(GravityCompat.START);
                       // startActivity(new Intent(MainActivity.this,Entertainment.class));

                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                       // editor.putString("storedCountry", countrys); // Storing string
                       // editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;

                    case R.id.tech:
                        preferenceCategory="technology";
                        editor.putString("storedCategory", preferenceCategory); // Storing string
                        editor.apply();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        onLoadingSwipeRefresh(urlss);
                        break;

                    case R.id.general:

                        preferenceCategory="general";
                        editor.putString("storedCategory", preferenceCategory); // Storing string
                        editor.apply();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        onLoadingSwipeRefresh(urlss);

                        break;

                    case R.id.sports:
                        preferenceCategory="sports";
                        editor.putString("storedCategory", preferenceCategory); // Storing string
                        editor.apply();

                        drawerLayout.closeDrawer(GravityCompat.START);
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        onLoadingSwipeRefresh(urlss);
                        break;

                    case R.id.dawn:


                        showChannels();


                    case R.id.health:

                        preferenceCategory="health";
                        editor.putString("storedCategory", preferenceCategory); // Storing string
                        editor.apply();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        onLoadingSwipeRefresh(urlss);
                        break;


                    case R.id.share:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody ="https://play.google.com/store/apps/details?id="+getPackageName();
                        String shareSub = "https://play.google.com/store/apps/details?id="+getPackageName();
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));
                        break;



                    case R.id.rate:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {


                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
                        }
                        break;

                    case R.id.privacy:

                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intenss=new Intent(MainActivity.this,Newwebviewforprivacy.class);
                        intenss.putExtra("urls","https://sites.google.com/view/fadrick/home");
                      //  Toast.makeText(context, link.toString(), Toast.LENGTH_SHORT).show();
                        startActivity(intenss);
                        break;

                    case R.id.country:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        showCountries();


                }
                return false;
            }
        });

//End of Navigation Drawer Corner
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showChannels(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Channels");
        String[] animals = {"Dawn News" , "Geo News", "Abc News", "Express Tribune"
        };
        int checkedItem = 0;
        builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0:

                        startActivity(new Intent(MainActivity.this, DawnNews.class));
                        break;

                    case 1:

                        startActivity(new Intent(MainActivity.this, Geo.class));
                        break;

                    case 2:

                        startActivity(new Intent(MainActivity.this, Abc.class));
                        break;

                    case 3:

                        startActivity(new Intent(MainActivity.this, express.class));
                        break;
                }

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                drawerLayout.closeDrawer(Gravity.START,false);
            }
        });
        builder.setNegativeButton("Cancel", null);

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showCountries() {


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Country");





        String[] animals = {"United Arab Emirates", "Argentina", "Austria", "Australia",
                "Belgium","Bulgaria","Brazil","Canada","Switzerland","China","Colombia","Cuba","Czech Republic","Egypt","Germany","France"
        ,"United Kingdom","Greece","HongKong","Hungary","Indonesia","Ireland","India","Italy","Japan","Korea","Lithuania","Latvia","Morocco","Mexico",
                "Malaysia","Nigeria","NetherLands","Norway","NewZealand","Philippines","Poland","Portugal","Romania","Serbia","Russia","Saudi Arabia","Sweden",
                "Singapore","Slivenia","Slovakia","Thailand","Turkey","Taiwan","Ukraine","United States","Venezuela","South Africa"
        };
        int checkedItem = 0;
        builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){

                switch (which){

                    case 0:
                        preferenceCountry="ae";

                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 1:
                        preferenceCountry="ar";

                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;


                    case 2:
                        preferenceCountry="at";
                        drawerLayout.closeDrawer(Gravity.START,false);
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;

                    case 3:
                        preferenceCountry="au";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 4:
                        preferenceCountry="be";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 5:
                        preferenceCountry="bg";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;

                    case 6:
                        preferenceCountry="br";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;

                    case 7:
                        preferenceCountry="ca";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 8:
                        preferenceCountry="ch";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 9:
                        preferenceCountry="cn";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 10:
                        preferenceCountry="co";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 11:
                        preferenceCountry="cu";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 12:
                        preferenceCountry="cz";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;

                    case 13:
                        preferenceCountry="eg";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 14:
                        preferenceCountry="de";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;

                    case 15:
                        preferenceCountry="fr";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;

                    case 16:
                        preferenceCountry="gb";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 17:
                        preferenceCountry="gr";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;

                    case 18:
                        preferenceCountry="hk";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 19:
                        preferenceCountry="hu";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 20:
                        preferenceCountry="id";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 21:
                        preferenceCountry="il";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;

                    case 22:
                        preferenceCountry="in";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 23:
                        preferenceCountry="it";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;

                    case 24:
                        preferenceCountry="jp";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 25:
                        preferenceCountry="kr";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 26:
                        preferenceCountry="lt";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);

                        break;
                    case 27:
                        preferenceCountry="lv";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 28:
                        preferenceCountry="ma";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 29:
                        preferenceCountry="mx";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 30:
                        preferenceCountry="my";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 31:
                        preferenceCountry="ng";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 32:
                        preferenceCountry="nl";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 33:
                        preferenceCountry="no";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 34:
                        preferenceCountry="nz";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;

                    case 35:
                        preferenceCountry="ph";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;

                    case 36:
                        preferenceCountry="pl";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 37:
                        preferenceCountry="pt";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 38:
                        preferenceCountry="ro";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 39:
                        preferenceCountry="rs";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 40:
                        preferenceCountry="ru";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 41:
                        preferenceCountry="sa";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;

                    case 42:
                        preferenceCountry="se";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 43:
                        preferenceCountry="sg";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 44:
                        preferenceCountry="si";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 45:
                        preferenceCountry="sk";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 46:
                        preferenceCountry="th";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 47:
                        preferenceCountry="tr";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 48:
                        preferenceCountry="tw";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 49:
                        preferenceCountry="ua";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 50:
                        preferenceCountry="us";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 51:
                        preferenceCountry="ve";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                    case 52:
                        preferenceCountry="za";
                        urlss=base+"?country="+preferenceCountry+"&category="+preferenceCategory+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                        editor.putString("storedCountry", preferenceCountry); // Storing string
                        editor.apply();
                        onLoadingSwipeRefresh(urlss);
                        break;
                }

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                drawerLayout.closeDrawer(Gravity.START,false);
            }
        });
        builder.setNegativeButton("Cancel", null);

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {














        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        SearchManager searchManagerget=(SearchManager)getSystemService(SEARCH_SERVICE);
        final SearchView searchView=(SearchView) menu.findItem(R.id.action_search).getActionView();
        final MenuItem searchMenuItem=menu.findItem(R.id.action_search);

        //For changing color of search icon from black to white
        ImageView icon = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        icon.setColorFilter(Color.WHITE);


        searchView.setSearchableInfo(searchManagerget.getSearchableInfo(getComponentName()));
      //  searchView.setQueryHint("Search News...");
        //searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" +"Search News" + "</font>"));
        EditText searchEditText = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHint("Search News ...");

        searchEditText.setHintTextColor(getResources().getColor(R.color.white));



        SearchView.SearchAutoComplete searchTextView =  searchView.findViewById(R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, 0);
            //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()>2){

                    searchurl="https://newsapi.org/v2/everything?q="+query.toString()+"&apiKey=ddb94057509a4a2998c2126e43d54a95";
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    searchView.setQuery("",false);

                    searchView.setIconified(true);

                    searchView.clearFocus();

                    Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                    intent.putExtra("searchurl",searchurl);

                    startActivity(intent);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //onLoadingSwipeRefresh(searchurl);
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false,false);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void request(String url){

        //setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
       // setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
     //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        swipeRefreshLayout.setRefreshing(true);
        stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                textheadline.setVisibility(View.VISIBLE);
                Log.v("Response", response.toString());
                Gson gson = new Gson();
                Users listModel = gson.fromJson(response.toString(), Users.class);
                movieList=listModel.getArticles();

                Adapter adapter = new Adapter(movieList, MainActivity.this);
                recycle.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               // Toast.makeText(MainActivity.this, "no response", Toast.LENGTH_SHORT).show();
                Log.d("Response", error.toString());

            }
        });

        RequestQueue queue=Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }



    private void setuptoolbar(){
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


    @Override
    public void onRefresh() {

        request(urlss);

    }
    private void onLoadingSwipeRefresh(final String hello){

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                request(hello);
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



}



