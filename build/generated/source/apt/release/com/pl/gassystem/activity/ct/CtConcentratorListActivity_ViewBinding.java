// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ct;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CtConcentratorListActivity_ViewBinding implements Unbinder {
  private CtConcentratorListActivity target;

  @UiThread
  public CtConcentratorListActivity_ViewBinding(CtConcentratorListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CtConcentratorListActivity_ViewBinding(CtConcentratorListActivity target, View source) {
    this.target = target;

    target.etSearchConcentrator = Utils.findRequiredViewAsType(source, R.id.etSearchConcentrator, "field 'etSearchConcentrator'", EditText.class);
    target.RvConcentratorList = Utils.findRequiredViewAsType(source, R.id.RvConcentratorList, "field 'RvConcentratorList'", LRecyclerView.class);
    target.tvAllNum = Utils.findRequiredViewAsType(source, R.id.tvAllNum, "field 'tvAllNum'", TextView.class);
    target.tvReadNum = Utils.findRequiredViewAsType(source, R.id.tvReadNum, "field 'tvReadNum'", TextView.class);
    target.tvNoReadNum = Utils.findRequiredViewAsType(source, R.id.tvNoReadNum, "field 'tvNoReadNum'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CtConcentratorListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etSearchConcentrator = null;
    target.RvConcentratorList = null;
    target.tvAllNum = null;
    target.tvReadNum = null;
    target.tvNoReadNum = null;
  }
}
