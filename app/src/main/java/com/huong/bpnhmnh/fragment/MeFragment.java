package com.huong.bpnhmnh.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.huong.bpnhmnh.R;


public class MeFragment<StorageTask> extends Fragment {

    private View mView;

    ImageView profileImageView;
    Button closeButton, saveButton;
    TextView fullName, email, phone, profileChangeBtn;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private Uri imageUri;
    private String myUri ="";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicaRef;




    public MeFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_me, container, false);

        phone = (TextView) mView.findViewById(R.id.profilePhone);
        fullName = (TextView) mView.findViewById(R.id.profileName);
        email = (TextView) mView.findViewById(R.id.profileEmail);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageProfilePicaRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        profileImageView = mView.findViewById(R.id.profileImage);

        closeButton = mView.findViewById(R.id.btnLogout);
        saveButton = mView.findViewById(R.id.btnSave);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfileImage();
            }
        });


        return mView;
    }

    private void uploadProfileImage() {
    }
}