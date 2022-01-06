package com.huong.bpnhmnh.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.huong.bpnhmnh.model.Dish;
import com.huong.bpnhmnh.utils.ItemClick;
import com.huong.bpnhmnh.adapter.LikeAdapter;
import com.huong.bpnhmnh.utils.PaginationRecyclerview;
import com.huong.bpnhmnh.R;
import com.huong.bpnhmnh.activity.ShowDishInforActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private LikeAdapter adapter;
    private DocumentSnapshot lastVisible;
    private boolean isLastItemReached;
    private boolean isLoading;
    private ProgressBar progress_circular;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_favorite, container, false);
        initView(mView);
        return mView;
    }

    private void initView(View mView) {
        recyclerView = mView.findViewById(R.id.recyclerView);
        progress_circular = mView.findViewById(R.id.progress_circular);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();

    }

    private FirebaseFirestore db;

    private void initList() {
        uid = FirebaseAuth.getInstance().getUid();
        isLastItemReached = false;
        isLoading = false;
        db = FirebaseFirestore.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LikeAdapter(requireActivity());
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
                loadLike(true);
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

        adapter.onLikeClick(new ItemClick() {
            @Override
            public void onClick(int position, Dish dish) {
                showLoading();
                unLike(position,dish);
            }
        });


    }


    private Dialog dl;

    private void showLoading() {
        dl = new Dialog(requireActivity());
        dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dl.setContentView(R.layout.progress_dialog);
        dl.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dl.setCanceledOnTouchOutside(false);
        dl.show();
    }

    private void dismissLoading() {
        if (dl != null && dl.isShowing()) {
            dl.dismiss();
        }
    }

    private void unLike(int p,Dish dish) {

        if (uid != null) {
            FirebaseFirestore.getInstance().collection("likes")
                    .document(uid)
                    .collection("list")
                    .document(dish.getDocId())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (requireActivity().isFinishing() || requireActivity().isDestroyed()) {
                                return;
                            }
                            dismissLoading();
                            if (task.isSuccessful()) {
                                adapter.updateView(p,dish);
                                Toast.makeText(requireActivity(), "Bỏ thích", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireActivity(), "Đã xảy ra sự cố!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            dismissLoading();
            Toast.makeText(requireActivity(), "Đã xảy ra sự cố!", Toast.LENGTH_SHORT).show();
        }
    }

    private String uid;

    private void loadLike(boolean isLoadMore) {

        Query first;
        first = db.collection("likes")
                .document(uid)
                .collection("list")
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
                progress_circular.setVisibility(View.GONE);
                Log.d("__index", "loadNote: " + task.getException());
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadLike(false);
    }
}