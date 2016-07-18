package com.zhuanfa.money.getmoney.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhuanfa.money.getmoney.R;
import com.zhuanfa.money.getmoney.entity.ForwardHistory;
import com.zhuanfa.money.getmoney.db.MyDBManager;
import com.zhuanfa.money.getmoney.dialog.ForwardDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class News_DetailsActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "News_DetailsActivity";
    private WebView news_details_wv;
    private Button forward_btn;
    private Tencent mTencent;
    private IUiListener listener;
    private IWXAPI wx_api;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        findView();
        listener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Log.d(TAG, o.toString());
                // 成功转发新闻,将新闻存入数据库
                insertToDatabase();
            }

            @Override
            public void onError(UiError uiError) {
                Log.d(TAG, uiError.errorMessage);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "cancel!");
            }
        };
    }

    /**
     * 将转发新闻的信息存入本地数据库
     */
    private void insertToDatabase() {
        MyDBManager myDBManager = new MyDBManager(this);
        // 先查看数据库中是否存在该新闻数据
        if (!myDBManager.isExist(getIntent().getStringExtra("news_id"))) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());    //获取系统时间

            List<ForwardHistory> list = new ArrayList<ForwardHistory>();
            ForwardHistory forwardHistory = new ForwardHistory("", getIntent().getStringExtra("news_id"),
                    getIntent().getStringExtra("title"),
                    getIntent().getStringExtra("forward_money") + "",
                    ForwardHistory.TYPE_NEWS_TASK,
                    date);
            list.add(forwardHistory);
            myDBManager.add(list);
            Toast.makeText(this, "任务完成!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "已完成过该任务!", Toast.LENGTH_SHORT).show();
        }
    }

    private void findView() {
        news_details_wv = (WebView) findViewById(R.id.news_details_wv);
        //webView支持JS
        news_details_wv.getSettings().setJavaScriptEnabled(true);
        //webView自适应屏幕
        news_details_wv.getSettings().setUseWideViewPort(true);
        news_details_wv.getSettings().setLoadWithOverviewMode(true);
        //设置页面放大缩小
        news_details_wv.getSettings().setBuiltInZoomControls(true);
        String link = getIntent().getStringExtra("link");
        news_details_wv.loadUrl(link);

        back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        //转发赚钱功能
        forward_btn = (Button) findViewById(R.id.forward_btn);
        forward_btn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化qq
     */
    private void initTencent() {
        mTencent = Tencent.createInstance(this.getString(R.string.qq_appId), this.getApplicationContext());
    }

    /**
     * 分享到QQ或者QQ空间
     */
    private void shareToQQ(int type) {
        if (null == mTencent) {
            initTencent();
        }
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);  //设置分享类型,默认:图文
        params.putString(QQShare.SHARE_TO_QQ_TITLE, getIntent().getStringExtra("title"));   //设置分享标题
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, getIntent().getStringExtra("link")); //设置分享点击链接
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "转发就可以赚钱,就在转发赚钱!"); //设置分享概要说明
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "转发赚钱"); //设置分享来源
        if (ForwardDialog.FORWARD_QQ == type) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, getIntent().getStringExtra("uri_img")); //设置分享图片
            mTencent.shareToQQ(this, params, listener);
        } else if (ForwardDialog.FORWARD_QZONE == type) {
            ArrayList<String> list = new ArrayList<String>();
            list.add(getIntent().getStringExtra("uri_img"));
            params.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, list);
            mTencent.shareToQzone(this, params, listener);
        }
    }

    /**
     * 分享到微信
     */
    private void shareToWX() {
        regToWx();
        // 初始化一个WXTextObject对象
        String text = "share our application";
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage(textObj);
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;

        wx_api.sendReq(req);
    }

    /**
     * 注册微信api
     */
    private void regToWx() {
        //获取实例
        wx_api = WXAPIFactory.createWXAPI(this, getString(R.string.wx_appId), true);

        //注册到微信
        wx_api.registerApp(getString(R.string.wx_appId));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.forward_btn:

                ForwardDialog forwardDialog = new ForwardDialog(this, R.style.forward_dialog, new ForwardDialog.ForwardDialogEventListener() {
                    @Override
                    public void customDialogEvent(int valueYouWantToSendBackToTheActivity) {
                        if (ForwardDialog.FORWARD_QQ == valueYouWantToSendBackToTheActivity) {
                            shareToQQ(ForwardDialog.FORWARD_QQ);
                        } else if (ForwardDialog.FORWARD_QZONE == valueYouWantToSendBackToTheActivity) {
                            shareToQQ(ForwardDialog.FORWARD_QZONE);
                        }
                    }
                });
                // 获取对话框的窗口，并设置窗口参数,让对话框从底部弹出
                WindowManager.LayoutParams lp = forwardDialog.getWindow().getAttributes();
                lp.dimAmount = 0.7f;
                forwardDialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);

                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                forwardDialog.getWindow().setGravity(Gravity.BOTTOM);
                forwardDialog.show();
                break;
        }
    }
}
