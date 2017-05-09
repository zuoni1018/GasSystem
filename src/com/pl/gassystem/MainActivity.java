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
 * 主菜单界面
 */
public class MainActivity extends Activity implements OnTouchListener {

    //主界面按钮
    private ImageButton ivCopy;//抄表按钮
    private ImageButton ivOneCopy;//单抄测试
    private ImageButton ivPhotoCopy;//摄像抄表
    private ImageButton ivQuery;//统计查询
    private ImageButton ivMaintain;//表具维护
    private ImageButton ivDataManger;//数据传输
    private ImageButton ivAbout;//关于
    private ImageButton ivSystemSet;//系统设置
    private TextView tvUserName;//用户名


    private PreferenceBiz preferenceBiz;

    private static Animation anim_btn_begin;
    private static Animation anim_btn_end;

    // private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 自定义标题栏
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
    // // 判断程序与第几次运行，如果是第一次运行则复制assets所有文件到外存储器中
    // if (count == 0) {
    // copyAssets();
    // }
    // Editor editor = preferences.edit();
    // // 存入数据
    // editor.putInt("count", ++count);
    // // 提交修改
    // editor.commit();
    // }

    private void setupView() {
        ivCopy = (ImageButton) findViewById(R.id.ivmainCopy);//抄表按钮
        ivOneCopy = (ImageButton) findViewById(R.id.ivmainOneCopy);//单抄测试
        ivPhotoCopy = (ImageButton) findViewById(R.id.ivmainPhotoCopy);//摄像抄表
        ivQuery = (ImageButton) findViewById(R.id.ivmainQuery);//统计查询
        ivMaintain = (ImageButton) findViewById(R.id.ivmainMaintain);//表具维护
        ivAbout = (ImageButton) findViewById(R.id.ivmainAbout);//关于
        ivDataManger = (ImageButton) findViewById(R.id.ivmainDataManger);//数据传输
        ivSystemSet = (ImageButton) findViewById(R.id.ivmainSetting);//系统设置
        tvUserName = (TextView) findViewById(R.id.tvmainUserName);//用户名
        preferenceBiz = new PreferenceBiz(this);
        tvUserName.setText(preferenceBiz.getUserName());//设置用户名

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
            public void onClick(View v) {// 抄表
                Intent intent = new Intent(MainActivity.this, BookInfoActivity.class);
                intent.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_COPY);
                startActivity(intent);
            }
        });

        ivOneCopy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 单抄测试
                Intent intent = new Intent(MainActivity.this, InputComNumActivity.class);
                intent.putExtra("operationType", 1);
                startActivity(intent);
            }
        });

        ivPhotoCopy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {// 摄像抄表
                Intent intent = new Intent(MainActivity.this, InputPhotoComNumActivity.class);
                startActivity(intent);
            }
        });

        ivMaintain.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {// 表具维护
                Intent intent = new Intent(MainActivity.this, MaintenanceActivity.class);
                startActivity(intent);
            }
        });

        ivDataManger.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 数据管理
                Intent intent = new Intent(MainActivity.this, DataManageActivity.class);
                startActivity(intent);
            }
        });

        ivAbout.setOnClickListener(new OnClickListener() { // 关于

            @Override
            public void onClick(View v) {//关于按钮
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        ivSystemSet.setOnClickListener(new OnClickListener() { // 系统设置

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        ivQuery.setOnClickListener(new OnClickListener() { // 统计查询

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookInfoActivity.class);
                intent.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_SELECT);
                startActivity(intent);
            }
        });
    }


    /**
     * 设置点击时候的动画效果
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
    // * 将assets文件复制到存储器中
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
