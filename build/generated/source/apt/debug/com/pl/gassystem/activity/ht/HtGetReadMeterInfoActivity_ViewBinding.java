// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtGetReadMeterInfoActivity_ViewBinding implements Unbinder {
  private HtGetReadMeterInfoActivity target;

  @UiThread
  public HtGetReadMeterInfoActivity_ViewBinding(HtGetReadMeterInfoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtGetReadMeterInfoActivity_ViewBinding(HtGetReadMeterInfoActivity target, View source) {
    this.target = target;

    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.mRecyclerView, "field 'mRecyclerView'", LRecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HtGetReadMeterInfoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mRecyclerView = null;
  }
}
