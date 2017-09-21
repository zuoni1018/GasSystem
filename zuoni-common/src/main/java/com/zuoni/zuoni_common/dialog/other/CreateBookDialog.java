package com.zuoni.zuoni_common.dialog.other;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zuoni.zuoni_common.R;
import com.zuoni.zuoni_common.dialog.other.callback.OnCreateBookListener;
import com.zuoni.zuoni_common.utils.common.ToastUtils;


/**
 * Created by zangyi_shuai_ge on 2017/7/24
 * 创建燃气表Dialog
 */
public class CreateBookDialog extends Dialog {

    private Params params;

    public CreateBookDialog(Context context, int themeResId) {
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


        public Builder setCreateBookListener(OnCreateBookListener createBookListener) {
            params.createBookListener = createBookListener;
            return this;
        }


        public CreateBookDialog create() {
            final CreateBookDialog dialog = new CreateBookDialog(context, params.shadow ?
                    R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_create_book, null);
            TextView tvCancel = view.findViewById(R.id.tvCancel);
            TextView tvSure = view.findViewById(R.id.tvSure);
            final EditText etBookNo = view.findViewById(R.id.etBookNo);
            final EditText etBookName = view.findViewById(R.id.etBookName);

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            tvSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String bookNo = etBookNo.getText().toString().trim();
                    String bookName = etBookName.getText().toString().trim();
                    if (bookNo.length() == 8) {
                        if (bookName.equals("")) {
                            ToastUtils.showToast(context, "请重新输入表具名称");
//                            Toast.makeText(context, "请重新输入表具名称", Toast.LENGTH_SHORT).show();
                        } else {
                            if (params.createBookListener != null) {
                                params.createBookListener.getBookInfo(bookName, bookNo);
                            }
                            dialog.dismiss();
                        }
                    } else {
                        ToastUtils.showToast(context, "杭天表号为8位表,请重新输入");
//                        Toast.makeText(context, "杭天表号为8位表,请重新输入", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Window win = dialog.getWindow();
            assert win != null;
            win.getDecorView().setPadding(20, 0, 20, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
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
        private boolean shadow = true;
        private boolean canCancel = true;

        private OnCreateBookListener createBookListener;
    }
}
