package com.zhuanfa.money.getmoney.activity;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuanfa.money.getmoney.R;
import com.zhuanfa.money.getmoney.fragment.ForwardFragment;
import com.zhuanfa.money.getmoney.fragment.InvitationFragment;
import com.zhuanfa.money.getmoney.fragment.MineFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout forward_ll, invitation_ll, mine_ll;
    private ImageView forward_iv, invitation_iv, mine_iv;
    private TextView forward_tv, invitation_tv, mine_tv;
    private Fragment forwardFragment, invitationFragment, mineFragment, currentFragment;
    private FragmentManager mFragmentManager;
    public static int FORWARD_PRESSED = 0;
    public static int INVITATION_PRESSED = 1;
    public static int MINE_PRESSED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        getFragment();
    }


    private void getFragment() {
        forwardFragment = new ForwardFragment();
        invitationFragment = new InvitationFragment();
        mineFragment = new MineFragment();
        mFragmentManager = getFragmentManager();
        if (mFragmentManager.beginTransaction().isEmpty()) {
            mFragmentManager.beginTransaction().add(R.id.main_content_ll, forwardFragment).commit();
        }
        currentFragment = forwardFragment;
        changeTabStyle(FORWARD_PRESSED);
    }

    public void switchContent(Fragment from, Fragment to) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (currentFragment != to) {
            currentFragment = to;
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from);
                transaction.add(R.id.main_content_ll, to);
                transaction.commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from); // 隐藏当前的fragment，显示下一个
                transaction.show(to);
                transaction.commit();
            }
        }
    }

    private void findView() {
        forward_ll = (LinearLayout) findViewById(R.id.forward_ll);
        invitation_ll = (LinearLayout) findViewById(R.id.invitation_ll);
        mine_ll = (LinearLayout) findViewById(R.id.mine_ll);
        forward_iv = (ImageView) findViewById(R.id.forward_iv);
        invitation_iv = (ImageView) findViewById(R.id.invitation_iv);
        mine_iv = (ImageView) findViewById(R.id.mine_iv);
        forward_tv = (TextView) findViewById(R.id.forward_tv);
        invitation_tv = (TextView) findViewById(R.id.invitation_tv);
        mine_tv = (TextView) findViewById(R.id.mine_tv);

        forward_ll.setOnClickListener(this);
        invitation_ll.setOnClickListener(this);
        mine_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forward_ll:   //转发页面
                switchContent(currentFragment, forwardFragment);
                changeTabStyle(FORWARD_PRESSED);
                break;
            case R.id.invitation_ll:    //邀请页面
                switchContent(currentFragment, invitationFragment);
                changeTabStyle(INVITATION_PRESSED);
                break;
            case R.id.mine_ll:     //我的页面
                switchContent(currentFragment, mineFragment);
                changeTabStyle(MINE_PRESSED);
                break;
        }
    }

    /**
     * 更改主页底部Tab样式
     * @param index
     */
    private void changeTabStyle(int index) {
        boolean f = false, i = false, m = false;
        switch (index) {
            case 0:
                f = true;
                break;
            case 1:
                i = true;
                break;
            case 2:
                m = true;
                break;
        }

        Resources rs = getResources();
        if (f) {
            forward_iv.setImageResource(R.drawable.tab_btn_forward_pressed);
            forward_tv.setTextColor(rs.getColor(R.color.foot_tab_text_pressed));
        } else {
            forward_iv.setImageResource(R.drawable.tab_btn_forward);
            forward_tv.setTextColor(rs.getColor(R.color.foot_tab_text));
        }

        if (i) {
            invitation_iv.setImageResource(R.drawable.tab_btn_invite_pressed);
            invitation_tv.setTextColor(rs.getColor(R.color.foot_tab_text_pressed));
        } else {
            invitation_iv.setImageResource(R.drawable.tab_btn_invite);
            invitation_tv.setTextColor(rs.getColor(R.color.foot_tab_text));
        }

        if (m) {
            mine_iv.setImageResource(R.drawable.tab_btn_me_pressed);
            mine_tv.setTextColor(rs.getColor(R.color.foot_tab_text_pressed));
        } else {
            mine_iv.setImageResource(R.drawable.tab_btn_me);
            mine_tv.setTextColor(rs.getColor(R.color.foot_tab_text));
        }
    }
}
