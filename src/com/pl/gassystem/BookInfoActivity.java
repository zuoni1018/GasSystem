package com.pl.gassystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pl.adapter.BookInfoAdapter;
import com.pl.bll.BookInfoBiz;
import com.pl.bll.GroupBindBiz;
import com.pl.bll.GroupInfoBiz;
import com.pl.bll.SetBiz;
import com.pl.bll.XmlParser;
import com.pl.common.NetWorkManager;
import com.pl.entity.BookInfo;
import com.pl.entity.GroupInfo;
import com.pl.utils.GlobalConsts;
import org.apache.http.Header;
import org.xmlpull.v1.XmlPullParserException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * 抄表界面
 */
public class BookInfoActivity extends Activity {

    private ListView lvBookInfo;
    private BookInfoAdapter adapter;
    private BookInfoBiz bookInfoBiz;
    private ImageButton btnquit, btnmenu;
    private TextView tvTitlebar_name;
    private SetBiz setBiz;

    private static final int MENU_CTX_UPDATE = 1;
    private static final int MENU_CTX_DELETE = 2;

    private int bookInfoType; // 操作模式
    private ArrayList<BookInfo> bookInfos;

    private final static int getBookInfoSuccess = 1;
    private final static int getBookInfoFailure = 11;

    private static String serverUrl;

    private void setupView() {
        btnquit = (ImageButton) findViewById(R.id.btnquit);
        btnmenu = (ImageButton) findViewById(R.id.btnmenu);
        lvBookInfo = (ListView) findViewById(R.id.lvBookInfo);
        if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_COPY) {
            bookInfos = bookInfoBiz.getBookInfos();//从本地数据库中拿信息
            adapter = new BookInfoAdapter(this, bookInfos);
            lvBookInfo.setAdapter(adapter);
        } else if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_DOWNLOAD) {
            getBookInfoData();
            btnmenu.setVisibility(View.INVISIBLE);
        } else if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_UPLOAD) {
            bookInfos = bookInfoBiz.getBookInfos();
            adapter = new BookInfoAdapter(this, bookInfos);
            lvBookInfo.setAdapter(adapter);
            btnmenu.setVisibility(View.INVISIBLE);
        } else if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_SELECT) {
            bookInfos = bookInfoBiz.getBookInfos();
            adapter = new BookInfoAdapter(this, bookInfos);
            lvBookInfo.setAdapter(adapter);
            btnmenu.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_book_info);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_listview);

        bookInfoType = getIntent().getIntExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_COPY);
        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_name);


        //判断是从哪个界面点进来的
        //如果是从抄表按钮、统计查询点进来的
        if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_COPY || bookInfoType == GlobalConsts.BOOKINFO_TYPE_SELECT) {
            tvTitlebar_name.setText("选择账册");
        } else {
            if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_UPLOAD) {
                tvTitlebar_name.setText("选择要上传的账册");
            } else if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_DOWNLOAD) {
                tvTitlebar_name.setText("选择要下载的账册");
            }
            setBiz = new SetBiz(this);
            serverUrl = setBiz.getBookInfoUrl();
            // 检测网络
            if (!NetWorkManager.isConnect(this)) {
                new AlertDialog.Builder(this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog)
                        .setTitle("网络错误")
                        .setMessage("网络连接失败，请先连接到网络。")
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                            }
                        }).show();
            }
        }

        bookInfoBiz = new BookInfoBiz(this);
        setupView();
        addListener();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case getBookInfoSuccess:
                    adapter = new BookInfoAdapter(BookInfoActivity.this, bookInfos);
                    lvBookInfo.setAdapter(adapter);
                    break;
                case getBookInfoFailure:
                    Toast.makeText(BookInfoActivity.this, "从服务器获取账册信息失败，请检查！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void getBookInfoData() {
        String url = serverUrl + "WebMain.asmx/GetBookInfo";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("bookNo", "");
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                InputStream in = new ByteArrayInputStream(arg2);
                XmlParser parser = new XmlParser();
                try {
                    bookInfos = parser.parseBookInfos(in);
                    in.close();
                    Message msg = Message.obtain(handler, getBookInfoSuccess);
                    handler.sendMessage(msg);

                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                Message msg = Message.obtain(handler, getBookInfoFailure);
                handler.sendMessage(msg);
            }
        });
    }

    private void addListener() {
        btnquit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnmenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        lvBookInfo.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_COPY) {
                    menu.setHeaderIcon(android.R.drawable.ic_dialog_info);
                    menu.setHeaderTitle("账册信息");
                    menu.add(1, MENU_CTX_UPDATE, 1, "修改账册");
                    menu.add(1, MENU_CTX_DELETE, 2, "删除账册");
                }
            }
        });

        lvBookInfo.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_COPY) {
                    //如果是从抄表按钮点进来的
//                    Intent intent = new Intent(BookInfoActivity.this, GroupInfoActivity.class);
                    Intent intent = new Intent(BookInfoActivity.this, GroupInfoStatisticActivity.class);
                    BookInfo bkinfo = adapter.getItem(position);
                    intent.putExtra("BookNo", bkinfo.getBookNo());
                    intent.putExtra("BookName", bkinfo.getBookName());
                    intent.putExtra("meterTypeNo", bkinfo.getMeterTypeNo());
                    intent.putExtra("isSelect", false);
                    startActivity(intent);
                } else if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_SELECT) {
                    //如果是从统计查询按钮点进来的
                    Intent intent = new Intent(BookInfoActivity.this, GroupInfoActivity.class);
                   //修改  不跳这里
//                    Intent intent = new Intent(BookInfoActivity.this, GroupInfoStatisticActivity.class);
                    BookInfo bkinfo = adapter.getItem(position);//拿到表名
                    intent.putExtra("BookNo", bkinfo.getBookNo());
                    intent.putExtra("BookName", bkinfo.getBookName());
                    intent.putExtra("meterTypeNo", bkinfo.getMeterTypeNo());
                    intent.putExtra("isSelect", true);
                    startActivity(intent);

                } else if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_DOWNLOAD) {
                    // 检测账册是否存在
                    final String bookNo = adapter.getItem(position).getBookNo();
                    final String meterTypeNo = adapter.getItem(position).getMeterTypeNo();
                    BookInfo bkInfo = bookInfoBiz.getBookInfoByBookNo(bookNo);
                    if (bkInfo != null) {
                        new AlertDialog.Builder(
                                BookInfoActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog)
                                .setTitle("账册已存在")
                                .setMessage("已存在相同账册编号数据，继续下载将会覆盖本机数据，是否确认下载？")
                                .setCancelable(false)
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                GroupInfoBiz groupInfoBiz = new GroupInfoBiz(BookInfoActivity.this);
                                                GroupBindBiz groupBindBiz = new GroupBindBiz(BookInfoActivity.this);
                                                ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(bookNo);
                                                for (int i = 0; i < groupInfos.size(); i++) {
                                                    groupBindBiz.removeGroupBindByGroupNo(groupInfos.get(i).getGroupNo()); // 删除绑定数据
                                                }
                                                groupInfoBiz.removeGroupInfoByBookNo(bookNo);// 删除分组数据
                                                bookInfoBiz.removeBookInfo(bookNo);// 删除账册数据
                                                Intent intent = new Intent(BookInfoActivity.this, DataDownloadActivity.class);
                                                intent.putExtra("bookNo", bookNo);
                                                intent.putExtra("meterTypeNo", meterTypeNo);
                                                intent.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_DOWNLOAD);
                                                startActivity(intent);
                                            }
                                        })
                                .setNegativeButton("取消",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                    } else {
                        Intent intent = new Intent(BookInfoActivity.this, DataDownloadActivity.class);
                        intent.putExtra("bookNo", bookNo);
                        intent.putExtra("meterTypeNo", meterTypeNo);
                        intent.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_DOWNLOAD);
                        startActivity(intent);
                    }
                } else if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_UPLOAD) {
                    String bookNo = adapter.getItem(position).getBookNo();
                    final Intent intent = new Intent(BookInfoActivity.this, DataDownloadActivity.class);
                    intent.putExtra("bookNo", bookNo);
                    intent.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_UPLOAD);
                    intent.putExtra("meterTypeNo", adapter.getItem(position).getMeterTypeNo());
                    new AlertDialog.Builder(BookInfoActivity.this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog)
                            .setTitle("确认上传")
                            .setMessage("确认上传本账册抄表数据至服务器？")
                            .setCancelable(false)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).show();
                }
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 获取被长按项数据
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        BookInfo bkinfo = adapter.getItem(info.position);

        // 执行操作
        switch (item.getItemId()) {
            case MENU_CTX_UPDATE:
                Intent intent = new Intent(this, BookInfoUpdateActivity.class);
                intent.putExtra(GlobalConsts.EXTRA_BOOKINFO_OP_TYPE, GlobalConsts.TYPE_UPDATE);
                intent.putExtra(GlobalConsts.EXTRA_BOOKINFO_OP_DATA, bkinfo);
                startActivity(intent);
                break;
            case MENU_CTX_DELETE:
                int count = bookInfoBiz.removeBookInfo(bkinfo.getBookNo());
                if (count != 0) {
                    adapter.removeItem(info.position);
                }
                break;
        }

        return super.onContextItemSelected(item);
    }


    /**
     * 右上角菜单按钮
     */
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.book_info);

        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_addbookinfo:// 新建
                        Intent intent = new Intent(BookInfoActivity.this, BookInfoUpdateActivity.class);
                        intent.putExtra(GlobalConsts.EXTRA_BOOKINFO_OP_TYPE, GlobalConsts.TYPE_ADD);
                        startActivity(intent);
                        break;
                    case R.id.action_importbookinfo:// 导入
                        Intent intent1 = new Intent(BookInfoActivity.this, BookInfoActivity.class);
                        intent1.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_DOWNLOAD);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.action_importExcel://从Excel导入账册

                        Intent intent2 = new Intent(BookInfoActivity.this, ImportExcelActivity.class);
                        intent2.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_DOWNLOAD);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    protected void onRestart() {
        // 返回该界面时刷新数据
        super.onRestart();
        if (bookInfoType == GlobalConsts.BOOKINFO_TYPE_COPY) {
            adapter.changeData(bookInfoBiz.getBookInfos());
        }
    }

}
