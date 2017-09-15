package com.pl.gassystem.activity.ct;

import android.os.Bundle;

import com.pl.bll.SetBiz;
import com.pl.gassystem.activity.base.BaseTitleActivity;

/**
 * Created by zangyi_shuai_ge on 2017/9/15
 */

public abstract class CtBaseTitleActivity extends BaseTitleActivity {
    public SetBiz setBiz;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBiz=new SetBiz(getContext());
        setFunctionName("¼¯ÖÐÆ÷³­±í");
    }
}
