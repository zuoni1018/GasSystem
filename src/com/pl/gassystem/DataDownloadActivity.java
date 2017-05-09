package com.pl.gassystem;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pl.bll.BookInfoBiz;
import com.pl.bll.CopyBiz;
import com.pl.bll.GroupBindBiz;
import com.pl.bll.GroupInfoBiz;
import com.pl.bll.SetBiz;
import com.pl.bll.XmlParser;
import com.pl.entity.BookInfo;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.entity.GroupBind;
import com.pl.entity.GroupInfo;
import com.pl.utils.GlobalConsts;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DataDownloadActivity extends Activity {

	private TextView tvTitlebar_name, tvDataDownTips, tvDataDownTips2;
	private ImageButton btnOnlybackQuit;
	private ImageView imgDataDownTips;
	private ProgressBar pgbDataDownTips;
	private final static int getBookInfoSuccess = 1;
	private final static int getGroupInfoSuccess = 2;
	private final static int getGroupBindSuccess = 3;
	private final static int getCopyDataSuccess = 4;
	private final static int uploadCopyDataSuccess = 5;
	private final static int saveCopyDataSuccess = 6;
	private final static int saveProgress = 7;
	private final static int getBookInfoFailure = 11;
	private final static int getGroupInfoFailure = 12;
	private final static int getGroupBindFailure = 13;
	private final static int getCopyDataFailure = 14;
	private final static int uploadCopyDataFailure = 15;
	private final static int saveCopyDataFailure = 16;

	private String bookNo;
	private ArrayList<BookInfo> bookInfos;
	private ArrayList<GroupInfo> groupInfos;
	private ArrayList<GroupBind> groupBinds;
	private ArrayList<CopyData> copyDatas;
	private ArrayList<CopyDataICRF> copyDataICRFs;
	private String meterTypeNo;
	private int bookInfoType; // 操作模式
	private BookInfoBiz bookInfoBiz;
	private GroupInfoBiz groupInfoBiz;
	private GroupBindBiz groupBindBiz;
	private CopyBiz copyBiz;
	private SetBiz setBiz;
	private ArrayList<GroupInfo> upLoadGroupInfos; // 需上传分组
	private int upCount;
	private ProgressBar pgbSaveing;
	// private int downCount;

	private static String serverUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_data_download);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);
		tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
		tvTitlebar_name.setText("数据管理");

		tvDataDownTips = (TextView) findViewById(R.id.tvDataDownTips);
		tvDataDownTips2 = (TextView) findViewById(R.id.tvDataDownTips2);
		tvDataDownTips2.setVisibility(View.INVISIBLE);
		btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
		imgDataDownTips = (ImageView) findViewById(R.id.imgDataDownTips);
		pgbDataDownTips = (ProgressBar) findViewById(R.id.pgbDataDownTips);
		imgDataDownTips.setVisibility(View.GONE);
		pgbSaveing = (ProgressBar) findViewById(R.id.pgbSaveing);

		bookNo = getIntent().getStringExtra("bookNo");
		meterTypeNo = getIntent().getStringExtra("meterTypeNo").toString();
		addListener();
		bookInfoBiz = new BookInfoBiz(this);
		groupInfoBiz = new GroupInfoBiz(this);
		groupBindBiz = new GroupBindBiz(this);
		copyBiz = new CopyBiz(this);
		setBiz = new SetBiz(this);
		serverUrl = setBiz.getBookInfoUrl();

		bookInfoType = getIntent().getIntExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_COPY);
		if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_DOWNLOAD) {
			tvDataDownTips.setText("正在下载数据，请稍后");
			getBookInfoData();
		} else if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_UPLOAD) {
			tvDataDownTips.setText("正在上传数据，请稍后");
			getUpLoadInfos();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case getBookInfoSuccess:
				// meterTypeNo = bookInfos.get(0).getMeterTypeNo();
				getGroupInfoData();
				break;
			case getGroupInfoSuccess:
				getGroupBindData();
				break;
			case getGroupBindSuccess:
				if (meterTypeNo.equals("04")) {
					getCopyDataICRF();
				} else if (meterTypeNo.equals("05")) {
					getCopyData();
				}
				break;
			case getCopyDataSuccess:
				tvDataDownTips.setText("正在保存下载数据，请稍后");
				pgbSaveing.setMax(groupBinds.size() * 2);
				new Thread() {
					@Override
					public void run() {
						// 将数据存储至本地数据库
						for (int i = 0; i < bookInfos.size(); i++) {
							bookInfoBiz.addBookInfo(bookInfos.get(i));
						}
						for (int j = 0; j < groupInfos.size(); j++) {
							groupInfoBiz.addGroupInfo(groupInfos.get(j));
						}
						for (int j2 = 0; j2 < groupBinds.size(); j2++) {
							groupBindBiz.addGroupBind(groupBinds.get(j2));
							handler.sendEmptyMessage(saveProgress);
						}
						if (meterTypeNo.equals("04")) {
							for (int k = 0; k < copyDataICRFs.size(); k++) {
								copyBiz.addCopyDataICRF(copyDataICRFs.get(k));
								handler.sendEmptyMessage(saveProgress);
							}
						} else if (meterTypeNo.equals("05")) {
							for (int k = 0; k < copyDatas.size(); k++) {
								copyBiz.addCopyData(copyDatas.get(k));
								handler.sendEmptyMessage(saveProgress);
							}
						}
						if (meterTypeNo.equals("04")) {
							Message msg = Message.obtain(handler,
									saveCopyDataSuccess, copyDataICRFs.size());
							handler.sendMessage(msg);
						} else if (meterTypeNo.equals("05")) {
							Message msg = Message.obtain(handler,
									saveCopyDataSuccess, copyDatas.size());
							handler.sendMessage(msg);
						}
					};
				}.start();
				break;
			case saveProgress:
				pgbSaveing.incrementProgressBy(1);
				break;
			case saveCopyDataSuccess:
				tvDataDownTips.setText("下载完毕，总计导入" + msg.obj.toString()
						+ "条表具数据");
				pgbDataDownTips.setVisibility(View.GONE);
				pgbSaveing.setVisibility(View.GONE);
				imgDataDownTips.setImageResource(R.drawable.ivdatasuccess);
				imgDataDownTips.setVisibility(View.VISIBLE);
				tvDataDownTips2.setVisibility(View.VISIBLE);
				new Thread() {
					@Override
					public void run() {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						finish();
					};
				}.start();
				break;
			case uploadCopyDataSuccess:
				if (upCount < upLoadGroupInfos.size() - 1) {
					upCount++;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					upLoadCopyData();
				} else {
					tvDataDownTips.setText("数据上传成功");
					pgbDataDownTips.setVisibility(View.GONE);
					pgbSaveing.setVisibility(View.GONE);
					imgDataDownTips.setImageResource(R.drawable.ivdatasuccess);
					imgDataDownTips.setVisibility(View.VISIBLE);
					tvDataDownTips2.setVisibility(View.VISIBLE);
					new Thread() {
						@Override
						public void run() {
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
							finish();
						};
					}.start();
				}
				break;
			case getBookInfoFailure:
				tvDataDownTips.setText("账册下载失败，请检查数据源");
				pgbDataDownTips.setVisibility(View.GONE);
				pgbSaveing.setVisibility(View.GONE);
				imgDataDownTips.setImageResource(R.drawable.ivdatafailure);
				imgDataDownTips.setVisibility(View.VISIBLE);
				tvDataDownTips2.setVisibility(View.INVISIBLE);
				break;
			case getGroupInfoFailure:
				tvDataDownTips.setText("分组下载失败，请检查数据源");
				pgbDataDownTips.setVisibility(View.GONE);
				pgbSaveing.setVisibility(View.GONE);
				imgDataDownTips.setImageResource(R.drawable.ivdatafailure);
				imgDataDownTips.setVisibility(View.VISIBLE);
				tvDataDownTips2.setVisibility(View.INVISIBLE);
				break;
			case getGroupBindFailure:
				tvDataDownTips.setText("表具绑定下载失败，请检查数据源");
				pgbDataDownTips.setVisibility(View.GONE);
				pgbSaveing.setVisibility(View.GONE);
				imgDataDownTips.setImageResource(R.drawable.ivdatafailure);
				imgDataDownTips.setVisibility(View.VISIBLE);
				tvDataDownTips2.setVisibility(View.INVISIBLE);
				break;
			case getCopyDataFailure:
				tvDataDownTips.setText("抄表数据下载失败，请检查数据源");
				pgbDataDownTips.setVisibility(View.GONE);
				pgbSaveing.setVisibility(View.GONE);
				imgDataDownTips.setImageResource(R.drawable.ivdatafailure);
				imgDataDownTips.setVisibility(View.VISIBLE);
				tvDataDownTips2.setVisibility(View.INVISIBLE);
				break;
			case saveCopyDataFailure:
				tvDataDownTips.setText("保存下载数据失败，请检查数据源");
				pgbDataDownTips.setVisibility(View.GONE);
				pgbSaveing.setVisibility(View.GONE);
				imgDataDownTips.setImageResource(R.drawable.ivdatafailure);
				imgDataDownTips.setVisibility(View.VISIBLE);
				tvDataDownTips2.setVisibility(View.INVISIBLE);
				break;
			case uploadCopyDataFailure:
				tvDataDownTips.setText("数据上传失败，请检查上传服务器");
				pgbDataDownTips.setVisibility(View.GONE);
				pgbSaveing.setVisibility(View.GONE);
				imgDataDownTips.setImageResource(R.drawable.ivdatafailure);
				imgDataDownTips.setVisibility(View.VISIBLE);
				tvDataDownTips2.setVisibility(View.INVISIBLE);
			}
		}
	};

	private void addListener() {
		btnOnlybackQuit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
	}

	private void getUpLoadInfos() {
		upCount = 0;
		upLoadGroupInfos = groupInfoBiz.getGroupInfos(bookNo);
		if (meterTypeNo.equals("04")) {
			upLoadCopyDataICRF();
		} else if (meterTypeNo.equals("05")) {
			upLoadCopyData();
		}

	}

	private void upLoadCopyDataICRF() {

	}

	private void upLoadCopyData() {
		String url = serverUrl + "WebMain.asmx/AddCopydata";
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		ArrayList<String> meterNos = groupBindBiz
				.getMeterNoByGroupNo(upLoadGroupInfos.get(upCount).getGroupNo());
		ArrayList<CopyData> copyDatas = copyBiz.getCopyDataByMeterNos(meterNos,
				2);
		tvDataDownTips.setText("正在上传分组："
				+ upLoadGroupInfos.get(upCount).getGroupName() + "数据");
		try {
			JSONObject json = new JSONObject();
			JSONArray jsonMembers = new JSONArray();
			for (int j = 0; j < copyDatas.size(); j++) {
				CopyData copyData = copyDatas.get(j);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("meterNo", copyData.getMeterNo());
				jsonObject.put("lastShow", copyData.getLastShow());
				jsonObject.put("lastDosage", copyData.getLastDosage());
				jsonObject.put("currentShow", copyData.getCurrentShow());
				jsonObject.put("currentDosage", copyData.getCurrentDosage());
				jsonObject.put("unitPrice", copyData.getUnitPrice());
				jsonObject.put("printFlag", copyData.getPrintFlag());
				jsonObject.put("meterState", copyData.getMeterState());
				jsonObject.put("copyWay", copyData.getCopyWay());
				jsonObject.put("copyState", copyData.getCopyState());
				jsonObject.put("copyTime", copyData.getCopyTime());
				jsonObject.put("copyMan", copyData.getCopyMan());
				jsonObject.put("Operator", copyData.getOperator());
				jsonObject.put("operateTime", copyData.getOperateTime());
				jsonObject.put("isBalance", copyData.getIsBalance());
				jsonObject.put("Remark", copyData.getRemark());
				jsonObject.put("meterName", copyData.getMeterName());
				jsonObject.put("dBm", copyData.getdBm());
				jsonObject.put("elec", copyData.getElec());
				jsonMembers.put(jsonObject);
			}
			json.put("copydata", jsonMembers);
			String result = json.toString();
			result = result.replace("{\"copydata\":", "");
			result = result.substring(0, result.length() - 1);
			// Log.i("json", result);

			params.put("list", result);
			client.post(url, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					Message msg = Message
							.obtain(handler, uploadCopyDataSuccess);
					handler.sendMessage(msg);
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					Message msg = Message
							.obtain(handler, uploadCopyDataFailure);
					handler.sendMessage(msg);
				}
			});
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	private void getCopyDataICRF() {
		String url = serverUrl + "WebMain.asmx/GetCopydataICRF";
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("bookNo", bookNo);
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				InputStream in = new ByteArrayInputStream(arg2);
				XmlParser parser = new XmlParser();
				try {
					copyDataICRFs = parser.parseCopyDataICRFs(in);
					in.close();
					if (copyDataICRFs != null) {
						Message msg = Message.obtain(handler, getCopyDataSuccess);
						handler.sendMessage(msg);
					} else {
						Message msg = Message.obtain(handler, getCopyDataFailure);
						handler.sendMessage(msg);
					}
				} catch (XmlPullParserException | IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Message msg = Message.obtain(handler, getCopyDataFailure);
				handler.sendMessage(msg);
			}
		});
	}

	private void getCopyData() {
		String url = serverUrl + "WebMain.asmx/GetCopydata";
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("bookNo", bookNo);
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				InputStream in = new ByteArrayInputStream(arg2);
				XmlParser parser = new XmlParser();
				try {
					copyDatas = parser.parseCopyDatas(in);
					in.close();
					if (copyDatas != null) {
						Message msg = Message.obtain(handler,
								getCopyDataSuccess);
						handler.sendMessage(msg);
					} else {
						Message msg = Message.obtain(handler,
								getCopyDataFailure);
						handler.sendMessage(msg);
					}
				} catch (XmlPullParserException | IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Message msg = Message.obtain(handler, getCopyDataFailure);
				handler.sendMessage(msg);
			}
		});
	}

	private void getBookInfoData() {
		// downCount = 0;
		String url = serverUrl + "WebMain.asmx/GetBookInfo";
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("bookNo", bookNo);
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				InputStream in = new ByteArrayInputStream(arg2);
				XmlParser parser = new XmlParser();
				try {
					bookInfos = parser.parseBookInfos(in);
					in.close();
					if (bookInfos != null) {
						Message msg = Message.obtain(handler, getBookInfoSuccess);
						handler.sendMessage(msg);
					} else {
						Message msg = Message.obtain(handler, getBookInfoFailure);
						handler.sendMessage(msg);
					}
				} catch (XmlPullParserException | IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Message msg = Message.obtain(handler, getBookInfoFailure);
				handler.sendMessage(msg);
			}
		});
	}

	private void getGroupInfoData() {
		AsyncHttpClient client = new AsyncHttpClient();
		String url = serverUrl + "WebMain.asmx/GetGroupInfo";
		RequestParams params = new RequestParams();
		params.put("bookNo", bookNo);
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Message msg = Message.obtain(handler, getGroupInfoFailure);
				handler.sendMessage(msg);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				InputStream in = new ByteArrayInputStream(arg2);
				XmlParser parser = new XmlParser();
				try {
					groupInfos = parser.parseGroupInfos(in);
					in.close();
					if (groupInfos != null) {
						Message msg = Message.obtain(handler, getGroupInfoSuccess);
						handler.sendMessage(msg);
					} else {
						Message msg = Message.obtain(handler, getGroupInfoFailure);
						handler.sendMessage(msg);
					}
				} catch (XmlPullParserException | IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}

		});
	}

	private void getGroupBindData() {
		AsyncHttpClient client = new AsyncHttpClient();
		String url = serverUrl + "WebMain.asmx/GetGroupBind";
		RequestParams params = new RequestParams();
		params.put("bookNo", bookNo);
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Message msg = Message.obtain(handler, getGroupBindFailure);
				handler.sendMessage(msg);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				InputStream in = new ByteArrayInputStream(arg2);
				XmlParser parser = new XmlParser();
				try {
					groupBinds = parser.parseGroupBind(in);
					in.close();
					if (groupBinds != null) {
						Message msg = Message.obtain(handler,
								getGroupBindSuccess);
						handler.sendMessage(msg);
					} else {
						Message msg = Message.obtain(handler,
								getGroupBindFailure);
						handler.sendMessage(msg);
					}
				} catch (XmlPullParserException | IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}

		});
	}

}
