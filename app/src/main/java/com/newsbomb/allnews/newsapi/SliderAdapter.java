package com.newsbomb.allnews.newsapi;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * Created by ravi on 2/7/18.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;
    }

    public int[] slideImages = {
      R.drawable.latest,
      R.drawable.cat,
            R.drawable.countr
    };

    public String[] slideHeadings ={
      "Get Latest News",
            "Different Categories",
            "Different Countries"
    };

    public String[] slideDescriptions ={
            "Get All the latest news from all over the world in real time.",
            "Discover your personalized news that interest you based on different categories which include Business, Entertainment, Science , Information Technology, Sports, Health and General news.",
            "Get Headline news of over 50 plus countries from trusted publishers."
    };


    @Override
    public int getCount() {
        return slideHeadings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (ConstraintLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.iv_image_icon);
        TextView slideHeading = (TextView) view.findViewById(R.id.tv_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.tv_description);

        slideImageView.setImageResource(slideImages[position]);
        slideHeading.setText(slideHeadings[position]);
        slideDescription.setText(slideDescriptions[position]);

        container.addView(view);

        return view;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);  //todo: RelativeLayout??
    }
}
