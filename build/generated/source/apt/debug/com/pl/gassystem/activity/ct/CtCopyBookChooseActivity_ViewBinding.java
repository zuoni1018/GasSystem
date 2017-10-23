// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ct;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CtCopyBookChooseActivity_ViewBinding implements Unbinder {
  private CtCopyBookChooseActivity target;

  private View view2131689893;

  @UiThread
  public CtCopyBookChooseActivity_ViewBinding(CtCopyBookChooseActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CtCopyBookChooseActivity_ViewBinding(final CtCopyBookChooseActivity target, View source) {
    this.target = target;

    View view;
    target.etSearch = Utils.findRequiredViewAsType(source, R.id.etSearch, "field 'etSearch'", EditText.class);
    target.ivSearch = Utils.findRequiredViewAsType(source, R.id.ivSearch, "field 'ivSearch'", ImageView.class);
    target.tvSelectAll = Utils.findRequiredViewAsType(source, R.id.tvSelectAll, "field 'tvSelectAll'", TextView.class);
    target.rvCopyBookChooseList = Utils.findRequiredViewAsType(source, R.id.rvCopyBookChooseList, "field 'rvCopyBookChooseList'", LRecyclerView.class);
    target.tvCopyNum = Utils.findRequiredViewAsType(source, R.id.tvCopyNum, "field 'tvCopyNum'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btStartCopy, "field 'btStartCopy' and method 'onViewClicked'");
    target.btStartCopy = Utils.castView(view, R.id.btStartCopy, "field 'btStartCopy'", Button.class);
    view2131689893 = view;
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
    CtCopyBookChooseActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etSearch = null;
    target.ivSearch = null;
    target.tvSelectAll = null;
    target.rvCopyBookChooseList = null;
    target.tvCopyNum = null;
    target.btStartCopy = null;

    view2131689893.setOnClickListener(null);
    view2131689893 = null;
  }
}
