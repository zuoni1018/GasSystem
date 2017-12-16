// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtCopyingActivity_ViewBinding implements Unbinder {
  private HtCopyingActivity target;

  private View view2131689653;

  private View view2131689693;

  private View view2131689694;

  @UiThread
  public HtCopyingActivity_ViewBinding(HtCopyingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtCopyingActivity_ViewBinding(final HtCopyingActivity target, View source) {
    this.target = target;

    View view;
    target.tvCopyingBackMsg = Utils.findRequiredViewAsType(source, R.id.tvCopyingBackMsg, "field 'tvCopyingBackMsg'", TextView.class);
    target.tvLoadingComNum = Utils.findRequiredViewAsType(source, R.id.tvLoadingComNum, "field 'tvLoadingComNum'", TextView.class);
    target.tvLoadingName = Utils.findRequiredViewAsType(source, R.id.tvLoadingName, "field 'tvLoadingName'", TextView.class);
    target.tvLoadingCount = Utils.findRequiredViewAsType(source, R.id.tvLoadingCount, "field 'tvLoadingCount'", TextView.class);
    target.tvLoadingAll = Utils.findRequiredViewAsType(source, R.id.tvLoadingAll, "field 'tvLoadingAll'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btnCopyScan, "field 'btnCopyScan' and method 'onViewClicked'");
    target.btnCopyScan = Utils.castView(view, R.id.btnCopyScan, "field 'btnCopyScan'", ImageButton.class);
    view2131689653 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnCopyingRead, "field 'btnCopyingRead' and method 'onViewClicked'");
    target.btnCopyingRead = Utils.castView(view, R.id.btnCopyingRead, "field 'btnCopyingRead'", ImageButton.class);
    view2131689693 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnCopyingStop, "field 'btnCopyingStop' and method 'onViewClicked'");
    target.btnCopyingStop = Utils.castView(view, R.id.btnCopyingStop, "field 'btnCopyingStop'", ImageButton.class);
    view2131689694 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvDeviceState = Utils.findRequiredViewAsType(source, R.id.tvDeviceState, "field 'tvDeviceState'", TextView.class);
    target.tvMessage = Utils.findRequiredViewAsType(source, R.id.tvMessage, "field 'tvMessage'", TextView.class);
    target.tvNeedNum = Utils.findRequiredViewAsType(source, R.id.tvNeedNum, "field 'tvNeedNum'", TextView.class);
    target.tvNowNum = Utils.findRequiredViewAsType(source, R.id.tvNowNum, "field 'tvNowNum'", TextView.class);
    target.tvTime = Utils.findRequiredViewAsType(source, R.id.tvTime, "field 'tvTime'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HtCopyingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvCopyingBackMsg = null;
    target.tvLoadingComNum = null;
    target.tvLoadingName = null;
    target.tvLoadingCount = null;
    target.tvLoadingAll = null;
    target.btnCopyScan = null;
    target.btnCopyingRead = null;
    target.btnCopyingStop = null;
    target.tvDeviceState = null;
    target.tvMessage = null;
    target.tvNeedNum = null;
    target.tvNowNum = null;
    target.tvTime = null;

    view2131689653.setOnClickListener(null);
    view2131689653 = null;
    view2131689693.setOnClickListener(null);
    view2131689693 = null;
    view2131689694.setOnClickListener(null);
    view2131689694 = null;
  }
}
