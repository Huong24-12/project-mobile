package com.huong.bpnhmnh.activity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.huong.bpnhmnh.BaseActivity;
import com.huong.bpnhmnh.R;
import com.huong.bpnhmnh.model.Dish;

import java.util.Objects;

public class ShowDishInforActivity extends BaseActivity {
    private MaterialToolbar mToolbar;
    private TextView name, author, introduce, recipe;
    private ImageView image;
    private ImageButton likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dish_infor_acitivity);
        initView();
        toolbar();
        uid = FirebaseAuth.getInstance().getUid();
        loadData();


        likes.setOnClickListener(view -> {
            if (isLike) {
                showLoading();
                unLike(dish, (ImageView) view);
            } else {
                addLike(dish, (ImageView) view);
            }
        });

    }
    //bấm vào 1 item ở home

    private void loadData() {
        showLoading();
        dish = getIntent().getParcelableExtra("dish");
        if (dish != null) {
            name.setText(dish.getName());
            author.setText(String.format("Tác giả: %s", dish.getAuthor()));
            introduce.setText(String.format("Giới thiệu: %s", dish.getIntroduce()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                recipe.setText(Html.fromHtml("Công thức: " + dish.getRecipe(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                recipe.setText(Html.fromHtml("Công thức: " + dish.getRecipe()));
            }

            Glide.with(this)
                    .load(dish.getImage())
                    .into(image);
            checkData();
        } else {
            finish();
        }

    }

    private void initView() {
        mToolbar = findViewById(R.id.toolBar);
        name = findViewById(R.id.name);
        author = findViewById(R.id.author);
        introduce = findViewById(R.id.introduce);
        recipe = findViewById(R.id.recipe);
        image = findViewById(R.id.image);
        likes = findViewById(R.id.likes);

    }

    private void toolbar() {
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }


    private Dialog dl;

    private void showLoading() {
        dl = new Dialog(ShowDishInforActivity.this);
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


    private Dish dish;
    private boolean isLike;
    private String uid;

    private void checkData() {
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("likes")
                    .document(uid)
                    .collection("list")
                    .document(dish.getDocId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (isUnavailable()) {
                                return;
                            }
                            isLike = documentSnapshot.exists();
                            if (isLike) {
                                likes.setImageResource(R.drawable.ic_favorite);
                            } else {
                                likes.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            }
                            dismissLoading();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (isUnavailable()) {
                        return;
                    }
                    isLike = false;
                    likes.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    dismissLoading();
                }
            });
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void unLike(Dish dish, ImageView view) {

        if (uid != null) {
            FirebaseFirestore.getInstance().collection("likes")
                    .document(uid)
                    .collection("list")
                    .document(dish.getDocId())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (isUnavailable()) {
                                return;
                            }
                            dismissLoading();
                            if (task.isSuccessful()) {
                                Toast.makeText(ShowDishInforActivity.this, "Bỏ thích", Toast.LENGTH_SHORT).show();
                                isLike = false;
                                view.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            } else {
                                Toast.makeText(ShowDishInforActivity.this, "Đã xảy ra sự cố!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            dismissLoading();
            Toast.makeText(ShowDishInforActivity.this, "Đã xảy ra sự cố!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addLike(Dish dish, ImageView view) {
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("likes")
                    .document(uid)
                    .collection("list")
                    .document(dish.getDocId())
                    .set(dish, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (isUnavailable()) {
                                return;
                            }
                            dismissLoading();
                            if (task.isSuccessful()) {
                                isLike = true;
                                Toast.makeText(ShowDishInforActivity.this, "Đã thích", Toast.LENGTH_SHORT).show();
                                view.setImageResource(R.drawable.ic_favorite);
                            } else {
                                Toast.makeText(ShowDishInforActivity.this, "Đã xảy ra sự cố!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            dismissLoading();
            Toast.makeText(ShowDishInforActivity.this, "Đã xảy ra sự cố!", Toast.LENGTH_SHORT).show();
        }

    }
}