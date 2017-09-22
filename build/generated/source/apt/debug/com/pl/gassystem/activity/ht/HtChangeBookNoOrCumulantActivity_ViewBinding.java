// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtChangeBookNoOrCumulantActivity_ViewBinding implements Unbinder {
  private HtChangeBookNoOrCumulantActivity target;

  private View view2131689991;

  @UiThread
  public HtChangeBookNoOrCumulantActivity_ViewBinding(HtChangeBookNoOrCumulantActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtChangeBookNoOrCumulantActivity_ViewBinding(final HtChangeBookNoOrCumulantActivity target,
      View source) {
    this.target = target;

    View view;
    target.etNowBookNo = Utils.findRequiredViewAsType(source, R.id.etNowBookNo, "field 'etNowBookNo'", EditText.class);
    target.etNewBookNo = Utils.findRequiredViewAsType(source, R.id.etNewBookNo, "field 'etNewBookNo'", EditText.class);
    target.etCumulant = Utils.findRequiredViewAsType(source, R.id.etCumulant, "field 'etCumulant'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btSure, "field 'btSure' and method 'onViewClicked'");
    target.btSure = Utils.castView(view, R.id.btSure, "field 'btSure'", Button.class);
    view2131689991 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    target.mRadioGroup = Utils.findRequiredViewAsType(source, R.id.mRadioGroup, "field 'mRadioGroup'", RadioGroup.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HtChangeBookNoOrCumulantActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etNowBookNo = null;
    target.etNewBookNo = null;
    target.etCumulant = null;
    target.btSure = null;
    target.mRadioGroup = null;

    view2131689991.setOnClickListener(null);
    view2131689991 = null;
  }
}
