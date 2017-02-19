package com.github.moviesappstagei.moviesappstagei;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grossman on 2/19/2017.
 */


class MainAdapter extends RecyclerView.Adapter<MovieViewHolder> {
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
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieObject movie = movieList.get(position);
        Picasso.with(context)
                .load(movie.getMoviePoster())
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
    ImageView imgView;

    public MovieViewHolder(View itemView) {
        super(itemView);
        imgView = (ImageView) itemView.findViewById(R.id.img_item);
    }

}
