// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity.ht;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HtSetBookParameterActivity_ViewBinding implements Unbinder {
  private HtSetBookParameterActivity target;

  private View view2131690013;

  private View view2131689991;

  @UiThread
  public HtSetBookParameterActivity_ViewBinding(HtSetBookParameterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HtSetBookParameterActivity_ViewBinding(final HtSetBookParameterActivity target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.tvChoose, "field 'tvChoose' and method 'onViewClicked'");
    target.tvChoose = Utils.castView(view, R.id.tvChoose, "field 'tvChoose'", TextView.class);
    view2131690013 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvKuoPinYinZi = Utils.findRequiredViewAsType(source, R.id.tvKuoPinYinZi, "field 'tvKuoPinYinZi'", TextView.class);
    target.tvKuoPinXinDao = Utils.findRequiredViewAsType(source, R.id.tvKuoPinXinDao, "field 'tvKuoPinXinDao'", TextView.class);
    target.etSheZhiDongJieRi = Utils.findRequiredViewAsType(source, R.id.etSheZhiDongJieRi, "field 'etSheZhiDongJieRi'", EditText.class);
    target.etKaiChuangShiJian = Utils.findRequiredViewAsType(source, R.id.etKaiChuangShiJian, "field 'etKaiChuangShiJian'", EditText.class);
    target.rbNeed = Utils.findRequiredViewAsType(source, R.id.rbNeed, "field 'rbNeed'", CheckBox.class);
    view = Utils.findRequiredView(source, R.id.btSure, "field 'btSure' and method 'onViewClicked'");
    target.btSure = Utils.castView(view, R.id.btSure, "field 'btSure'", Button.class);
    view2131689991 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvNum = Utils.findRequiredViewAsType(source, R.id.tvNum, "field 'tvNum'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HtSetBookParameterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvChoose = null;
    target.tvKuoPinYinZi = null;
    target.tvKuoPinXinDao = null;
    target.etSheZhiDongJieRi = null;
    target.etKaiChuangShiJian = null;
    target.rbNeed = null;
    target.btSure = null;
    target.tvNum = null;

    view2131690013.setOnClickListener(null);
    view2131690013 = null;
    view2131689991.setOnClickListener(null);
    view2131689991 = null;
  }
}
