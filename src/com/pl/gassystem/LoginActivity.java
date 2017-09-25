package com.pl.gassystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pl.bll.PreferenceBiz;
import com.pl.bll.SetBiz;
import com.pl.bll.UserBiz;
import com.pl.bll.XmlParser;
import com.pl.common.CustomProgressDialog;
import com.pl.common.NetWorkManager;
import com.pl.entity.User;

import org.apache.http.Header;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ��½����  ��������
 */
public class LoginActivity extends Activity {

    private ImageButton btnLogin;
    private EditText etName, etPassWord;
    private UserBiz userBiz;
    private PreferenceBiz preferenceBiz;

    private static Animation anim_btn_begin;
    private static Animation anim_btn_end;

    private static CustomProgressDialog cpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userBiz = new UserBiz(this);
        preferenceBiz = new PreferenceBiz(this);

        anim_btn_begin = AnimationUtils.loadAnimation(this, R.anim.btn_alpha_scale_begin);
        anim_btn_end = AnimationUtils.loadAnimation(this, R.anim.btn_alpha_scale_end);
        String userName = preferenceBiz.getUserName();
        if (userName != null) {
            // �ѵ�¼�û�ֱ���Զ���¼����ת
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            // ������Activity
            finish();
        }

        // ����豸��Ļ�ֱ���
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay(); // Activity#getWindowManager()
        display.getMetrics(metrics);
        int heightPixels;
        int widthPixels;
        heightPixels = metrics.heightPixels;
        widthPixels = metrics.widthPixels;
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                Method method = display.getClass().getMethod("getRawHeight");
                heightPixels = (Integer) method.invoke(display);
            } catch (NoSuchMethodException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point size = new Point();
                Display.class.getMethod("getRealSize",
                        android.graphics.Point.class).invoke(display, size);
                heightPixels = size.y;
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        setupView();
        addListener();//���ü���
        addOnTouchListener();//����ť���ô����¼�  ���õ��͸���Ƚ���

    }

    private void setupView() {
        btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        etName = (EditText) findViewById(R.id.etName);
        etPassWord = (EditText) findViewById(R.id.etPassWord);
    }

    private void addListener() {
        //��½��ť����¼�
        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ��ȡ����
                String name = etName.getText().toString().trim();
                String password = etPassWord.getText().toString().trim();
                if ("".equals(name)) {
                    Toast.makeText(LoginActivity.this, "���������û�����", Toast.LENGTH_SHORT).show();
                } else if ("".equals(password)) {
                    Toast.makeText(LoginActivity.this, "���������û�����", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(name, password);
                    // �ж��Ƿ����
                    if (userBiz.exists(user)) {
                        // ��¼�ɹ�,�����û���
                        preferenceBiz.save(name);
                        // ��ת
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        // ������Activity
                        finish();
                    } else {
                        // ��¼ʧ�ܣ����������¼
                        // �������
                        if (NetWorkManager.isConnect(LoginActivity.this) == false) {
                            Toast.makeText(LoginActivity.this, "�û�����������������ԣ�", Toast.LENGTH_SHORT).show();
                            etPassWord.getText().clear();
                        } else {
                            if (cpDialog == null) {
                                cpDialog = CustomProgressDialog.createDialog(LoginActivity.this);
                            }
                            if (!cpDialog.isShowing()) {
                                cpDialog.show();
                            }
                            SetBiz setBiz = new SetBiz(LoginActivity.this);
                            String url = setBiz.getBookInfoUrl() + "WebMain.asmx/Login";
                            AsyncHttpClient client = new AsyncHttpClient();
                            RequestParams params = new RequestParams();
                            params.put("Username", name);
                            params.put("passWord", password);
                            client.post(url, params,
                                    new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                                            InputStream in = new ByteArrayInputStream(arg2);
                                            XmlParser parser = new XmlParser();
                                            try {
                                                if (parser.parseUserName(in) != null) {
                                                    // ��¼�ɹ�,�����û���
                                                    preferenceBiz.save(etName.getText().toString());
                                                    if (cpDialog != null && cpDialog.isShowing()) {
                                                        cpDialog.dismiss();
                                                        cpDialog = null;
                                                    }
                                                    // ��ת
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();// ������Activity

                                                } else {
                                                    if (cpDialog != null && cpDialog.isShowing()) {
                                                        cpDialog.dismiss();
                                                        cpDialog = null;
                                                    }
                                                    Toast.makeText(LoginActivity.this, "�û�����������������ԣ�", Toast.LENGTH_SHORT).show();
                                                    etPassWord.getText().clear();
                                                }
                                            } catch (XmlPullParserException | IOException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                                            if (cpDialog != null && cpDialog.isShowing()) {
                                                cpDialog.dismiss();
                                                cpDialog = null;
                                            }
                                            Toast.makeText(LoginActivity.this, "�û�����������������ԣ�", Toast.LENGTH_SHORT).show();
                                            etPassWord.getText().clear();
                                        }
                                    });
                        }
                    }
                }
            }
        });
    }


    private void addOnTouchListener() {
        btnLogin.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnLogin.startAnimation(anim_btn_begin);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnLogin.startAnimation(anim_btn_end);
                }
                return false;
            }
        });
    }

}
