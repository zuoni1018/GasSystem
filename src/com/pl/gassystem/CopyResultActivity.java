package com.pl.gassystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pl.gassystem.adapter.main.CopyDataAdapter;
import com.pl.gassystem.adapter.main.CopyDataICRFAdapter;
import com.pl.bll.CopyBiz;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.utils.GlobalConsts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CopyResultActivity extends Activity {

    private TextView tvTitlebar_name;
    private ImageButton btnquit, btnmenu;
    private ArrayList<String> meterNos;
    private String meterTypeNo;
    private CopyBiz copyBiz;
    private ListView lvCopyResult;
    private int copyState;

    private CopyDataICRFAdapter copyDataICRFAdapter;
    private CopyDataAdapter copyDataAdapter;
    private boolean isCopy;
    private EditText search;
    private ImageView delete;
    private TextView tvCopyResultCount;


    private Button btGoCopy;

    private void setupView() {
        btGoCopy = (Button) findViewById(R.id.btGoCopy);
        btnquit = (ImageButton) findViewById(R.id.btnquit);
        btnmenu = (ImageButton) findViewById(R.id.btnmenu);
        search = (EditText) findViewById(R.id.search);
        meterNos = getIntent().getStringArrayListExtra("meterNos");
        meterTypeNo = getIntent().getStringExtra("meterTypeNo").toString();
        delete = (ImageView) findViewById(R.id.delete);
        tvCopyResultCount = (TextView) findViewById(R.id.tvCopyResultCount);
        lvCopyResult = (ListView) findViewById(R.id.lvCopyResult);
        if (meterTypeNo.equals("04")) {// IC������
            ArrayList<CopyDataICRF> copyDataICRFs = copyBiz.getCopyDataICRFByMeterNos(meterNos, copyState);
            //��������
            Collections.sort(copyDataICRFs, new Comparator<CopyDataICRF>() {

                public int compare(CopyDataICRF arg0, CopyDataICRF arg1) {
                    return arg0.getMeterNo().compareTo(arg1.getMeterNo());
                }
            });

            copyDataICRFAdapter = new CopyDataICRFAdapter(this, copyDataICRFs);
            lvCopyResult.setAdapter(copyDataICRFAdapter);
            tvCopyResultCount.setText("��" + copyDataICRFAdapter.getCount() + "��");
        } else if (meterTypeNo.equals("05")) {// ������
            ArrayList<CopyData> copyDatas = copyBiz.getCopyDataByMeterNos(meterNos, copyState);


            Collections.sort(copyDatas, new Comparator<CopyData>() {

                public int compare(CopyData arg0, CopyData arg1) {
                    return arg0.getMeterNo().compareTo(arg1.getMeterNo());
                }
            });

            copyDataAdapter = new CopyDataAdapter(this, copyDatas);
            lvCopyResult.setAdapter(copyDataAdapter);
            tvCopyResultCount.setText("��" + copyDataAdapter.getCount() + "��");
        }

        btGoCopy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meterTypeNo.equals("04")) {// IC������
                    List<CopyDataICRF> getCopyDataICRFs = copyDataICRFAdapter.getCopyDataICRFs();
                    ArrayList<String> meterNos2 = new ArrayList<>();
                    for (int i = 0; i < getCopyDataICRFs.size(); i++) {
                        if (getCopyDataICRFs.get(i).isChoose()) {
                            meterNos2.add(getCopyDataICRFs.get(i).getMeterNo());
                        }
                    }
                    Intent intent = new Intent(CopyResultActivity.this, CopyingActivity.class);
                    intent.putExtra("meterNos", meterNos2);
                    intent.putExtra("meterTypeNo", "04");
                    intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);
                    intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
                    startActivity(intent);

                } else {
                    List<CopyData> copyDatas=copyDataAdapter.getCopyDatas();
                    ArrayList<String> meterNos2 = new ArrayList<>();
                    for (int i = 0; i < copyDatas.size(); i++) {
                        if (copyDatas.get(i).isChoose()) {
                            meterNos2.add(copyDatas.get(i).getMeterNo());
                        }
                    }
                    Intent intent = new Intent(CopyResultActivity.this, CopyingActivity.class);
                    intent.putExtra("meterNos", meterNos2);
                    intent.putExtra("meterTypeNo", "05");
                    intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);
                    intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
                    startActivity(intent);
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

                    if (meterTypeNo.equals("04")) {// IC������
                        copyDataICRFAdapter.changeData(copyBiz.getCopyDataICRFByMeterNos_N(meterNos, copyState, "'%" + txt + "%'"));
                        tvCopyResultCount.setText("��" + copyDataICRFAdapter.getCount() + "��");
                    } else if (meterTypeNo.equals("05")) {// ������
                        copyDataAdapter.changeData(copyBiz.getCopyDataByMeterNos_N(meterNos, copyState, "'%" + txt + "%'"));
                        tvCopyResultCount.setText("��" + copyDataAdapter.getCount() + "��");
                    }
                } else {
                    if (txt.length() == 0) {
                        if (meterTypeNo.equals("04")) {// IC������
                            copyDataICRFAdapter.changeData(copyBiz.getCopyDataICRFByMeterNos(meterNos, copyState));
                            tvCopyResultCount.setText("��" + copyDataICRFAdapter.getCount() + "��");
                        } else if (meterTypeNo.equals("05")) {// ������
                            copyDataAdapter.changeData(copyBiz.getCopyDataByMeterNos(meterNos, copyState));
                            tvCopyResultCount.setText("��" + copyDataAdapter.getCount() + "��");
                        }
                        delete.setVisibility(View.GONE);
                    } else {
                        delete.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                search.setText("");
                if (meterTypeNo.equals("04")) {// IC������
                    copyDataICRFAdapter.changeData(copyBiz.getCopyDataICRFByMeterNos(meterNos, copyState));
                    tvCopyResultCount.setText("��" + copyDataICRFAdapter.getCount() + "��");
                } else if (meterTypeNo.equals("05")) {// ������
                    copyDataAdapter.changeData(copyBiz.getCopyDataByMeterNos(meterNos, copyState));
                    tvCopyResultCount.setText("��" + copyDataAdapter.getCount() + "��");
                }
            }
        });
    }

    // @Override
    // protected void onRestart() {
    // // TODO �Զ����ɵķ������
    // super.onRestart();
    // if(meterTypeNo.equals("04")){
    // copyDataICRFAdapter.changeData(copyBiz.getCopyDataICRFByMeterNos(meterNos,
    // copyState));
    // }else if (meterTypeNo.equals("05")) {
    // copyDataAdapter.changeData(copyBiz.getCopyDataByMeterNos(meterNos,copyState));
    // }
    // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_copy_result);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.titlebar_listview);
        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_name);
        int resultType = getIntent().getIntExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, 0);
        if (resultType == GlobalConsts.RE_TYPE_COPY) {// ������ɺ���ת������ʾ�ѳ�����
            tvTitlebar_name.setText("�������");
            copyState = 1;
            isCopy = false;
        } else if (resultType == GlobalConsts.RE_TYPE_SHOWUNCOPY) {// ֱ����ʾδ������
            tvTitlebar_name.setText("��ʾδ��");
            copyState = 0;
            isCopy = false;
        } else if (resultType == GlobalConsts.RE_TYPE_SHOWALL) {// ֱ����ʾȫ������
            tvTitlebar_name.setText("��ʾȫ��");
            copyState = 2;
            isCopy = false;
        } else if (resultType == GlobalConsts.RE_TYPE_SELECTUNCOPY) {// ����ѡȡδ������
            tvTitlebar_name.setText("ѡȡδ��");
            copyState = 0;
            isCopy = true;
        } else if (resultType == GlobalConsts.RE_TYPE_SELECTCOPY) {// ����ѡȡ�ѳ�����
            tvTitlebar_name.setText("ѡȡ�ѳ�");
            copyState = 1;
            isCopy = true;
        } else if (resultType == GlobalConsts.RE_TYPE_SELECTALL) {// ����ѡȡȫ������
            tvTitlebar_name.setText("��ʾȫ��");
            copyState = 2;
            isCopy = true;
        } else {
            tvTitlebar_name.setText("����ҳ��");
        }

        copyBiz = new CopyBiz(this);

        setupView();
        addListener();
    }

    private void addListener() {
        btnquit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lvCopyResult.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (isCopy) {// ��������ת���������
                    if (meterTypeNo.equals("04")) {
                        CopyDataICRF copyDataICRF = copyDataICRFAdapter.getItem(position);
                        Intent intent = new Intent(CopyResultActivity.this, CopyingActivity.class);
                        meterNos.clear();
                        meterNos.add(copyDataICRF.getMeterNo());
                        intent.putExtra("meterNos", meterNos);
                        intent.putExtra("meterTypeNo", meterTypeNo);
                        intent.putExtra("copyType", GlobalConsts.COPY_TYPE_SINGLE);
                        intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
                        startActivity(intent);
                    } else if (meterTypeNo.equals("05")) {
                        CopyData copyData = copyDataAdapter.getItem(position);
                        Intent intent = new Intent(CopyResultActivity.this, CopyingActivity.class);
                        meterNos.clear();
                        meterNos.add(copyData.getMeterNo());
                        intent.putExtra("meterNos", meterNos);
                        intent.putExtra("meterTypeNo", meterTypeNo);
                        intent.putExtra("copyType", GlobalConsts.COPY_TYPE_SINGLE);
                        intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
                        startActivity(intent);
                    }
                    finish(); // �رյ�ǰҳ��.
                } else {// �鿴����ת����ϸ����
                    if (meterTypeNo.equals("04")) {
                        CopyDataICRF copyDataICRF = copyDataICRFAdapter.getItem(position);
                        Intent intent = new Intent(CopyResultActivity.this, CopyDataICRFDetailActivity.class);
                        intent.putExtra("Id", copyDataICRF.getId());
                        startActivity(intent);
                    } else if (meterTypeNo.equals("05")) {
                        CopyData copyData = copyDataAdapter.getItem(position);
                        Intent intent = new Intent(CopyResultActivity.this, CopyDataDetailActivity.class);
                        intent.putExtra("Id", copyData.getId());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.copy_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
