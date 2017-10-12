package com.pl.gassystem.activity.ht;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pl.gassystem.activity.base.BaseTitleActivity;
import com.pl.gassystem.utils.SPUtils;

/**
 * Created by zangyi_shuai_ge on 2017/9/14
 */

public abstract class HtBaseTitleActivity extends BaseTitleActivity {

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFunctionName("º¼Ìì³­±í");

    }


   public  String getUserName(){
       String name = (String) SPUtils.get(getContext(), "HtUserName", "");
       if(name.equals("")){
           return "admin";
       }
       return  name;
   }
}
