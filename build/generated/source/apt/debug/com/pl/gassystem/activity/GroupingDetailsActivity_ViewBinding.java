// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GroupingDetailsActivity_ViewBinding implements Unbinder {
  private GroupingDetailsActivity target;

  private View view2131689759;

  @UiThread
  public GroupingDetailsActivity_ViewBinding(GroupingDetailsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public GroupingDetailsActivity_ViewBinding(final GroupingDetailsActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.layoutMenu, "field 'layoutMenu' and method 'onViewClicked'");
    target.layoutMenu = Utils.castView(view, R.id.layoutMenu, "field 'layoutMenu'", LinearLayout.class);
    view2131689759 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    target.etSearch = Utils.findRequiredViewAsType(source, R.id.etSearch, "field 'etSearch'", EditText.class);
    target.tvAllNum = Utils.findRequiredViewAsType(source, R.id.tvAllNum, "field 'tvAllNum'", TextView.class);
    target.tvCopyNum = Utils.findRequiredViewAsType(source, R.id.tvCopyNum, "field 'tvCopyNum'", TextView.class);
    target.tvNoCopyNum = Utils.findRequiredViewAsType(source, R.id.tvNoCopyNum, "field 'tvNoCopyNum'", TextView.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.mRecyclerView, "field 'mRecyclerView'", LRecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GroupingDetailsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutMenu = null;
    target.etSearch = null;
    target.tvAllNum = null;
    target.tvCopyNum = null;
    target.tvNoCopyNum = null;
    target.mRecyclerView = null;

    view2131689759.setOnClickListener(null);
    view2131689759 = null;
  }
}
