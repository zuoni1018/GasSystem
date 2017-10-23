// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtQueryParameterActivity_ViewBinding implements Unbinder {
  private HtQueryParameterActivity target;

  private View view2131689999;

  @UiThread
  public HtQueryParameterActivity_ViewBinding(HtQueryParameterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtQueryParameterActivity_ViewBinding(final HtQueryParameterActivity target, View source) {
    this.target = target;

    View view;
    target.etInputNo = Utils.findRequiredViewAsType(source, R.id.etInputNo, "field 'etInputNo'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btSure, "field 'btSure' and method 'onViewClicked'");
    target.btSure = Utils.castView(view, R.id.btSure, "field 'btSure'", Button.class);
    view2131689999 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    HtQueryParameterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etInputNo = null;
    target.btSure = null;

    view2131689999.setOnClickListener(null);
    view2131689999 = null;
  }
}
