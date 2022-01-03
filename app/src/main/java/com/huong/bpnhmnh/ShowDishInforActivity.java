package com.huong.bpnhmnh;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Objects;

public class ShowDishInforActivity extends AppCompatActivity {
    private MaterialToolbar mToolbar;
    private TextView name, author, introduce, recipe;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dish_infor_acitivity);
        initView();
        toolbar();

        loadData();

    }
    //bấm vào 1 item ở home

    private void loadData() {
        Dish dish = getIntent().getParcelableExtra("dish");
        if (dish != null) {
            name.setText(dish.getName());
            author.setText(String.format("Tác giả: %s", dish.getAuthor()));
            introduce.setText(String.format("Giới thiệu: %s", dish.getIntroduce()));
            recipe.setText(String.format("Công thức: %s", dish.getRecipe()));
            Glide.with(this)
                    .load(dish.getImage())
                    .into(image);
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

    }

    private void toolbar() {
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onContextItemSelected(item);

    }
}