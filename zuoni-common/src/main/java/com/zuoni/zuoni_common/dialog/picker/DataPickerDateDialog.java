package com.zuoni.zuoni_common.dialog.picker;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.zuoni.zuoni_common.R;
import com.zuoni.zuoni_common.dialog.picker.view.LoopListener;
import com.zuoni.zuoni_common.dialog.picker.view.LoopView;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 日期选择器
 */
public class DataPickerDateDialog extends Dialog {
    //设置最小年份
    private static final int MIN_YEAR = 1870;
    private Params params;

    public DataPickerDateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(Params params) {
        this.params = params;
    }

    public interface OnDateSelectedListener {
        void onDateSelected(int[] dates);
    }


    public static class Builder {
        private final Context context;
        private final Params params;

        public Builder(Context context) {
            this.context = context;
            params = new Params();
        }

        /**
         * 获取当前选择的日期
         *
         * @return int[]数组形式返回。例[1990,6,15]
         */
        private final int[] getCurrDateValues() {
            int currYear = Integer.parseInt(params.loopYear.getCurrentItemValue());
            int currMonth = Integer.parseInt(params.loopMonth.getCurrentItemValue());
            int currDay = Integer.parseInt(params.loopDay.getCurrentItemValue());
            return new int[]{currYear, currMonth, currDay};
        }

        public Builder setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
            params.callback = onDateSelectedListener;
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
        public Builder canCancel(boolean canCancel) {
            params.canCancel = canCancel;
            return this;
        }

        public DataPickerDateDialog create() {
            final DataPickerDateDialog dialog = new DataPickerDateDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.data_picker_date_dialog, null);

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);//当前年份
            int month=c.get(Calendar.MONTH);
            int day=c.get(Calendar.DAY_OF_MONTH);

            final LoopView loopDay = (LoopView) view.findViewById(R.id.loop_day);
            loopDay.setArrayList(d(1, 30));
            loopDay.setCurrentItem(day-1);
            loopDay.setNotLoop();


            final LoopView loopYear = (LoopView) view.findViewById(R.id.loop_year);
            loopYear.setArrayList(d(MIN_YEAR, year - MIN_YEAR + 10));
            loopYear.setCurrentItem(year - MIN_YEAR);//设置默认年份
            loopYear.setNotLoop();

            final LoopView loopMonth = (LoopView) view.findViewById(R.id.loop_month);
            loopMonth.setArrayList(d(1, 12));
            loopMonth.setCurrentItem(month);
            loopMonth.setNotLoop();
            view.findViewById(R.id.tvLeft).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            final LoopListener maxDaySyncListener = new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    Calendar c = Calendar.getInstance();
                    c.set(Integer.parseInt(loopYear.getCurrentItemValue()), Integer.parseInt(loopMonth.getCurrentItemValue()) - 1, 1);
                    c.roll(Calendar.DATE, false);
                    int maxDayOfMonth = c.get(Calendar.DATE);
                    int fixedCurr = loopDay.getCurrentItem();
                    loopDay.setArrayList(d(1, maxDayOfMonth));
                    // 修正被选中的日期最大值
                    if (fixedCurr > maxDayOfMonth) fixedCurr = maxDayOfMonth - 1;
                    loopDay.setCurrentItem(fixedCurr);
                }
            };
            loopYear.setListener(maxDaySyncListener);
            loopMonth.setListener(maxDaySyncListener);

            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onDateSelected(getCurrDateValues());
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

            params.loopYear = loopYear;
            params.loopMonth = loopMonth;
            params.loopDay = loopDay;
            dialog.setParams(params);

            return dialog;
        }

        /**
         * 将数字传化为集合，并且补充0
         *
         * @param startNum 数字起点
         * @param count    数字个数
         * @return
         */
        private static List<String> d(int startNum, int count) {
            String[] values = new String[count];
            for (int i = startNum; i < startNum + count; i++) {
                String tempValue = (i < 10 ? "0" : "") + i;
                values[i - startNum] = tempValue;
            }
            return Arrays.asList(values);
        }
    }


    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        private LoopView loopYear, loopMonth, loopDay;

        private String leftText = "";//左边的字
        private String rightText = "";//右边的字
        private int leftTextColor;//左边字的颜色
        private int rightTextColor;//右边字的颜色
        private int selectColor;//滚动的时候选中的颜色
        private int normalColor;//滚动的时候未选中的颜色

        private OnDateSelectedListener callback;
    }
}
