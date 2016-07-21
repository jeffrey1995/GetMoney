package com.zhuanfa.money.getmoney.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tencent.tauth.Tencent;
import com.zhuanfa.money.getmoney.R;
import com.zhuanfa.money.getmoney.share.ShareManager;

public class WelcomeActivity extends BaseActivity {
    private final String TAG = "WelcomeActivity";
    private Tencent mTencent;
    private ShareManager shareManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                shareManager = new ShareManager(WelcomeActivity.this);
                initTencent();
                Intent intent;
                if (mTencent.isSessionValid()) {
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    /**
     * 初始化qq
     */
    private void initTencent() {
        mTencent = Tencent.createInstance(this.getString(R.string.qq_appId), this.getApplicationContext());
        mTencent.setAccessToken(shareManager.getAccess_token(), shareManager.getExpires_in());
        mTencent.setOpenId(shareManager.getOpenId());
    }
}
