package com.pl.gassystem;

import com.pl.bll.CopyBiz;
import com.pl.entity.CopyDataICRF;
import com.pl.utils.MeterType;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class CopyDataICRFDetailActivity extends Activity {

	private TextView tvTitlebar_name;
	private TextView tvCopyDataICRFDetailMeterNo,
			tvCopyDataICRFDetailMeterName, tvCopyDataICRFDetailCumulant,
			tvCopyDataICRFDetailSurplusMoney,
			tvCopyDataICRFDetailOverZeroMoney, tvCopyDataICRFDetailBuyTimes,
			tvCopyDataICRFDetailCurrMonthTotal,
			tvCopyDataICRFDetailLast1MonthTotal,
			tvCopyDataICRFDetailLast2MonthTotal,
			tvCopyDataICRFDetailLast3MonthTotal, tvCopyDataICRFDetailcopyWay,
			tvCopyDataICRFDetailcopyState, tvCopyDataICRFDetailcopyTime,
			tvCopyDataICRFDetailcopyMan, tvCopyDataICRFDetailOverFlowTimes,
			tvCopyDataICRFDetailMagAttTimes, tvCopyDataICRFDetailMeterState,
			tvCopyDataICRFDetailCardAttTimes, tvCopyDatalCRFDetailUnitPrice,
			tvCopyDataICRFDetailAccMoney, tvCopyDataICRFDetailAccBuyMoney,
			tvCopyDataICRFDetailCurrentShow;
	private CopyBiz copyBiz;
	private ImageButton btnquit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_copydataicrf_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_listview);
		tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_name);
		tvTitlebar_name.setText("表具详细信息");

		copyBiz = new CopyBiz(this);

		setupView();
		bindData();
		addListener();
	}

	private void setupView() {
		btnquit = (ImageButton) findViewById(R.id.btnquit);
		tvCopyDataICRFDetailMeterNo = (TextView) findViewById(R.id.tvCopyDataICRFDetailMeterNo);
		tvCopyDataICRFDetailMeterName = (TextView) findViewById(R.id.tvCopyDataICRFDetailMeterName);
		tvCopyDataICRFDetailCumulant = (TextView) findViewById(R.id.tvCopyDataICRFDetailCumulant);
		tvCopyDataICRFDetailSurplusMoney = (TextView) findViewById(R.id.tvCopyDataICRFDetailSurplusMoney);
		tvCopyDataICRFDetailOverZeroMoney = (TextView) findViewById(R.id.tvCopyDataICRFDetailOverZeroMoney);
		tvCopyDataICRFDetailBuyTimes = (TextView) findViewById(R.id.tvCopyDataICRFDetailBuyTimes);
		tvCopyDataICRFDetailCurrMonthTotal = (TextView) findViewById(R.id.tvCopyDataICRFDetailCurrMonthTotal);
		tvCopyDataICRFDetailLast1MonthTotal = (TextView) findViewById(R.id.tvCopyDataICRFDetailLast1MonthTotal);
		// tvCopyDataICRFDetailLast2MonthTotal = (TextView)
		// findViewById(R.id.tvCopyDataICRFDetailLast2MonthTotal);
		// tvCopyDataICRFDetailLast3MonthTotal = (TextView)
		// findViewById(R.id.tvCopyDataICRFDetailLast3MonthTotal);
		// tvCopyDataICRFDetailcopyWay = (TextView)
		// findViewById(R.id.tvCopyDataICRFDetailcopyWay);
		tvCopyDataICRFDetailcopyState = (TextView) findViewById(R.id.tvCopyDataICRFDetailcopyState);
		tvCopyDataICRFDetailcopyTime = (TextView) findViewById(R.id.tvCopyDataICRFDetailcopyTime);
		// tvCopyDataICRFDetailcopyMan = (TextView)
		// findViewById(R.id.tvCopyDataICRFDetailcopyMan);
		tvCopyDataICRFDetailOverFlowTimes = (TextView) findViewById(R.id.tvCopyDataICRFDetailOverFlowTimes);
		tvCopyDataICRFDetailMagAttTimes = (TextView) findViewById(R.id.tvCopyDataICRFDetailMagAttTimes);
		tvCopyDataICRFDetailMeterState = (TextView) findViewById(R.id.tvCopyDataICRFDetailMeterState);
		// tvCopyDataICRFDetailCardAttTimes = (TextView)
		// findViewById(R.id.tvCopyDataICRFDetailCardAttTimes);
		tvCopyDatalCRFDetailUnitPrice = (TextView) findViewById(R.id.tvCopyDatalCRFDetailUnitPrice);
		tvCopyDataICRFDetailAccMoney = (TextView) findViewById(R.id.tvCopyDataICRFDetailAccMoney);
		tvCopyDataICRFDetailAccBuyMoney = (TextView) findViewById(R.id.tvCopyDataICRFDetailAccBuyMoney);
		tvCopyDataICRFDetailCurrentShow = (TextView) findViewById(R.id.tvCopyDataICRFDetailCurrentShow);
	}

	private void bindData() {
		int Id = getIntent().getIntExtra("Id", 0);
		CopyDataICRF copyDataICRF = copyBiz.getCopyDataICRFById(Id + "");
		tvCopyDataICRFDetailMeterNo.setText(copyDataICRF.getMeterNo());
		tvCopyDataICRFDetailMeterName.setText(copyDataICRF.getMeterName());
		tvCopyDataICRFDetailCumulant.setText(copyDataICRF.getCumulant());
		tvCopyDataICRFDetailSurplusMoney
				.setText(copyDataICRF.getSurplusMoney());
		tvCopyDataICRFDetailOverZeroMoney.setText(copyDataICRF
				.getOverZeroMoney());
		tvCopyDataICRFDetailBuyTimes.setText(copyDataICRF.getBuyTimes() + "");
		tvCopyDataICRFDetailCurrMonthTotal.setText(copyDataICRF
				.getCurrMonthTotal());
		tvCopyDataICRFDetailLast1MonthTotal.setText(copyDataICRF
				.getLast1MonthTotal());
		// tvCopyDataICRFDetailLast2MonthTotal.setText(copyDataICRF.getLast2MonthTotal());
		// tvCopyDataICRFDetailLast3MonthTotal.setText(copyDataICRF.getLast3MonthTotal());
		// tvCopyDataICRFDetailcopyWay.setText(MeterType.GetCopyWay(copyDataICRF.getCopyWay()));
		tvCopyDataICRFDetailcopyState.setText(MeterType
				.GetCopyState(copyDataICRF.getCopyState()));
		tvCopyDataICRFDetailcopyTime.setText(copyDataICRF.getCopyTime());
		// tvCopyDataICRFDetailcopyMan.setText(copyDataICRF.getCopyMan());
		tvCopyDataICRFDetailOverFlowTimes.setText(copyDataICRF
				.getOverFlowTimes() + "");
		tvCopyDataICRFDetailMagAttTimes.setText(copyDataICRF.getMagAttTimes()
				+ "");
		tvCopyDatalCRFDetailUnitPrice.setText(copyDataICRF.getUnitPrice());
		tvCopyDataICRFDetailAccMoney.setText(copyDataICRF.getAccMoney());
		tvCopyDataICRFDetailAccBuyMoney.setText(copyDataICRF.getAccBuyMoney());
		tvCopyDataICRFDetailCurrentShow.setText(copyDataICRF.getCurrentShow());
		String state = MeterType.GetICMeterStateMsg(copyDataICRF
				.getMeterState());
		if (!state.equals("正常")) {
			tvCopyDataICRFDetailMeterState.setTextColor(Color.RED);
		}
		tvCopyDataICRFDetailMeterState.setText(state);

	}

	private void addListener() {
		btnquit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.copy_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
