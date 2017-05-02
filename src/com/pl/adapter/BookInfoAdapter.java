package com.pl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pl.entity.BookInfo;
import com.pl.gassystem.R;
import com.pl.utils.MeterType;

import java.util.ArrayList;

public class BookInfoAdapter extends BaseAdapter {

    private ArrayList<BookInfo> bookInfos;
    private LayoutInflater inflater;

    public void setBookInfos(ArrayList<BookInfo> bookInfos) {
        if (bookInfos != null) {
            this.bookInfos = bookInfos;
        } else {
            this.bookInfos = new ArrayList<>();
        }
    }

    public BookInfoAdapter(Context context, ArrayList<BookInfo> bookInfos) {
        this.setBookInfos(bookInfos);
        inflater = LayoutInflater.from(context);
    }

    // 更新ListView数据
    public void changeData(ArrayList<BookInfo> bookInfos) {
        this.setBookInfos(bookInfos);
        this.notifyDataSetChanged();
    }

    // 移除数据并更新的方法
    public void removeItem(int position) {
        if (position >= 0 && position < bookInfos.size()) {
            this.bookInfos.remove(position);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return bookInfos.size();
    }

    @Override
    public BookInfo getItem(int position) {
        return bookInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_book_info, null);
            holder = new ViewHolder();
            holder.tvBookInfoName = (TextView) convertView.findViewById(R.id.tvBookInfoName);
            holder.tvMeterType = (TextView) convertView.findViewById(R.id.tvMeterType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BookInfo bookInfo = getItem(position);
        holder.tvBookInfoName.setText(bookInfo.getBookName());
        holder.tvMeterType.setText("表类型: " + MeterType.GetMeterTypeName(bookInfo.getMeterTypeNo())+"");
        return convertView;
    }

    class ViewHolder {
        private TextView tvBookInfoName;
        private TextView tvMeterType;
    }
}
