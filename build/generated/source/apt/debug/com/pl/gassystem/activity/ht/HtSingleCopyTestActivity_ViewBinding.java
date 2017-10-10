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

public class HtSingleCopyTestActivity_ViewBinding implements Unbinder {
  private HtSingleCopyTestActivity target;

  private View view2131689997;

  @UiThread
  public HtSingleCopyTestActivity_ViewBinding(HtSingleCopyTestActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtSingleCopyTestActivity_ViewBinding(final HtSingleCopyTestActivity target, View source) {
    this.target = target;

    View view;
    target.etInputNo = Utils.findRequiredViewAsType(source, R.id.etInputNo, "field 'etInputNo'", EditText.class);
    target.mRadioGroup = Utils.findRequiredViewAsType(source, R.id.mRadioGroup, "field 'mRadioGroup'", RadioGroup.class);
    target.rbCopyNormal = Utils.findRequiredViewAsType(source, R.id.rbCopyNormal, "field 'rbCopyNormal'", RadioButton.class);
    view = Utils.findRequiredView(source, R.id.btSure, "method 'onViewClicked'");
    view2131689997 = view;
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
    HtSingleCopyTestActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etInputNo = null;
    target.mRadioGroup = null;
    target.rbCopyNormal = null;

    view2131689997.setOnClickListener(null);
    view2131689997 = null;
  }
}
