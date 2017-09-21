// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ct;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CtCopyingActivity_ViewBinding implements Unbinder {
  private CtCopyingActivity target;

  @UiThread
  public CtCopyingActivity_ViewBinding(CtCopyingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CtCopyingActivity_ViewBinding(CtCopyingActivity target, View source) {
    this.target = target;

    target.btnCopyScan = Utils.findRequiredViewAsType(source, R.id.btnCopyScan, "field 'btnCopyScan'", ImageButton.class);
    target.tvBtInfo = Utils.findRequiredViewAsType(source, R.id.tvBtInfo, "field 'tvBtInfo'", TextView.class);
    target.btnCopyingRead = Utils.findRequiredViewAsType(source, R.id.btnCopyingRead, "field 'btnCopyingRead'", ImageButton.class);
    target.btnCopyingStop = Utils.findRequiredViewAsType(source, R.id.btnCopyingStop, "field 'btnCopyingStop'", ImageButton.class);
    target.tvCopyingBackMsg = Utils.findRequiredViewAsType(source, R.id.tvCopyingBackMsg, "field 'tvCopyingBackMsg'", TextView.class);
    target.tvLoadingComNum = Utils.findRequiredViewAsType(source, R.id.tvLoadingComNum, "field 'tvLoadingComNum'", TextView.class);
    target.tvLoadingName = Utils.findRequiredViewAsType(source, R.id.tvLoadingName, "field 'tvLoadingName'", TextView.class);
    target.tvLoadingCount = Utils.findRequiredViewAsType(source, R.id.tvLoadingCount, "field 'tvLoadingCount'", TextView.class);
    target.tvLoadingAll = Utils.findRequiredViewAsType(source, R.id.tvLoadingAll, "field 'tvLoadingAll'", TextView.class);
    target.pgbCopying = Utils.findRequiredViewAsType(source, R.id.pgbCopying, "field 'pgbCopying'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CtCopyingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnCopyScan = null;
    target.tvBtInfo = null;
    target.btnCopyingRead = null;
    target.btnCopyingStop = null;
    target.tvCopyingBackMsg = null;
    target.tvLoadingComNum = null;
    target.tvLoadingName = null;
    target.tvLoadingCount = null;
    target.tvLoadingAll = null;
    target.pgbCopying = null;
  }
}
