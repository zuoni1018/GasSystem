package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.pl.bll.SetBiz;
import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtSendMessage;
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

public class HtSetBookParameterActivity extends HtBaseTitleActivity {


    @BindView(R.id.tvChoose)
    TextView tvChoose;
    @BindView(R.id.tvKuoPinYinZi)
    TextView tvKuoPinYinZi;
    @BindView(R.id.tvKuoPinXinDao)
    TextView tvKuoPinXinDao;
    @BindView(R.id.etSheZhiDongJieRi)
    EditText etSheZhiDongJieRi;
    @BindView(R.id.etKaiChuangShiJian)
    EditText etKaiChuangShiJian;
    @BindView(R.id.rbNeed)
    CheckBox rbNeed;
    @BindView(R.id.btSure)
    Button btSure;
    @BindView(R.id.tvNum)
    TextView tvNum;
    private ArrayList<String> bookNos;
    private DataPickerHtChooseDialog dataPickerHtChooseDialog;
    private SetBiz setBiz;


    private String newYinZi="09";
    private String newXinDao="14";


    @Override
    protected int setLayout() {
        return R.layout.ht_activity_set_book_parameter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("批量设置参数");
        setBiz = new SetBiz(getContext());
        bookNos = getIntent().getStringArrayListExtra("bookNos");
        if(bookNos!=null){
            tvNum.setText("您选择了"+bookNos.size()+"张燃气表");
        }

    }


    @OnClick({R.id.tvChoose, R.id.btSure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvChoose:
                //默认设置成 14 09
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
                        newYinZi=itemValue;

                    }

                    @Override
                    public void onDataSelected2(String itemValue) {
                        tvKuoPinXinDao.setText(itemValue);
                        newXinDao=itemValue;
                    }
                });

                dataPickerHtChooseDialog = builder.create();
                dataPickerHtChooseDialog.show();
                break;
            case R.id.btSure:

                Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
                mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_SET_PARAMETER);//命令类型
                mIntent.putStringArrayListExtra("bookNos", bookNos);//操作表
                mIntent.putExtra("yinzi","09");//扩频因子
                mIntent.putExtra("xindao","14");//扩频信道
                mIntent.putExtra("dongjieri","0000");//设置冻结日
                mIntent.putExtra("kaichuangshijian","0023");//开窗起止时间
                mIntent.putExtra("isSetYinZi",false);
                startActivity(mIntent);
                break;
        }
    }
}
