package com.zhuanfa.money.getmoney.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.zhuanfa.money.getmoney.MyApplication;
import com.zhuanfa.money.getmoney.R;
import com.zhuanfa.money.getmoney.entity.MyNews;

import java.util.List;

/**
 * Created by tianxiying on 16/7/8.
 */
public class MyNewsAdapter extends ArrayAdapter<MyNews> {
    private final String TAG = "MyNewsAdapter";
    private int resourceId;
    private int height;

    private final int TYPE_MAIN = 0;
    private final int TYPE_ITEM = 1;

    public MyNewsAdapter(Context context, int resource, List<MyNews> newsList, int height) {
        super(context, resource, newsList);
        this.resourceId = resource;
        this.height = height;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;   //添加类型后记得修改此处数目
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_MAIN viewHolder_main = null;
        ViewHolder_ITEM viewHolder_item = null;
        int type = getItemViewType(position);
        Log.d(TAG, position + "   type :" + type);
        MyNews myNews = getItem(position);
        if (convertView == null) {
            //适配不同样式
            switch (type) {
                case TYPE_MAIN:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_main_news, null);
                    viewHolder_main = new ViewHolder_MAIN();
                    RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.news_main_rl);
                    ViewGroup.LayoutParams relativeParams = (ViewGroup.LayoutParams) rl.getLayoutParams();
                    relativeParams.height = 1000 * 250 / 460;
                    rl.setLayoutParams(relativeParams);
                    viewHolder_main.news_main_title_tv = (TextView) convertView.findViewById(R.id.news_main_title_tv);
                    viewHolder_main.forward_money_tv = (TextView) convertView.findViewById(R.id.forward_money_tv);
                    viewHolder_main.news_main_uri_img = (ImageView) convertView.findViewById(R.id.news_main_uri_img);
                    convertView.setTag(viewHolder_main);
                    break;
                case TYPE_ITEM:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_news, null);
                    viewHolder_item = new ViewHolder_ITEM();
                    RelativeLayout rl_item = (RelativeLayout) convertView.findViewById(R.id.news_rl);
                    ViewGroup.LayoutParams relativeParams_item = (ViewGroup.LayoutParams) rl_item.getLayoutParams();
                    relativeParams_item.height = height / 7;
                    rl_item.setLayoutParams(relativeParams_item);
                    viewHolder_item.news_item_title_tv = (TextView) convertView.findViewById(R.id.news_item_title_tv);
                    viewHolder_item.news_item_forward_money_tv = (TextView) convertView.findViewById(R.id.news_item_forward_money_tv);
                    viewHolder_item.news_item_uri_img = (ImageView) convertView.findViewById(R.id.news_item_uri_img);
                    convertView.setTag(viewHolder_item);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_MAIN:
                    viewHolder_main = (ViewHolder_MAIN) convertView.getTag();
                    break;
                case TYPE_ITEM:
                    viewHolder_item = (ViewHolder_ITEM) convertView.getTag();
                    break;
            }
        }
        //填充数据
        switch (type) {
            case TYPE_MAIN:
                viewHolder_main.news_main_title_tv.setText(myNews.getTitle());
                viewHolder_main.forward_money_tv.setText("转发可赚" + myNews.getForward_money() + "元");
                netWorkImageViewVolley(myNews.getUri_img(), viewHolder_main.news_main_uri_img);
                break;
            case TYPE_ITEM:
                viewHolder_item.news_item_title_tv.setText(myNews.getTitle());
                viewHolder_item.news_item_forward_money_tv.setText("转发可赚" + myNews.getForward_money() + "元");
                netWorkImageViewVolley(myNews.getUri_img(), viewHolder_item.news_item_uri_img);
                break;
        }
        return convertView;
    }

    class ViewHolder_ITEM {
        public TextView news_item_title_tv;
        public TextView news_item_forward_money_tv;
        public ImageView news_item_uri_img;
    }

    class ViewHolder_MAIN {
        public TextView news_main_title_tv;
        public TextView forward_money_tv;
        public ImageView news_main_uri_img;
    }

    public void netWorkImageViewVolley(String imageUrl, ImageView img) {
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(100);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get("key");
            }

            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }
        };
        ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueue(), imageCache);
        ImageLoader.ImageListener listener = imageLoader.getImageListener(img, 0, 0);
        imageLoader.get(imageUrl, listener);
    }
}
