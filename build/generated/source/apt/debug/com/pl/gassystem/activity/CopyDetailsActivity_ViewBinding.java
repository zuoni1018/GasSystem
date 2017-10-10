// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CopyDetailsActivity_ViewBinding implements Unbinder {
  private CopyDetailsActivity target;

  private View view2131689625;

  private View view2131689626;

  private View view2131689627;

  private View view2131689628;

  private View view2131689629;

  private View view2131689630;

  private View view2131689631;

  private View view2131689632;

  @UiThread
  public CopyDetailsActivity_ViewBinding(CopyDetailsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CopyDetailsActivity_ViewBinding(final CopyDetailsActivity target, View source) {
    this.target = target;

    View view;
    target.tvCopyNum = Utils.findRequiredViewAsType(source, R.id.tvCopyNum, "field 'tvCopyNum'", TextView.class);
    target.ivCopy = Utils.findRequiredViewAsType(source, R.id.ivCopy, "field 'ivCopy'", ImageView.class);
    target.tvNoCopyNum = Utils.findRequiredViewAsType(source, R.id.tvNoCopyNum, "field 'tvNoCopyNum'", TextView.class);
    target.ivNoCopy = Utils.findRequiredViewAsType(source, R.id.ivNoCopy, "field 'ivNoCopy'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.layoutBeginCopy, "field 'layoutBeginCopy' and method 'onViewClicked'");
    target.layoutBeginCopy = Utils.castView(view, R.id.layoutBeginCopy, "field 'layoutBeginCopy'", LinearLayout.class);
    view2131689625 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutNoCopy, "field 'layoutNoCopy' and method 'onViewClicked'");
    target.layoutNoCopy = Utils.castView(view, R.id.layoutNoCopy, "field 'layoutNoCopy'", LinearLayout.class);
    view2131689626 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutShowNoCopy, "field 'layoutShowNoCopy' and method 'onViewClicked'");
    target.layoutShowNoCopy = Utils.castView(view, R.id.layoutShowNoCopy, "field 'layoutShowNoCopy'", LinearLayout.class);
    view2131689627 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutCopyAll, "field 'layoutCopyAll' and method 'onViewClicked'");
    target.layoutCopyAll = Utils.castView(view, R.id.layoutCopyAll, "field 'layoutCopyAll'", LinearLayout.class);
    view2131689628 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutShowAll, "field 'layoutShowAll' and method 'onViewClicked'");
    target.layoutShowAll = Utils.castView(view, R.id.layoutShowAll, "field 'layoutShowAll'", LinearLayout.class);
    view2131689629 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutMaintain, "field 'layoutMaintain' and method 'onViewClicked'");
    target.layoutMaintain = Utils.castView(view, R.id.layoutMaintain, "field 'layoutMaintain'", LinearLayout.class);
    view2131689630 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutSetting, "field 'layoutSetting' and method 'onViewClicked'");
    target.layoutSetting = Utils.castView(view, R.id.layoutSetting, "field 'layoutSetting'", LinearLayout.class);
    view2131689631 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.ivWarn = Utils.findRequiredViewAsType(source, R.id.ivWarn, "field 'ivWarn'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.layoutWarnBig, "field 'layoutWarnBig' and method 'onViewClicked'");
    target.layoutWarnBig = Utils.castView(view, R.id.layoutWarnBig, "field 'layoutWarnBig'", LinearLayout.class);
    view2131689632 = view;
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
    CopyDetailsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvCopyNum = null;
    target.ivCopy = null;
    target.tvNoCopyNum = null;
    target.ivNoCopy = null;
    target.layoutBeginCopy = null;
    target.layoutNoCopy = null;
    target.layoutShowNoCopy = null;
    target.layoutCopyAll = null;
    target.layoutShowAll = null;
    target.layoutMaintain = null;
    target.layoutSetting = null;
    target.ivWarn = null;
    target.layoutWarnBig = null;

    view2131689625.setOnClickListener(null);
    view2131689625 = null;
    view2131689626.setOnClickListener(null);
    view2131689626 = null;
    view2131689627.setOnClickListener(null);
    view2131689627 = null;
    view2131689628.setOnClickListener(null);
    view2131689628 = null;
    view2131689629.setOnClickListener(null);
    view2131689629 = null;
    view2131689630.setOnClickListener(null);
    view2131689630 = null;
    view2131689631.setOnClickListener(null);
    view2131689631 = null;
    view2131689632.setOnClickListener(null);
    view2131689632 = null;
  }
}
