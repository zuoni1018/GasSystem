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

  private View view2131690011;

  private View view2131690012;

  private View view2131690014;

  private View view2131690013;

  private View view2131690015;

  private View view2131690016;

  private View view2131690017;

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
    view2131690011 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutQueryParameter, "field 'layoutQueryParameter' and method 'onViewClicked'");
    target.layoutQueryParameter = Utils.castView(view, R.id.layoutQueryParameter, "field 'layoutQueryParameter'", LinearLayout.class);
    view2131690012 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutSetParameter, "field 'layoutSetParameter' and method 'onViewClicked'");
    target.layoutSetParameter = Utils.castView(view, R.id.layoutSetParameter, "field 'layoutSetParameter'", LinearLayout.class);
    view2131690014 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutChangeBookNoOrCumulant, "field 'layoutChangeBookNoOrCumulant' and method 'onViewClicked'");
    target.layoutChangeBookNoOrCumulant = Utils.castView(view, R.id.layoutChangeBookNoOrCumulant, "field 'layoutChangeBookNoOrCumulant'", LinearLayout.class);
    view2131690013 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutUpdateKey, "method 'onViewClicked'");
    view2131690015 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutSetCopKey, "method 'onViewClicked'");
    view2131690016 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutSetChannel, "method 'onViewClicked'");
    view2131690017 = view;
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

    view2131690011.setOnClickListener(null);
    view2131690011 = null;
    view2131690012.setOnClickListener(null);
    view2131690012 = null;
    view2131690014.setOnClickListener(null);
    view2131690014 = null;
    view2131690013.setOnClickListener(null);
    view2131690013 = null;
    view2131690015.setOnClickListener(null);
    view2131690015 = null;
    view2131690016.setOnClickListener(null);
    view2131690016 = null;
    view2131690017.setOnClickListener(null);
    view2131690017 = null;
  }
}
