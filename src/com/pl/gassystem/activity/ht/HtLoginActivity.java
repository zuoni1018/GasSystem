package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.pl.gassystem.HtAppUrl;
import com.pl.gassystem.R;
import com.pl.gassystem.activity.base.BaseActivity;
import com.pl.gassystem.bean.gson.Login;
import com.pl.gassystem.utils.KeyBoardUtils;
import com.pl.gassystem.utils.LogUtil;
import com.pl.gassystem.utils.SPUtils;
import com.pl.gassystem.utils.Xml2Json;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zangyi_shuai_ge on 2017/10/12
 */

public class HtLoginActivity extends BaseActivity {
    @BindView(R.id.UserName)
    EditText UserName;
    @BindView(R.id.Password)
    EditText Password;
    @BindView(R.id.btLogin)
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ht_activity_login);
        ButterKnife.bind(this);

        String name = (String) SPUtils.get(getContext(), "HtUserName", "");
        assert name != null;
        if(!name.trim().equals("")){
            Intent mIntent = new Intent(getContext(), HtMainActivity.class);
            startActivity(mIntent);
            finish();
        }
    }


    @OnClick(R.id.btLogin)
    public void onViewClicked() {
        String userName = UserName.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if (userName.equals("") | password.equals("")) {
            showToast("用户名或密码不能为空");
        } else {
            Login(userName, password);
        }
    }

    private void Login(final String userName, String password) {

        OkHttpUtils
                .post()
                .url(HtAppUrl.LOGIN)
                .addParams("UserName", userName)
                .addParams("passWord", password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("登录", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("登录", response);
                        LogUtil.i("登录", Xml2Json.xml2JSON(response));

                        Gson gson = new Gson();
                        Login info = gson.fromJson(Xml2Json.xml2JSON(response), Login.class);

                        if (info.getString().getContent().equals("0")) {
                            SPUtils.put(getContext(), "HtUserName", userName);
                            Intent mIntent = new Intent(getContext(), HtMainActivity.class);
                            startActivity(mIntent);
                            finish();

                        } else {
                            showToast("用户名或密码错误");
                        }
                    }
                });

    }

    @OnClick(R.id.view)
    public void onViewClicked2() {
        KeyBoardUtils.closeKeybord(UserName, getContext());
    }
}
