package com.huong.bpnhmnh.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.huong.bpnhmnh.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AdapterConfession extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Confession> list = new ArrayList<>();
    private final Activity activity;

    public AdapterConfession(Activity context) {
        this.activity = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH2(LayoutInflater.from(activity).inflate(R.layout.item_news, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int p) {
        Confession confession = list.get(holder.getAdapterPosition());
        VH2 vh2 = (VH2) holder;
        vh2.bind(confession, holder.getAdapterPosition(), activity);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Confession> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(List<Confession> list) {
        int size = this.list.size();
        this.list.addAll(list);
        notifyItemRangeChanged(size, list.size());
    }

    public void updateInsert(Confession confession) {
        this.list.add(0, confession);
        notifyItemInserted(0);
    }

    static class VH2 extends RecyclerView.ViewHolder {
        private final ImageView mAvatar;
        private final ImageView image;
        private final TextView txtName;
        private final TextView txtTime;
        private final TextView txtContent;
        private final MaterialCardView cardViewImage;

        public VH2(@NonNull View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.mAvatar);
            image = itemView.findViewById(R.id.image);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtContent = itemView.findViewById(R.id.txtContent);

            cardViewImage = itemView.findViewById(R.id.cardViewImage);

        }


        private void loadAvatar(String url, ImageView image, Context context) {
            Glide.with(context)
                    .load(url)
                    .circleCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions.circleCropTransform())
                    .into(image);
        }

        public void bind(Confession confession, int position, Context context) {
            if (confession == null) {
                return;
            }
            if (confession.getImageConfession() != null && !confession.getImageConfession().isEmpty()) {
                cardViewImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(confession.getImageConfession())
                        .placeholder(R.mipmap.image_faild)
                        .dontAnimate()
                        .error(R.mipmap.image_faild)
                        .into(image);
            } else {
                cardViewImage.setVisibility(View.GONE);
            }

            txtName.setText(confession.getCreatorName());
            txtContent.setText(confession.getContentConfession());
            DateFormat f = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault());
            Date date = new Date();
            date.setTime(confession.getApprovedTime());
            txtTime.setText(f.format(date));

        }
    }


}
