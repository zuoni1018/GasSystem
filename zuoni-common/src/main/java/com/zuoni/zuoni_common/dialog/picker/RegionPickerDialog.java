//package com.zuoni.zuoni_common.dialog.picker;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//
//import com.zuoni.zuoni_common.R;
//import com.zuoni.zuoni_common.dialog.picker.view.LoopListener;
//import com.zuoni.zuoni_common.dialog.picker.view.LoopView;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class RegionPickerDialog extends Dialog  {
//
////    private Params params;
////
////    public RegionPickerDialog(Context context, int themeResId) {
////        super(context, themeResId);
////    }
////
////    private void setParams(Params params) {
////        this.params = params;
////    }
////
////
////    public interface OnRegionSelectedListener {
////        void onRegionSelected(String[] cityAndArea);
////    }
////
////
////    private static final class Params {
////        private boolean shadow = true;
////        private boolean canCancel = true;
////        private LoopView loopCity;
////        private LoopView loopArea;
////        private int initSelection;
////        private OnRegionSelectedListener callback;
////        private Map<String, List<String>> dataList;//键值对
////
////    }
////
////    public static class Builder {
////        private final Context context;
////        private final Params params;
////
////        public Builder(Context context) {
////            this.context = context;
////            params = new Params();
////
////            try {
////                InputStreamReader inputReader = new InputStreamReader(context.getAssets().open("city_data.json"));
////                BufferedReader bufReader = new BufferedReader(inputReader);
////                String line = "";
////                StringBuffer result = new StringBuffer();
////                while ((line = bufReader.readLine()) != null) {
////                    result.append(line);
////                }
////                //获得所有键值对
////                params.dataList = new Gson().fromJson(result.toString(), new TypeToken<Map<String, List<String>>>() {
////                }.getType());
////            } catch (Exception e) {
////                Log.e("RegionPickerDialog", "The Region source file does not exist or has been damaged");
////                params.dataList = new HashMap<>();
////            }
////        }
////
////        private final String[] getCurrRegionValue() {
////            return new String[]{params.loopCity.getCurrentItemValue(), params.loopArea.getCurrentItemValue()};
////        }
////
////        public Builder setSelection(int initSelection) {
////            params.initSelection = initSelection;
////            return this;
////        }
////
////        public Builder setOnRegionSelectedListener(OnRegionSelectedListener onRegionSelectedListener) {
////            params.callback = onRegionSelectedListener;
////            return this;
////        }
////
////        public RegionPickerDialog create() {
////            final RegionPickerDialog dialog = new RegionPickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
////            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_region, null);
////
////            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    dialog.dismiss();
////                    params.callback.onRegionSelected(getCurrRegionValue());
////                }
////            });
////
////            final LoopView loopCity = (LoopView) view.findViewById(R.id.loop_city);
////            loopCity.setArrayList(new ArrayList(params.dataList.keySet()));
////            loopCity.setNotLoop();
////            if (params.dataList.size() > 0) loopCity.setCurrentItem(params.initSelection);
////
////            final LoopView loopArea = (LoopView) view.findViewById(R.id.loop_area);
////            String selectedCity = loopCity.getCurrentItemValue();
////            loopArea.setArrayList(params.dataList.get(selectedCity));
////            loopArea.setNotLoop();
////
////            loopCity.setListener(new LoopListener() {
////                @Override
////                public void onItemSelect(int item) {
////                    String selectedCity = loopCity.getCurrentItemValue();
////                    loopArea.setArrayList(params.dataList.get(selectedCity));
////                }
////            });
////
////            Window win = dialog.getWindow();
////            win.getDecorView().setPadding(0, 0, 0, 0);
////            WindowManager.LayoutParams lp = win.getAttributes();
////            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
////            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
////            win.setAttributes(lp);
////            win.setGravity(Gravity.BOTTOM);
////            win.setWindowAnimations(R.style.Animation_Bottom_Rising);
////
////            dialog.setContentView(view);
////            dialog.setCanceledOnTouchOutside(params.canCancel);
////            dialog.setCancelable(params.canCancel);
////
////            params.loopCity = loopCity;
////            params.loopArea = loopArea;
////            dialog.setParams(params);
////
////            return dialog;
////        }
////    }
//}
