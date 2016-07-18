package com.zhuanfa.money.getmoney.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhuanfa.money.getmoney.MyApplication;
import com.zhuanfa.money.getmoney.R;
import com.zhuanfa.money.getmoney.share.ShareManager;

import java.util.HashMap;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "LoginActivity";
    private Button login_btn;
    private Tencent mTencent;
    private IUiListener iUiListener;
    private ShareManager shareManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        shareManager = new ShareManager(this);
        iUiListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                showResult(o.toString());
                //登录成功,开始保存参数,跳转界面
                saveInfo(o);
                getUserInfo();//请求用户个人信息
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(UiError uiError) {
                showResult(uiError.errorMessage);
            }

            @Override
            public void onCancel() {
                showResult("cancel!!");
            }
        };
    }

    private void saveInfo(Object o) {
        Gson gson = new Gson();
        HashMap<String, Object> map = gson.fromJson(o.toString(), HashMap.class);
        String openid = map.get("openid").toString();
        String access_token = map.get("access_token").toString();
        String expires_in = map.get("expires_in").toString();
        expires_in = expires_in.substring(0,expires_in.length()-2); //去除小数点

        shareManager.setOpenId(openid);
        shareManager.setAccess_token(access_token);
        shareManager.setExpires_in(expires_in);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 这句很很很很很很重要, 不然不会回调!
        Tencent.onActivityResultData(requestCode, resultCode, data, iUiListener);

        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                mTencent.handleLoginData(data, iUiListener);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void findView() {
        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
    }

    private void showResult(String msg) {
        Log.d(TAG, msg);
    }

    /**
     *  初始化qq
     */
    private void initTencent() {
        mTencent = Tencent.createInstance(this.getString(R.string.qq_appId), this.getApplicationContext());
        Log.d(TAG,"isLogin :" + mTencent.isSessionValid());
    }

    /**
     * 登录qq
     */
    private void loginTencent() {
        mTencent.login(this, "all", iUiListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                //QQ登录
                if (mTencent == null) {
                    initTencent();
                }
                loginTencent();
                break;
        }
    }

    /**
     * 用来请求个人信息,登录成功后调用
     */
    private void getUserInfo() {
        String url = "https://graph.qq.com/user/get_user_info?access_token=" + shareManager.getAccess_token() + "&oauth_consumer_key=" + getString(R.string.qq_appId) + "&openid=" + shareManager.getOpenId();
        Log.d(TAG, "url : " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功返回,开始解析数据
                Log.d(TAG, "success! " + s);
                Gson gson = new Gson();
                HashMap<String, Object> map = gson.fromJson(s.toString(), HashMap.class);
                shareManager.setNickname(map.get("nickname").toString());
                shareManager.setHead_img(map.get("figureurl_qq_2").toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //请求失败
                Log.d(TAG, "error response!" + volleyError);
            }
        }
        );
        request.setTag("doGet");
        MyApplication.getHttpQueue().add(request);
    }
}
