package com.huong.bpnhmnh;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class RegisterUser extends BaseActivity {
    private EditText emailedit, passedit, ageEdit, fullNameEdit;
    private Button btnregis;
    private FirebaseAuth mAuth;
    private FirebaseAuth mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();


        emailedit = findViewById(R.id.email);
        passedit = findViewById(R.id.password);
        btnregis = findViewById(R.id.btnregis);
        fullNameEdit = findViewById(R.id.fullName);
        ageEdit = findViewById(R.id.age);

        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String email, pass, name, age;
        email = emailedit.getText().toString();
        pass = passedit.getText().toString();
        age = ageEdit.getText().toString();
        name = fullNameEdit.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "vui lòng nhập password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "vui lòng nhập Tên", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(age)) {
            Toast.makeText(this, "vui lòng nhập Tuổi!", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoading();
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();

                    Map<String, Object> map = new HashMap<>();
                    map.put("age", age);
                    map.put("email", email);
                    map.put("name", name);


                    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                    FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(uid)
                            .set(map, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> {
                                if (isUnavailable()) {
                                    return;
                                }
                                dismissLoading();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }).addOnFailureListener(e -> {
                        if (isUnavailable()) {
                            return;
                        }
                        dismissLoading();
                        Log.d("__first", "handleUpdate: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Đã gặp lỗi", Toast.LENGTH_SHORT).show();
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Dialog dl;

    private void showLoading() {
        dl = new Dialog(RegisterUser.this);
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


}
