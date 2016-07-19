package com.zhuanfa.money.getmoney.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zhuanfa.money.getmoney.R;
import com.zhuanfa.money.getmoney.entity.ForwardHistory;

import java.util.List;

/**
 * Created by tianxiying on 16/7/18.
 */
public class IncomeAdapter extends ArrayAdapter<ForwardHistory> {
    private int resourceId;
    private List<ForwardHistory> list;

    public IncomeAdapter(Context context, int resource, List<ForwardHistory> list) {
        super(context, resource, list);
        this.resourceId = resource;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        ForwardHistory forwardHistory = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.income_list_item_title_tv = (TextView) convertView.findViewById(R.id.income_list_item_title_tv);
            viewHolder.income_list_item_date_tv = (TextView) convertView.findViewById(R.id.income_list_item_date_tv);
            viewHolder.income_list_item_money_tv = (TextView) convertView.findViewById(R.id.income_list_item_money_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("Income :",forwardHistory.getType()+"");
        if (forwardHistory.getType() == ForwardHistory.TYPE_NEWS_TASK) {
            viewHolder.income_list_item_title_tv.setText("[转发佣金]" + forwardHistory.getTitle());
        }
        else if (forwardHistory.getType() == ForwardHistory.TYPE_NEW_PEOPLE_TASK) {
            viewHolder.income_list_item_title_tv.setText("[新手任务]" + forwardHistory.getTitle());
        }
        viewHolder.income_list_item_date_tv.setText(forwardHistory.getDate());
        viewHolder.income_list_item_money_tv.setText(forwardHistory.getForward_money() + "元");
        return convertView;
    }

    class ViewHolder {
        public TextView income_list_item_title_tv;
        public TextView income_list_item_date_tv;
        public TextView income_list_item_money_tv;
    }
}
