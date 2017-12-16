// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtResultCopyActivity_ViewBinding implements Unbinder {
  private HtResultCopyActivity target;

  private View view2131690025;

  @UiThread
  public HtResultCopyActivity_ViewBinding(HtResultCopyActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtResultCopyActivity_ViewBinding(final HtResultCopyActivity target, View source) {
    this.target = target;

    View view;
    target.tvFunctionName = Utils.findRequiredViewAsType(source, R.id.tvFunctionName, "field 'tvFunctionName'", TextView.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.mRecyclerView, "field 'mRecyclerView'", LRecyclerView.class);
    view = Utils.findRequiredView(source, R.id.bt, "field 'bt' and method 'onViewClicked'");
    target.bt = Utils.castView(view, R.id.bt, "field 'bt'", Button.class);
    view2131690025 = view;
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
    HtResultCopyActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvFunctionName = null;
    target.mRecyclerView = null;
    target.bt = null;

    view2131690025.setOnClickListener(null);
    view2131690025 = null;
  }
}
