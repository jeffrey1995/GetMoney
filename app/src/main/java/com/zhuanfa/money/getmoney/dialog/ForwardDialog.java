package com.zhuanfa.money.getmoney.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.zhuanfa.money.getmoney.R;

/**
 * Created by tianxiying on 16/7/15.
 */
public class ForwardDialog extends Dialog implements View.OnClickListener{
    private Context mContext;

    private RelativeLayout forward_qzone_rl;
    private RelativeLayout forward_qq_rl;
    private Button cancel_btn;

    public final static int CANCEL = 0;
    public final static int FORWARD_QQ = 1;
    public final static int FORWARD_QZONE = 2;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forward_qzone_rl :
                forwardDialogEventListener.customDialogEvent(FORWARD_QZONE);
                hide();
                break;
            case R.id.forward_qq_rl:
                forwardDialogEventListener.customDialogEvent(FORWARD_QQ);
                hide();
                break;
            case R.id.cancel_btn:
                forwardDialogEventListener.customDialogEvent(CANCEL);
                hide();
                break;
        }
    }

    // 利用interface来构造一个回调函数
    public interface ForwardDialogEventListener {
        public void customDialogEvent(int valueYouWantToSendBackToTheActivity);
    }

    private ForwardDialogEventListener forwardDialogEventListener;

    public ForwardDialog(Context context,ForwardDialogEventListener forwardDialogEventListener) {
        super(context);
        mContext = context;
        this.forwardDialogEventListener = forwardDialogEventListener;
    }

    public ForwardDialog(Context context, int themeResId,ForwardDialogEventListener forwardDialogEventListener) {
        super(context, themeResId);
        this.mContext = context;
        this.forwardDialogEventListener = forwardDialogEventListener;
    }

    private void findView() {
        forward_qq_rl = (RelativeLayout) findViewById(R.id.forward_qq_rl);
        forward_qzone_rl = (RelativeLayout) findViewById(R.id.forward_qzone_rl);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        forward_qq_rl.setOnClickListener(this);
        forward_qzone_rl.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_forward);
        findView();
    }
}
