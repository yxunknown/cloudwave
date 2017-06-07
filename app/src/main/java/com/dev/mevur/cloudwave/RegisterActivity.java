package com.dev.mevur.cloudwave;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dev.mevur.cloudwave.http.IHttpHander;
import com.dev.mevur.cloudwave.http.POST;

public class RegisterActivity extends AppCompatActivity implements IHttpHander {
    private View mLoginControlPanel;
    private View mRegisterControlPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        mLoginControlPanel = findViewById(R.id.login_control);
        mRegisterControlPanel = findViewById(R.id.register_control);
        //默认显示登录
        mLoginControlPanel.setVisibility(View.VISIBLE);
        mRegisterControlPanel.setVisibility(View.GONE);
    }

    public void btnLoginControlClicked(View view) {
        if (mLoginControlPanel.getVisibility() != View.VISIBLE
                && mRegisterControlPanel.getVisibility() == View.VISIBLE) {
            mRegisterControlPanel.setVisibility(View.GONE);
            mLoginControlPanel.setVisibility(View.VISIBLE);
        }
    }

    public void btnRegisterControlClicked(View view) {
        if (mLoginControlPanel.getVisibility() == View.VISIBLE
                && mRegisterControlPanel.getVisibility() != View.VISIBLE) {
            mRegisterControlPanel.setVisibility(View.VISIBLE);
            mLoginControlPanel.setVisibility(View.GONE);
        }
    }

    public void btnLoginClicked(View view) {
        TextInputLayout username = (TextInputLayout) findViewById(R.id.login_username);
        String strUser = username.getEditText().getText().toString();
        if ("".equals(strUser)) {
            username.setError("用户名不能为空");
            return;
        }
        TextInputLayout password = (TextInputLayout) findViewById(R.id.login_password);
        String strPass = password.getEditText().getText().toString();
        if ("".equals(strPass)) {
            password.setError("密码不能为空");
            return;
        }
        //验证用户名密码
        if (true) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(view, "用户名或密码匹配失败", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void btnRegisterClicked(View view) {

        TextInputLayout id = (TextInputLayout) findViewById(R.id.register_id);
        String strId = id.getEditText().getText().toString();
        if ("".equals(id)) {
            id.setError("id不能为空");
            return;
        }
        TextInputLayout username = (TextInputLayout) findViewById(R.id.register_username);
        String strUser = username.getEditText().toString();
        if ("".equals(strUser)) {
            username.setError("用户名不能为空");
            return;
        }
        TextInputLayout password = (TextInputLayout) findViewById(R.id.register_password);
        String strPass = password.getEditText().toString();
        if ("".equals(strPass)) {
            password.setError("密码不能为空");
            return;
        }
        TextInputLayout commitPassword = (TextInputLayout) findViewById(R.id.register_commit_password);
        String strCommitPass = commitPassword.getEditText().toString();
        if ("".equals(strCommitPass)) {
            commitPassword.setError("确认密码不能为空");
            return;
        }
        if (!strPass.equals(strCommitPass)) {
            commitPassword.setError("确认密码与原密码输入不一致");
            return;
        }
        POST post = new POST("server", "/api/login", this, 1);
        Snackbar.make(view, "注册成功", Snackbar.LENGTH_SHORT).show();
        mRegisterControlPanel.setVisibility(View.GONE);
        mLoginControlPanel.setVisibility(View.VISIBLE);
    }

    @Override
    public void handler(int response, String data, String error, int request) {
        
    }
}
