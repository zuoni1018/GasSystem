package com.zuoni.zuoni_common.dialog.loading;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.zuoni.zuoni_common.R;


/**
 * 从底部弹出的Dialog
 */
public class LoadingDialog extends Dialog {

    private Params params;

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(Params params) {
        this.params = params;
    }


    public static class Builder {
        private final Context context;
        private final Params params;

        public Builder(Context context) {
            this.context = context;
            params = new Params();
        }

        public Builder setMessage(String message) {
            params.message = message;
            return this;
        }

        public LoadingDialog create() {
            LoadingDialog dialog = new LoadingDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null);
            TextView tvMessage= (TextView) view.findViewById(R.id.tvMessage);

            if(!params.message.equals("")){
                tvMessage.setText(params.message);
            }


            Window win = dialog.getWindow();
            assert win != null;
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
            win.setGravity(Gravity.CENTER);
//            win.setWindowAnimations(R.style.Animation_Bottom_Rising);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(params.canCancel);//点击外部取消
            dialog.setCancelable(params.canCancel);
            dialog.setParams(params);
            return dialog;
        }

    }


    private static final class Params {
        private boolean shadow = false;
        private boolean canCancel = false;
        private String message="";
    }
}
