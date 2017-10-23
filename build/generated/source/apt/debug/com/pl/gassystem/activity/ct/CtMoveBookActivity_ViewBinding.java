// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ct;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CtMoveBookActivity_ViewBinding implements Unbinder {
  private CtMoveBookActivity target;

  private View view2131689904;

  @UiThread
  public CtMoveBookActivity_ViewBinding(CtMoveBookActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CtMoveBookActivity_ViewBinding(final CtMoveBookActivity target, View source) {
    this.target = target;

    View view;
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.mRecyclerView, "field 'mRecyclerView'", LRecyclerView.class);
    view = Utils.findRequiredView(source, R.id.btMove, "field 'btMove' and method 'onViewClicked'");
    target.btMove = Utils.castView(view, R.id.btMove, "field 'btMove'", Button.class);
    view2131689904 = view;
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
    CtMoveBookActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mRecyclerView = null;
    target.btMove = null;

    view2131689904.setOnClickListener(null);
    view2131689904 = null;
  }
}
