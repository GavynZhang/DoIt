package com.gavynzhang.doit.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;
import com.gavynzhang.doit.app.state.LoginContext;
import com.gavynzhang.doit.model.db.MyDatabaseHelper;
import com.gavynzhang.doit.model.entities.Tomato;

public class TomatoBreakDialogActivity extends BaseActivity {

    private EditText tomatoBreakDialogEditText;
    private TextView cancel;
    private TextView save;

    private Tomato mTomato;

    private MyDatabaseHelper dbHelper = MyDatabaseHelper.getMyDatabasesHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tomato_break_dialog);

        mTomato = (Tomato)getIntent().getSerializableExtra("tomato");

        initViews();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加保存到本地数据库
                mTomato.setBrokenReason(tomatoBreakDialogEditText.getText().toString());
                mTomato.setIsBroken(1);
                LoginContext.getLoginContext().saveTomatoDate(mTomato);

                Intent intent = new Intent(TomatoBreakDialogActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initViews(){
        tomatoBreakDialogEditText = $(R.id.tomato_break_dialog_edit_text);
        cancel = $(R.id.tomato_break_dialog_cancel_btn);
        save = $(R.id.tomato_break_dialog_save_btn);
    }
}
