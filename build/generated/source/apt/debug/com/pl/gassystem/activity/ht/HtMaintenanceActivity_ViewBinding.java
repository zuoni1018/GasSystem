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

  private View view2131690017;

  private View view2131690018;

  private View view2131690020;

  private View view2131690019;

  private View view2131690021;

  private View view2131690022;

  private View view2131690023;

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
    view2131690017 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutQueryParameter, "field 'layoutQueryParameter' and method 'onViewClicked'");
    target.layoutQueryParameter = Utils.castView(view, R.id.layoutQueryParameter, "field 'layoutQueryParameter'", LinearLayout.class);
    view2131690018 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutSetParameter, "field 'layoutSetParameter' and method 'onViewClicked'");
    target.layoutSetParameter = Utils.castView(view, R.id.layoutSetParameter, "field 'layoutSetParameter'", LinearLayout.class);
    view2131690020 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutChangeBookNoOrCumulant, "field 'layoutChangeBookNoOrCumulant' and method 'onViewClicked'");
    target.layoutChangeBookNoOrCumulant = Utils.castView(view, R.id.layoutChangeBookNoOrCumulant, "field 'layoutChangeBookNoOrCumulant'", LinearLayout.class);
    view2131690019 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutUpdateKey, "method 'onViewClicked'");
    view2131690021 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutSetCopKey, "method 'onViewClicked'");
    view2131690022 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutSetChannel, "method 'onViewClicked'");
    view2131690023 = view;
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

    view2131690017.setOnClickListener(null);
    view2131690017 = null;
    view2131690018.setOnClickListener(null);
    view2131690018 = null;
    view2131690020.setOnClickListener(null);
    view2131690020 = null;
    view2131690019.setOnClickListener(null);
    view2131690019 = null;
    view2131690021.setOnClickListener(null);
    view2131690021 = null;
    view2131690022.setOnClickListener(null);
    view2131690022 = null;
    view2131690023.setOnClickListener(null);
    view2131690023 = null;
  }
}
