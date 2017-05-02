package com.pl.gassystem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.bll.BookInfoBiz;
import com.pl.entity.BookInfo;
import com.pl.utils.GlobalConsts;
import com.pl.utils.StringFormatter;

/**
 * �½��˲����
 */
public class BookInfoUpdateActivity extends Activity {

    private TextView tvBookNo;
    private EditText etBookName, etestateNo, etstaffNo, etRemark;
    private RadioGroup rgMeterType;
    private Button btnSubmit, btnQuit;
    private BookInfoBiz bookInfoBiz;
    private int opType;
    private TextView tvTitleBarName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_book_info_update);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_main);
        bookInfoBiz = new BookInfoBiz(this);
        opType = getIntent().getIntExtra(GlobalConsts.EXTRA_BOOKINFO_OP_TYPE, GlobalConsts.TYPE_ADD);
        setupview();
        addListener();
    }

    private void setupview() {
        tvBookNo = (TextView) findViewById(R.id.tvBookNo);
        etBookName = (EditText) findViewById(R.id.etBookName);
        etestateNo = (EditText) findViewById(R.id.etestateNo);
        etstaffNo = (EditText) findViewById(R.id.etstaffNo);
        etRemark = (EditText) findViewById(R.id.etRemark);
        rgMeterType = (RadioGroup) findViewById(R.id.rgMeterType);
        btnSubmit = (Button) findViewById(R.id.btnBkUpSubmit);
        btnQuit = (Button) findViewById(R.id.btnBkUpQuit);
        tvTitleBarName = (TextView) findViewById(R.id.tvMainTitleBarName);

        switch (opType) {
            case GlobalConsts.TYPE_ADD:
                btnSubmit.setText("�ύ");
                tvTitleBarName.setText("�½��˲�");
                break;
            case GlobalConsts.TYPE_UPDATE:
                btnSubmit.setText("�޸�");
                tvTitleBarName.setText("�޸��˲�");
                BookInfo bkInfo = (BookInfo) getIntent().getSerializableExtra(GlobalConsts.EXTRA_BOOKINFO_OP_DATA);
                tvBookNo.setText(bkInfo.getBookNo());
                etBookName.setText(bkInfo.getBookName());
                etestateNo.setText(bkInfo.getEstateNo());
                etstaffNo.setText(bkInfo.getStaffNo());
                etRemark.setText(bkInfo.getRemark());
                if ("04".equals(bkInfo.getMeterTypeNo())) {
                    rgMeterType.check(R.id.rdoIC);
                } else if ("05".equals(bkInfo.getMeterTypeNo())) {
                    rgMeterType.check(R.id.rdoWX);
                }
                break;
        }
    }

    private void addListener() {

        //�ύ��ť����¼�
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String bookName = etBookName.getText().toString().trim();
                String remark = etRemark.getText().toString().trim();
                String etestateNo2 = etestateNo.getText().toString().trim();
                String etstaffNo2 =   etstaffNo.getText().toString().trim();

                if ("".equals(bookName)) {
                    Toast.makeText(BookInfoUpdateActivity.this, "���������˲�����", Toast.LENGTH_SHORT).show();
                } else if ("".equals(etstaffNo2)) {
                    Toast.makeText(BookInfoUpdateActivity.this, "����������", Toast.LENGTH_SHORT).show();
                } else if ("".equals(etestateNo2)) {
                    Toast.makeText(BookInfoUpdateActivity.this, "������С�����", Toast.LENGTH_SHORT).show();
                } else {
                    // ��ȡ����
                    BookInfo bkInfo = new BookInfo();
                    bkInfo.setBookName(bookName);
                    bkInfo.setEstateNo(etestateNo2);
                    bkInfo.setRemark(remark);
                    bkInfo.setStaffNo(remark);
                    //����˲�����
                    String meterType = "";
                    if (rgMeterType.getCheckedRadioButtonId() == R.id.rdoIC) {
                        meterType = "04";
                    } else if (rgMeterType.getCheckedRadioButtonId() == R.id.rdoWX) {
                        meterType = "05";
                    }
                    bkInfo.setMeterTypeNo(meterType);
                    // ���� �½��˵��������ݿ����½�
                    switch (opType) {
                        case GlobalConsts.TYPE_ADD:
                            String bookNo = bookInfoBiz.getLastBookNo();
                            bkInfo.setBookNo(StringFormatter.getAddStringNo(bookNo));
                            bookInfoBiz.addBookInfo(bkInfo);
                            break;

                        case GlobalConsts.TYPE_UPDATE:
                            bkInfo.setBookNo(tvBookNo.getText().toString());
                            bookInfoBiz.updateBookInfo(bkInfo);
                            break;
                    }
                    finish();
                }
            }
        });

        btnQuit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
