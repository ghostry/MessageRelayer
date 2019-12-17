package com.whf.messagerelayer.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.whf.messagerelayer.R;
import com.whf.messagerelayer.confing.Constant;
import com.whf.messagerelayer.utils.APIRelayerManager;
import com.whf.messagerelayer.utils.NativeDataManager;

public class APIRelayerActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private Switch mAPISwitch;
    private RelativeLayout mLayoutID, mLayoutToken;
    private TextView mTextID, mTextToken;

    private NativeDataManager mNativeDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_relayer);
        initActionbar();

        this.mNativeDataManager = new NativeDataManager(this);
        initView();
        initData();
        initListener();
    }

    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        mAPISwitch.setChecked(mNativeDataManager.getAPIRelay());
        mTextID.setText(mNativeDataManager.getAPIID());
        mTextToken.setText(mNativeDataManager.getAPIToken());
    }

    private void initView() {
        mLayoutID = (RelativeLayout) findViewById(R.id.layout_id);
        mLayoutToken = (RelativeLayout) findViewById(R.id.layout_token);

        mAPISwitch = (Switch) findViewById(R.id.switch_api);
        mTextID = (TextView) findViewById(R.id.text_id);
        mTextToken = (TextView) findViewById(R.id.text_token);
    }

    private void initListener() {
        mAPISwitch.setOnCheckedChangeListener(this);
        mLayoutID.setOnClickListener(this);
        mLayoutToken.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_api:
                mNativeDataManager.setAPIRelay(isChecked);
                System.out.println(isChecked);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        System.out.println("'click'");
        switch (v.getId()) {
            case R.id.layout_id:
                showIDDialog();
                break;
            case R.id.layout_token:
                showTokenDialog();
                break;
        }
    }

    private void showIDDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null, false);
        TextView textViewTitle = (TextView) view.findViewById(R.id.dialog_title);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_edit);

        textViewTitle.setText("请输入ID");
        editText.setText(mNativeDataManager.getAPIID());
        builder.setView(view);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNativeDataManager.setAPIID(editText.getText().toString());
                mTextID.setText(editText.getText().toString());
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    private void showTokenDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null, false);
        TextView textViewTitle = (TextView) view.findViewById(R.id.dialog_title);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_edit);

        textViewTitle.setText("请输入Token");
        String text = mTextToken.getText().toString();
        editText.setText(text);

        builder.setView(view);
        final AlertDialog.Builder progressBuilder = new AlertDialog.Builder(this);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNativeDataManager.setAPIToken(editText.getText().toString());
                mTextToken.setText(editText.getText().toString());
            }
        });

        builder.setNeutralButton("测试", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final AlertDialog progressDialog = progressBuilder.show();
                new AsyncTask<Void, Void, Integer>() {
                    @Override
                    protected Integer doInBackground(Void... params) {
                        return APIRelayerManager.relayAPI(mNativeDataManager, "0", "配置正确！");
                    }

                    @Override
                    protected void onPostExecute(Integer integer) {
                        progressDialog.cancel();
                        if (integer == APIRelayerManager.CODE_SUCCESS) {
                            Toast.makeText(getApplicationContext(), "配置正确", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "配置有误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}
