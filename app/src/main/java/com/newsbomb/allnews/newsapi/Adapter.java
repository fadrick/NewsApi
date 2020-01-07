package com.newsbomb.allnews.newsapi;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.newsbomb.allnews.newsapi.Model.Articles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by FAHAD ALI on 11/12/2018.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.SampleViewHolder>{

    private List<Articles> moviesList;
    Context context;
    Date myDate;
    public Adapter(List<Articles> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @NonNull
    @Override
    public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.item_user_layout,parent,false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SampleViewHolder holder, int position) {

        final Articles movie=moviesList.get(position);
        
        if(movie.getDescription()==null){

            holder.gener.setText(movie.getTitle());

        }else
        {
            holder.gener.setText(movie.getDescription());

        }


        String createdate =
                null;
        try {
            createdate = new SimpleDateFormat("dd-MM-yyyy").format(
                    new SimpleDateFormat("yyyy-MM-dd").parse (movie.getPublishedAt().toString().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
      holder.hours.setText("\u2022"+Utils.DateToTimeFormat(movie.getPublishedAt()));
        holder.date.setText(Utils.DateFormat(movie.getPublishedAt()));


        holder.textView.setText(movie.getTitle().toString());


       holder.rating.setText(movie.getSource().getName());


        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(6));
        requestOptions.placeholder(R.drawable.noimage);




if(movie.getUrlToImage()!=null){
    Glide.with(context)
            .load(movie.getUrlToImage())

            .apply(requestOptions)

            .into(holder.poster);

}else{
    holder.poster.setImageResource(R.drawable.noimage);
}

        /*if (movie.getUrlToImage() != null) {
            Glide.with(context)
                    .load(movie.getUrlToImage())
                    .centerCrop()
                    .placeholder(R.drawable.noimage)
                    .into(holder.poster);

            Log.d("titleis", movie.getUrlToImage() + "");
        } else {
            Glide.clear(holder.poster);
        }

*/

       holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, movie.getUrl().toString());
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,movie.getUrl().toString());
                context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i=new Intent(context,Second.class);
              i.putExtra("url",movie.getUrl().toString());
               i.putExtra("titles",movie.getTitle().toString());
               context.startActivity(i);


            }
        });



    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }
    public class SampleViewHolder extends RecyclerView.ViewHolder {
        TextView textView,gener,rating,share,date,hours;
        ImageView poster;
        public SampleViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text);
            gener=itemView.findViewById(R.id.gener);
            rating=itemView.findViewById(R.id.rating);
            share=(TextView) itemView.findViewById(R.id.share);
           date=(TextView) itemView.findViewById(R.id.date);
            hours=(TextView) itemView.findViewById(R.id.hours);

            poster = (ImageView) itemView.findViewById(R.id.posters);

        }
    }
}