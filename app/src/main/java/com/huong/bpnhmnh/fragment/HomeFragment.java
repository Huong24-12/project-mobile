package com.huong.bpnhmnh.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.huong.bpnhmnh.R;

import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends Fragment {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    NavigationView navigationView;
    ListView listView,listViewNew,listViewThongTin;
    DrawerLayout drawerLayout;
    private View mView;

    public HomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home2, container, false);
        return mView;


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Anhxa();
        ActionViewFlipper();
    }

    // phương thức cho chạy quảng cáo với ViewFilipper
    private void ActionViewFlipper() {
        //mảng chứa tấm ảnh cho quảng cáo
        ArrayList<Integer> mangquangcao = new ArrayList<>();
        //add ảnh vào mảng

        mangquangcao.add(R.drawable.quang_cao_1);
        mangquangcao.add(R.drawable.quang_cao_2);
        mangquangcao.add(R.drawable.quang_cao_3);
        mangquangcao.add(R.drawable.quang_cao_4);
        mangquangcao.add(R.drawable.quang_cao_5);

        //thực hiện vòng lặp for gán ảnh vào ImageView, rồi từ imgview lên app
        for(int i = 0; i < mangquangcao.size(); i++){
            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            //dùng hàm thư viện Glide
            Glide.with(requireActivity())
                    .load(mangquangcao.get(i))
                    .into(imageView);
            //phương thức chỉnh tấm hình vừa khung quảng cáo
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            //thêm ảnh từ imageview vào ViewFlipper
            viewFlipper.addView(imageView);
        }
        //thiết lập tự động chạy cho viewFlipper
        viewFlipper.setFlipInterval(4000);
        //run auto viewFlipper
        viewFlipper.setAutoStart(true);

        //gọi anination cho vào và ra

        Animation animation_slide_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out_right);

        //gọi Animation vào viewFlipper
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setInAnimation(animation_slide_out);
    }


    private void Anhxa(){
        toolbar = mView.findViewById(R.id.toolbarfragmenthome);
        viewFlipper = mView.findViewById(R.id.viewflipper);
        listViewNew = mView.findViewById(R.id.listviewNew);
        listView = mView.findViewById(R.id.listviewfragmenthome);
        listViewThongTin = mView.findViewById(R.id.listviewthongtin);
        navigationView = mView.findViewById(R.id.navigationView);
        drawerLayout = mView.findViewById(R.id.drawerlayout);
    }


}