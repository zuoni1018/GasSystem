// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SettingActivity_ViewBinding implements Unbinder {
  private SettingActivity target;

  private View view2131689860;

  private View view2131689862;

  private View view2131689864;

  private View view2131689865;

  private View view2131689866;

  private View view2131689867;

  @UiThread
  public SettingActivity_ViewBinding(SettingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SettingActivity_ViewBinding(final SettingActivity target, View source) {
    this.target = target;

    View view;
    target.tvSettingUserName = Utils.findRequiredViewAsType(source, R.id.tvSettingUserName, "field 'tvSettingUserName'", TextView.class);
    view = Utils.findRequiredView(source, R.id.linSettingBookInfoUrl, "method 'onViewClicked'");
    view2131689860 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.linSettingCopyPhotoUrl, "method 'onViewClicked'");
    view2131689862 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.linSettingRunMode, "method 'onViewClicked'");
    view2131689864 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.linSettingCopyTimeSet, "method 'onViewClicked'");
    view2131689865 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.linSettingMJCSet, "method 'onViewClicked'");
    view2131689866 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.linSettingLogOut, "method 'onViewClicked'");
    view2131689867 = view;
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
    SettingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvSettingUserName = null;

    view2131689860.setOnClickListener(null);
    view2131689860 = null;
    view2131689862.setOnClickListener(null);
    view2131689862 = null;
    view2131689864.setOnClickListener(null);
    view2131689864 = null;
    view2131689865.setOnClickListener(null);
    view2131689865 = null;
    view2131689866.setOnClickListener(null);
    view2131689866 = null;
    view2131689867.setOnClickListener(null);
    view2131689867 = null;
  }
}
