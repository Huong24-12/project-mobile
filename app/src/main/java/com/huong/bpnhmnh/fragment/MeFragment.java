package com.huong.bpnhmnh.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.huong.bpnhmnh.activity.Login;
import com.huong.bpnhmnh.R;
import com.huong.bpnhmnh.model.User;


public class MeFragment extends Fragment {

    Button btnLogout, saveButton;
    TextView fullName, email, age;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_me, container, false);

        age = (TextView) view.findViewById(R.id.profileAge);
        fullName = (TextView) view.findViewById(R.id.profileName);
        email = (TextView) view.findViewById(R.id.profileEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        saveButton = view.findViewById(R.id.btnSave);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                pushData();
            }
        });
        activity = requireActivity();
        return view;
    }

    private void pushData() {
        if (fullName.getText().toString().trim().isEmpty()) {
            Toast.makeText(activity, "Vui lòng điền tên của bạn!", Toast.LENGTH_SHORT).show();
            fullName.requestFocus();
            return;
        }
        if (age.getText().toString().trim().isEmpty()) {
            Toast.makeText(activity, "Vui lòng điền tuổi của bạn!", Toast.LENGTH_SHORT).show();
            age.requestFocus();
            return;
        }
        if (email.getText().toString().trim().isEmpty()) {
            Toast.makeText(activity, "Vui lòng điền email của bạn!", Toast.LENGTH_SHORT).show();
            fullName.requestFocus();
            return;
        }
        User user = new User();
        user.setAge(Integer.parseInt(age.getText().toString()));
        user.setEmail(email.getText().toString().trim());
        user.setName(fullName.getText().toString().trim());
        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .set(user, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (activity.isDestroyed() || activity.isFinishing()) {
                            return;
                        }
                        dismissLoading();
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "Thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uid = FirebaseAuth.getInstance().getUid();
        loadData();
    }

    private Activity activity;
    private String uid;

    private void loadData() {
        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    fullName.setText(user.getName());
                    age.setText(String.valueOf(user.getAge()));
                    email.setText(user.getEmail());
                } else {
                    logout();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (activity.isDestroyed() || activity.isFinishing()) {
                    return;
                }
                Toast.makeText(activity, "Đã có lỗi sảy ra!! Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            auth.signOut();
            Intent intent = new Intent(getContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    private Dialog dl;

    private void showLoading() {
        dl = new Dialog(activity);
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