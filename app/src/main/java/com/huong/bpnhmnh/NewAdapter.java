package com.huong.bpnhmnh;

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

import java.util.ArrayList;
import java.util.List;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.VH> {
    private Context context;
    private List<Dish> list = new ArrayList<>();
    private ItemClick itemClick;

    public void onClickItem(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public NewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NewAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false);

        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewAdapter.VH holder, int position) {
        Dish dish = list.get(holder.getAdapterPosition());
        holder.name.setText(dish.getName());
        holder.author.setText(dish.getAuthor());
        Glide.with(context)
                .load(dish.getImage())
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onClick(holder.getAdapterPosition(),dish);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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

    static class VH extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView author;
        private ImageView image;


        public VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            author = itemView.findViewById(R.id.author);
        }
    }
}
