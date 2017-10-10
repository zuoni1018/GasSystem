// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtSetCopyChannelActivity_ViewBinding implements Unbinder {
  private HtSetCopyChannelActivity target;

  private View view2131689762;

  private View view2131689993;

  @UiThread
  public HtSetCopyChannelActivity_ViewBinding(HtSetCopyChannelActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtSetCopyChannelActivity_ViewBinding(final HtSetCopyChannelActivity target, View source) {
    this.target = target;

    View view;
    target.tvKuoPinYinZi = Utils.findRequiredViewAsType(source, R.id.tvKuoPinYinZi, "field 'tvKuoPinYinZi'", TextView.class);
    target.tvKuoPinXinDao = Utils.findRequiredViewAsType(source, R.id.tvKuoPinXinDao, "field 'tvKuoPinXinDao'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btChoose, "field 'btChoose' and method 'onViewClicked'");
    target.btChoose = Utils.castView(view, R.id.btChoose, "field 'btChoose'", Button.class);
    view2131689762 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btSure, "field 'btSure' and method 'onViewClicked'");
    target.btSure = Utils.castView(view, R.id.btSure, "field 'btSure'", Button.class);
    view2131689993 = view;
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
    HtSetCopyChannelActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvKuoPinYinZi = null;
    target.tvKuoPinXinDao = null;
    target.btChoose = null;
    target.btSure = null;

    view2131689762.setOnClickListener(null);
    view2131689762 = null;
    view2131689993.setOnClickListener(null);
    view2131689993 = null;
  }
}
