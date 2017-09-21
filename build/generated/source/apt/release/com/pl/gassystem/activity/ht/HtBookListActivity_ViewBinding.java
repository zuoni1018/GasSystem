// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtBookListActivity_ViewBinding implements Unbinder {
  private HtBookListActivity target;

  private View view2131689652;

  @UiThread
  public HtBookListActivity_ViewBinding(HtBookListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtBookListActivity_ViewBinding(final HtBookListActivity target, View source) {
    this.target = target;

    View view;
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.mRecyclerView, "field 'mRecyclerView'", LRecyclerView.class);
    view = Utils.findRequiredView(source, R.id.btGoCopy, "field 'btGoCopy' and method 'onViewClicked'");
    target.btGoCopy = Utils.castView(view, R.id.btGoCopy, "field 'btGoCopy'", Button.class);
    view2131689652 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    target.mRadioGroup = Utils.findRequiredViewAsType(source, R.id.mRadioGroup, "field 'mRadioGroup'", RadioGroup.class);
    target.etAddBook = Utils.findRequiredViewAsType(source, R.id.etAddBook, "field 'etAddBook'", EditText.class);
    target.tvAddBook = Utils.findRequiredViewAsType(source, R.id.tvAddBook, "field 'tvAddBook'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HtBookListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mRecyclerView = null;
    target.btGoCopy = null;
    target.mRadioGroup = null;
    target.etAddBook = null;
    target.tvAddBook = null;

    view2131689652.setOnClickListener(null);
    view2131689652 = null;
  }
}
