package com.gavynzhang.doit.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;
import com.gavynzhang.doit.utils.RegularUtils;

import cn.bmob.v3.Bmob;

public class SignInActivity extends BaseActivity implements View.OnClickListener{

    private Button resetPasswordBtn;
    private Button createAccountBtn;
    private Button signIn;

    private EditText userEmail;
    private EditText userPassword;

    private String email;
    private String password;


    public static void actionStart(Context context){
        Intent intent = new Intent(context, SignInActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Bmob.initialize(this, "9f9400b18ebbb85039231d8bd0cf24d2");

        resetPasswordBtn = $(R.id.reset_password_btn);
        createAccountBtn = $(R.id.create_account_btn);
        userEmail = $(R.id.sign_in_user_email);
        userPassword = $(R.id.sign_in_password);
        signIn = $(R.id.sign_in_btn);

        resetPasswordBtn.setOnClickListener(this);
        createAccountBtn.setOnClickListener(this);
        signIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reset_password_btn:
                break;
            case R.id.create_account_btn:
                SignUpActivity.actionStart(SignInActivity.this);
                break;
            case R.id.sign_in_btn:
                email = userEmail.getText().toString().trim();
                password = userPassword.getText().toString().trim();
                if (!RegularUtils.isEmail(email)){
                    Toast.makeText(SignInActivity.this,"邮箱有误，请重新输入！", Toast.LENGTH_SHORT).show();
                    userEmail.selectAll();
                }
            default:
                break;
        }
    }

    /**
     * 进行登录操作
     * */
    private void login(){

    }
}
