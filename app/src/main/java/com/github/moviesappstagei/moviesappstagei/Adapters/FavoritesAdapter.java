package com.github.moviesappstagei.moviesappstagei.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.moviesappstagei.moviesappstagei.R;

/**
 * Created by Grossman on 5/21/2017.
 */

public class FavoritesAdapter extends  RecyclerView.Adapter<FavoritesAdapter.FavAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public FavoritesAdapter(@NonNull Context context) {
        mContext = context;
    }


    @Override
    public FavAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.fav_item, parent, false);

        view.setFocusable(true);
        return new FavAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(FavAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String MovieName = mCursor.getString(1);
        holder.favItem.setText(MovieName);

    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class FavAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView favItem;

        FavAdapterViewHolder(View view) {
            super(view);

            favItem = (TextView) view.findViewById(R.id.fav_item_data);

        }

        @Override
        public void onClick(View view) {

        }
    }
}
