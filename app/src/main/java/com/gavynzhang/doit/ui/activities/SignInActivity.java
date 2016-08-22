package com.gavynzhang.doit.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;
import com.gavynzhang.doit.app.state.LoginContext;
import com.gavynzhang.doit.app.state.SignInState;
import com.gavynzhang.doit.model.entities.MyUser;
import com.gavynzhang.doit.utils.LogUtils;
import com.gavynzhang.doit.utils.RegularUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignInActivity extends BaseActivity implements View.OnClickListener{

    private Button resetPasswordBtn;
    private Button createAccountBtn;
    private Button signIn;

    private EditText username;
    private EditText userPassword;

    private String usernameString;
    private String passwordString;

    private Toolbar mToolbar;


    public static void actionStart(Context context){
        Intent intent = new Intent(context, SignInActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_in);
        Bmob.initialize(this, "9f9400b18ebbb85039231d8bd0cf24d2");

        initViews();

        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle("登录");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.md_grey_100));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setOnClickListener();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reset_password_btn:
                break;
            case R.id.create_account_btn:
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.sign_in_btn:
                usernameString = username.getText().toString().trim();
                passwordString = userPassword.getText().toString().trim();
                login();
//                if (!RegularUtils.isEmail(usernameString)){
//                    Toast.makeText(SignInActivity.this,"邮箱有误，请重新输入！", Toast.LENGTH_SHORT).show();
//                    userEmail.selectAll();
//                }
            default:
                break;
        }
    }

    /**
     * 进行登录操作
     * */
    private void login(){
        MyUser user = new MyUser();
        user.setUsername(usernameString);
        user.setPassword(passwordString);

        user.login(new SaveListener<MyUser>() {

            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e == null){
                    LoginContext.getLoginContext().setState(new SignInState());
                }else{

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    usernameString = data.getStringExtra("username");
                    passwordString = data.getStringExtra("password");

                    username.setText(usernameString);
                    LogUtils.w("SignInActivity","username: "+usernameString);
                    userPassword.setText(passwordString);
                }
                break;
            default:
                break;
        }
    }

    private void initViews(){
        resetPasswordBtn = $(R.id.reset_password_btn);
        createAccountBtn = $(R.id.create_account_btn);
        username = $(R.id.sign_in_user_name);
        userPassword = $(R.id.sign_in_password);
        signIn = $(R.id.sign_in_btn);
        mToolbar = $(R.id.toolbar);
    }

    private void setOnClickListener(){
        resetPasswordBtn.setOnClickListener(this);
        createAccountBtn.setOnClickListener(this);
        signIn.setOnClickListener(this);
    }

}
