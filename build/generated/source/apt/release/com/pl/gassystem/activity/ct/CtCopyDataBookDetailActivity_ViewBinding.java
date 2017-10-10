// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ct;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CtCopyDataBookDetailActivity_ViewBinding implements Unbinder {
  private CtCopyDataBookDetailActivity target;

  @UiThread
  public CtCopyDataBookDetailActivity_ViewBinding(CtCopyDataBookDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CtCopyDataBookDetailActivity_ViewBinding(CtCopyDataBookDetailActivity target,
      View source) {
    this.target = target;

    target.tvCopyDataDetailMeterNo = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailMeterNo, "field 'tvCopyDataDetailMeterNo'", TextView.class);
    target.tvCopyDataDetailMeterName = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailMeterName, "field 'tvCopyDataDetailMeterName'", TextView.class);
    target.tvCopyDataDetailCurrentShow = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailCurrentShow, "field 'tvCopyDataDetailCurrentShow'", TextView.class);
    target.tvCopyDataDetailCurrentDosage = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailCurrentDosage, "field 'tvCopyDataDetailCurrentDosage'", TextView.class);
    target.tvCopyDataDetailLastShow = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailLastShow, "field 'tvCopyDataDetailLastShow'", TextView.class);
    target.tvCopyDataDetailLastDosage = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailLastDosage, "field 'tvCopyDataDetailLastDosage'", TextView.class);
    target.tvCopyDataDetailCopyWay = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailCopyWay, "field 'tvCopyDataDetailCopyWay'", TextView.class);
    target.tvCopyDataDetailCopyState = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailCopyState, "field 'tvCopyDataDetailCopyState'", TextView.class);
    target.tvCopyDataDetailElec = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailElec, "field 'tvCopyDataDetailElec'", TextView.class);
    target.tvCopyDataDetailDbm = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailDbm, "field 'tvCopyDataDetailDbm'", TextView.class);
    target.tvCopyDataDetailCopyMan = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailCopyMan, "field 'tvCopyDataDetailCopyMan'", TextView.class);
    target.tvCopyDataDetailCopyTime = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailCopyTime, "field 'tvCopyDataDetailCopyTime'", TextView.class);
    target.tvCopyDataDetailRemark = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailRemark, "field 'tvCopyDataDetailRemark'", TextView.class);
    target.tvCopyDataDetailMeterState = Utils.findRequiredViewAsType(source, R.id.tvCopyDataDetailMeterState, "field 'tvCopyDataDetailMeterState'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CtCopyDataBookDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvCopyDataDetailMeterNo = null;
    target.tvCopyDataDetailMeterName = null;
    target.tvCopyDataDetailCurrentShow = null;
    target.tvCopyDataDetailCurrentDosage = null;
    target.tvCopyDataDetailLastShow = null;
    target.tvCopyDataDetailLastDosage = null;
    target.tvCopyDataDetailCopyWay = null;
    target.tvCopyDataDetailCopyState = null;
    target.tvCopyDataDetailElec = null;
    target.tvCopyDataDetailDbm = null;
    target.tvCopyDataDetailCopyMan = null;
    target.tvCopyDataDetailCopyTime = null;
    target.tvCopyDataDetailRemark = null;
    target.tvCopyDataDetailMeterState = null;
  }
}
