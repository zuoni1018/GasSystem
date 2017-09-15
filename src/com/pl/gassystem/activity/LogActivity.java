package com.pl.gassystem.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.activity.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * 更新日志界面
 */
public class LogActivity extends BaseTitleActivity {

    @BindView(R.id.logInfo)
    TextView logInfo;

    @Override
    protected int setLayout() {
        return R.layout.activity_log;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setTitle("更新日志");

        String log = "2017-04-28 V1.4更新：\n1、增加批抄自动补抄未抄到表2次的机制\n2、优化数据统计和显示的内容\n\n" +
                "2016-12-16 V1.3 更新：\n1、增加对蓝牙透传模块频点等参数的设置\n2、摄像抄表增加设置表计频点功能\n3、增加对新加长帧摄像抄表模式的支持\n4、修复手机识别图片显示问题、优化补帧机制\n\n"
                + "2016-07-24 V1.2 更新：\n1、旧程序无线表抄表兼容性修正\n2、上海模式新增网络控制协议若干\n\n"
                + "2015-10-30 V1.1 更新：\n1、抄表模式选择、抄表参数自定义配置\n2、服务端网络账户登录功能（需联网）\n3、摄像表现场安装信息上传完善\n\n"
                + "2015-08-15 V1.0 发布功能： \n"
                + "1、单抄、群抄抄表并查看\n2、开阀、关阀功能\n3、软件内自建账册及表具绑定\n4、下载服务器账册及绑定信息（需联网）\n"
                + "5、上传抄表数据至服务器（需联网）\n6、摄像表单抄、上传识别功能（需联网）\n7、摄像表图片位置上下调整";
        logInfo.setText(log);
    }

}
