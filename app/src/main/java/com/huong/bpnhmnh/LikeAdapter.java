package com.huong.bpnhmnh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.VH> {
    private Context context;
    private List<Dish> list = new ArrayList<>();
    private ItemClick itemClick;
    private ItemClick likeClick;

    public LikeAdapter(Context context) {
        this.context = context;
    }

    public void onClickItem(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public void onLikeClick(ItemClick itemClick) {
        this.likeClick = itemClick;
    }

    @NonNull
    @Override
    public LikeAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_like, parent, false);

        return new LikeAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeAdapter.VH holder, int position) {
        Dish dish = list.get(holder.getAdapterPosition());
        holder.name.setText(dish.getName());
        holder.author.setText(dish.getAuthor());
        Glide.with(context)
                .load(dish.getImage())
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onClick(holder.getAdapterPosition(), dish);
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeClick.onClick(holder.getAdapterPosition(), dish);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Dish> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(List<Dish> list) {
        int size = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(size, this.list.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateView(int p, Dish dish) {
        this.list.remove(dish);
        notifyItemRemoved(p);
    }

    static class VH extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView author;
        private ImageView image;
        private ImageButton like;

        public VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            author = itemView.findViewById(R.id.author);
            like = itemView.findViewById(R.id.like);
        }
    }

}
