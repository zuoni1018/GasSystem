package com.zuoni.zuoni_common.dialog.picker;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zuoni.zuoni_common.R;
import com.zuoni.zuoni_common.dialog.picker.view.LoopView;

import java.util.ArrayList;
import java.util.List;

public class DataPickerDialog2 extends Dialog {

    private Params params;
    public static boolean isLaw;

    public DataPickerDialog2(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(Params params) {
        this.params = params;
    }


    public void setSelection(String itemValue) {
        if (params.dataList.size() > 0) {
            int idx = params.dataList.indexOf(itemValue);
            if (idx >= 0) {
                params.initSelection = idx;
                params.loopData.setCurrentItem(params.initSelection);
            }
        }
    }

    public void setSelection2(String itemValue) {
        if (params.dataList2.size() > 0) {
            int idx = params.dataList2.indexOf(itemValue);
            if (idx >= 0) {
                params.initSelection2 = idx;
                params.loopData2.setCurrentItem(params.initSelection2);
            }
        }
    }

    public interface OnDataSelectedListener {
        void onDataSelected(String itemValue);

        void onDataSelected2(String itemValue);
    }


    public static class Builder {
        private final Context context;
        private final Params params;

        public Builder(Context context) {
            this.context = context;
            params = new Params();
        }

        private final String getCurrDateValue() {
            return params.loopData.getCurrentItemValue();
        }

        private final String getCurrDateValue2() {
            return params.loopData2.getCurrentItemValue();
        }

        public Builder setData(List<String> dataList) {
            params.dataList.clear();
            params.dataList.addAll(dataList);
            return this;
        }
        public Builder setData2(List<String> dataList2) {
            params.dataList2.clear();
            params.dataList2.addAll(dataList2);
            return this;
        }
        public Builder setTitle(String title) {
            params.title = title;

            return this;
        }

        public Builder setUnit(String unit) {
            params.unit = unit;
            return this;
        }

        public Builder setSelection(int selection) {
            params.initSelection = selection;
            return this;
        }
        public Builder setSelection2(int selection) {
            params.initSelection2 = selection;
            return this;
        }
        public Builder setOnDataSelectedListener(OnDataSelectedListener onDataSelectedListener) {
            params.callback = onDataSelectedListener;
            return this;
        }


        public DataPickerDialog2 create() {
            final DataPickerDialog2 dialog = new DataPickerDialog2(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_data2, null);

            if (!TextUtils.isEmpty(params.title)) {
                TextView txTitle = (TextView) view.findViewById(R.id.tx_title);
                txTitle.setText(params.title);
                txTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
            if (!TextUtils.isEmpty(params.unit)) {
                TextView txUnit = (TextView) view.findViewById(R.id.tx_unit);
                txUnit.setText(params.unit);
            }
            //设置左右两个选项卡

            final TextView tvTabRight = (TextView) view.findViewById(R.id.tvTabRight);
            final TextView tvTabLeft = (TextView) view.findViewById(R.id.tvTabLeft);


            final LinearLayout layoutRight= (LinearLayout) view.findViewById(R.id.layoutRight);
            LinearLayout layoutLeft= (LinearLayout) view.findViewById(R.id.layoutLeft);
            TextView tvRight= (TextView) view.findViewById(R.id.tvRight);
            final TextView tvLeft= (TextView) view.findViewById(R.id.tvLeft);
            //第一次进来先初始化
//            isLaw=true;
             isLaw = true;
            tvTabLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isLaw=true;

                    tvTabLeft.setBackgroundResource(R.drawable.bg_left_01);//左侧选中
                    tvTabRight.setBackgroundResource(R.drawable.bg_right_2);

                    tvTabLeft.setTextColor(context.getResources().getColor(R.color.color_white));
                    tvTabRight.setTextColor(context.getResources().getColor(R.color.tab02));

                    layoutRight.setVisibility(View.GONE);//隐藏右边
                    tvLeft.setVisibility(View.INVISIBLE);


                }
            });

            tvTabRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isLaw=false;
                    tvTabLeft.setBackgroundResource(R.drawable.bg_left_02);
                    tvTabRight.setBackgroundResource(R.drawable.bg_right_1);//右侧选中

                    tvTabRight.setTextColor(context.getResources().getColor(R.color.color_white));
                    tvTabLeft.setTextColor(context.getResources().getColor(R.color.tab02));

                    layoutRight.setVisibility(View.VISIBLE);//隐藏右边
                    tvLeft.setVisibility(View.VISIBLE);
                }
            });




            final LoopView loopData = (LoopView) view.findViewById(R.id.loop_data);
            loopData.setArrayList(params.dataList);
            loopData.setNotLoop();
            if (params.dataList.size() > 0) loopData.setCurrentItem(params.initSelection);


            final LoopView loopData2 = (LoopView) view.findViewById(R.id.loop_data2);
            loopData2.setArrayList(params.dataList2);
            loopData2.setNotLoop();
            if (params.dataList2.size() > 0) loopData2.setCurrentItem(params.initSelection2);

            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onDataSelected(getCurrDateValue());
                    params.callback.onDataSelected2(getCurrDateValue2());
                }
            });

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

            params.loopData2 = loopData2;
            dialog.setParams(params);

            return dialog;
        }
    }


    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        private LoopView loopData;
        private String title;
        private String unit;
        private int initSelection;
        private OnDataSelectedListener callback;
        private final List<String> dataList = new ArrayList<>();


        private LoopView loopData2;
        private final List<String> dataList2 = new ArrayList<>();
        private int initSelection2;
    }
}
