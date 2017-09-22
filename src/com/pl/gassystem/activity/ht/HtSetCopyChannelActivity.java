package com.pl.gassystem.activity.ht;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.utils.SPUtils;
import com.zuoni.zuoni_common.dialog.other.DataPickerHtChooseDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/18
 * 批量设置表具参数
 */

public class HtSetCopyChannelActivity extends HtBaseTitleActivity {


    @BindView(R.id.tvKuoPinYinZi)
    TextView tvKuoPinYinZi;
    @BindView(R.id.tvKuoPinXinDao)
    TextView tvKuoPinXinDao;
    @BindView(R.id.btChoose)
    Button btChoose;
    @BindView(R.id.btSure)
    Button btSure;
    private ArrayList<String> bookNos;
    private DataPickerHtChooseDialog dataPickerHtChooseDialog;

    private  String a1="09";
    private String a2="14";

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_set_copy_channel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("设置抄表扩频因子、信道");
        a1=(String) SPUtils.get(getContext(),"HtKuoPinYinZi","09");
        a2=(String) SPUtils.get(getContext(),"HtKuoPinXinDao","14");
        tvKuoPinYinZi.setText(a1);
        tvKuoPinXinDao.setText(a2);
    }

    @OnClick({R.id.btChoose, R.id.btSure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btChoose:
                DataPickerHtChooseDialog.Builder builder = new DataPickerHtChooseDialog.Builder(getContext());

                List<String> right = new ArrayList<>();
                for (int i = 0; i < 28; i++) {
                    if (i < 10) {
                        right.add("0" + i);
                    } else {
                        right.add("" + i);
                    }
                }

                builder.setData2(right);
                builder.setSelection2(14);
                List<String> left = new ArrayList<>();
                for (int i = 7; i < 13; i++) {
                    if (i < 10) {
                        left.add("0" + i);
                    } else {
                        left.add("" + i);
                    }
                }
                builder.setData(left);
                builder.setSelection(2);

                builder.setOnDataSelectedListener(new DataPickerHtChooseDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        tvKuoPinYinZi.setText(itemValue);
                        a1=itemValue;
                    }

                    @Override
                    public void onDataSelected2(String itemValue) {
                        tvKuoPinXinDao.setText(itemValue);
                        a2=itemValue;
                    }
                });

                dataPickerHtChooseDialog = builder.create();
                dataPickerHtChooseDialog.show();
                break;
            case R.id.btSure:
                SPUtils.put(getContext(), "HtKuoPinYinZi", a1);
                SPUtils.put(getContext(), "HtKuoPinXinDao", a2);
                finishActivity();
                break;
        }
    }


}
