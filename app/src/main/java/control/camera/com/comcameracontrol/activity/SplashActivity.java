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

public class SplashActivity extends AppCompatActivity {

    private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄
    boolean bRun = true;
    boolean bThread = false;
    private String smsg = "";    //显示用数据缓存
    private String fmsg = "";    //保存用数据缓存
    private TextView connecting_device;
    private InputStream SplashisInStre;    //输入流，用来接收蓝牙数据

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
                bRun = false;
                Thread.sleep(2000);
                SplashisInStre.close();
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
//                    _device = _bluetooth.getRemoteDevice(address);
                    App.getApp().set_device(App.getApp().get_bluetooth().getRemoteDevice(address));
                    // 用服务号得到socket
                    try {
//                        _socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(ContextUtil.MY_UUID));
                        App.getApp().set_socket(App.getApp().get_device().createRfcommSocketToServiceRecord(UUID.fromString(ContextUtil.MY_UUID)));

                    } catch (IOException e) {
                        Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                    }
                    //连接socket
//                    Button btn = (Button) findViewById(R.id.BtnConnect);
                    try {
//                        _socket.connect();
                        App.getApp().get_socket().connect();
//                        Toast.makeText(this, "连接" + _device.getName() + "成功！", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "连接" + App.getApp().get_device().getName() + "成功！", Toast.LENGTH_SHORT).show();
                        onSendButtonClicked(ContextUtil.handshake);
//                        App.getApp().onSendButtonClicked(ContextUtil.handshake);
//                        startActivity(new Intent(SplashActivity.this,HomeVideoFrag.class));
//                        finish();
                    } catch (IOException e) {
                        try {
                            Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
//                            _socket.close();
//                            _socket = null;
                            App.getApp().get_socket().close();
                            App.getApp().set_socket(null);
                        } catch (IOException ee) {
                            Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    //打开接收线程
                    try {
//                        App.getApp().setIsInStre(App.getApp().get_socket().getInputStream());
                        SplashisInStre = App.getApp().get_socket().getInputStream();
                    } catch (IOException e) {
                        Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    if (bThread == false) {
//                        readThread.start();
//                        bThread = true;
//                    } else {
//                        bRun = true;
//                    }
                }
                break;
            default:
                break;
        }
    }


    //接收数据线程
    Thread readThread = new Thread() {

        public void run() {
            int num = 0;
            byte[] buffer = new byte[1024];
            byte[] buffer_new = new byte[1024];
            int i = 0;
            int n = 0;
            bRun = true;
            //接收线程
            while (true) {
                try {
                    while (SplashisInStre.available() == 0) {
                        while (bRun == false) {
                        }
                    }
                    while (true) {
                        if (!bThread)//跳出循环
                            return;
//                        num = is.read(buffer);         //读入数据
                        num = SplashisInStre.read(buffer);
                        n = 0;
                        String s0 = new String(buffer, 0, num);
                        fmsg += s0;    //保存收到数据
                        for (i = 0; i < num; i++) {
                            if ((buffer[i] == 0x0d) && (buffer[i + 1] == 0x0a)) {
                                buffer_new[n] = 0x0a;
                                i++;
                            } else {
                                buffer_new[n] = buffer[i];
                            }
                            n++;
                        }
                        String s = new String(buffer_new, 0, n);
                        smsg = s;   //写入接收缓存
//                        if (is.available() == 0) break;  //短时间没有数据才跳出进行显示
                        if (SplashisInStre.available() == 0) break;  //短时间没有数据才跳出进行显示
                    }
                    //发送显示消息，进行显示刷新
                    handler.sendMessage(handler.obtainMessage());
                } catch (IOException e) {
                }
            }
        }
    };

    //消息处理队列
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("SplashActivity==", "" + smsg);
            if (smsg.contains("SOK")) {
                startActivity(new Intent(SplashActivity.this, DotLocationActivity.class));
                finish();
            }
        }
    };

    public void onSendButtonClicked(String type) {
        int i = 0;
        int n = 0;
        try {
//            OutputStream os = _socket.getOutputStream();   //蓝牙连接输出流
            OutputStream os = App.getApp().get_socket().getOutputStream();   //蓝牙连接输出流
            byte[] bos = type.getBytes();
            for (i = 0; i < bos.length; i++) {
                if (bos[i] == 0x0a) n++;
            }
            byte[] bos_new = new byte[bos.length + n];
            n = 0;
            for (i = 0; i < bos.length; i++) { //手机中换行为0a,将其改为0d 0a后再发送
                if (bos[i] == 0x0a) {
                    bos_new[n] = 0x0d;
                    n++;
                    bos_new[n] = 0x0a;
                } else {
                    bos_new[n] = bos[i];
                }
                n++;
            }

            os.write(bos_new);
        } catch (IOException e) {
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        readThread.interrupt();
        handler.removeCallbacks(readThread);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
