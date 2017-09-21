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

public class SetNewCopyKeyActivity_ViewBinding implements Unbinder {
  private HtSetNewCopyKeyActivity target;

  private View view2131689987;

  @UiThread
  public SetNewCopyKeyActivity_ViewBinding(HtSetNewCopyKeyActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SetNewCopyKeyActivity_ViewBinding(final HtSetNewCopyKeyActivity target, View source) {
    this.target = target;

    View view;
    target.tvNowKey = Utils.findRequiredViewAsType(source, R.id.tvNowKey, "field 'tvNowKey'", TextView.class);
    target.etNewCopyKey = Utils.findRequiredViewAsType(source, R.id.etNewCopyKey, "field 'etNewCopyKey'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btSure, "field 'btSure' and method 'onViewClicked'");
    target.btSure = Utils.castView(view, R.id.btSure, "field 'btSure'", Button.class);
    view2131689987 = view;
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
    HtSetNewCopyKeyActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvNowKey = null;
    target.etNewCopyKey = null;
    target.btSure = null;

    view2131689987.setOnClickListener(null);
    view2131689987 = null;
  }
}
