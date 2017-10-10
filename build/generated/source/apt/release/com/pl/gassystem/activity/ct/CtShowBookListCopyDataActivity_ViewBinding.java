// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ct;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CtShowBookListCopyDataActivity_ViewBinding implements Unbinder {
  private CtShowBookListCopyDataActivity target;

  @UiThread
  public CtShowBookListCopyDataActivity_ViewBinding(CtShowBookListCopyDataActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CtShowBookListCopyDataActivity_ViewBinding(CtShowBookListCopyDataActivity target,
      View source) {
    this.target = target;

    target.tvBookNum = Utils.findRequiredViewAsType(source, R.id.tvBookNum, "field 'tvBookNum'", TextView.class);
    target.etSearch = Utils.findRequiredViewAsType(source, R.id.etSearch, "field 'etSearch'", EditText.class);
    target.rvBookList = Utils.findRequiredViewAsType(source, R.id.rvBookList, "field 'rvBookList'", LRecyclerView.class);
    target.btBeginCopy = Utils.findRequiredViewAsType(source, R.id.btBeginCopy, "field 'btBeginCopy'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CtShowBookListCopyDataActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvBookNum = null;
    target.etSearch = null;
    target.rvBookList = null;
    target.btBeginCopy = null;
  }
}
