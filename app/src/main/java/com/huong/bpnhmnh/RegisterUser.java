package com.huong.bpnhmnh;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class RegisterUser extends AppCompatActivity {
    private EditText emailedit, passedit;
    private Button  btnregis;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth=FirebaseAuth.getInstance();

        emailedit=findViewById(R.id.email);
        passedit=findViewById(R.id.password);
        btnregis=findViewById(R.id.btnregis);

        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register(){
        String email, pass;
        email = emailedit.getText().toString();
        pass = passedit.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "vui lòng nhập password!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterUser.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
