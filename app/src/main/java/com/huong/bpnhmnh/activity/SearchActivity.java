package com.huong.bpnhmnh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.huong.bpnhmnh.BaseActivity;
import com.huong.bpnhmnh.R;
import com.huong.bpnhmnh.adapter.SearchAdapter;
import com.huong.bpnhmnh.model.Dish;
import com.huong.bpnhmnh.utils.PaginationRecyclerview;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends BaseActivity {
    SearchAdapter adapter;
    private DocumentSnapshot lastVisible;
    private boolean isLastItemReached;
    private boolean isLoading;
    private RecyclerView mRecyclerView;
    private LinearLayout layoutNoItem;
    private ProgressBar progress_circular;
    private ImageButton btnSearch;
    private EditText edtSearch;
    private MaterialToolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findView();
        toolbar();
        initList();
        edtSearch.requestFocus();
        btnSearch.setOnClickListener(v -> {
            List<String> listName = new ArrayList<>();
            String key = edtSearch.getText().toString().trim();
            listName.add(key);
            listName.add(key.toLowerCase());
            listName.add(key.toUpperCase());
            if (!key.isEmpty() && listName.size() > 0) {
                btnSearch.setVisibility(View.GONE);
                progress_circular.setVisibility(View.VISIBLE);
                layoutNoItem.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                loadPeople(false, listName);
            }

        });
    }

    private void findView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        progress_circular = findViewById(R.id.progress_circular);
        layoutNoItem = findViewById(R.id.layoutNoItem);
        btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearch);
        toolBar = findViewById(R.id.toolBar);
    }

    private void toolbar() {
        setSupportActionBar(toolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initList() {
        adapter = new SearchAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new PaginationRecyclerview() {
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
                if (isUnavailable()) {
                    return;
                }
                loadPeople(true, listName);
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
            Intent intent = new Intent(SearchActivity.this, ShowDishInforActivity.class);
            intent.putExtra("dish", dish);
            startActivity(intent);
        });

    }

    List<String> listName;

    private void loadPeople(boolean isLoadMore, List<String> listName) {
        this.listName = listName;
        Query query = FirebaseFirestore.getInstance().collection("data")
                .whereIn("name", listName)
                .limit(10);

        if (isLoadMore && lastVisible != null && !lastVisible.getMetadata().hasPendingWrites()) {
            query = query.startAfter(lastVisible);
        }
        query = query.limit(10);
        isLoading = true;
        query.get()
                .addOnSuccessListener(querySnapshot -> {
                    if (isUnavailable()) {
                        return;
                    }

                    isLoading = false;
                    List<Dish> list = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        Dish model = documentSnapshot.toObject(Dish.class);
                        if (model == null) {
                            continue;
                        }
                        model.setDocId(documentSnapshot.getId());
                        list.add(model);
                    }
                    if (!isLoadMore) {
                        adapter.setData(list);
                        if (list.isEmpty()) {
                            layoutNoItem.setVisibility(View.VISIBLE);
                        } else {
                            layoutNoItem.setVisibility(View.GONE);
                        }
                    } else {
                        adapter.addData(list);
                    }
                    btnSearch.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    progress_circular.setVisibility(View.GONE);
                    int size = querySnapshot.size();
                    if (size > 0) {
                        lastVisible = querySnapshot.getDocuments().get(querySnapshot.size() - 1);
                    }
                    if (size < 10) {
                        isLastItemReached = true;
                    }
                })
                .addOnFailureListener(e -> {
                    if (isUnavailable()) {
                        return;
                    }
                    isLoading = false;
                    if (e instanceof FirebaseFirestoreException
                            && ((FirebaseFirestoreException) e).getCode() == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                    } else {
                        Log.d("__index", "loadPeople: " + e.getLocalizedMessage());

                    }

                });

    }

}