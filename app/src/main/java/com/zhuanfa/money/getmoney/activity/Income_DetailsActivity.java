package com.zhuanfa.money.getmoney.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuanfa.money.getmoney.R;
import com.zhuanfa.money.getmoney.adapter.IncomeAdapter;
import com.zhuanfa.money.getmoney.db.MyDBManager;
import com.zhuanfa.money.getmoney.entity.ForwardHistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Income_DetailsActivity extends BaseActivity implements View.OnClickListener{
    private TextView income_remain_tv, income_today_tv, income_total_tv;
    private RelativeLayout back_rl;
    private ListView listview;
    private List<ForwardHistory> fh_list;
    private double income_remain=-1;
    private double income_today=-1;
    private double income_total=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_details);
        refreshMoney(); //计算余额数据
        findView();
    }

    private void findView() {
        income_remain_tv = (TextView) findViewById(R.id.income_remain_tv);
        income_today_tv = (TextView) findViewById(R.id.income_today_tv);
        income_total_tv = (TextView) findViewById(R.id.income_total_tv);
        back_rl = (RelativeLayout) findViewById(R.id.back_rl);
        listview = (ListView) findViewById(R.id.income_list);
        back_rl.setOnClickListener(this);
        IncomeAdapter adapter = new IncomeAdapter(this,R.layout.list_item_income, fh_list);
        listview.setAdapter(adapter);
        income_remain_tv.setText(String.format("%.2f",income_remain) + "元");
        income_today_tv.setText(String.format("%.2f",income_today) + "元");
        income_total_tv.setText(String.format("%.2f",income_total) + "元");

    }

    private void refreshMoney() {
        //刷新余额
        MyDBManager myDBManager = new MyDBManager(this);
        List<ForwardHistory> forwardHistoryList = myDBManager.query();
        fh_list = forwardHistoryList;
        income_remain = 0;
        income_today = 0;
        income_total = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar currentDate = Calendar.getInstance();
        Calendar historyDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        for (ForwardHistory fh : forwardHistoryList) {
            try {
                //  累加当日收入
                historyDate.setTime(df.parse(fh.getDate()));
                if (currentDate.YEAR == historyDate.YEAR &&
                        currentDate.MONTH == historyDate.MONTH &&
                        currentDate.DAY_OF_MONTH == historyDate.DAY_OF_MONTH) {
                    income_today += Double.parseDouble(fh.getForward_money());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            income_total += Double.parseDouble(fh.getForward_money());
        }
        income_remain = income_total;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
        }
    }
}
