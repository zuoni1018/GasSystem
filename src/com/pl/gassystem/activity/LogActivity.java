package com.pl.gassystem.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.activity.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * ������־����
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

        setTitle("������־");

        String log = "2017-04-28 V1.4���£�\n1�����������Զ�����δ������2�εĻ���\n2���Ż�����ͳ�ƺ���ʾ������\n\n" +
                "2016-12-16 V1.3 ���£�\n1�����Ӷ�����͸��ģ��Ƶ��Ȳ���������\n2�����񳭱��������ñ��Ƶ�㹦��\n3�����Ӷ��¼ӳ�֡���񳭱�ģʽ��֧��\n4���޸��ֻ�ʶ��ͼƬ��ʾ���⡢�Ż���֡����\n\n"
                + "2016-07-24 V1.2 ���£�\n1���ɳ������߱������������\n2���Ϻ�ģʽ�����������Э������\n\n"
                + "2015-10-30 V1.1 ���£�\n1������ģʽѡ�񡢳�������Զ�������\n2������������˻���¼���ܣ���������\n3��������ֳ���װ��Ϣ�ϴ�����\n\n"
                + "2015-08-15 V1.0 �������ܣ� \n"
                + "1��������Ⱥ�������鿴\n2���������ط�����\n3��������Խ��˲ἰ��߰�\n4�����ط������˲ἰ����Ϣ����������\n"
                + "5���ϴ���������������������������\n6������������ϴ�ʶ���ܣ���������\n7�������ͼƬλ�����µ���";
        logInfo.setText(log);
    }

}
