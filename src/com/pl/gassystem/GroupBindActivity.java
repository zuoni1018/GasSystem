package com.pl.gassystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.bll.CopyBiz;
import com.pl.bll.GroupBindBiz;
import com.pl.bll.SetBiz;
import com.pl.common.CustomProgressDialog;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.entity.CopyDataPhoto;
import com.pl.entity.GroupBind;
import com.pl.entity.GroupInfo;
import com.pl.gassystem.adapter.main.GroupBindAdapter;
import com.pl.gassystem.bean.ht.HtSendMessage;
import com.pl.gassystem.utils.JumpActivityUtils;
import com.pl.utils.GlobalConsts;

import java.util.ArrayList;

/**
 * 开始抄表界面
 */
public class GroupBindActivity extends Activity {
    private LinearLayout layout;
    private String groupNo;
    private String groupName;
    private String meterTypeNo;
    private GroupInfo gpInfo;
    private ListView lvGroupBind;
    private GroupBindBiz groupBindBiz;
    private GroupBindAdapter adapter;
    private TextView tvTitlebar_name;
    private ImageButton btnquit;
    private Button btnGroupBindAdd;
    private EditText etGroupBindMeterNo, etGroupBindMeterName;
    private CopyBiz copyBiz;
    private RadioGroup rgMeterWIType;
    private Button item0, item1, item2;
    private static final int MENU_CTX_DELETE = 2;
    private ArrayList<String> meterNos;
    private EditText search;
    private ImageView delete;
    private static CustomProgressDialog cpDialog;
    private TextView chek_all;
    private boolean isCheck = false;
    private TextView chek_all_tip;
    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    ArrayList<GroupBind> binds = adapter.getObjs();
                    if (!isNotAll(binds)) {

                        chek_all.setBackgroundResource(R.drawable.ck_false);
                        isCheck = false;
                    } else {
                        chek_all.setBackgroundResource(R.drawable.ck_true);
                        isCheck = true;
                    }
                    chek_all_tip.setText("共" + getCount(binds) + "户");
                    break;

                default:
                    break;
            }
        }
    };

    private int getCount(ArrayList<GroupBind> binds) {
        int count = 0;
        for (int i = 0; i < binds.size(); i++) {
            if (binds.get(i).isCheck()) {
                count++;
            }
        }
        return count;
    }

    private boolean isNotAll(ArrayList<GroupBind> binds) {
        for (int i = 0; i < binds.size(); i++) {
            if (binds.get(i).isCheck() == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_group_bind);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);
        chek_all_tip = (TextView) findViewById(R.id.chek_all_tip);
        search = (EditText) findViewById(R.id.search);
        gpInfo = (GroupInfo) getIntent().getSerializableExtra("GroupInfo");
        if (gpInfo != null) {
            groupNo = gpInfo.getGroupNo();
            groupName = gpInfo.getGroupName();
            meterTypeNo = gpInfo.getMeterTypeNo();
        } else {
            finish();
        }
        chek_all = (TextView) findViewById(R.id.chek_all);
        item0 = (Button) findViewById(R.id.item0);
        item1 = (Button) findViewById(R.id.item1);
        item2 = (Button) findViewById(R.id.item2);
        delete = (ImageView) findViewById(R.id.delete);
        layout = (LinearLayout) findViewById(R.id.layout);
        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
        tvTitlebar_name.setText(groupName);

        groupBindBiz = new GroupBindBiz(this);
        copyBiz = new CopyBiz(this);
        setupView();
        addListener();
        //添加按钮
        item0.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (layout.isShown()) {
                    layout.setVisibility(View.GONE);
                } else {
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                search.setText("");
                adapter.changeData(groupBindBiz.getGroupBindByGroupNo(groupNo));
            }
        });
        chek_all.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ArrayList<GroupBind> binds = adapter.getObjs();
                for (int i = 0; i < binds.size(); i++) {
                    if (isCheck) {

                        binds.get(i).setCheck(false);

                    } else {

                        binds.get(i).setCheck(true);

                    }
                }
                chek_all_tip.setText("共" + getCount(binds) + "户");
                if (isCheck) {
                    chek_all.setBackgroundResource(R.drawable.ck_false);
                    isCheck = false;
                } else {
                    chek_all.setBackgroundResource(R.drawable.ck_true);
                    isCheck = true;
                }
                adapter.changeData(binds);

            }
        });


        //开始抄表按钮
        item1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                meterNos = new ArrayList<>();
                ArrayList<GroupBind> binds = adapter.getObjs();
                int k = 0;
                for (int i = 0; i < binds.size(); i++) {
                    if (binds.get(i).isCheck()) {

                        GroupBind gpBind = adapter.getItem(i);
                        meterNos.add(gpBind.getMeterNo());
                        k++;
                    }
                }
                if (meterNos.size() > 0) {
                    final Intent intent = new Intent(GroupBindActivity.this, JumpActivityUtils.getCopyingActivity(GroupBindActivity.this));
                    intent.putExtra("meterNos", meterNos);
                    intent.putExtra("meterTypeNo", meterTypeNo);
                    intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);
                    intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);

                    //杭天表需要传的参数(这里没统一好)
                    intent.putExtra("bookNos",meterNos);
                    intent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_COPY_NORMAL);//输入命令指令
                    intent.putExtra("copyType", HtSendMessage.COPY_TYPE_GROUP);//群抄

                    if(new SetBiz(GroupBindActivity.this).getRunMode().equals(GlobalConsts.RUN_MODE_HANG_TIAN)){

                        AlertDialog.Builder builder = new AlertDialog.Builder(GroupBindActivity.this);
                        builder.setTitle("选择抄取的无线表类型");
                        final String[] cities = {"实时抄表", "抄取冻结量"};
                        builder.setItems(cities, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    intent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_COPY_NORMAL);
                                }else {
                                    intent.putExtra("commandType",HtSendMessage.COMMAND_TYPE_COPY_FROZEN);
                                }
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    }else {
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(GroupBindActivity.this, "尚未选中任何表具!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //删除按钮
        item2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (cpDialog == null) {
                    cpDialog = CustomProgressDialog
                            .createDialog(GroupBindActivity.this);
                }
                if (!cpDialog.isShowing()) {
                    cpDialog.show();
                }

                ArrayList<GroupBind> binds = adapter.getObjs();
                for (int i = 0; i < binds.size(); i++) {
                    if (binds.get(i).isCheck()) {
                        GroupBind gpBind = adapter.getItem(i);
                        int count = groupBindBiz.removeGroupBind(
                                gpBind.getMeterNo(), gpBind.getGroupNo());
                        /*
                         * if(count !=0){ adapter.removeItem(i); }
						 */
                    }

                }
                String txt = search.getText().toString();
                if (txt.length() == 0) {
                    adapter.changeData(groupBindBiz
                            .getGroupBindByGroupNo(groupNo));

                } else {
                    adapter.changeData(groupBindBiz.getGroupBindByGroupName(
                            groupNo, "%" + txt + "%"));
                }
                if (cpDialog != null && cpDialog.isShowing()) {
                    cpDialog.dismiss();
                    cpDialog = null;
                }

            }
        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String txt = arg0.toString();
                if (txt.length() > 1) {
                    System.out.println(groupNo + "-->" + txt);
                    adapter.changeData(groupBindBiz.getGroupBindByGroupName(groupNo, "%" + txt + "%"));

                } else {
                    if (txt.length() == 0) {
                        adapter.changeData(groupBindBiz.getGroupBindByGroupNo(groupNo));
                        delete.setVisibility(View.GONE);
                    } else {
                        delete.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void setupView() {
        lvGroupBind = (ListView) findViewById(R.id.lvGroupBind);
        ArrayList<GroupBind> groupBinds = groupBindBiz.getGroupBindByGroupNo(groupNo);

        adapter = new GroupBindAdapter(this, groupBinds, handler);
        lvGroupBind.setAdapter(adapter);
        btnquit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
        btnGroupBindAdd = (Button) findViewById(R.id.btnGroupBindAdd);
        etGroupBindMeterNo = (EditText) findViewById(R.id.etGroupBindMeterNo);
        etGroupBindMeterName = (EditText) findViewById(R.id.etGroupBindMeterName);
        rgMeterWIType = (RadioGroup) findViewById(R.id.rgMeterWIType);
    }

    private void addListener() {
        btnquit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lvGroupBind.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                        menu.setHeaderIcon(android.R.drawable.ic_dialog_info);
                        menu.add(1, MENU_CTX_DELETE, 2, "删除表具");
                    }
                });

        btnGroupBindAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String meterNo = etGroupBindMeterNo.getText().toString();
                int  needSize;
                SetBiz setBiz=new SetBiz(GroupBindActivity.this);
                if(setBiz.getRunMode().equals(GlobalConsts.RUN_MODE_HANG_TIAN)){
                    needSize=8;
                }else {
                    needSize=10;
                }
                if (meterNo.length() < needSize) {
                    Toast.makeText(getApplicationContext(), "通讯编号必须大于等于"+needSize+"位!", Toast.LENGTH_SHORT).show();
                } else {
                    GroupBind groupBind = new GroupBind();
                    groupBind.setGroupNo(groupNo);
                    groupBind.setMeterNo(etGroupBindMeterNo.getText().toString());
                    groupBind.setMeterName(etGroupBindMeterName.getText().toString());
                    String meterType = "";
                    if (rgMeterWIType.getCheckedRadioButtonId() == R.id.rdoLORA) {
                        meterType = "2";
                    } else if (rgMeterWIType.getCheckedRadioButtonId() == R.id.rdoFSK) {
                        meterType = "1";
                    }
                    groupBind.setMeterType(meterType);
                    groupBindBiz.addGroupBind(groupBind);
                    // 添加初始化数据
                    if (meterTypeNo.equals("04")) {// IC卡无线
                        CopyDataICRF copyDataICRF = new CopyDataICRF();
                        copyDataICRF.setMeterNo(etGroupBindMeterNo.getText().toString());
                        copyDataICRF.setCumulant("0.00");
                        copyDataICRF.setSurplusMoney("0");
                        copyDataICRF.setOverZeroMoney("0");
                        copyDataICRF.setBuyTimes(0);
                        copyDataICRF.setOverFlowTimes(0);
                        copyDataICRF.setMagAttTimes(0);
                        copyDataICRF.setCardAttTimes(0);
                        copyDataICRF.setCurrMonthTotal("0.00");
                        copyDataICRF.setLast1MonthTotal("0.00");
                        copyDataICRF.setLast2MonthTotal("0.00");
                        copyDataICRF.setLast3MonthTotal("0.00");
                        copyDataICRF.setMeterName(etGroupBindMeterName.getText().toString());
                        copyBiz.addCopyDataICRF(copyDataICRF);
                    } else if (meterTypeNo.equals("05")) {// 纯无线
                        CopyData copyData = new CopyData();
                        copyData.setMeterNo(etGroupBindMeterNo.getText().toString());
                        copyData.setLastShow("0.00");
                        copyData.setLastDosage("0");
                        copyData.setCurrentShow("0.00");
                        copyData.setCurrentDosage("0");
                        copyData.setUnitPrice("2.5");
                        copyData.setPrintFlag(0);
                        copyData.setMeterName(etGroupBindMeterName.getText().toString());
                        copyBiz.addCopyData(copyData);
                    } else if (meterTypeNo.equals("10")) {// 摄像表
                        CopyDataPhoto copyDataPhoto = new CopyDataPhoto();
                        copyDataPhoto.setCommunicateNo(etGroupBindMeterNo.getText().toString());
                        copyDataPhoto.setMeterName(etGroupBindMeterName.getText().toString());
                        copyBiz.addCopyDataPhoto(copyDataPhoto);
                    }
                    adapter.changeData(groupBindBiz.getGroupBindByGroupNo(groupNo));
//                    etGroupBindMeterNo.setText("");
//                    etGroupBindMeterName.setText("");
                }
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        GroupBind gpBind = adapter.getItem(info.position);
        switch (item.getItemId()) {
            case MENU_CTX_DELETE:
                int count = groupBindBiz.removeGroupBind(gpBind.getMeterNo(), gpBind.getGroupNo());
                if (count != 0) {
                    adapter.removeItem(info.position);
                }
                break;
        }

        return super.onContextItemSelected(item);
    }

}
