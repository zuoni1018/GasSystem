// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ct;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CtCopySituationActivity_ViewBinding implements Unbinder {
  private CtCopySituationActivity target;

  private View view2131689891;

  private View view2131689892;

  private View view2131689895;

  private View view2131689894;

  private View view2131689896;

  private View view2131689897;

  private View view2131689899;

  private View view2131689627;

  private View view2131689893;

  @UiThread
  public CtCopySituationActivity_ViewBinding(CtCopySituationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CtCopySituationActivity_ViewBinding(final CtCopySituationActivity target, View source) {
    this.target = target;

    View view;
    target.tvCopyNum = Utils.findRequiredViewAsType(source, R.id.tvCopyNum, "field 'tvCopyNum'", TextView.class);
    target.ivCopy = Utils.findRequiredViewAsType(source, R.id.ivCopy, "field 'ivCopy'", ImageView.class);
    target.tvNoCopyNum = Utils.findRequiredViewAsType(source, R.id.tvNoCopyNum, "field 'tvNoCopyNum'", TextView.class);
    target.ivNoCopy = Utils.findRequiredViewAsType(source, R.id.ivNoCopy, "field 'ivNoCopy'", ImageView.class);
    target.mSwipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.mSwipeRefreshLayout, "field 'mSwipeRefreshLayout'", SwipeRefreshLayout.class);
    view = Utils.findRequiredView(source, R.id.btBeginCopy, "method 'onViewClicked'");
    view2131689891 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btCopyAllBook, "method 'onViewClicked'");
    view2131689892 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btShowAllBook, "method 'onViewClicked'");
    view2131689895 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btMaintain, "method 'onViewClicked'");
    view2131689894 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutNetworking, "method 'onViewClicked'");
    view2131689896 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutUpData, "method 'onViewClicked'");
    view2131689897 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btSetting, "method 'onViewClicked'");
    view2131689899 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutShowNoCopy, "method 'onViewClicked'");
    view2131689627 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutShowCopy, "method 'onViewClicked'");
    view2131689893 = view;
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
    CtCopySituationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvCopyNum = null;
    target.ivCopy = null;
    target.tvNoCopyNum = null;
    target.ivNoCopy = null;
    target.mSwipeRefreshLayout = null;

    view2131689891.setOnClickListener(null);
    view2131689891 = null;
    view2131689892.setOnClickListener(null);
    view2131689892 = null;
    view2131689895.setOnClickListener(null);
    view2131689895 = null;
    view2131689894.setOnClickListener(null);
    view2131689894 = null;
    view2131689896.setOnClickListener(null);
    view2131689896 = null;
    view2131689897.setOnClickListener(null);
    view2131689897 = null;
    view2131689899.setOnClickListener(null);
    view2131689899 = null;
    view2131689627.setOnClickListener(null);
    view2131689627 = null;
    view2131689893.setOnClickListener(null);
    view2131689893 = null;
  }
}
