// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ct;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CtCopyBookActivity_ViewBinding implements Unbinder {
  private CtCopyBookActivity target;

  private View view2131689885;

  private View view2131689887;

  private View view2131689888;

  @UiThread
  public CtCopyBookActivity_ViewBinding(CtCopyBookActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CtCopyBookActivity_ViewBinding(final CtCopyBookActivity target, View source) {
    this.target = target;

    View view;
    target.etSearch = Utils.findRequiredViewAsType(source, R.id.etSearch, "field 'etSearch'", EditText.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.mRecyclerView, "field 'mRecyclerView'", LRecyclerView.class);
    target.ivChooseAll = Utils.findRequiredViewAsType(source, R.id.ivChooseAll, "field 'ivChooseAll'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.layoutChooseAll, "field 'layoutChooseAll' and method 'onViewClicked'");
    target.layoutChooseAll = Utils.castView(view, R.id.layoutChooseAll, "field 'layoutChooseAll'", LinearLayout.class);
    view2131689885 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btCopyChoose, "field 'btCopyChoose' and method 'onViewClicked'");
    target.btCopyChoose = Utils.castView(view, R.id.btCopyChoose, "field 'btCopyChoose'", Button.class);
    view2131689887 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btCopyAll, "field 'btCopyAll' and method 'onViewClicked'");
    target.btCopyAll = Utils.castView(view, R.id.btCopyAll, "field 'btCopyAll'", Button.class);
    view2131689888 = view;
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
    CtCopyBookActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etSearch = null;
    target.mRecyclerView = null;
    target.ivChooseAll = null;
    target.layoutChooseAll = null;
    target.btCopyChoose = null;
    target.btCopyAll = null;

    view2131689885.setOnClickListener(null);
    view2131689885 = null;
    view2131689887.setOnClickListener(null);
    view2131689887 = null;
    view2131689888.setOnClickListener(null);
    view2131689888 = null;
  }
}
