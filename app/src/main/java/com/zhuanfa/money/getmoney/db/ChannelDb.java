package com.zhuanfa.money.getmoney.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianxiying on 16/7/8.
 */
public class ChannelDb {
    private static List<String> selectedChannel=new ArrayList<String>();
    static{
        selectedChannel.add("首页");
        selectedChannel.add("搞笑");
        selectedChannel.add("美文");
        selectedChannel.add("养生");
        selectedChannel.add("军事");
        selectedChannel.add("情感");
        selectedChannel.add("星座");
        selectedChannel.add("亲子");
        selectedChannel.add("励志");
        selectedChannel.add("科技");
        selectedChannel.add("美食");
    }
    public static  List<String> getSelectedChannel(){
        return selectedChannel;
    }
}
