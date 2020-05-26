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
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.List;

public class ToursAdapter2 extends RecyclerView.Adapter<ToursAdapter2.ToursViewHolder> {

    private Context mCtx;
    private List<TourReader> tourList;

    public ToursAdapter2(Context mCtx, List<TourReader> tourList){
        this.mCtx = mCtx;
        this.tourList = tourList;
    }
    @NonNull
    @Override
    public ToursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycleview_tours2, parent, false);
        return new ToursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToursViewHolder holder, int position) {
        TourReader tour = tourList.get(position);
        holder.textViewName.setText(tour.Name);
        Glide.with(mCtx).load(tour.link).centerCrop().into(holder.foregroundLinearLayout);
        //Picasso.get().load(tour.link).into(new Target() {
            //@Override
            //public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                //holder.foregroundLinearLayout.setBackground(new BitmapDrawable(bitmap));
                //holder.foregroundLinearLayout.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            //}

            //@Override
            //public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                //Log.d("TAG", "FAILED");
            //}
            //@Override
            //public void onPrepareLoad(Drawable placeHolderDrawable) {
                //Log.d("TAG", "Prepare Load");
            //}
        //});
        holder.textViewName.setBackgroundColor(Color.parseColor(tour.color));
        holder.timetext.setText(Long.toString(tour.time));
        holder.liketext.setText(Long.toString(tour.like));
        holder.foregroundLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, Gps.class);
                intent.putExtra("title", holder.textViewName.getText().toString());
                mCtx.startActivity(intent);
            }
        });
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, Gps.class);
                intent.putExtra("title", holder.textViewName.getText().toString());
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
        CardView cardView;
        public ToursViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            textViewName = itemView.findViewById(R.id.info_text);
            foregroundLinearLayout = itemView.findViewById(R.id.photo);
            timetext = itemView.findViewById(R.id.time);
            liketext = itemView.findViewById(R.id.like);

        }
    }
}