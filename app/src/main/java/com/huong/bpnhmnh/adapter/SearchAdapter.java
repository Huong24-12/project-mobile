package com.huong.bpnhmnh.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.huong.bpnhmnh.R;
import com.huong.bpnhmnh.model.Dish;
import com.huong.bpnhmnh.utils.ItemClick;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    Context mContext;
    List<Dish> mData;
    private ItemClick itemClick;
    public void onClickItem(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public SearchAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_dish, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(mData.get(holder.getLayoutPosition()).getName());
        holder.author.setText(mData.get(holder.getLayoutPosition()).getAuthor());
        Glide.with(mContext)
                .load(mData.get(holder.getLayoutPosition()).getImage())
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onClick(holder.getAdapterPosition(),mData.get(holder.getLayoutPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Dish> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public void addData(List<Dish> list) {
        int size = mData.size();
        this.mData.addAll(list);
        notifyItemRangeInserted(size, mData.size());
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView author;
        private ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            author = (TextView) itemView.findViewById(R.id.author);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }


}
