// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SetRunModeActivity_ViewBinding implements Unbinder {
  private SetRunModeActivity target;

  private View view2131689837;

  private View view2131689839;

  private View view2131689841;

  private View view2131689843;

  private View view2131689845;

  private View view2131689847;

  private View view2131689849;

  private View view2131689851;

  @UiThread
  public SetRunModeActivity_ViewBinding(SetRunModeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SetRunModeActivity_ViewBinding(final SetRunModeActivity target, View source) {
    this.target = target;

    View view;
    target.tvRunModeTipStandard = Utils.findRequiredViewAsType(source, R.id.tvRunModeTipStandard, "field 'tvRunModeTipStandard'", TextView.class);
    view = Utils.findRequiredView(source, R.id.linRunModeStandard, "field 'linRunModeStandard' and method 'onViewClicked'");
    target.linRunModeStandard = Utils.castView(view, R.id.linRunModeStandard, "field 'linRunModeStandard'", LinearLayout.class);
    view2131689837 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvRunModeTipLORA = Utils.findRequiredViewAsType(source, R.id.tvRunModeTipLORA, "field 'tvRunModeTipLORA'", TextView.class);
    view = Utils.findRequiredView(source, R.id.linRunModeLORA, "field 'linRunModeLORA' and method 'onViewClicked'");
    target.linRunModeLORA = Utils.castView(view, R.id.linRunModeLORA, "field 'linRunModeLORA'", LinearLayout.class);
    view2131689839 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvRunModeTipFSK = Utils.findRequiredViewAsType(source, R.id.tvRunModeTipFSK, "field 'tvRunModeTipFSK'", TextView.class);
    view = Utils.findRequiredView(source, R.id.linRunModeFSK, "field 'linRunModeFSK' and method 'onViewClicked'");
    target.linRunModeFSK = Utils.castView(view, R.id.linRunModeFSK, "field 'linRunModeFSK'", LinearLayout.class);
    view2131689841 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvRunModeTipHuiZhou = Utils.findRequiredViewAsType(source, R.id.tvRunModeTipHuiZhou, "field 'tvRunModeTipHuiZhou'", TextView.class);
    view = Utils.findRequiredView(source, R.id.linRunModeHuiZhou, "field 'linRunModeHuiZhou' and method 'onViewClicked'");
    target.linRunModeHuiZhou = Utils.castView(view, R.id.linRunModeHuiZhou, "field 'linRunModeHuiZhou'", LinearLayout.class);
    view2131689843 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvRunModeTipShangHai = Utils.findRequiredViewAsType(source, R.id.tvRunModeTipShangHai, "field 'tvRunModeTipShangHai'", TextView.class);
    view = Utils.findRequiredView(source, R.id.linRunModeShangHai, "field 'linRunModeShangHai' and method 'onViewClicked'");
    target.linRunModeShangHai = Utils.castView(view, R.id.linRunModeShangHai, "field 'linRunModeShangHai'", LinearLayout.class);
    view2131689845 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvRunModeTipPhoto = Utils.findRequiredViewAsType(source, R.id.tvRunModeTipPhoto, "field 'tvRunModeTipPhoto'", TextView.class);
    view = Utils.findRequiredView(source, R.id.linRunModePhoto, "field 'linRunModePhoto' and method 'onViewClicked'");
    target.linRunModePhoto = Utils.castView(view, R.id.linRunModePhoto, "field 'linRunModePhoto'", LinearLayout.class);
    view2131689847 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvRunModeTipZHGT = Utils.findRequiredViewAsType(source, R.id.tvRunModeTipZHGT, "field 'tvRunModeTipZHGT'", TextView.class);
    view = Utils.findRequiredView(source, R.id.linRunModeZHGT, "field 'linRunModeZHGT' and method 'onViewClicked'");
    target.linRunModeZHGT = Utils.castView(view, R.id.linRunModeZHGT, "field 'linRunModeZHGT'", LinearLayout.class);
    view2131689849 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvYinZi = Utils.findRequiredViewAsType(source, R.id.tvYinZi, "field 'tvYinZi'", TextView.class);
    target.tvXinDao = Utils.findRequiredViewAsType(source, R.id.tvXinDao, "field 'tvXinDao'", TextView.class);
    target.tvModeChooseHt = Utils.findRequiredViewAsType(source, R.id.tvModeChooseHt, "field 'tvModeChooseHt'", TextView.class);
    view = Utils.findRequiredView(source, R.id.layoutModeChooseHt, "field 'layoutModeChooseHt' and method 'onViewClicked'");
    target.layoutModeChooseHt = Utils.castView(view, R.id.layoutModeChooseHt, "field 'layoutModeChooseHt'", LinearLayout.class);
    view2131689851 = view;
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
    SetRunModeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvRunModeTipStandard = null;
    target.linRunModeStandard = null;
    target.tvRunModeTipLORA = null;
    target.linRunModeLORA = null;
    target.tvRunModeTipFSK = null;
    target.linRunModeFSK = null;
    target.tvRunModeTipHuiZhou = null;
    target.linRunModeHuiZhou = null;
    target.tvRunModeTipShangHai = null;
    target.linRunModeShangHai = null;
    target.tvRunModeTipPhoto = null;
    target.linRunModePhoto = null;
    target.tvRunModeTipZHGT = null;
    target.linRunModeZHGT = null;
    target.tvYinZi = null;
    target.tvXinDao = null;
    target.tvModeChooseHt = null;
    target.layoutModeChooseHt = null;

    view2131689837.setOnClickListener(null);
    view2131689837 = null;
    view2131689839.setOnClickListener(null);
    view2131689839 = null;
    view2131689841.setOnClickListener(null);
    view2131689841 = null;
    view2131689843.setOnClickListener(null);
    view2131689843 = null;
    view2131689845.setOnClickListener(null);
    view2131689845 = null;
    view2131689847.setOnClickListener(null);
    view2131689847 = null;
    view2131689849.setOnClickListener(null);
    view2131689849 = null;
    view2131689851.setOnClickListener(null);
    view2131689851 = null;
  }
}
