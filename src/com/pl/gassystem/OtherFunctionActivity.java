package com.pl.gassystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pl.gassystem.base.BaseTitleActivity2;
import com.pl.gassystem.userinfo.XiNingUserInfoImportActivity;

/**
 * Created by zangyi_shuai_ge on 2017/8/29
 */

public class OtherFunctionActivity extends BaseTitleActivity2 {


    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(OtherFunctionActivity.this);
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent mIntent=new Intent(OtherFunctionActivity.this, XiNingUserInfoImportActivity.class);
                startActivity(mIntent);
            }
        });
        builder.setTitle("提示");
        builder.setMessage("该功能只是用来导入、修改用户信息 通过表具编号去绑定用户信息（该功能并不会去导入账册） ");
        alertDialog=builder.create();
        setTitle("其他功能");
        Button btXiNingUserInfoImport = (Button) findViewById(R.id.btXiNingUserInfoImport);
        btXiNingUserInfoImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
//                Intent mIntent=new Intent(OtherFunctionActivity.this, XiNingUserInfoImportActivity.class);
//                startActivity(mIntent);
            }
        });
    }


    @Override
    protected int setLayout() {
        return R.layout.activity_other_function;
    }


}
