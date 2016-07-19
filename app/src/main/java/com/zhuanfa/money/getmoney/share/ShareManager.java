package com.zhuanfa.money.getmoney.share;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mrtian on 2016/7/7.
 */
public class ShareManager {
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private String TAG = "ShareManager";


    public ShareManager(Context context) {
        super();
        share = context.getSharedPreferences(ShareContents.ShareName, Context.MODE_PRIVATE);
        editor = share.edit();
    }

    public void clear() {
        editor.clear().commit();
    }

    public String getOpenId() {
        return share.getString(ShareContents.openId, null);
    }

    public void setOpenId(String openId) {
        editor.putString(ShareContents.openId, openId).commit();
    }

    public String getAccess_token() {
        return share.getString(ShareContents.access_token, null);
    }

    public void setAccess_token(String access_token) {
        editor.putString(ShareContents.access_token, access_token).commit();
    }

    public String getExpires_in() {
        return share.getString(ShareContents.expires_in, null);
    }

    public void setExpires_in(String expires_in) {
        editor.putString(ShareContents.expires_in, expires_in).commit();
    }

    public String getNickname() {
        return share.getString(ShareContents.nickname, null);
    }

    public void setNickname(String nickname) {
        editor.putString(ShareContents.nickname, nickname).commit();
    }

    public String getHead_img() {
        return share.getString(ShareContents.head_img, null);
    }

    public void setHead_img(String head_img) {
        editor.putString(ShareContents.head_img, head_img).commit();
    }

    public boolean getIsNewPeople() {
        return share.getBoolean(ShareContents.isNewPeople, true);
    }

    public void setIsNewPople(boolean newPople) {
        editor.putBoolean(ShareContents.isNewPeople, newPople).commit();
    }
}
