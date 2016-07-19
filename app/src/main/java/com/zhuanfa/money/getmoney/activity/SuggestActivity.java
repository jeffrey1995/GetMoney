package com.zhuanfa.money.getmoney.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhuanfa.money.getmoney.R;

public class SuggestActivity extends Activity implements View.OnClickListener{
    private RelativeLayout back_rl;
    private EditText suggest_edt, qq_number_edt;
    private Button commit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        findView();
    }

    private void findView() {
        back_rl = (RelativeLayout) findViewById(R.id.back_rl);
        suggest_edt = (EditText) findViewById(R.id.suggest_edt);
        qq_number_edt = (EditText) findViewById(R.id.qq_number_edt);
        commit_btn = (Button) findViewById(R.id.commit_btn);
        back_rl.setOnClickListener(this);
        commit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.commit_btn:
                commitSuggest();
                break;
        }
    }

    /**
     * 上传建议
     */
    private void commitSuggest() {
        if (TextUtils.isEmpty(suggest_edt.getText())) {
            Toast.makeText(this,"请输入内容!",Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(qq_number_edt.getText())) {
            Toast.makeText(this,"请输入qq号码!",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            suggest_edt.setText("");
            qq_number_edt.setText("");
            Toast.makeText(this,"提交成功!",Toast.LENGTH_LONG).show();
        }
    }
}
