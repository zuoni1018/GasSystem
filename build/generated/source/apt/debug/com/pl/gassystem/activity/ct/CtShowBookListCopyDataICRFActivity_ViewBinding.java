// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ct;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CtShowBookListCopyDataICRFActivity_ViewBinding implements Unbinder {
  private CtShowBookListCopyDataICRFActivity target;

  private View view2131689895;

  @UiThread
  public CtShowBookListCopyDataICRFActivity_ViewBinding(CtShowBookListCopyDataICRFActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CtShowBookListCopyDataICRFActivity_ViewBinding(final CtShowBookListCopyDataICRFActivity target,
      View source) {
    this.target = target;

    View view;
    target.tvBookNum = Utils.findRequiredViewAsType(source, R.id.tvBookNum, "field 'tvBookNum'", TextView.class);
    target.etSearch = Utils.findRequiredViewAsType(source, R.id.etSearch, "field 'etSearch'", EditText.class);
    target.rvBookList = Utils.findRequiredViewAsType(source, R.id.rvBookList, "field 'rvBookList'", LRecyclerView.class);
    view = Utils.findRequiredView(source, R.id.btBeginCopy, "field 'btBeginCopy' and method 'onViewClicked'");
    target.btBeginCopy = Utils.castView(view, R.id.btBeginCopy, "field 'btBeginCopy'", Button.class);
    view2131689895 = view;
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
    CtShowBookListCopyDataICRFActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvBookNum = null;
    target.etSearch = null;
    target.rvBookList = null;
    target.btBeginCopy = null;

    view2131689895.setOnClickListener(null);
    view2131689895 = null;
  }
}
