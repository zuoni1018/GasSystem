// Generated code from Butter Knife. Do not modify!
package com.pl.gassystem.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.pl.gassystem.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DataManageActivity_ViewBinding implements Unbinder {
  private DataManageActivity target;

  private View view2131689720;

  private View view2131689721;

  private View view2131689722;

  private View view2131689723;

  private View view2131689724;

  @UiThread
  public DataManageActivity_ViewBinding(DataManageActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DataManageActivity_ViewBinding(final DataManageActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.linDataDownload, "method 'onViewClicked'");
    view2131689720 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.linDataUpdate, "method 'onViewClicked'");
    view2131689721 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.linDataDelete, "method 'onViewClicked'");
    view2131689722 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutExcelBooksImport, "method 'onViewClicked'");
    view2131689723 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layoutExcelXiNingUserInfoImport, "method 'onViewClicked'");
    view2131689724 = view;
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
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view2131689720.setOnClickListener(null);
    view2131689720 = null;
    view2131689721.setOnClickListener(null);
    view2131689721 = null;
    view2131689722.setOnClickListener(null);
    view2131689722 = null;
    view2131689723.setOnClickListener(null);
    view2131689723 = null;
    view2131689724.setOnClickListener(null);
    view2131689724 = null;
  }
}
