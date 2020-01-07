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

import java.util.ArrayList;

public class AbcAdapter extends RecyclerView.Adapter<AbcAdapter.ViewHolder> {

    Context context;
    ArrayList<String> title;
    ArrayList<String> desc;
    ArrayList<Integer> images;

    public AbcAdapter(Context context, ArrayList<String> title, ArrayList<String> desc,ArrayList<Integer> images) {
        this.context = context;
        this.title = title;
        this.desc = desc;
        this.images=images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.title.setText(title.get(i));
        viewHolder.desc.setText(desc.get(i));
        viewHolder.poster.setImageResource(images.get(i));


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(i==0){
                    Intent i=new Intent(context,Second.class);
                    i.putExtra("url","https://cricketpakistan.com.pk/en/news/detail/cricket-australia-xi-pakistan-practice-match-drawn-after-low-key-secon");
                    i.putExtra("titles","Pakistan practice match drawn");
                    context.startActivity(i);
                }
                if(i==1){


                    Intent i=new Intent(context,Second.class);
                    i.putExtra("url","https://cricketpakistan.com.pk/en/news/detail/cricket-australia-xi-pakistan-practice-match-drawn-after-low-key-secon");
                    i.putExtra("titles","Pakistan practice match drawn");
                    context.startActivity(i);
                }
                if(i==2){


                    Intent i=new Intent(context,Second.class);
                    i.putExtra("url","https://cricketpakistan.com.pk/en/news/detail/cricket-australia-xi-pakistan-practice-match-drawn-after-low-key-secon");
                    i.putExtra("titles","Pakistan practice match drawn");
                    context.startActivity(i);
                }
                if(i==3){


                    Intent i=new Intent(context,Second.class);
                    i.putExtra("url","https://cricketpakistan.com.pk/en/news/detail/cricket-australia-xi-pakistan-practice-match-drawn-after-low-key-secon");
                    i.putExtra("titles","Pakistan practice match drawn");
                    context.startActivity(i);
                }
                if(i==4){


                    Intent i=new Intent(context,Second.class);
                    i.putExtra("url","https://cricketpakistan.com.pk/en/news/detail/cricket-australia-xi-pakistan-practice-match-drawn-after-low-key-secon");
                    i.putExtra("titles","Pakistan practice match drawn");
                    context.startActivity(i);
                }
                if(i==5){


                    Intent i=new Intent(context,Second.class);
                    i.putExtra("url","https://cricketpakistan.com.pk/en/news/detail/cricket-australia-xi-pakistan-practice-match-drawn-after-low-key-secon");
                    i.putExtra("titles","Pakistan practice match drawn");
                    context.startActivity(i);
                }
                if(i==6){


                    Intent i=new Intent(context,Second.class);
                    i.putExtra("url","https://cricketpakistan.com.pk/en/news/detail/cricket-australia-xi-pakistan-practice-match-drawn-after-low-key-secon");
                    i.putExtra("titles","Pakistan practice match drawn");
                    context.startActivity(i);
                }
                if(i==7){


                    Intent i=new Intent(context,Second.class);
                    i.putExtra("url","https://cricketpakistan.com.pk/en/news/detail/cricket-australia-xi-pakistan-practice-match-drawn-after-low-key-secon");
                    i.putExtra("titles","Pakistan practice match drawn");
                    context.startActivity(i);
                }
                if(i==8){


                    Intent i=new Intent(context,Second.class);
                    i.putExtra("url","https://cricketpakistan.com.pk/en/news/detail/cricket-australia-xi-pakistan-practice-match-drawn-after-low-key-secon");
                    i.putExtra("titles","Pakistan practice match drawn");
                    context.startActivity(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, desc;
        ImageView poster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            poster=(ImageView) itemView.findViewById(R.id.posters);
            title=(TextView) itemView.findViewById(R.id.text);
            desc=(TextView) itemView.findViewById(R.id.desc);



        }
    }

}
