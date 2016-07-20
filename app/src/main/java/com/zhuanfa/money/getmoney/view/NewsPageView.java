package com.zhuanfa.money.getmoney.view;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import com.zhuanfa.money.getmoney.MyApplication;
import com.zhuanfa.money.getmoney.R;
import com.zhuanfa.money.getmoney.activity.News_DetailsActivity;
import com.zhuanfa.money.getmoney.entity.MyNews;
import com.zhuanfa.money.getmoney.adapter.MyNewsAdapter;
import com.zhuanfa.money.getmoney.service.PushService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tianxiying on 16/7/8.
 */
public class NewsPageView {
    private final String TAG = "NewsPageView";
    private View view;
    private PullToRefreshLayout refresh_rl;
    private PullableListView listview;
    private MyNewsAdapter myNewsAdapter;
    private List newsList = new ArrayList();
    private String type;
    private Activity activity;
    boolean isRefreshing = false;   //刷新标记

    private final int INIT_QUEST = 0;
    private final int AGAIN_QUEST = 1;

    public NewsPageView(String type, Activity activity) {
        this.type = type;
        this.activity = activity;
        onCreateView(activity);
        getInformation();
    }

    public void onCreateView(Activity activity) {
        view = LayoutInflater.from(activity).inflate(R.layout.view_news, null);
        findView();
    }

    public View getView() {
        return view;
    }

    private void findView() {
        listview = (PullableListView) view.findViewById(R.id.news_list);
        refresh_rl = (PullToRefreshLayout) view.findViewById(R.id.refresh_rl);
        refresh_rl.setPullUpEnable(false);
        Display display = activity.getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        myNewsAdapter = new MyNewsAdapter(view.getContext(), R.layout.list_item_news, newsList, height);
        listview.setAdapter(myNewsAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyNews myNews = (MyNews) newsList.get(position);
                Intent intent = new Intent(activity, News_DetailsActivity.class);
                intent.putExtra("news_id", myNews.getId());
                intent.putExtra("title", myNews.getTitle());
                intent.putExtra("forward_money", myNews.getForward_money() + "");
                intent.putExtra("data", myNews.getDate());
                intent.putExtra("uri_img", myNews.getUri_img());
                intent.putExtra("link", myNews.getLink());
                activity.startActivity(intent);
            }
        });

        refresh_rl.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                refresh();
                isRefreshing = true;
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });

    }

    private void getData(String data) {
        newsList.clear();
        Gson gson = new Gson();
        HashMap<String, Object> map = gson.fromJson(data, HashMap.class);
        int size = (int) (double) map.get("size");
        ArrayList<Map<String, Object>> news_list = (ArrayList<Map<String, Object>>) map.get("doc");
        for (int i = 0; i < news_list.size(); i++) {
            Map<String, Object> news = news_list.get(i);
            Iterator<Map.Entry<String, Object>> iterator = news.entrySet().iterator();
            MyNews myNews = new MyNews(news.get("id").toString(), news.get("title").toString(),
                    Double.parseDouble(news.get("forward_money") + ""),
                    news.get("content").toString(), news.get("date").toString(),
                    news.get("uri_img").toString(), news.get("link").toString());
            newsList.add(myNews);
        }
        myNewsAdapter.notifyDataSetChanged();
        //开启随机推送!
        starPullService();
    }

    private void stopRefreshing(int refreshResult) {
        if (isRefreshing) {
            refresh_rl.refreshFinish(refreshResult);
            isRefreshing = false;
        }
    }

    /**
     * 开启后台Service推送新闻
     */
    private void starPullService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30000); //延时30秒
                    if ("0".equals(type)) {     //从首页里随机抽取一条新闻进行推送
                        int i = (int) (Math.random() * 15);
                        if (i < newsList.size()) {
                            MyNews myNews = (MyNews) newsList.get(i);
                            Intent intent = new Intent(view.getContext(), PushService.class);
                            intent.putExtra("news_id", myNews.getId());
                            intent.putExtra("title", myNews.getTitle());
                            intent.putExtra("content", myNews.getContent());
                            intent.putExtra("forward_money", myNews.getForward_money() + "");
                            intent.putExtra("data", myNews.getDate());
                            intent.putExtra("uri_img", myNews.getUri_img());
                            intent.putExtra("link", myNews.getLink());
                            intent.setAction(Intent.ACTION_EDIT);
                            view.getContext().startService(intent);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 刷新操作
     */
    public void refresh() {
        volley_Get(type, AGAIN_QUEST);
    }

    /**
     * 开始网络请求数据
     */
    public void getInformation() {
        volley_Get(type, INIT_QUEST);
    }

    /**
     * @param type      新闻分类
     * @param isRefresh 首次(INIT_REQUEST) :0,刷新(AGAIN_QUEST) :1
     */
    private void volley_Get(String type, final int isRefresh) {
        String url = activity.getString(R.string.url_host) + activity.getString(R.string.news) + type + "&r=" + isRefresh;
        Log.d(TAG, "url : " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功返回,开始解析数据
                Log.d(TAG, "success! " + s);
                getData(s);
                if (isRefresh == AGAIN_QUEST) {
                    stopRefreshing(PullToRefreshLayout.SUCCEED);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //请求失败
                Log.d(TAG, "error response!" + volleyError);
                if (isRefresh == AGAIN_QUEST) {
                    stopRefreshing(PullToRefreshLayout.FAIL);
                }
            }
        }
        );
        request.setTag("doGet");
        MyApplication.getHttpQueue().add(request);
    }
}
