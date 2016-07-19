package com.zhuanfa.money.getmoney.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.zhuanfa.money.getmoney.MyApplication;
import com.zhuanfa.money.getmoney.R;
import com.zhuanfa.money.getmoney.activity.Income_DetailsActivity;
import com.zhuanfa.money.getmoney.activity.SuggestActivity;
import com.zhuanfa.money.getmoney.entity.ForwardHistory;
import com.zhuanfa.money.getmoney.db.MyDBManager;
import com.zhuanfa.money.getmoney.share.ShareManager;
import com.zhuanfa.money.getmoney.user_defined_widget.RoundImageView;

import java.util.List;

/**
 * Created by tianxiying on 16/7/7.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "MineFragment";
    private View view;
    private ShareManager shareManager;
    private TextView qq_nickname;
    private TextView remain_money_total_tv;
    private RelativeLayout remain_money_tv;
    private ImageView suggest_iv;
    private RoundImageView qq_head_img;
    private double total_money;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        shareManager = new ShareManager(this.getActivity());
        findView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMoney();
    }

    private void refreshMoney() {
        //刷新余额
        MyDBManager myDBManager = new MyDBManager(getActivity());
        List<ForwardHistory> forwardHistoryList = myDBManager.query();
        double money = 0;
        for (ForwardHistory fh : forwardHistoryList) {
            money += Double.parseDouble(fh.getForward_money());
        }
        total_money = money;
        remain_money_total_tv.setText(String.format("%.2f", total_money) + "元");//取两位小数
    }


    private void findView() {
        qq_nickname = (TextView) view.findViewById(R.id.qq_nickname);
        remain_money_total_tv = (TextView) view.findViewById(R.id.remain_money_total_tv);
        qq_head_img = (RoundImageView) view.findViewById(R.id.qq_head_img);
        remain_money_tv = (RelativeLayout) view.findViewById(R.id.remain_money_rl);
        suggest_iv = (ImageView) view.findViewById(R.id.suggest_iv);
        remain_money_tv.setOnClickListener(this);
        suggest_iv.setOnClickListener(this);
        qq_nickname.setText(shareManager.getNickname());
        netWorkImageViewVolley(shareManager.getHead_img(), qq_head_img);
    }

    /**
     * 加载圆形QQ图像
     *
     * @param imageUrl
     * @param img
     */
    public void netWorkImageViewVolley(String imageUrl, RoundImageView img) {
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remain_money_rl:
                intent = new Intent(getActivity(), Income_DetailsActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.suggest_iv:
                intent = new Intent(getActivity(), SuggestActivity.class);
                getActivity().startActivity(intent);
        }
    }
}
