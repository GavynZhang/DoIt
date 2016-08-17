package com.gavynzhang.doit.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;
import com.gavynzhang.doit.app.state.LoginContext;
import com.gavynzhang.doit.app.state.LogoutState;
import com.gavynzhang.doit.model.entities.MyUser;
import com.gavynzhang.doit.utils.RegularUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 *
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener{

    private EditText signUpUsername;
    private EditText signUpUserEmail;
    private EditText signUpPassword;
    private Button signUpBtn;

    private String username;
    private String email;
    private String password;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);
        Bmob.initialize(this, "9f9400b18ebbb85039231d8bd0cf24d2");

        signUpUsername = $(R.id.sign_up_username);
        signUpUserEmail = $(R.id.sign_up_user_email);
        signUpPassword = $(R.id.sign_up_password);
        mToolbar = $(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle("注册");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.md_grey_100));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signUpBtn = $(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(this);


    }
    /**
     *  用于启动本Activity
     *
     * @param context :上一个容器：Activity
     * */
    public static void actionStart(Context context){
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_up_btn:

                username = signUpUsername.getText().toString().trim();
                email = signUpUserEmail.getText().toString().trim();
                password = signUpPassword.getText().toString().trim();

                if(!RegularUtils.isEmail(email)){
                    Toast.makeText(SignUpActivity.this, "邮箱有误，请重新输入", Toast.LENGTH_SHORT).show();
                    signUpUserEmail.selectAll();

                    if(RegularUtils.isChz(password) || password.length() < 6){
                        Toast.makeText(SignUpActivity.this, "密码只能是数字，英文，及符号,且长度不小于6位", Toast.LENGTH_SHORT).show();
                        signUpPassword.selectAll();
                    }

                }else{
                    signUp();
                }


                break;

        }
    }

    /**
     * 注册逻辑
     */
    private void signUp(){
        MyUser user = new MyUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e == null){
                    Toast.makeText(SignUpActivity.this, R.string.sign_up_success, Toast.LENGTH_SHORT).show();
                    LoginContext.getLoginContext().setState(new LogoutState());
                    finish();
                }
            }
        });
    }
}
