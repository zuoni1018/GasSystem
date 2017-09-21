package com.zuoni.zuoni_common.dialog.picker;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zuoni.zuoni_common.R;
import com.zuoni.zuoni_common.dialog.picker.callback.OnSingleDataSelectedListener;
import com.zuoni.zuoni_common.dialog.picker.view.LoopView;

import java.util.ArrayList;
import java.util.List;


/**
 * 数据选择单列选择
 */
public class DataPickerSingleDialog extends Dialog {

    private Params params;

    //通过create来调用构造方法
    private DataPickerSingleDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    //在create中给Dialog设置参数
    private void setParams(Params params) {
        this.params = params;
    }

    //设置默认选中的位置
    public void setSelection(String itemValue) {
        if (params.dataList.size() > 0) {
            int idx = params.dataList.indexOf(itemValue);
            if (idx >= 0) {
                params.initSelection = idx;
                params.loopData.setCurrentItem(params.initSelection);
            }
        }
    }

    /**
     * Dialog构造器
     */
    public static class Builder {

        private final Context context;
        private final Params params;

        //构造器的构造方法
        public Builder(Context context) {
            this.context = context;
            params = new Params();
        }

        //获得滚轮选中的数据
        private String getCurrDateValue() {
            return params.loopData.getCurrentItemValue();
        }

        //给滚轮设置数据
        public Builder setData(List<String> dataList) {
            params.dataList.clear();
            params.dataList.addAll(dataList);
            return this;
        }

        //设置左边的字
        public Builder setLeftText(String leftText) {
            params.leftText = leftText;
            return this;
        }

        //设置右边的字
        public Builder setRightText(String rightText) {
            params.rightText = rightText;
            return this;
        }

        //设置左边的字及其颜色
        public Builder setLeftText(String leftText, @ColorInt int leftColor) {
            params.leftText = leftText;
            params.leftTextColor = leftColor;
            return this;
        }

        //设置右边的字及其颜色
        public Builder setRightText(String rightText, @ColorInt int rightColor) {
            params.rightText = rightText;
            params.rightTextColor = rightColor;
            return this;
        }

        //设置指针第一次默认指到的位置
        public Builder setSelection(int selection) {
            params.initSelection = selection;
            return this;
        }

        //滚轮数据监听
        public Builder setOnDataSelectedListener(OnSingleDataSelectedListener onDataSelectedListener) {
            params.callback = onDataSelectedListener;
            return this;
        }

        //设置滚轮未选中状态下的颜色
        public Builder setNormalTextColor(@ColorInt int color) {
            params.normalColor = color;
            return this;
        }

        //设置滚轮选中状态下的颜色
        public Builder setSelectTextColor(@ColorInt int color) {
            params.selectColor = color;
            return this;
        }


        //通过生成的params来创建一个dialog
        public DataPickerSingleDialog create() {
            //创建一个Dialog
            final DataPickerSingleDialog dialog = new DataPickerSingleDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            //自定义View
            View view = LayoutInflater.from(context).inflate(R.layout.data_picker_single_dialog, null);

            TextView tvLeft = (TextView) view.findViewById(R.id.tvLeft);
            TextView tvRight = (TextView) view.findViewById(R.id.tvRight);

            //判断有无自定义属性
            if (!params.leftText.equals("")) {
                tvLeft.setText(params.leftText);
            }
            if (!params.rightText.equals("")) {
                tvRight.setText(params.rightText);
            }
            if (params.leftTextColor != 0) {
                tvLeft.setTextColor(params.leftTextColor);
            }
            if (params.rightTextColor != 0) {
                tvRight.setTextColor(params.rightTextColor);
            }

            //设置左右两个按钮的点击事件
            tvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onDataSelected(getCurrDateValue());
                }
            });

            tvLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            //设置滚轮控件
            LoopView loopData = (LoopView) view.findViewById(R.id.loop_data);
            loopData.setArrayList(params.dataList);//给滚轮控件设置数据源
            loopData.setNotLoop();
            if (params.dataList.size() > 0) {
                loopData.setCurrentItem(params.initSelection);//滚轮默认指到的位置
            }
            //设置选中和未选中字的颜色
            if (params.normalColor != 0) {
                loopData.setNormalColor(params.normalColor);
            }

            if (params.selectColor != 0) {
                loopData.setSelectColor(params.selectColor);
            }


            //设置dialog显示的位置
            Window win = dialog.getWindow();
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
            win.setGravity(Gravity.BOTTOM);
            win.setWindowAnimations(R.style.Animation_Bottom_Rising);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(params.canCancel);
            dialog.setCancelable(params.canCancel);
            params.loopData = loopData;
            dialog.setParams(params);
            return dialog;
        }
    }


    //设置dialog各个属性
    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;//点击外部是否可取消
        private LoopView loopData;//滚轮
        private String leftText = "";//左边的字
        private String rightText = "";//右边的字
        private int leftTextColor;//左边字的颜色
        private int rightTextColor;//右边字的颜色
        private int selectColor;//滚动的时候选中的颜色
        private int normalColor;//滚动的时候未选中的颜色
        private int initSelection;//第一次选择的内容
        private OnSingleDataSelectedListener callback;//滚轮监听回调
        private final List<String> dataList = new ArrayList<>();
    }
}
