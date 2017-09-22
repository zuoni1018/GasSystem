// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ct;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CtCopyBookActivity_ViewBinding implements Unbinder {
  private CtCopyBookActivity target;

  private View view2131689905;

  private View view2131689907;

  private View view2131689910;

  @UiThread
  public CtCopyBookActivity_ViewBinding(CtCopyBookActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CtCopyBookActivity_ViewBinding(final CtCopyBookActivity target, View source) {
    this.target = target;

    View view;
    target.etSearch = Utils.findRequiredViewAsType(source, R.id.etSearch, "field 'etSearch'", EditText.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.mRecyclerView, "field 'mRecyclerView'", LRecyclerView.class);
    view = Utils.findRequiredView(source, R.id.ivTurnLeft, "field 'ivTurnLeft' and method 'onViewClicked'");
    target.ivTurnLeft = Utils.castView(view, R.id.ivTurnLeft, "field 'ivTurnLeft'", ImageView.class);
    view2131689905 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvNowPageNum = Utils.findRequiredViewAsType(source, R.id.tvNowPageNum, "field 'tvNowPageNum'", TextView.class);
    view = Utils.findRequiredView(source, R.id.ivTurnRight, "field 'ivTurnRight' and method 'onViewClicked'");
    target.ivTurnRight = Utils.castView(view, R.id.ivTurnRight, "field 'ivTurnRight'", ImageView.class);
    view2131689907 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvAllPageNum = Utils.findRequiredViewAsType(source, R.id.tvAllPageNum, "field 'tvAllPageNum'", TextView.class);
    target.etGoPageNum = Utils.findRequiredViewAsType(source, R.id.etGoPageNum, "field 'etGoPageNum'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btGoPageNum, "field 'btGoPageNum' and method 'onViewClicked'");
    target.btGoPageNum = Utils.castView(view, R.id.btGoPageNum, "field 'btGoPageNum'", Button.class);
    view2131689910 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.layoutChooseAll = Utils.findRequiredViewAsType(source, R.id.layoutChooseAll, "field 'layoutChooseAll'", LinearLayout.class);
    target.ivChooseAll = Utils.findRequiredViewAsType(source, R.id.ivChooseAll, "field 'ivChooseAll'", ImageView.class);
    target.btCopy = Utils.findRequiredViewAsType(source, R.id.btCopy, "field 'btCopy'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CtCopyBookActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etSearch = null;
    target.mRecyclerView = null;
    target.ivTurnLeft = null;
    target.tvNowPageNum = null;
    target.ivTurnRight = null;
    target.tvAllPageNum = null;
    target.etGoPageNum = null;
    target.btGoPageNum = null;
    target.layoutChooseAll = null;
    target.ivChooseAll = null;
    target.btCopy = null;

    view2131689905.setOnClickListener(null);
    view2131689905 = null;
    view2131689907.setOnClickListener(null);
    view2131689907 = null;
    view2131689910.setOnClickListener(null);
    view2131689910 = null;
  }
}
