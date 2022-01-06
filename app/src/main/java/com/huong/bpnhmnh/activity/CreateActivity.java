package com.huong.bpnhmnh.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.huong.bpnhmnh.BaseActivity;
import com.huong.bpnhmnh.R;
import com.huong.bpnhmnh.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateActivity extends BaseActivity {
    private MaterialToolbar toolBar;
    private EditText name, recipe, introduce;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        checkAndRequestPermission();
        loadData();
        initView();
        toolbar();

    }

    private void initView() {
        toolBar = findViewById(R.id.toolBar);
        name = findViewById(R.id.name);
        recipe = findViewById(R.id.recipe);
        introduce = findViewById(R.id.introduce);
        imageView = findViewById(R.id.imageView);
    }

    private void getImageFromAlbum() {
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 111);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }
    }

    private void toolbar() {
        setSupportActionBar(toolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
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
        } else if (item.getItemId() == R.id.image) {
            getImageFromAlbum();

        } else if (item.getItemId() == R.id.save) {
            addData();
        }
        return super.onOptionsItemSelected(item);
    }

    UploadTask uploadTask;

    private Dialog dl;

    private void addData() {

        dl = new Dialog(CreateActivity.this);
        dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dl.setContentView(R.layout.progress_dialog);
        dl.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dl.setCanceledOnTouchOutside(false);

        if (name.getText().toString().trim().isEmpty()) {
            Toast.makeText(CreateActivity.this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return;
        }
        if (introduce.getText().toString().trim().isEmpty()) {
            Toast.makeText(CreateActivity.this, "Vui lòng nhập mô tả sản phẩm", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return;
        }
        if (recipe.getText().toString().trim().isEmpty()) {
            Toast.makeText(CreateActivity.this, "Vui lòng nhập công thức", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return;
        }
        if (path == null) {
            Toast.makeText(CreateActivity.this, "Vui lòng thêm 1 ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        dl.show();

        Map<String, Object> map = new HashMap<>();
        map.put("name", name.getText().toString().trim());
        map.put("introduce", introduce.getText().toString().trim());
        map.put("recipe", recipe.getText().toString().trim());
        map.put("time", System.currentTimeMillis());
        map.put("author", author);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("image").child(uid).child(String.valueOf(System.currentTimeMillis()));
        uploadTask = storageRef.putFile(path);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (isUnavailable()) {
                    return;
                }
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Log.d("__url", "handleFirestore: " + uri.toString());
                    map.put("image", uri.toString());
                    uploadFirestore(map);
                });
            }
        });

        uploadTask.addOnFailureListener(e -> {
            if (isDestroyed() || isFinishing()) {
                return;
            }
            Toast.makeText(CreateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            if (dl != null && dl.isShowing()) {
                dl.dismiss();
            }
        });
        uploadTask.addOnCanceledListener(() -> {
            if (isDestroyed() || isFinishing()) {
                return;
            }
            if (dl != null && dl.isShowing()) {
                dl.dismiss();
            }
        });

    }

    private void uploadFirestore(Map<String, Object> map) {
        FirebaseFirestore.getInstance().collection("data").document()
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (isUnavailable()) {
                            return;
                        }
                        dl.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent();
                            intent.putExtra("add","add");
                            setResult(Activity.RESULT_OK, intent);
                            Toast.makeText(CreateActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CreateActivity.this, "Đã có lỗi sảy ra", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private String uid;
    private String author;

    private void loadData() {
        uid = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (isUnavailable()) {
                    return;
                }
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    author = user.getName();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (isUnavailable()) {
                    return;
                }
                Toast.makeText(CreateActivity.this, "Đã có lỗi sảy ra!! Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Uri path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 111) {
                if (data != null) {
                    path = data.getData();
                    Glide.with(this)
                            .load(path)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .into(imageView);
                }
            }

        }
    }

    private void checkAndRequestPermission() {
        int p1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int p2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (p1 != PackageManager.PERMISSION_GRANTED || p2 != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(CreateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

}