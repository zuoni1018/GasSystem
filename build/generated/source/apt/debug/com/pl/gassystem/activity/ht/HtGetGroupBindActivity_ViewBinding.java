// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
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

public class HtGetGroupBindActivity_ViewBinding implements Unbinder {
  private HtGetGroupBindActivity target;

  private View view2131690049;

  private View view2131689652;

  @UiThread
  public HtGetGroupBindActivity_ViewBinding(HtGetGroupBindActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtGetGroupBindActivity_ViewBinding(final HtGetGroupBindActivity target, View source) {
    this.target = target;

    View view;
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.mRecyclerView, "field 'mRecyclerView'", LRecyclerView.class);
    target.etSearch = Utils.findRequiredViewAsType(source, R.id.etSearch, "field 'etSearch'", EditText.class);
    view = Utils.findRequiredView(source, R.id.tvChoose, "field 'tvChoose' and method 'onViewClicked2'");
    target.tvChoose = Utils.castView(view, R.id.tvChoose, "field 'tvChoose'", TextView.class);
    view2131690049 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked2();
      }
    });
    target.layoutSearchBar = Utils.findRequiredViewAsType(source, R.id.layoutSearchBar, "field 'layoutSearchBar'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.btGoCopy, "field 'btGoCopy' and method 'onViewClicked'");
    target.btGoCopy = Utils.castView(view, R.id.btGoCopy, "field 'btGoCopy'", Button.class);
    view2131689652 = view;
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
    HtGetGroupBindActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mRecyclerView = null;
    target.etSearch = null;
    target.tvChoose = null;
    target.layoutSearchBar = null;
    target.btGoCopy = null;

    view2131690049.setOnClickListener(null);
    view2131690049 = null;
    view2131689652.setOnClickListener(null);
    view2131689652 = null;
  }
}
