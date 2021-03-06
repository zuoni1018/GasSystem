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

  private View view2131690000;

  private View view2131690001;

  private View view2131690002;

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
    view2131690000 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.mRecyclerView, "field 'mRecyclerView'", LRecyclerView.class);
    view = Utils.findRequiredView(source, R.id.btSure, "field 'btSure' and method 'onViewClicked'");
    target.btSure = Utils.castView(view, R.id.btSure, "field 'btSure'", Button.class);
    view2131690001 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btAdd, "method 'onViewClicked'");
    view2131690002 = view;
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

    view2131690000.setOnClickListener(null);
    view2131690000 = null;
    view2131690001.setOnClickListener(null);
    view2131690001 = null;
    view2131690002.setOnClickListener(null);
    view2131690002 = null;
  }
}
