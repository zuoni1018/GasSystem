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

public class HtGroupActivity_ViewBinding implements Unbinder {
  private HtGroupActivity target;

  private View view2131690004;

  private View view2131689984;

  private View view2131689985;

  private View view2131689986;

  private View view2131689987;

  private View view2131690006;

  @UiThread
  public HtGroupActivity_ViewBinding(HtGroupActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtGroupActivity_ViewBinding(final HtGroupActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.bt1, "field 'bt1' and method 'onViewClicked'");
    target.bt1 = Utils.castView(view, R.id.bt1, "field 'bt1'", Button.class);
    view2131690004 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt2, "field 'bt2' and method 'onViewClicked'");
    target.bt2 = Utils.castView(view, R.id.bt2, "field 'bt2'", Button.class);
    view2131689984 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt3, "field 'bt3' and method 'onViewClicked'");
    target.bt3 = Utils.castView(view, R.id.bt3, "field 'bt3'", Button.class);
    view2131689985 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt4, "method 'onViewClicked'");
    view2131689986 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt5, "method 'onViewClicked'");
    view2131689987 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt6, "method 'onViewClicked'");
    view2131690006 = view;
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
    HtGroupActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.bt1 = null;
    target.bt2 = null;
    target.bt3 = null;

    view2131690004.setOnClickListener(null);
    view2131690004 = null;
    view2131689984.setOnClickListener(null);
    view2131689984 = null;
    view2131689985.setOnClickListener(null);
    view2131689985 = null;
    view2131689986.setOnClickListener(null);
    view2131689986 = null;
    view2131689987.setOnClickListener(null);
    view2131689987 = null;
    view2131690006.setOnClickListener(null);
    view2131690006 = null;
  }
}
