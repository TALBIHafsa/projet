package com.example.halalscan.App;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.halalscan.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DataClass> dataList;
    private OnHeartClickListener heartClickListener;

    public void setOnHeartClickListener(OnHeartClickListener listener) {
        this.heartClickListener = listener;
    }

    public void setSearchList(List<DataClass> dataSearchList) {
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass currentItem = dataList.get(position);

        holder.ListHeart.setImageResource(currentItem.getIsFavorite() ? R.drawable.heart_filled : R.drawable.heart_blank);

        holder.ListHeart.setOnClickListener(v -> {
            if (heartClickListener != null) {
                heartClickListener.onHeartClick(position);
            }
        });

        if (currentItem.getDataImage2() == null) {
            holder.ListImage.setImageResource(currentItem.getDataImage());
        } else {
            Picasso.get().load(currentItem.getDataImage2()).into(holder.ListImage);
        }

        holder.ListName.setText(currentItem.getDataTitle());

        holder.ListImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, products.class);
            intent.putExtra("productId", currentItem.getDataId());
            context.startActivity(intent);
        });
        holder.ListName.setOnClickListener(v -> {
            Intent intent = new Intent(context, products.class);
            intent.putExtra("productId", currentItem.getDataId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView ListImage;
    ImageView ListHeart;
    TextView ListName;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        ListImage = itemView.findViewById(R.id.listImage);
        ListHeart = itemView.findViewById(R.id.listHeart);
        ListName = itemView.findViewById(R.id.listName);
    }
}

