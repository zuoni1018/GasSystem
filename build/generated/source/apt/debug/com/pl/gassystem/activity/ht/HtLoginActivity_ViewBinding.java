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

public class HtLoginActivity_ViewBinding implements Unbinder {
  private HtLoginActivity target;

  private View view2131690010;

  private View view2131690007;

  @UiThread
  public HtLoginActivity_ViewBinding(HtLoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtLoginActivity_ViewBinding(final HtLoginActivity target, View source) {
    this.target = target;

    View view;
    target.UserName = Utils.findRequiredViewAsType(source, R.id.UserName, "field 'UserName'", EditText.class);
    target.Password = Utils.findRequiredViewAsType(source, R.id.Password, "field 'Password'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btLogin, "field 'btLogin' and method 'onViewClicked'");
    target.btLogin = Utils.castView(view, R.id.btLogin, "field 'btLogin'", Button.class);
    view2131690010 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.view, "method 'onViewClicked2'");
    view2131690007 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked2();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    HtLoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.UserName = null;
    target.Password = null;
    target.btLogin = null;

    view2131690010.setOnClickListener(null);
    view2131690010 = null;
    view2131690007.setOnClickListener(null);
    view2131690007 = null;
  }
}
