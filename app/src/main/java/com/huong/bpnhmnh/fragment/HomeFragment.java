package com.huong.bpnhmnh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.huong.bpnhmnh.Dish;
import com.huong.bpnhmnh.NewAdapter;
import com.huong.bpnhmnh.PaginationRecyclerview;
import com.huong.bpnhmnh.R;
import com.huong.bpnhmnh.ShowDishInforActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private DocumentSnapshot lastVisible;
    private boolean isLastItemReached;
    private boolean isLoading;
    private ProgressBar progress_circular;

    RecyclerView recyclerView;

    NewAdapter adapter;

    ViewFlipper viewFlipper;
    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View mView = inflater.inflate(R.layout.fragment_home2, container, false);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        Anhxa(mView);
        return mView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uid = FirebaseAuth.getInstance().getUid();
        ActionViewFlipper();
        initList();
        LoadDish(false);

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
        for (int i = 0; i < mangquangcao.size(); i++) {
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
        //san sale
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setInAnimation(animation_slide_out);
    }


    private void Anhxa(View view) {
        viewFlipper = view.findViewById(R.id.viewflipper);
        progress_circular = view.findViewById(R.id.progress_circular);
        recyclerView = view.findViewById(R.id.recyclerView);

    }

    private FirebaseFirestore db;

    private void initList() {

        isLastItemReached = false;
        isLoading = false;
        db = FirebaseFirestore.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NewAdapter(requireActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationRecyclerview() {
            @Override
            public int listSize() {
                return adapter.getItemCount();
            }

            @Override
            public int lastItemPosition() {
                return linearLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void loadMoreItem() {
                if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                    return;
                }
                LoadDish(true);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastItemReached;
            }
        });

        adapter.onClickItem((position, dish) -> {
            Intent intent = new Intent(requireActivity(), ShowDishInforActivity.class);
            intent.putExtra("dish", dish);
            requireActivity().startActivity(intent);
        });


    }

    private String uid;

    private void LoadDish(boolean isLoadMore) {

        Query first;
        first = db.collection("data")
                .orderBy("time", Query.Direction.DESCENDING);
        if (isLoadMore && lastVisible != null) {
            first = first.startAfter(lastVisible);
        }
        first = first.limit(10);
        isLoading = true;
        first.get().addOnCompleteListener(task -> {
            isLoading = false;
            if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                return;
            }
            if (task.isSuccessful()) {

                List<Dish> list = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                    Dish model = documentSnapshot.toObject(Dish.class);
                    if (model == null) {
                        continue;
                    }
                    model.setDocId(documentSnapshot.getId());
                    list.add(model);
                }
                if (!isLoadMore) {
                    adapter.setData(list);
                } else {
                    adapter.addData(list);
                }
                progress_circular.setVisibility(View.GONE);
                int size = task.getResult().size();
                if (size > 0) {
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                }
                if (size < 10) {
                    isLastItemReached = true;
                }
            } else {
                Log.d("__index", "loadNote: " + task.getException());
            }

        });
    }

}