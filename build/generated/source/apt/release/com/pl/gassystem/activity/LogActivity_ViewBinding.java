// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LogActivity_ViewBinding implements Unbinder {
  private LogActivity target;

  @UiThread
  public LogActivity_ViewBinding(LogActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LogActivity_ViewBinding(LogActivity target, View source) {
    this.target = target;

    target.logInfo = Utils.findRequiredViewAsType(source, R.id.logInfo, "field 'logInfo'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LogActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.logInfo = null;
  }
}
