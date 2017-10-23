// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

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

public class HtResultQueryParameterActivity_ViewBinding implements Unbinder {
  private HtResultQueryParameterActivity target;

  private View view2131690019;

  @UiThread
  public HtResultQueryParameterActivity_ViewBinding(HtResultQueryParameterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtResultQueryParameterActivity_ViewBinding(final HtResultQueryParameterActivity target,
      View source) {
    this.target = target;

    View view;
    target.MLLX = Utils.findRequiredViewAsType(source, R.id.MLLX, "field 'MLLX'", TextView.class);
    target.ID = Utils.findRequiredViewAsType(source, R.id.ID, "field 'ID'", TextView.class);
    target.KPXD = Utils.findRequiredViewAsType(source, R.id.KPXD, "field 'KPXD'", TextView.class);
    target.KPYZ = Utils.findRequiredViewAsType(source, R.id.KPYZ, "field 'KPYZ'", TextView.class);
    target.DJR = Utils.findRequiredViewAsType(source, R.id.DJR, "field 'DJR'", TextView.class);
    target.KCQZSJ = Utils.findRequiredViewAsType(source, R.id.KCQZSJ, "field 'KCQZSJ'", TextView.class);
    target.BJZT = Utils.findRequiredViewAsType(source, R.id.BJZT, "field 'BJZT'", TextView.class);
    target.DYZ = Utils.findRequiredViewAsType(source, R.id.DYZ, "field 'DYZ'", TextView.class);
    target.RJBBH = Utils.findRequiredViewAsType(source, R.id.RJBBH, "field 'RJBBH'", TextView.class);
    target.BJDQSJ = Utils.findRequiredViewAsType(source, R.id.BJDQSJ, "field 'BJDQSJ'", TextView.class);
    target.MMBBH = Utils.findRequiredViewAsType(source, R.id.MMBBH, "field 'MMBBH'", TextView.class);
    target.MM = Utils.findRequiredViewAsType(source, R.id.MM, "field 'MM'", TextView.class);
    target.SCCSSZKZW = Utils.findRequiredViewAsType(source, R.id.SCCSSZKZW, "field 'SCCSSZKZW'", TextView.class);
    target.SBBXD = Utils.findRequiredViewAsType(source, R.id.SBBXD, "field 'SBBXD'", TextView.class);
    target.SBBFPYZ = Utils.findRequiredViewAsType(source, R.id.SBBFPYZ, "field 'SBBFPYZ'", TextView.class);
    target.SBBDJR = Utils.findRequiredViewAsType(source, R.id.SBBDJR, "field 'SBBDJR'", TextView.class);
    target.SBBKCQZSJ = Utils.findRequiredViewAsType(source, R.id.SBBKCQZSJ, "field 'SBBKCQZSJ'", TextView.class);
    target.SCCSSZSJ = Utils.findRequiredViewAsType(source, R.id.SCCSSZSJ, "field 'SCCSSZSJ'", TextView.class);
    target.layoutOther = Utils.findRequiredViewAsType(source, R.id.layoutOther, "field 'layoutOther'", LinearLayout.class);
    target.SBBMYBBH = Utils.findRequiredViewAsType(source, R.id.SBBMYBBH, "field 'SBBMYBBH'", TextView.class);
    target.SBBMY = Utils.findRequiredViewAsType(source, R.id.SBBMY, "field 'SBBMY'", TextView.class);
    target.SCMYSZSJ = Utils.findRequiredViewAsType(source, R.id.SCMYSZSJ, "field 'SCMYSZSJ'", TextView.class);
    target.CBCS = Utils.findRequiredViewAsType(source, R.id.CBCS, "field 'CBCS'", TextView.class);
    view = Utils.findRequiredView(source, R.id.bt, "method 'onViewClicked'");
    view2131690019 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    HtResultQueryParameterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.MLLX = null;
    target.ID = null;
    target.KPXD = null;
    target.KPYZ = null;
    target.DJR = null;
    target.KCQZSJ = null;
    target.BJZT = null;
    target.DYZ = null;
    target.RJBBH = null;
    target.BJDQSJ = null;
    target.MMBBH = null;
    target.MM = null;
    target.SCCSSZKZW = null;
    target.SBBXD = null;
    target.SBBFPYZ = null;
    target.SBBDJR = null;
    target.SBBKCQZSJ = null;
    target.SCCSSZSJ = null;
    target.layoutOther = null;
    target.SBBMYBBH = null;
    target.SBBMY = null;
    target.SCMYSZSJ = null;
    target.CBCS = null;

    view2131690019.setOnClickListener(null);
    view2131690019 = null;
  }
}
