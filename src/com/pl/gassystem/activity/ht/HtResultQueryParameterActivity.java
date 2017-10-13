package com.pl.gassystem.activity.ht;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtGetMessageQueryParameter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/10/13
 */

public class HtResultQueryParameterActivity extends HtBaseTitleActivity {
    @BindView(R.id.MLLX)
    TextView MLLX;
    @BindView(R.id.ID)
    TextView ID;
    @BindView(R.id.KPXD)
    TextView KPXD;
    @BindView(R.id.KPYZ)
    TextView KPYZ;
    @BindView(R.id.DJR)
    TextView DJR;
    @BindView(R.id.KCQZSJ)
    TextView KCQZSJ;
    @BindView(R.id.BJZT)
    TextView BJZT;
    @BindView(R.id.DYZ)
    TextView DYZ;
    @BindView(R.id.RJBBH)
    TextView RJBBH;
    @BindView(R.id.BJDQSJ)
    TextView BJDQSJ;
    @BindView(R.id.MMBBH)
    TextView MMBBH;
    @BindView(R.id.MM)
    TextView MM;
    @BindView(R.id.SCCSSZKZW)
    TextView SCCSSZKZW;
    @BindView(R.id.SBBXD)
    TextView SBBXD;
    @BindView(R.id.SBBFPYZ)
    TextView SBBFPYZ;
    @BindView(R.id.SBBDJR)
    TextView SBBDJR;
    @BindView(R.id.SBBKCQZSJ)
    TextView SBBKCQZSJ;
    @BindView(R.id.SCCSSZSJ)
    TextView SCCSSZSJ;
    @BindView(R.id.layoutOther)
    LinearLayout layoutOther;
    @BindView(R.id.SBBMYBBH)
    TextView SBBMYBBH;
    @BindView(R.id.SBBMY)
    TextView SBBMY;
    @BindView(R.id.SCMYSZSJ)
    TextView SCMYSZSJ;
    @BindView(R.id.CBCS)
    TextView CBCS;

    private HtGetMessageQueryParameter htGetMessageQueryParameter;
    @Override
    protected int setLayout() {
        return R.layout.ht_activity_result_query_parameter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("查询表计参数");
        htGetMessageQueryParameter= (HtGetMessageQueryParameter) getIntent().getSerializableExtra("HtGetMessageQueryParameter");

        String CommandType=getIntent().getStringExtra("CommandType");
        String BookNo=getIntent().getStringExtra("BookNo");


        MLLX.setText(CommandType);
        ID.setText(BookNo);

        KPXD.setText(htGetMessageQueryParameter.getKuo_pin_xin_dao());
        KPYZ.setText(htGetMessageQueryParameter.getKuo_pin_yin_zi());
        DJR.setText(htGetMessageQueryParameter.getDong_jie_ri());

        KCQZSJ.setText(htGetMessageQueryParameter.getKai_chuang_qi_zhi_shi_jian());
        BJZT.setText(htGetMessageQueryParameter.getBiao_ju_zhuang_tai());
        DYZ.setText(htGetMessageQueryParameter.getVoltage());
        RJBBH.setText(htGetMessageQueryParameter.getRuan_jian_ban_ben_hao());
        BJDQSJ.setText(htGetMessageQueryParameter.getBiao_ju_dang_qian_shi_jian());
        MMBBH.setText(htGetMessageQueryParameter.getMi_ma_ban_ben_hao());
        MM.setText(htGetMessageQueryParameter.getMi_ma());
        SCCSSZKZW.setText(htGetMessageQueryParameter.getShang_ci_can_shu_kong_zhi_wei());
        SBBXD.setText(htGetMessageQueryParameter.getShang_ban_ben_xin_dao());
        SBBFPYZ.setText(htGetMessageQueryParameter.getShang_ban_ben_fen_pin_yi_zi());
        SBBDJR.setText(htGetMessageQueryParameter.getShang_ban_ben_dong_jie_ri());
        SBBKCQZSJ.setText(htGetMessageQueryParameter.getShang_ban_ben_kai_chuang_qi_zhi_shi_jian());


        SCCSSZSJ.setText(htGetMessageQueryParameter.getShang_ban_ben_can_shu_she_zhi_shi_jian());
        SBBMYBBH.setText(htGetMessageQueryParameter.getShang_ban_ben_mi_yao_ban_ben_hao());
        SBBMY.setText(htGetMessageQueryParameter.getShang_ban_ben_mi_yao());
        SCMYSZSJ.setText(htGetMessageQueryParameter.getShang_ci_mi_yao_she_zhi_shi_jian());
        CBCS.setText(htGetMessageQueryParameter.getChao_biao_ci_shu());
    }

    @OnClick(R.id.bt)
    public void onViewClicked() {
        finishActivity();
    }
}
