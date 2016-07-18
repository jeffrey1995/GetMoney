package com.zhuanfa.money.getmoney.activity;

import android.os.Bundle;
import android.view.View;

import com.zhuanfa.money.getmoney.R;

public class InvitationRuleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_rule);
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
