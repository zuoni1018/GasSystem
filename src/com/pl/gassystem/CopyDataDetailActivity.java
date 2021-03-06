package com.pl.gassystem;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pl.bean.UserInfo;
import com.pl.bll.CopyBiz;
import com.pl.bll.SetBiz;
import com.pl.dal.userinfo.UserInfoDao;
import com.pl.entity.CopyData;
import com.pl.utils.GlobalConsts;
import com.pl.utils.MeterType;

public class CopyDataDetailActivity extends Activity {

    private TextView tvTitlebar_name;
    private TextView tvCopyDataDetailMeterNo, tvCopyDataDetailMeterName,
            tvCopyDataDetailCurrentShow, tvCopyDataDetailCurrentDosage,
            tvCopyDataDetailLastShow, tvCopyDataDetailLastDosage,
            tvCopyDataDetailCopyWay, tvCopyDataDetailCopyState,
            tvCopyDataDetailCopyTime, tvCopyDataDetailCopyMan,
            tvCopyDataDetailElec, tvCopyDataDetailDbm, tvCopyDataDetailRemark,
            tvCopyDataDetailMeterState;
    private CopyBiz copyBiz;
    private ImageButton btnquit;
    private SetBiz setBiz;
    private static String runMode;
    private TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_copydata_detail);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_listview);
        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_name);
        tvTitlebar_name.setText("表具详细信息");

        copyBiz = new CopyBiz(this);
        setBiz = new SetBiz(this);
        runMode = setBiz.getRunMode();

        setupView();
        bindData();
        addListener();

    }

    private void setupView() {
        btnquit = (ImageButton) findViewById(R.id.btnquit);
        tvCopyDataDetailMeterNo = (TextView) findViewById(R.id.tvCopyDataDetailMeterNo);
        tvCopyDataDetailMeterName = (TextView) findViewById(R.id.tvCopyDataDetailMeterName);
        tvCopyDataDetailCurrentShow = (TextView) findViewById(R.id.tvCopyDataDetailCurrentShow);
        tvCopyDataDetailCurrentDosage = (TextView) findViewById(R.id.tvCopyDataDetailCurrentDosage);
        tvCopyDataDetailLastShow = (TextView) findViewById(R.id.tvCopyDataDetailLastShow);
        tvCopyDataDetailLastDosage = (TextView) findViewById(R.id.tvCopyDataDetailLastDosage);
        // tvCopyDataDetailUnitPrice = (TextView)
        // findViewById(R.id.tvCopyDataDetailUnitPrice);
        // tvCopyDataDetailPrintFlag = (TextView)
        // findViewById(R.id.tvCopyDataDetailPrintFlag);
        tvCopyDataDetailCopyWay = (TextView) findViewById(R.id.tvCopyDataDetailCopyWay);
        tvCopyDataDetailCopyState = (TextView) findViewById(R.id.tvCopyDataDetailCopyState);
        tvCopyDataDetailCopyTime = (TextView) findViewById(R.id.tvCopyDataDetailCopyTime);
        tvCopyDataDetailCopyMan = (TextView) findViewById(R.id.tvCopyDataDetailCopyMan);
        tvCopyDataDetailElec = (TextView) findViewById(R.id.tvCopyDataDetailElec);
        tvCopyDataDetailDbm = (TextView) findViewById(R.id.tvCopyDataDetailDbm);
        // tvCopyDataDetailIsBalance = (TextView)
        // findViewById(R.id.tvCopyDataDetailIsBalance);
        tvCopyDataDetailRemark = (TextView) findViewById(R.id.tvCopyDataDetailRemark);
        tvCopyDataDetailMeterState = (TextView) findViewById(R.id.tvCopyDataDetailMeterState);

        tvPhone= (TextView) findViewById(R.id.tvPhone);
    }

    private void bindData() {
        int Id = getIntent().getIntExtra("Id", 0);
        CopyData copyData = copyBiz.getCopyDataById(Id + "");
        tvCopyDataDetailMeterNo.setText(copyData.getMeterNo());
        tvCopyDataDetailMeterName.setText(copyData.getMeterName());
        tvCopyDataDetailCurrentShow.setText(copyData.getCurrentShow());
        tvCopyDataDetailCurrentDosage.setText(copyData.getCurrentDosage());
        tvCopyDataDetailLastShow.setText(copyData.getLastShow());
        tvCopyDataDetailLastDosage.setText(copyData.getLastDosage());
        // tvCopyDataDetailUnitPrice.setText(copyData.getUnitPrice());
        // tvCopyDataDetailPrintFlag.setText(MeterType.GetPrintFlag(copyData.getPrintFlag()));
        tvCopyDataDetailCopyWay.setText(MeterType.GetCopyWay(copyData
                .getCopyWay()));
        tvCopyDataDetailCopyState.setText(MeterType.GetCopyState(copyData
                .getCopyState()));
        tvCopyDataDetailCopyTime.setText(copyData.getCopyTime());
        tvCopyDataDetailCopyMan.setText(copyData.getCopyMan());
        // 剩余电量
        String elec = copyData.getElec();
        elec=elec.replace(".","");
        elec=elec.replace("V","");
        if (elec != null &!"".equals(elec)) {
            elec = elec.substring(0, 1) + "." + elec.substring(1) + "V";
            tvCopyDataDetailElec.setText(elec);
        }
        // 信号强度
        String dBm = copyData.getdBm();
        if (dBm != null&!"".equals(dBm)) {
            if(!"".equals(dBm.trim())){
                try {
                    int dbm_1 = Integer.parseInt(dBm) + 25;
                    if (dbm_1 < -100) {
                        dbm_1 = -100;
                    }
                    tvCopyDataDetailDbm.setText(100 + dbm_1 + "%");
                }catch (NumberFormatException e){
                    tvCopyDataDetailDbm.setText(dBm);
                }
            }
        }
        // tvCopyDataDetailIsBalance.setText(MeterType.GetIsBalance(copyData.getIsBalance()));
        tvCopyDataDetailRemark.setText(copyData.getRemark());
        // 表具状态
        String state = "";
        if (runMode.equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) { // 惠州FSK
            state = MeterType.GetHuiZhouWIMeterStateMsg(copyData
                    .getMeterState());
        } else {
            state = MeterType.GetWIMeterStateMsg(copyData.getMeterState());
        }
        if (!state.equals("正常")) {
            tvCopyDataDetailMeterState.setTextColor(Color.RED);
        }
        tvCopyDataDetailMeterState.setText(state);

        UserInfoDao userInfoDao = new UserInfoDao(CopyDataDetailActivity.this);
        UserInfo userInfo=userInfoDao.getUserInfo(copyData.getMeterNo());
        if(userInfo!=null){
            tvPhone.setText(userInfo.getUserPhone());
        }

//        tvPhone.setText(copyData.get);
    }

    private void addListener() {
        btnquit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
