package com.whf.messagerelayer.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.whf.messagerelayer.R;
import com.whf.messagerelayer.utils.NativeDataManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mSmsLayout, mEmailLayout, mRuleLayout,mAPILayout;
    private NativeDataManager mNativeDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNativeDataManager = new NativeDataManager(this);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Boolean isReceiver = mNativeDataManager.getReceiver();
        final MenuItem menuItem = menu.add("开关");
        if (isReceiver) {
            menuItem.setIcon(R.mipmap.ic_send_on);
        } else {
            menuItem.setIcon(R.mipmap.ic_send_off);
        }

        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Boolean receiver = mNativeDataManager.getReceiver();
                if(receiver){
                    mNativeDataManager.setReceiver(false);
                    menuItem.setIcon(R.mipmap.ic_send_off);
                    Toast.makeText(MainActivity.this,"总闸已关闭",Toast.LENGTH_SHORT).show();
                }else{
                    mNativeDataManager.setReceiver(true);
                    menuItem.setIcon(R.mipmap.ic_send_on);
                    Toast.makeText(MainActivity.this,"总闸已开启",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add("关于").setIcon(R.mipmap.ic_about)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        startActivity(new Intent(MainActivity.this,AboutActivity.class));
                        return false;
                    }
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        mSmsLayout = (RelativeLayout) findViewById(R.id.sms_relay_layout);
        mEmailLayout = (RelativeLayout) findViewById(R.id.email_relay_layout);
        mAPILayout = (RelativeLayout) findViewById(R.id.api_relay_layout);
        mRuleLayout = (RelativeLayout) findViewById(R.id.rule_layout);

        mSmsLayout.setOnClickListener(this);
        mAPILayout.setOnClickListener(this);
        mEmailLayout.setOnClickListener(this);
        mRuleLayout.setOnClickListener(this);
        checkAndGetPermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sms_relay_layout:
                startActivity(new Intent(this, SmsRelayerActivity.class));
                break;
            case R.id.email_relay_layout:
                startActivity(new Intent(this, EmailRelayerActivity.class));
                break;
            case R.id.api_relay_layout:
                startActivity(new Intent(this, APIRelayerActivity.class));
                break;
            case R.id.rule_layout:
                startActivity(new Intent(this, RuleActivity.class));
        }
    }
    private void checkAndGetPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_CALL_LOG}, 0);
    }
}
