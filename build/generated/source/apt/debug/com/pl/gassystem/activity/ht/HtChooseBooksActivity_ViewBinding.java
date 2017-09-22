// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtChooseBooksActivity_ViewBinding implements Unbinder {
  private HtChooseBooksActivity target;

  private View view2131689990;

  private View view2131689991;

  private View view2131689992;

  @UiThread
  public HtChooseBooksActivity_ViewBinding(HtChooseBooksActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtChooseBooksActivity_ViewBinding(final HtChooseBooksActivity target, View source) {
    this.target = target;

    View view;
    target.etSearch = Utils.findRequiredViewAsType(source, R.id.etSearch, "field 'etSearch'", EditText.class);
    view = Utils.findRequiredView(source, R.id.ivSelectAll, "field 'ivSelectAll' and method 'onViewClicked'");
    target.ivSelectAll = Utils.castView(view, R.id.ivSelectAll, "field 'ivSelectAll'", ImageView.class);
    view2131689990 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.mRecyclerView, "field 'mRecyclerView'", LRecyclerView.class);
    view = Utils.findRequiredView(source, R.id.btSure, "field 'btSure' and method 'onViewClicked'");
    target.btSure = Utils.castView(view, R.id.btSure, "field 'btSure'", Button.class);
    view2131689991 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btAdd, "method 'onViewClicked'");
    view2131689992 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    HtChooseBooksActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etSearch = null;
    target.ivSelectAll = null;
    target.mRecyclerView = null;
    target.btSure = null;

    view2131689990.setOnClickListener(null);
    view2131689990 = null;
    view2131689991.setOnClickListener(null);
    view2131689991 = null;
    view2131689992.setOnClickListener(null);
    view2131689992 = null;
  }
}
