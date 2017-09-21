package com.zuoni.zuoni_common.dialog.picker;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zuoni.zuoni_common.R;
import com.zuoni.zuoni_common.dialog.picker.view.LoopView;

import java.util.Arrays;
import java.util.List;

/**
 * 时间选择器
 */
public class TimePickerDialog extends Dialog {

    public interface OnTimeSelectedListener {
        void onTimeSelected(int[] times);
    }

    private Params params;

    public TimePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(Params params) {
        this.params = params;
    }


    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        private LoopView loopHour, loopMin;
        private OnTimeSelectedListener callback;
    }

    public static class Builder {
        private final Context context;
        private final Params params;

        public Builder(Context context) {
            this.context = context;
            params = new Params();
        }
        public Builder canCancel(boolean canCancel) {
            params.canCancel = canCancel;
            return this;
        }
        /**
         * 获取当前选择的时间
         *
         * @return int[]数组形式返回。例[12,30]
         */
        private final int[] getCurrDateValues() {
            int currHour = Integer.parseInt(params.loopHour.getCurrentItemValue());
            int currMin = Integer.parseInt(params.loopMin.getCurrentItemValue());
            return new int[]{currHour, currMin};
        }

        public TimePickerDialog create() {
            final TimePickerDialog dialog = new TimePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.data_picker_time_dialog, null);

            final LoopView loopHour = (LoopView) view.findViewById(R.id.loop_hour);
            loopHour.setArrayList(d(1, 24));
            loopHour.setCurrentItem(12);
            loopHour.setNotLoop();

            final LoopView loopMin = (LoopView) view.findViewById(R.id.loop_min);
            loopMin.setArrayList(d(0, 60));
            loopMin.setCurrentItem(30);
            loopMin.setNotLoop();
            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onTimeSelected(getCurrDateValues());
                }
            });

            view.findViewById(R.id.tvLeft).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
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

            params.loopHour = loopHour;
            params.loopMin = loopMin;
            dialog.setParams(params);

            return dialog;
        }


        public Builder setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
            params.callback = onTimeSelectedListener;
            return this;
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
}
