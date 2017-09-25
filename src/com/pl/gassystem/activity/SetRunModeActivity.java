package com.pl.gassystem.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.gassystem.utils.SPUtils;
import com.pl.bll.SetBiz;
import com.pl.gassystem.R;
import com.pl.gassystem.activity.base.BaseTitleActivity;
import com.pl.utils.GlobalConsts;
import com.zuoni.zuoni_common.dialog.other.DataPickerHtChooseDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * 设置抄表运行模式
 */
public class SetRunModeActivity extends BaseTitleActivity {

    @BindView(R.id.tvRunModeTipStandard)
    TextView tvRunModeTipStandard;
    @BindView(R.id.linRunModeStandard)
    LinearLayout linRunModeStandard;
    @BindView(R.id.tvRunModeTipLORA)
    TextView tvRunModeTipLORA;
    @BindView(R.id.linRunModeLORA)
    LinearLayout linRunModeLORA;
    @BindView(R.id.tvRunModeTipFSK)
    TextView tvRunModeTipFSK;
    @BindView(R.id.linRunModeFSK)
    LinearLayout linRunModeFSK;
    @BindView(R.id.tvRunModeTipHuiZhou)
    TextView tvRunModeTipHuiZhou;
    @BindView(R.id.linRunModeHuiZhou)
    LinearLayout linRunModeHuiZhou;
    @BindView(R.id.tvRunModeTipShangHai)
    TextView tvRunModeTipShangHai;
    @BindView(R.id.linRunModeShangHai)
    LinearLayout linRunModeShangHai;
    @BindView(R.id.tvRunModeTipPhoto)
    TextView tvRunModeTipPhoto;
    @BindView(R.id.linRunModePhoto)
    LinearLayout linRunModePhoto;
    @BindView(R.id.tvRunModeTipZHGT)
    TextView tvRunModeTipZHGT;
    @BindView(R.id.linRunModeZHGT)
    LinearLayout linRunModeZHGT;
    @BindView(R.id.tvYinZi)
    TextView tvYinZi;
    @BindView(R.id.tvXinDao)
    TextView tvXinDao;
    @BindView(R.id.tvModeChooseHt)
    TextView tvModeChooseHt;
    @BindView(R.id.layoutModeChooseHt)
    LinearLayout layoutModeChooseHt;


    private SetBiz setBiz;

    private DataPickerHtChooseDialog dataPickerHtChooseDialog;


    @Override
    protected int setLayout() {
        return R.layout.activity_set_run_mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("抄表运行模式");
        setBiz = new SetBiz(this);
        loadMode();

    }

    @OnClick({R.id.linRunModeStandard, R.id.linRunModeLORA, R.id.linRunModeFSK,
            R.id.linRunModeHuiZhou, R.id.linRunModeShangHai, R.id.linRunModePhoto,
            R.id.linRunModeZHGT, R.id.layoutModeChooseHt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linRunModeStandard:
                setBiz.updateRunMode("1");
                loadMode();
                break;
            case R.id.linRunModeLORA:
                setBiz.updateRunMode("2");
                loadMode();
                break;
            case R.id.linRunModeFSK:
                setBiz.updateRunMode("3");
                loadMode();
                break;
            case R.id.linRunModeHuiZhou:
                setBiz.updateRunMode("4");
                loadMode();
                break;
            case R.id.linRunModeShangHai:
                setBiz.updateRunMode("5");
                loadMode();
                break;
            case R.id.linRunModePhoto:
                setBiz.updateRunMode("6");
                loadMode();
                break;
            case R.id.linRunModeZHGT:
                setBiz.updateRunMode("7");
                loadMode();
                break;
            case R.id.layoutModeChooseHt:
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
                        setBiz.updateRunMode(GlobalConsts.RUN_MODE_HANG_TIAN);
                        loadMode();
                        tvYinZi.setText(itemValue);
                        SPUtils.put(getContext(), "HtKuoPinYinZi", itemValue);
                    }

                    @Override
                    public void onDataSelected2(String itemValue) {
                        tvXinDao.setText(itemValue);
                        SPUtils.put(getContext(), "HtKuoPinXinDao", itemValue);
                    }
                });

                dataPickerHtChooseDialog = builder.create();
                dataPickerHtChooseDialog.show();
                break;
        }
    }

    private void loadMode() {
        String runMode = setBiz.getRunMode();
        //把所有勾都取消
        tvRunModeTipStandard.setVisibility(View.INVISIBLE);
        tvRunModeTipHuiZhou.setVisibility(View.INVISIBLE);
        tvRunModeTipFSK.setVisibility(View.INVISIBLE);
        tvRunModeTipLORA.setVisibility(View.INVISIBLE);
        tvRunModeTipShangHai.setVisibility(View.INVISIBLE);
        tvRunModeTipPhoto.setVisibility(View.INVISIBLE);
        tvRunModeTipZHGT.setVisibility(View.INVISIBLE);
        tvModeChooseHt.setVisibility(View.INVISIBLE);
        //给当前模式打勾
        switch (runMode) {
            case GlobalConsts.RUN_MODE_STANDARD:
                tvRunModeTipStandard.setVisibility(View.VISIBLE);
                break;
            case GlobalConsts.RUN_MODE_HUI_ZHOU:
                tvRunModeTipHuiZhou.setVisibility(View.VISIBLE);
                break;
            case GlobalConsts.RUN_MODE_LORA:
                tvRunModeTipLORA.setVisibility(View.VISIBLE);
                break;
            case GlobalConsts.RUN_MODE_FSK:
                tvRunModeTipFSK.setVisibility(View.VISIBLE);
                break;
            case GlobalConsts.RUN_MODE_SHANGHAI:
                tvRunModeTipShangHai.setVisibility(View.VISIBLE);
                break;
            case GlobalConsts.RUN_MODE_PHOTO:
                tvRunModeTipPhoto.setVisibility(View.VISIBLE);
                break;
            case GlobalConsts.RUN_MODE_ZHGT:
                tvRunModeTipZHGT.setVisibility(View.VISIBLE);
                break;
            case GlobalConsts.RUN_MODE_HANG_TIAN:
                tvModeChooseHt.setVisibility(View.VISIBLE);
                break;
        }
        tvYinZi.setText((String) SPUtils.get(getContext(), "HtKuoPinYinZi", "09"));
        tvXinDao.setText((String) SPUtils.get(getContext(), "HtKuoPinXinDao", "14"));
    }
}
