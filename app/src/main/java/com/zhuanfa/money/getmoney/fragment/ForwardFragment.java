package com.zhuanfa.money.getmoney.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhuanfa.money.getmoney.R;
import com.zhuanfa.money.getmoney.view.NewsPageView;
import com.zhuanfa.money.getmoney.adapter.MyViewPagerAdapter;
import com.zhuanfa.money.getmoney.db.ChannelDb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianxiying on 16/7/7.
 */
public class ForwardFragment extends Fragment {
    private final String TAG = "ForwardFragment";


    private RadioGroup rgChannel = null;
    private HorizontalScrollView hvChannel;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter = null;
    private List<NewsPageView> viewList = new ArrayList<NewsPageView>();

    private static boolean hasShow = false;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forward, null);
        findView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkVersion();
    }

    /**
     * 检查版本跟新
     */
    private void checkVersion() {
        if (!hasShow) {

        }
        hasShow = true;
    }

    private void findView() {
        viewPager = (ViewPager) view.findViewById(R.id.news_list_vp);
        rgChannel = (RadioGroup) view.findViewById(R.id.channel_rg);
        hvChannel = (HorizontalScrollView) view.findViewById(R.id.channel_hv);

        rgChannel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                viewPager.setCurrentItem(checkedId);
            }
        });
        initTab();//动态产生RadioButton
        rgChannel.check(0);

        //实现新闻页活动效果
        viewPager = (ViewPager) view.findViewById(R.id.news_list_vp);
        for (int i = 0; i <= 11; i++) {
            NewsPageView view = new NewsPageView(i + "", getActivity());
            viewList.add(view);
        }
        pagerAdapter = new MyViewPagerAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 11) {  //如果已经是最后一页,跳转到第一页
                    setTab(0);
                } else {
                    setTab(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 初始化RadioButton
     */
    private void initTab() {
        List<String> channelList = ChannelDb.getSelectedChannel();
        for (int i = 0; i < channelList.size(); i++) {
            RadioButton rb = (RadioButton) LayoutInflater.from(view.getContext()).
                    inflate(R.layout.tab_rb, null);
            rb.setId(i);
            rb.setText(channelList.get(i));
            RadioGroup.LayoutParams params = new
                    RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            rgChannel.addView(rb, params);
        }
    }


    /**
     * 滑动ViewPager时调整ScroollView的位置以便显示按钮
     *
     * @param idx
     */
    private void setTab(int idx) {
        RadioButton rb = (RadioButton) rgChannel.getChildAt(idx);
        rb.setChecked(true);
        int left = rb.getLeft();
        int width = rb.getMeasuredWidth();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int len = left + width / 2 - screenWidth / 2;
        hvChannel.smoothScrollTo(len, 0);//滑动ScroollView
    }

}
