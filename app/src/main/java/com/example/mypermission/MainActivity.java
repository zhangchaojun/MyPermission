package com.example.mypermission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 运行时权限最终还是没能解决
 * 因为第一次不显示是否不再提醒的勾选框
 * 所以总结下来的方法只有自定义界面了
 * 写一个界面，程序需要获取您的权限
 * 然后请求权限就不分那么多种了，直接请求和判断是否授权，如果没有授权就显示程序需要获取您的权限
 * 点击跳到系统设置界面。
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "cj";
    private Button mBtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtPhone = findViewById(R.id.bt_phone);

        mBtPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_phone:
                checkCallPermission();

                break;
        }
    }


    /**
     * 打电话的方法
     */
    public void doCall() {
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

            boolean b = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE);
            //有没有"不再提示"的chexkbox，第一次没有，所以返回false，执行else里面的内容
            //第二次以后有，为true
            //若勾选不再提醒，就又变成false了。

            if (b == true) {

                Log.e(TAG, "checkCallPermission:  true" );
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 2);
                //一调用就会弹出选择对话框，第二.三.四次弹出的对话框，不管点击接受或拒绝，都会回调下面的方法，请求码是2.特殊的是如果一旦勾选了不再提示，再点击的时候就只会执行下面2的else里的代码了。


            } else {
                Log.e(TAG, "checkCallPermission:  false" );

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                //一调用就会弹出选择对话框，第一次弹出的对话框，不管点击接受或拒绝，都会回调下面的方法，请求码是1.把判断扔给回调方法，推卸责任。

            }

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
                //第一次
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //接受

                    doCall();

                } else {
                    //拒绝

                    Toast.makeText(MainActivity.this, "您拒绝了打电话权限", Toast.LENGTH_SHORT).show();
                }

                break;

            case 2:
                //第二。三。。。次
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //接受
                    doCall();
                } else {
                    //拒绝
                    Toast.makeText(MainActivity.this, "您可以去设置，应用里面打开权限", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);


                }

                break;
        }


    }
}
