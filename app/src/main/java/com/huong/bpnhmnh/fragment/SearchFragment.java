package com.huong.bpnhmnh.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.huong.bpnhmnh.R;
import com.huong.bpnhmnh.activity.CreateNewsActivity;
import com.huong.bpnhmnh.adapter.AdapterConfession;
import com.huong.bpnhmnh.adapter.Confession;
import com.huong.bpnhmnh.utils.PaginationRecyclerview;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SearchFragment extends Fragment {
    public static final int LIMIT = 10;
    private RecyclerView mRecyclerView;
    private AdapterConfession adapterConfession;
    private FirebaseUser user;
    private LinearLayout no_item;
    private boolean isLastItemReached;
    private boolean isLoading;
    private Activity activity;
    private ProgressBar progress_circular;
    private SwipeRefreshLayout mSwipeRefresh;
    private DocumentSnapshot lastVisible;
    private FloatingActionButton add;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_search, container, false);
        activity = requireActivity();
        findView(mView);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
        setSwipeRefresh();
        loadConfession(false);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartForResult.launch(new Intent(activity, CreateNewsActivity.class));
            }
        });

    }

    private void findView(View view) {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        no_item = view.findViewById(R.id.no_item);
        progress_circular = view.findViewById(R.id.progress_circular);
        mSwipeRefresh = view.findViewById(R.id.mSwipeRefresh);
        add = view.findViewById(R.id.add);

    }

    private void setSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.red,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefresh.setOnRefreshListener(() -> {
            isLastItemReached = false;
            lastVisible = null;
            loadConfession(false);
        });
    }

    private void initList() {
        adapterConfession = new AdapterConfession(requireActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapterConfession);
        mRecyclerView.addOnScrollListener(new PaginationRecyclerview() {
            @Override
            public int listSize() {
                return adapterConfession.getItemCount();
            }

            @Override
            public int lastItemPosition() {
                return linearLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void loadMoreItem() {
                if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
                    return;
                }
                loadConfession(true);
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


    }

    private void loadConfession(boolean isLoadMore) {
        Query query;
        query = FirebaseFirestore.getInstance()
                .collection("news")
                .orderBy("approvedTime", Query.Direction.DESCENDING);
        if (isLoadMore && lastVisible != null) {
            query = query.startAfter(lastVisible);
        }
        query = query.limit(LIMIT);

        isLoading = true;

        query.get().addOnCompleteListener(task -> {
            isLoading = false;
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            if (task.isSuccessful()) {
                List<Confession> list = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                    Confession confession = documentSnapshot.toObject(Confession.class);
                    if (confession == null) {
                        continue;
                    }
                    list.add(confession);
                }

                if (!isLoadMore) {
                    adapterConfession.setData(list);
                    if (!list.isEmpty()) {
                        no_item.setVisibility(View.GONE);
                    } else {
                        no_item.setVisibility(View.VISIBLE);
                    }
                } else {
                    adapterConfession.addData(list);
                }
                progress_circular.setVisibility(View.GONE);
                mSwipeRefresh.setRefreshing(false);
                int size = task.getResult().size();
                if (size > 0) {
                    lastVisible = task.getResult().getDocuments().get(size - 1);
                }
                if (size < LIMIT) {
                    isLastItemReached = true;
                }
            } else {
                Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("__index", "onComplete: " + task.getException());
            }
        });


    }


    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Confession confession = intent.getParcelableExtra("confession");
                        adapterConfession.updateInsert(confession);
                        no_item.setVisibility(View.GONE);

                    }
                }
            });

}