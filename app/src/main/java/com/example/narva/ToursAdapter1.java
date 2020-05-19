package com.example.narva;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import java.util.Random;

public class ToursAdapter1 extends RecyclerView.Adapter<ToursAdapter1.ToursViewHolder> {

    private Context mCtx;
    private List<TourReader> tourList;


    public ToursAdapter1(Context mCtx, List<TourReader> tourList){
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
    public void onBindViewHolder(@NonNull ToursViewHolder holder, int position) {
        TourReader tour = tourList.get(position);
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
        holder.timetext.setText(Long.toString(tour.time));
        holder.liketext.setText(Long.toString(tour.like));

        holder.foregroundLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, Gps.class);
                mCtx.startActivity(intent);
            }
        });
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, Gps.class);
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    class ToursViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, timetext, liketext;
        ImageView foregroundLinearLayout;
        public ToursViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.info_text);
            foregroundLinearLayout = itemView.findViewById(R.id.photo);
            timetext = itemView.findViewById(Math.toIntExact(R.id.time));
            liketext = itemView.findViewById(Math.toIntExact(R.id.like));

        }
    }
}