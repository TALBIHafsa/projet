package com.example.halalscan.App;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.halalscan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    List<product> allDataList;
    private List<product> completeDataList;
    private ValueFilter valueFilter;
    private OnItemClickListener listener;


    public MyAdapter(Context context, ArrayList<product> list, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.allDataList = list;
        this.completeDataList = list;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(product item);
    }


    public void updateList(List<product> newList) {
        allDataList.clear();
        if(newList != null){
            allDataList.addAll(newList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        product p = allDataList.get(position);
        holder.name.setText(p.getName());
        holder.id.setText(p.getId());

        // Set click listener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to navigate to products activity
                Intent intent = new Intent(context, products.class);
                // Pass product details to the intent
                intent.putExtra("productId", p.getId());
                intent.putExtra("productName", p.getName());
                intent.putExtra("productName", p.getName());

                intent.putExtra("productImage", p.getImage());


                // Start the activity
                context.startActivity(intent);
            }
        });

        // Load image using Picasso (add Picasso library to your project)
        Picasso.get().load(p.getImage()).into(holder.image);
    }


    @Override
    public int getItemCount() {
        return allDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private OnItemClickListener listener = null;
        TextView id, name, statut;
        ImageView image;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            this.listener = listener;

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(allDataList.get(position));
                    }
                }
            });
        }

    }

    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<product> filterList = new ArrayList<>();
                for (int i = 0; i < completeDataList.size(); i++) {
                    if ((completeDataList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(completeDataList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = completeDataList.size();
                results.values = completeDataList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            allDataList = (ArrayList<product>) results.values;
            notifyDataSetChanged();
        }
    }
}
