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
 * 新建账册界面
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
                btnSubmit.setText("提交");
                tvTitleBarName.setText("新建账册");
                break;
            case GlobalConsts.TYPE_UPDATE:
                btnSubmit.setText("修改");
                tvTitleBarName.setText("修改账册");
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

        //提交按钮点击事件
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String bookName = etBookName.getText().toString().trim();
                String remark = etRemark.getText().toString().trim();
                String etestateNo2 = etestateNo.getText().toString().trim();
                String etstaffNo2 =   etstaffNo.getText().toString().trim();

                if ("".equals(bookName)) {
                    Toast.makeText(BookInfoUpdateActivity.this, "请先输入账册名称", Toast.LENGTH_SHORT).show();
                } else if ("".equals(etstaffNo2)) {
                    Toast.makeText(BookInfoUpdateActivity.this, "请输入表工编号", Toast.LENGTH_SHORT).show();
                } else if ("".equals(etestateNo2)) {
                    Toast.makeText(BookInfoUpdateActivity.this, "请输入小区编号", Toast.LENGTH_SHORT).show();
                } else {
                    // 获取输入
                    BookInfo bkInfo = new BookInfo();
                    bkInfo.setBookName(bookName);
                    bkInfo.setEstateNo(etestateNo2);
                    bkInfo.setRemark(remark);
                    bkInfo.setStaffNo(remark);
                    //获得账册类型
                    String meterType = "";
                    if (rgMeterType.getCheckedRadioButtonId() == R.id.rdoIC) {
                        meterType = "04";
                    } else if (rgMeterType.getCheckedRadioButtonId() == R.id.rdoWX) {
                        meterType = "05";
                    }
                    bkInfo.setMeterTypeNo(meterType);
                    // 操作 新建账单则向数据库里新建
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
