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

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DataClass> dataList;
    private OnHeartClickListener heartClickListener;


    public void setOnHeartClickListener(OnHeartClickListener listener) {
        this.heartClickListener = listener;
    }

    public void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size());
    }

    public void setSearchList(List<DataClass> dataSearchList){
        this.dataList=dataSearchList;
        notifyDataSetChanged();

    }
    public MyAdapter(Context context, List<DataClass> dataList ){
        this.context=context;
        this.dataList=dataList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DataClass currentItem = dataList.get(position);
        // Bind data to views
        holder.ListHeart.setImageResource(currentItem.getIsFavorite() ? R.drawable.heart_filled : R.drawable.heart_blank);

        // Handle heart click
        holder.ListHeart.setOnClickListener(v -> {
            if (heartClickListener != null) {
                heartClickListener.onHeartClick(position);
            }
        });
        holder.ListImage.setImageResource(dataList.get(position).getDataImage());

        holder.ListName.setText(dataList.get(position).getDataTitle());
        holder.ListDesc.setText(dataList.get(position).getDataDesc());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,product.class);
                i.putExtra("Image",dataList.get(holder.getAdapterPosition()).getDataImage());
                i.putExtra("Heart",dataList.get(holder.getAdapterPosition()).getIsFavorite());
                i.putExtra("Name",dataList.get(holder.getAdapterPosition()).getDataTitle());
                i.putExtra("Desc",dataList.get(holder.getAdapterPosition()).getDataDesc());

                context.startActivity(i);


            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView ListImage;
    ImageView ListHeart;
    TextView ListDesc;
    TextView ListName;
    CardView cardView;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        ListImage =itemView.findViewById(R.id.listImage);
        ListHeart =itemView.findViewById(R.id.listHeart);
        ListDesc =itemView.findViewById(R.id.listDesc);
        ListName =itemView.findViewById(R.id.listName);
        cardView =itemView.findViewById(R.id.cardView);


    }

}