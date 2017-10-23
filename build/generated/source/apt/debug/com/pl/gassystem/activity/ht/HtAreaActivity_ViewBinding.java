// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtAreaActivity_ViewBinding implements Unbinder {
  private HtAreaActivity target;

  private View view2131689984;

  private View view2131689985;

  private View view2131689986;

  private View view2131689987;

  @UiThread
  public HtAreaActivity_ViewBinding(HtAreaActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtAreaActivity_ViewBinding(final HtAreaActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.bt2, "method 'onViewClicked'");
    view2131689984 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt3, "method 'onViewClicked'");
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
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view2131689984.setOnClickListener(null);
    view2131689984 = null;
    view2131689985.setOnClickListener(null);
    view2131689985 = null;
    view2131689986.setOnClickListener(null);
    view2131689986 = null;
    view2131689987.setOnClickListener(null);
    view2131689987 = null;
  }
}
