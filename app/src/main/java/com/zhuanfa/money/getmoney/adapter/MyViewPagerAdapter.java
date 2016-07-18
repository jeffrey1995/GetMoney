package com.zhuanfa.money.getmoney.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.zhuanfa.money.getmoney.view.NewsPageView;

import java.util.List;

/**
 * Created by tianxiying on 16/7/8.
 */
public class MyViewPagerAdapter extends PagerAdapter
{
    private List<NewsPageView> views;

    public MyViewPagerAdapter(List<NewsPageView> views)
    {
        this.views = views;
    }

    @Override
    public int getCount()
    {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView(views.get(position).getView());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        container.addView(views.get(position).getView());
        return views.get(position).getView();
    }
}