package com.example.mypermission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 运行时权限最终还是没能解决
 * 因为第一次不显示是否不再提醒的勾选框
 * 所以总结下来的方法只有自定义界面了
 * 写一个界面，程序需要获取您的权限
 * 然后请求权限就不分那么多种了，直接请求和判断是否授权，如果没有授权就显示程序需要获取您的权限
 * 点击跳到系统设置界面。
 *
 *
 * 如果每个权限的地方都这么弄，那不费劲死了？
 *
 */
public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "cj";
    private Button mBtPhone;
    private RelativeLayout rl_permission;
    private Button bt_request_permission;
    private RelativeLayout rl_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView() {
        mBtPhone = findViewById(R.id.bt_phone);
        rl_bg = findViewById(R.id.rl_bg);
        rl_permission = findViewById(R.id.rl_permission);
        bt_request_permission = findViewById(R.id.bt_request_permission);

        mBtPhone.setOnClickListener(this);
        bt_request_permission.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_phone:
                checkCallPermission();

                break;
            case R.id.bt_request_permission:
                boolean show = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE);
                if (!show) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivityForResult(intent, 0);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                }

                break;
        }
    }


    /**
     * 打电话的方法
     */
    public void doCall() {
        rl_bg.setVisibility(View.GONE);
        rl_permission.setVisibility(View.GONE);
//        Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:15538307513"));//拨号界面
        Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:15538307513"));//直接拨打
        startActivity(intentPhone);
    }


    /**
     * 检查打电话权限，点击就会回调下面的方法哦
     */
    public void checkCallPermission() {
        //运行时权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            rl_bg.setVisibility(View.VISIBLE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {

            doCall();
        }

    }


    /**
     * 申请权限回调方法
     *
     * @param requestCode  申请时传入的code
     * @param permissions  申请的权限数组
     * @param grantResults 对应的申请结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doCall();
                } else {
                    rl_permission.setVisibility(View.VISIBLE);
                }

                break;

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(Main2Activity.this, "您拒绝了打电话权限", Toast.LENGTH_SHORT).show();
        } else {
            doCall();
        }

    }
}
