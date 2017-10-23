// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtMessageActivity_ViewBinding implements Unbinder {
  private HtMessageActivity target;

  @UiThread
  public HtMessageActivity_ViewBinding(HtMessageActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtMessageActivity_ViewBinding(HtMessageActivity target, View source) {
    this.target = target;

    target.tvMessage = Utils.findRequiredViewAsType(source, R.id.tvMessage, "field 'tvMessage'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HtMessageActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvMessage = null;
  }
}
