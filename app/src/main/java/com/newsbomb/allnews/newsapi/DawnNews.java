package com.newsbomb.allnews.newsapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class DawnNews extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> title=new ArrayList<>();
    ArrayList<Integer> image=new ArrayList<>();
    ArrayList<String> desc=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dawn_news);

        recyclerView=(RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DawnAdapter adapter=new DawnAdapter(this,title,desc,image);
        recyclerView.setAdapter(adapter);


        init();

    }

    public void init(){

        title.add("Hasnain six-fer guides Pakistan to win over Sri Lanka");
        desc.add("Chasing 184 to win the match, the islanders were blown away by a fiery spell of fast bowling");
        image.add(R.drawable.pak);


        title.add("Trump to attend NATO summit in London, days before UK vote");
        desc.add("United States has ejected Turkey from the F-35 advanced stealth warplane program in retaliation");
        image.add(R.drawable.trump);

        title.add("In a world full of ads, here&#8217;s an app free of clickbait");
        desc.add("On the new social media platform, the newest link will be posted first rather than one with the most likes");
        image.add(R.drawable.pak);


        title.add("Hasnain six-fer guides Pakistan to win over Sri Lanka");
        desc.add("Chasing 184 to win the match, the islanders were blown away by a fiery spell of fast bowling");
        image.add(R.drawable.pak);


        title.add("Trump to attend NATO summit in London, days before UK vote");
        desc.add("United States has ejected Turkey from the F-35 advanced stealth warplane program in retaliation");
        image.add(R.drawable.trump);

        title.add("In a world full of ads, here&#8217;s an app free of clickbait");
        desc.add("On the new social media platform, the newest link will be posted first rather than one with the most likes");
        image.add(R.drawable.pak);

        title.add("Hasnain six-fer guides Pakistan to win over Sri Lanka");
        desc.add("Chasing 184 to win the match, the islanders were blown away by a fiery spell of fast bowling");
        image.add(R.drawable.pak);


        title.add("Trump to attend NATO summit in London, days before UK vote");
        desc.add("United States has ejected Turkey from the F-35 advanced stealth warplane program in retaliation");
        image.add(R.drawable.trump);

        title.add("In a world full of ads, here&#8217;s an app free of clickbait");
        desc.add("On the new social media platform, the newest link will be posted first rather than one with the most likes");
        image.add(R.drawable.pak);

    }


}
