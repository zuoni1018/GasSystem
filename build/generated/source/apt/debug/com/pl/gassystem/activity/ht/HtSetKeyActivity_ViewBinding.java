// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtSetKeyActivity_ViewBinding implements Unbinder {
  private HtSetKeyActivity target;

  private View view2131689991;

  @UiThread
  public HtSetKeyActivity_ViewBinding(HtSetKeyActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtSetKeyActivity_ViewBinding(final HtSetKeyActivity target, View source) {
    this.target = target;

    View view;
    target.etKey01 = Utils.findRequiredViewAsType(source, R.id.etKey01, "field 'etKey01'", EditText.class);
    target.etKey02 = Utils.findRequiredViewAsType(source, R.id.etKey02, "field 'etKey02'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btSure, "field 'btSure' and method 'onViewClicked'");
    target.btSure = Utils.castView(view, R.id.btSure, "field 'btSure'", Button.class);
    view2131689991 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    target.tvNum = Utils.findRequiredViewAsType(source, R.id.tvNum, "field 'tvNum'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HtSetKeyActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etKey01 = null;
    target.etKey02 = null;
    target.btSure = null;
    target.tvNum = null;

    view2131689991.setOnClickListener(null);
    view2131689991 = null;
  }
}
