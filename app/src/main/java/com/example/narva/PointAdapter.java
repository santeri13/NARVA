package com.example.narva;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class PointAdapter extends RecyclerView.Adapter<PointAdapter.ToursViewHolder>{

    private Context contex;
    private List<PointReader> tourList;
    Dialog dialog;

    public PointAdapter(Context contex, List<PointReader> tourList){
        this.tourList = tourList;
        this.contex=contex;
    }

    @NonNull
    @Override
    public ToursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(contex).inflate(R.layout.map_recycleview, parent, false);
        ToursViewHolder holder = new ToursViewHolder(v);

        dialog = new Dialog(contex);
        dialog.setContentView(R.layout.hint_text_layout);

        holder.hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = dialog.findViewById(R.id.text_header);
                TextView text = dialog.findViewById(R.id.hint_text);
                name.setText(tourList.get(holder.getAdapterPosition()).getName());
                //text.setText(tourList.get(holder.getAdapterPosition()).getText());
                dialog.show();
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull PointAdapter.ToursViewHolder holder, int position) {
        PointReader pointAdapter = tourList.get(position);
        holder.textViewName.setText(pointAdapter.name);
    }
    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public class ToursViewHolder extends RecyclerView.ViewHolder {
        ImageView foregroundLinearLayout;
        TextView textViewName;
        ImageButton hint;
        public ToursViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.info_text);
            foregroundLinearLayout = itemView.findViewById(R.id.photo);
            hint = itemView.findViewById(R.id.show_hint);
        }
    }
}
