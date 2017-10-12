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

public class HtGetReadMeterInfoActivity_ViewBinding implements Unbinder {
  private HtGetReadMeterInfoActivity target;

  private View view2131690026;

  private View view2131689982;

  @UiThread
  public HtGetReadMeterInfoActivity_ViewBinding(HtGetReadMeterInfoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtGetReadMeterInfoActivity_ViewBinding(final HtGetReadMeterInfoActivity target,
      View source) {
    this.target = target;

    View view;
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.mRecyclerView, "field 'mRecyclerView'", LRecyclerView.class);
    target.etSearch = Utils.findRequiredViewAsType(source, R.id.etSearch, "field 'etSearch'", EditText.class);
    view = Utils.findRequiredView(source, R.id.tvChoose, "field 'tvChoose' and method 'onViewClicked'");
    target.tvChoose = Utils.castView(view, R.id.tvChoose, "field 'tvChoose'", TextView.class);
    view2131690026 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    target.layoutSearchBar = Utils.findRequiredViewAsType(source, R.id.layoutSearchBar, "field 'layoutSearchBar'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.bt2, "field 'bt2' and method 'onViewClicked'");
    target.bt2 = Utils.castView(view, R.id.bt2, "field 'bt2'", Button.class);
    view2131689982 = view;
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
    HtGetReadMeterInfoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mRecyclerView = null;
    target.etSearch = null;
    target.tvChoose = null;
    target.layoutSearchBar = null;
    target.bt2 = null;

    view2131690026.setOnClickListener(null);
    view2131690026 = null;
    view2131689982.setOnClickListener(null);
    view2131689982 = null;
  }
}
