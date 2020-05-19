package com.example.narva;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import java.util.Random;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ToursViewHolder>  {

    private Context mCtx;
    private List<SearchReader> tourList;

    public SearchAdapter(Context mCtx,  List<SearchReader> tourList){
        this.mCtx = mCtx;
        this.tourList = tourList;
    }

    @NonNull
    @Override
    public ToursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycleview_tours1, parent, false);
        return new ToursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ToursViewHolder holder, int position) {
        SearchReader tour = tourList.get(position);
        holder.textViewName.setText(tour.Name);
        Picasso.get().load(tour.link).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.foregroundLinearLayout.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.d("TAG", "FAILED");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d("TAG", "Prepare Load");
            }
        });
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.textViewName.setBackgroundColor(color);
    }
    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public class ToursViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName;
        ImageView foregroundLinearLayout;

        public ToursViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.info_text);
            foregroundLinearLayout = itemView.findViewById(R.id.photo);

        }
    }
}
