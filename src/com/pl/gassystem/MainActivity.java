package com.pl.gassystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pl.bll.PreferenceBiz;
import com.pl.common.MyApplication;
import com.pl.utils.GlobalConsts;

/**
 * ���˵�����
 */
public class MainActivity extends Activity implements OnTouchListener {

    //�����水ť
    private ImageButton ivCopy;//����ť
    private ImageButton ivOneCopy;//��������
    private ImageButton ivPhotoCopy;//���񳭱�
    private ImageButton ivQuery;//ͳ�Ʋ�ѯ
    private ImageButton ivMaintain;//���ά��
    private ImageButton ivDataManger;//���ݴ���
    private ImageButton ivAbout;//����
    private ImageButton ivSystemSet;//ϵͳ����
    private TextView tvUserName;//�û���


    private PreferenceBiz preferenceBiz;

    private static Animation anim_btn_begin;
    private static Animation anim_btn_end;

    // private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // �Զ��������
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_main);
        MyApplication.getInstance().addActivity(this);
        anim_btn_begin = AnimationUtils.loadAnimation(this, R.anim.btn_alpha_scale_begin);
        anim_btn_end = AnimationUtils.loadAnimation(this, R.anim.btn_alpha_scale_end);

        setupView();
        addOnTouchListener();
        addListener();
        // assCopy();



    }


    private int getInt(String str){

        try {
            return  Integer.parseInt(str);
        }catch (Exception e){
            return 0;
        }
    }

    // private void assCopy(){
    // preferences = getSharedPreferences("count", Context.MODE_PRIVATE);
    // int count = preferences.getInt("count", 0);
    //
    // // �жϳ�����ڼ������У�����ǵ�һ����������assets�����ļ�����洢����
    // if (count == 0) {
    // copyAssets();
    // }
    // Editor editor = preferences.edit();
    // // ��������
    // editor.putInt("count", ++count);
    // // �ύ�޸�
    // editor.commit();
    // }

    private void setupView() {
        ivCopy = (ImageButton) findViewById(R.id.ivmainCopy);//����ť
        ivOneCopy = (ImageButton) findViewById(R.id.ivmainOneCopy);//��������
        ivPhotoCopy = (ImageButton) findViewById(R.id.ivmainPhotoCopy);//���񳭱�
        ivQuery = (ImageButton) findViewById(R.id.ivmainQuery);//ͳ�Ʋ�ѯ
        ivMaintain = (ImageButton) findViewById(R.id.ivmainMaintain);//���ά��
        ivAbout = (ImageButton) findViewById(R.id.ivmainAbout);//����
        ivDataManger = (ImageButton) findViewById(R.id.ivmainDataManger);//���ݴ���
        ivSystemSet = (ImageButton) findViewById(R.id.ivmainSetting);//ϵͳ����
        tvUserName = (TextView) findViewById(R.id.tvmainUserName);//�û���
        preferenceBiz = new PreferenceBiz(this);
        tvUserName.setText(preferenceBiz.getUserName());//�����û���

    }
    ///////////////////////////////////

    //    private ArrayList<ArrayList<String>> getBillData() {
//        Cursor mCrusor = mDbHelper.exeSql("select * from family_bill");
//        while (mCrusor.moveToNext()) {
//            ArrayList<String> beanList=new ArrayList<String>();
//            beanList.add(mCrusor.getString(1));
//            beanList.add(mCrusor.getString(2));
//            beanList.add(mCrusor.getString(3));
//            beanList.add(mCrusor.getString(4));
//            beanList.add(mCrusor.getString(5));
//            beanList.add(mCrusor.getString(6));
//            beanList.add(mCrusor.getString(7));
//            beanList.add(mCrusor.getString(8));
//            beanList.add(mCrusor.getString(9));
//            beanList.add(mCrusor.getString(10));
//            beanList.add(mCrusor.getString(11));
//            beanList.add(mCrusor.getString(12));
//            bill2List.add(beanList);
//        }
//        mCrusor.close();
//        return bill2List;
//    }
    private void addListener() {


/////////////////////////
        ivCopy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {// ����
                Intent intent = new Intent(MainActivity.this, BookInfoActivity.class);
                intent.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_COPY);
                startActivity(intent);
            }
        });

        ivOneCopy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // ��������
                Intent intent = new Intent(MainActivity.this, InputComNumActivity.class);
                intent.putExtra("operationType", 1);
                startActivity(intent);
            }
        });

        ivPhotoCopy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {// ���񳭱�
                Intent intent = new Intent(MainActivity.this, InputPhotoComNumActivity.class);
                startActivity(intent);
            }
        });

        ivMaintain.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {// ���ά��
                Intent intent = new Intent(MainActivity.this, MaintenanceActivity.class);
                startActivity(intent);
            }
        });

        ivDataManger.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // ���ݹ���
                Intent intent = new Intent(MainActivity.this, DataManageActivity.class);
                startActivity(intent);
            }
        });

        ivAbout.setOnClickListener(new OnClickListener() { // ����

            @Override
            public void onClick(View v) {//���ڰ�ť
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        ivSystemSet.setOnClickListener(new OnClickListener() { // ϵͳ����

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        ivQuery.setOnClickListener(new OnClickListener() { // ͳ�Ʋ�ѯ

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookInfoActivity.class);
                intent.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_SELECT);
                startActivity(intent);
            }
        });
    }


    /**
     * ���õ��ʱ��Ķ���Ч��
     */
    private void addOnTouchListener() {
        ivCopy.setOnTouchListener(this);
        ivOneCopy.setOnTouchListener(this);
        ivPhotoCopy.setOnTouchListener(this);
        ivQuery.setOnTouchListener(this);
        ivDataManger.setOnTouchListener(this);
        ivMaintain.setOnTouchListener(this);
        ivSystemSet.setOnTouchListener(this);
        ivAbout.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            v.startAnimation(anim_btn_begin);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            v.startAnimation(anim_btn_end);
        }
        return false;
    }

    // /**
    // * ��assets�ļ����Ƶ��洢����
    // */
    // public void copyAssets() {
    // AssetManager assetManager = getAssets();
    // String[] files = null;
    // try {
    // files = assetManager.list("");
    // } catch (IOException e) {
    // Log.e("tag", "Failed to get asset file list.", e);
    // }
    // for (String filename : files) {
    // InputStream in = null;
    // OutputStream out = null;
    // try {
    // in = assetManager.open(filename);
    // File outFile = new File(getExternalFilesDir(null), filename);
    // out = new FileOutputStream(outFile);
    // copyFile(in, out);
    // in.close();
    // in = null;
    // out.flush();
    // out.close();
    // out = null;
    // } catch (IOException e) {
    // Log.e("tag", "Failed to copy asset file: " + filename, e);
    // }
    // }
    // }
    //
    // private void copyFile(InputStream in, OutputStream out) throws
    // IOException {
    // byte[] buffer = new byte[1024];
    // int read;
    // while ((read = in.read(buffer)) != -1) {
    // out.write(buffer, 0, read);
    // }
    // out.close();
    // }

}
