package com.pl.gassystem.activity.ct;

import android.os.Bundle;
import android.widget.TextView;

import com.pl.gassystem.bean.ct.CtCopyData;
import com.pl.gassystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class CtCopyDataBookDetailActivity extends CtBaseTitleActivity {
    @BindView(R.id.tvCopyDataDetailMeterNo)
    TextView tvCopyDataDetailMeterNo;
    @BindView(R.id.tvCopyDataDetailMeterName)
    TextView tvCopyDataDetailMeterName;
    @BindView(R.id.tvCopyDataDetailCurrentShow)
    TextView tvCopyDataDetailCurrentShow;
    @BindView(R.id.tvCopyDataDetailCurrentDosage)
    TextView tvCopyDataDetailCurrentDosage;
    @BindView(R.id.tvCopyDataDetailLastShow)
    TextView tvCopyDataDetailLastShow;
    @BindView(R.id.tvCopyDataDetailLastDosage)
    TextView tvCopyDataDetailLastDosage;
    @BindView(R.id.tvCopyDataDetailCopyWay)
    TextView tvCopyDataDetailCopyWay;
    @BindView(R.id.tvCopyDataDetailCopyState)
    TextView tvCopyDataDetailCopyState;
    @BindView(R.id.tvCopyDataDetailElec)
    TextView tvCopyDataDetailElec;
    @BindView(R.id.tvCopyDataDetailDbm)
    TextView tvCopyDataDetailDbm;
    @BindView(R.id.tvCopyDataDetailCopyMan)
    TextView tvCopyDataDetailCopyMan;
    @BindView(R.id.tvCopyDataDetailCopyTime)
    TextView tvCopyDataDetailCopyTime;
    @BindView(R.id.tvCopyDataDetailRemark)
    TextView tvCopyDataDetailRemark;
    @BindView(R.id.tvCopyDataDetailMeterState)
    TextView tvCopyDataDetailMeterState;

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_copydata_book_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("表具详情信息");

        CtCopyData info= (CtCopyData) getIntent().getSerializableExtra("info");
        if (info!=null){
            tvCopyDataDetailMeterNo.setText(info.getCommunicateNo());
            tvCopyDataDetailMeterName.setText(info.getMeterName());
            tvCopyDataDetailCurrentShow.setText(info.getCurrentShow());
            tvCopyDataDetailCurrentDosage.setText(info.getCurrentDosage()+"");
            tvCopyDataDetailCopyWay.setText(info.getCopyWay());
            tvCopyDataDetailCopyState.setText(info.getCopyState()+"");
            tvCopyDataDetailElec.setText(info.getElec());
            tvCopyDataDetailDbm.setText(info.getdBm());
            tvCopyDataDetailCopyTime.setText(info.getCopyTime());
        }



    }
}
