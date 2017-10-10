// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtMainActivity_ViewBinding implements Unbinder {
  private HtMainActivity target;

  private View view2131690005;

  private View view2131689982;

  private View view2131689983;

  @UiThread
  public HtMainActivity_ViewBinding(HtMainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtMainActivity_ViewBinding(final HtMainActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.bt1, "field 'bt1' and method 'onViewClicked'");
    target.bt1 = Utils.castView(view, R.id.bt1, "field 'bt1'", Button.class);
    view2131690005 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt2, "field 'bt2' and method 'onViewClicked'");
    target.bt2 = Utils.castView(view, R.id.bt2, "field 'bt2'", Button.class);
    view2131689982 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt3, "field 'bt3' and method 'onViewClicked'");
    target.bt3 = Utils.castView(view, R.id.bt3, "field 'bt3'", Button.class);
    view2131689983 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    HtMainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.bt1 = null;
    target.bt2 = null;
    target.bt3 = null;

    view2131690005.setOnClickListener(null);
    view2131690005 = null;
    view2131689982.setOnClickListener(null);
    view2131689982 = null;
    view2131689983.setOnClickListener(null);
    view2131689983 = null;
  }
}
