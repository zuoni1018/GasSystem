package com.pl.gassystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.bll.SetBiz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetUpdateActivity extends Activity {

    private TextView tvTitlebar_name;
    private EditText etSetUpdate1;
    private ImageButton btnOnlybackQuit;
    private Button btnSetUpdateSubmit, btnSetUpdateCancel;
    private SetBiz setBiz;
    private static int SetType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_set_update);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);

        SetType = getIntent().getIntExtra("SetType", 1);
        setupView();
        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
        setBiz = new SetBiz(this);
        if (SetType == 1) {
            tvTitlebar_name.setText("数据上传下载地址");
            etSetUpdate1.setText(setBiz.getBookInfoUrl());
        } else if (SetType == 2) {
            tvTitlebar_name.setText("摄像图片识别地址");
            etSetUpdate1.setText(setBiz.getCopyPhotoUrl());
        }

        addListener();
    }

    private void setupView() {
        btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
        etSetUpdate1 = (EditText) findViewById(R.id.etSetUpdate1);
        btnSetUpdateSubmit = (Button) findViewById(R.id.btnSetUpdateSubmit);
        btnSetUpdateCancel = (Button) findViewById(R.id.btnSetUpdateCancel);
    }

    private void addListener() {
        btnOnlybackQuit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSetUpdateCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSetUpdateSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (SetType == 1) {

                    if(!isTopURL(etSetUpdate1.getText().toString().trim()+ AppUrl.GET_COLLECTOR_INFO)){
                        Toast.makeText(SetUpdateActivity.this," 请输入正确的url地址",Toast.LENGTH_SHORT).show();

                    }else {
                        setBiz.updateBookInfoUrl(etSetUpdate1.getText().toString());
                        new AlertDialog.Builder(SetUpdateActivity.this,
                                android.R.style.Theme_DeviceDefault_Light_Dialog)
                                .setTitle("成功")
                                .setMessage("修改成功")
                                .setCancelable(false)
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                finish();
                                            }
                                        }).show();
                    }
                } else if (SetType == 2) {
                    setBiz.updateCopyPhotoUrl(etSetUpdate1.getText().toString());
                    new AlertDialog.Builder(SetUpdateActivity.this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog)
                            .setTitle("成功")
                            .setMessage("修改成功")
                            .setCancelable(false)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            finish();
                                        }
                                    }).show();
                }

            }
        });
    }
    public static boolean isTopURL(String str){
        //转换为小写
        str = str.toLowerCase();
        String domainRules = "com.cn|net.cn|org.cn|gov.cn|com.hk|公司|中国|网络|com|net|org|int|edu|gov|mil|arpa|Asia|biz|info|name|pro|coop|aero|museum|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cf|cg|ch|ci|ck|cl|cm|cn|co|cq|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|eh|es|et|ev|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gh|gi|gl|gm|gn|gp|gr|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|in|io|iq|ir|is|it|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|ml|mm|mn|mo|mp|mq|mr|ms|mt|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nt|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|pt|pw|py|qa|re|ro|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|st|su|sy|sz|tc|td|tf|tg|th|tj|tk|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|va|vc|ve|vg|vn|vu|wf|ws|ye|yu|za|zm|zr|zw";
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "(([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]+\\.)?" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "("+domainRules+"))" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher isUrl = pattern.matcher(str);
        return isUrl.matches();
    }
}
