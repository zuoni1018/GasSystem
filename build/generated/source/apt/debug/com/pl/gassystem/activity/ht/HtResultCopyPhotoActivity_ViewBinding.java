// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtResultCopyPhotoActivity_ViewBinding implements Unbinder {
  private HtResultCopyPhotoActivity target;

  private View view2131690019;

  @UiThread
  public HtResultCopyPhotoActivity_ViewBinding(HtResultCopyPhotoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtResultCopyPhotoActivity_ViewBinding(final HtResultCopyPhotoActivity target,
      View source) {
    this.target = target;

    View view;
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.mRecyclerView, "field 'mRecyclerView'", LRecyclerView.class);
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
    HtResultCopyPhotoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mRecyclerView = null;

    view2131690019.setOnClickListener(null);
    view2131690019 = null;
  }
}
