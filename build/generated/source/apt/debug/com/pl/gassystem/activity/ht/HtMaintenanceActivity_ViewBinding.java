// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtMaintenanceActivity_ViewBinding implements Unbinder {
  private HtMaintenanceActivity target;

  private View view2131690000;

  private View view2131690001;

  private View view2131690003;

  private View view2131690002;

  private View view2131690004;

  private View view2131690005;

  private View view2131690006;

  @UiThread
  public HtMaintenanceActivity_ViewBinding(HtMaintenanceActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtMaintenanceActivity_ViewBinding(final HtMaintenanceActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.layoutValveMaintain, "field 'layoutValveMaintain' and method 'onViewClicked'");
    target.layoutValveMaintain = Utils.castView(view, R.id.layoutValveMaintain, "field 'layoutValveMaintain'", LinearLayout.class);
    view2131690000 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutQueryParameter, "field 'layoutQueryParameter' and method 'onViewClicked'");
    target.layoutQueryParameter = Utils.castView(view, R.id.layoutQueryParameter, "field 'layoutQueryParameter'", LinearLayout.class);
    view2131690001 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutSetParameter, "field 'layoutSetParameter' and method 'onViewClicked'");
    target.layoutSetParameter = Utils.castView(view, R.id.layoutSetParameter, "field 'layoutSetParameter'", LinearLayout.class);
    view2131690003 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutChangeBookNoOrCumulant, "field 'layoutChangeBookNoOrCumulant' and method 'onViewClicked'");
    target.layoutChangeBookNoOrCumulant = Utils.castView(view, R.id.layoutChangeBookNoOrCumulant, "field 'layoutChangeBookNoOrCumulant'", LinearLayout.class);
    view2131690002 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutUpdateKey, "method 'onViewClicked'");
    view2131690004 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutSetCopKey, "method 'onViewClicked'");
    view2131690005 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutSetChannel, "method 'onViewClicked'");
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
    HtMaintenanceActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutValveMaintain = null;
    target.layoutQueryParameter = null;
    target.layoutSetParameter = null;
    target.layoutChangeBookNoOrCumulant = null;

    view2131690000.setOnClickListener(null);
    view2131690000 = null;
    view2131690001.setOnClickListener(null);
    view2131690001 = null;
    view2131690003.setOnClickListener(null);
    view2131690003 = null;
    view2131690002.setOnClickListener(null);
    view2131690002 = null;
    view2131690004.setOnClickListener(null);
    view2131690004 = null;
    view2131690005.setOnClickListener(null);
    view2131690005 = null;
    view2131690006.setOnClickListener(null);
    view2131690006 = null;
  }
}
