package com.github.moviesappstagei.moviesappstagei.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.moviesappstagei.moviesappstagei.DetailsActivity;
import com.github.moviesappstagei.moviesappstagei.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.github.moviesappstagei.moviesappstagei.MovieGallery.MOVIE_EXTRA;

/**
 * Created by RGHERTA on 2/19/2017.
 */


public class MainAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    List<MovieObject> movieList;
    private LayoutInflater inflater;
    private Context context;

    public MainAdapter(Context mcontext) {
        context = mcontext;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<MovieObject> movieList) {
        this.movieList = new ArrayList<>();
        this.movieList.addAll(movieList);
        notifyDataSetChanged();  //Need to notify adapter otherwise the app will crash
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean shouldAttachToParentImmediately = false;  //Performance optimization?
        View view = inflater.inflate(R.layout.item, parent, shouldAttachToParentImmediately);
        final MovieViewHolder viewHolder = new MovieViewHolder(view);

        view.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View mView) {
                int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(MOVIE_EXTRA, movieList.get(position));
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieObject movie = movieList.get(position);
        Picasso.with(context)
                .load(movie.getMoviePoster())
                //.placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        if(movieList == null) {
            return 0;
        }
        else {
            return movieList.size();
        }
    }
}


class MovieViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgView;

    public MovieViewHolder(View itemView) {
        super(itemView);
        imgView = (ImageView) itemView.findViewById(R.id.img_item);
    }

}
