package control.camera.com.comcameracontrol.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import control.camera.com.comcameracontrol.App;
import control.camera.com.comcameracontrol.R;
import control.camera.com.comcameracontrol.utls.ContextUtil;

public class SplashActivity extends AppCompatActivity implements App.SockeMsg {

    private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄
    //    boolean bRun = true;
//    boolean bThread = false;
//    private String smsg = "";    //显示用数据缓存
//    private String fmsg = "";    //保存用数据缓存
    private TextView connecting_device;
    //    private InputStream SplashisInStre;    //输入流，用来接收蓝牙数据
    //接收数据线程
    private Thread readThread = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
        final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
            }
        }

        //如果打开本地蓝牙设备不成功，提示信息，结束程序
        if (App.getApp().get_bluetooth() == null) {
            Toast.makeText(this, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 设置设备可以被搜索
        new Thread() {
            public void run() {
                if (App.getApp().get_bluetooth().isEnabled() == false) {
                    App.getApp().get_bluetooth().enable();
                }
            }
        }.start();

        initData();
        onConnectButtonClicked();

    }


    public void initData() {
        connecting_device = findViewById(R.id.connecting_device);
        connecting_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConnectButtonClicked();
            }
        });
        App.getApp().setMonMesgIstener(this);
    }

    public void onConnectButtonClicked() {

        if (App.getApp().get_bluetooth().isEnabled() == false) {  //如果蓝牙服务不可用则提示
            Toast.makeText(this, " 打开蓝牙中...", Toast.LENGTH_LONG).show();
            return;
        }

        if (App.getApp().get_socket() == null) {
            Intent serverIntent = new Intent(this, DeviceListActivity.class); //跳转程序设置
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //设置返回宏定义
        } else {
            //关闭连接socket
            try {
//                bRun = false;
                Thread.sleep(2000);
                App.getApp().getIsInStre().close();
                App.getApp().get_socket().close();
                App.getApp().set_socket(null);
            } catch (IOException e) {
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:     //连接结果，由DeviceListActivity设置返回
                // 响应返回结果
                if (resultCode == Activity.RESULT_OK) {   //连接成功，由DeviceListActivity设置返回
                    // MAC地址，由DeviceListActivity设置返回
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // 得到蓝牙设备句柄
                    ContextUtil.ADDRESS = address;
                    App.getApp().equipment(address);
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getApp().setMonMesgIstener(null);
    }

    @Override
    public void onMessAge(String message) {
        Log.e("splash",""+message);
        if (message.contains("SOK")) {
            startActivity(new Intent(this,DotLocationActivity.class));
            finish();
        }
    }
}
