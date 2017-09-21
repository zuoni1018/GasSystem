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
import com.zuoni.zuoni_common.dialog.picker.callback.OnDoubleDataSelectedListener;
import com.zuoni.zuoni_common.dialog.picker.view.LoopView;
import java.util.ArrayList;
import java.util.List;


/**
 * 数据选择双列选择
 */
public class DataPickerDoubleDialog extends Dialog {

    private Params params;

    //通过create来调用构造方法
    private DataPickerDoubleDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    //在create中给Dialog设置参数
    private void setParams(Params params) {
        this.params = params;
    }

    //设置默认选中的位置
    public void setSelection(String itemValue) {
        if (params.leftDataList.size() > 0) {
            int idx = params.leftDataList.indexOf(itemValue);
            if (idx >= 0) {
                params.initSelectionLeft = idx;
                params.leftLoopData.setCurrentItem(params.initSelectionLeft);
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
        private String getCurrDateValueLeft() {
            return params.leftLoopData.getCurrentItemValue();
        }

        private String getCurrDateValueRight() {
            return params.rightLoopData.getCurrentItemValue();
        }

        //给滚轮设置数据
        public Builder setLeftData(List<String> dataList) {
            params.leftDataList.clear();
            params.leftDataList.addAll(dataList);
            return this;
        }

        public Builder setRightData(List<String> dataList) {
            params.rightDataList.clear();
            params.rightDataList.addAll(dataList);
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
        public Builder setSelectionLeft(int selection) {
            params.initSelectionLeft = selection;
            return this;
        }

        public Builder setSelectionRight(int selection) {
            params.initSelectionRight = selection;
            return this;
        }

        //滚轮数据监听
        public Builder setOnDoubleDataSelectedListener(OnDoubleDataSelectedListener doubleDataSelectedListener) {
            params.callback = doubleDataSelectedListener;
            return this;
        }

        //设置滚轮未选中状态下的颜色
        public Builder setLeftNormalTextColor(@ColorInt int color) {
            params.leftNormalColor = color;
            return this;
        }

        //设置滚轮选中状态下的颜色
        public Builder setLeftSelectTextColor(@ColorInt int color) {
            params.leftSelectColor = color;
            return this;
        }

        //设置滚轮未选中状态下的颜色
        public Builder setRightNormalTextColor(@ColorInt int color) {
            params.rightNormalColor = color;
            return this;
        }

        //设置滚轮选中状态下的颜色
        public Builder setRightSelectTextColor(@ColorInt int color) {
            params.rightSelectColor = color;
            return this;
        }


        //通过生成的params来创建一个dialog
        public DataPickerDoubleDialog create() {
            //创建一个Dialog
            final DataPickerDoubleDialog dialog = new DataPickerDoubleDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            //自定义View
            View view = LayoutInflater.from(context).inflate(R.layout.data_picker_double_dialog, null);

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
                    params.callback.onDataSelectedLeft(getCurrDateValueLeft());
                    params.callback.onDataSelectedRight(getCurrDateValueRight());
//                    params.callback.onDataSelected(getCurrDateValue());
                }
            });

            tvLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            //设置滚轮控件
            LoopView leftLoopData = (LoopView) view.findViewById(R.id.loop_left);
            leftLoopData.setArrayList(params.leftDataList);//给滚轮控件设置数据源
            leftLoopData.setNotLoop();
            if (params.leftDataList.size() > 0) {
                leftLoopData.setCurrentItem(params.initSelectionLeft);//滚轮默认指到的位置
            }
            //设置选中和未选中字的颜色
            if (params.leftNormalColor != 0) {
                leftLoopData.setNormalColor(params.leftNormalColor);
            }

            if (params.leftSelectColor != 0) {
                leftLoopData.setSelectColor(params.leftSelectColor);
            }


            LoopView rightLoopData = (LoopView) view.findViewById(R.id.loop_right);
            rightLoopData.setArrayList(params.rightDataList);//给滚轮控件设置数据源
            rightLoopData.setNotLoop();
            if (params.rightDataList.size() > 0) {
                leftLoopData.setCurrentItem(params.initSelectionRight);//滚轮默认指到的位置
            }
            //设置选中和未选中字的颜色
            if (params.rightNormalColor != 0) {
                leftLoopData.setNormalColor(params.rightNormalColor);
            }

            if (params.rightSelectColor != 0) {
                leftLoopData.setSelectColor(params.rightSelectColor);
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
            params.leftLoopData = leftLoopData;
            params.rightLoopData = rightLoopData;
            dialog.setParams(params);
            return dialog;
        }
    }


    //设置dialog各个属性
    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;//点击外部是否可取消

        private String leftText = "";//左边的字
        private String rightText = "";//右边的字
        private int leftTextColor;//左边字的颜色
        private int rightTextColor;//右边字的颜色

        //左侧滚轮
        private List<String> leftDataList = new ArrayList<>();
        private LoopView leftLoopData;
        private int leftSelectColor;
        private int leftNormalColor;
        //右侧滚轮
        private List<String> rightDataList = new ArrayList<>();
        private LoopView rightLoopData;
        private int rightSelectColor;
        private int rightNormalColor;

        private int initSelectionLeft;//第一次选择的内容
        private int initSelectionRight;//第一次选择的内容
        private OnDoubleDataSelectedListener callback;//滚轮监听回调

    }
}
