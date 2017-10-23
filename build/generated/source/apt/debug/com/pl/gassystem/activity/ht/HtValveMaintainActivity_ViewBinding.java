// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtValveMaintainActivity_ViewBinding implements Unbinder {
  private HtValveMaintainActivity target;

  private View view2131689999;

  @UiThread
  public HtValveMaintainActivity_ViewBinding(HtValveMaintainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtValveMaintainActivity_ViewBinding(final HtValveMaintainActivity target, View source) {
    this.target = target;

    View view;
    target.etInputNo = Utils.findRequiredViewAsType(source, R.id.etInputNo, "field 'etInputNo'", EditText.class);
    target.rbValveState = Utils.findRequiredViewAsType(source, R.id.rbValveState, "field 'rbValveState'", RadioButton.class);
    target.mRadioGroup = Utils.findRequiredViewAsType(source, R.id.mRadioGroup, "field 'mRadioGroup'", RadioGroup.class);
    view = Utils.findRequiredView(source, R.id.btSure, "method 'onViewClicked'");
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
    HtValveMaintainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etInputNo = null;
    target.rbValveState = null;
    target.mRadioGroup = null;

    view2131689999.setOnClickListener(null);
    view2131689999 = null;
  }
}
