package control.camera.com.comcameracontrol.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

import control.camera.com.comcameracontrol.App;
import control.camera.com.comcameracontrol.R;
import control.camera.com.comcameracontrol.frag.HomeVideoFrag;
import control.camera.com.comcameracontrol.utls.ContextUtil;

public class DotLocationActivity extends Activity implements View.OnClickListener {
    public Button do_location_A;
    public Button do_location_A_ok;

    public Button do_location_B;
    public Button do_location_B_ok;

    public Button do_location_ok;

    private String smsg = "";    //显示用数据缓存
    boolean bRun = true;
    boolean bThread = false;
    private String fmsg = "";    //保存用数据缓存

    public boolean IsADW=false;
    public boolean IsADWOK=false;
    public boolean IsBDW=false;
    public boolean IsBDWOK=false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_location);
        initView();
        initData();
    }

    public void initView() {
        do_location_A = findViewById(R.id.do_location_A);
        do_location_A_ok = findViewById(R.id.do_location_A_ok);
        do_location_B = findViewById(R.id.do_location_B);
        do_location_B_ok = findViewById(R.id.do_location_B_ok);
        do_location_ok=findViewById(R.id.do_location_ok);
        do_location_A.setOnClickListener(this);
        do_location_A_ok.setOnClickListener(this);
        do_location_B.setOnClickListener(this);
        do_location_B_ok.setOnClickListener(this);
        do_location_ok.setOnClickListener(this);
    }

    public void initData() {
        if (bThread == false) {
            readThread.start();
            bThread = true;
        } else {
            bRun = true;
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.do_location_A:
                IsADW=true;
                onSendButtonClicked(ContextUtil.ADW);

                break;
            case R.id.do_location_A_ok:
                if (IsADW){
                    IsADWOK=true;
                    onSendButtonClicked(ContextUtil.ADWOK);
                }else {
                    Toast.makeText(this, "请先定位A点", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.do_location_B:
                if (IsADW&&IsADWOK){
                    IsBDW=true;
                    onSendButtonClicked(ContextUtil.BDW);
                }else {
                    Toast.makeText(this, "请先定位A点", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.do_location_B_ok:
                if (IsBDW){
                    IsBDWOK=true;
                    onSendButtonClicked(ContextUtil.BDWOK);
                }else {
                    Toast.makeText(this, "请先定位B点", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.do_location_ok:
                if (IsADWOK&&IsBDWOK){
                    startActivity(new Intent(this, HomeVideoFrag.class));
                    finish();
                }else {
                    Toast.makeText(this, "请先完成AB点定位", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //接收数据线程
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
//                    while (is.available() == 0) {
//                        while (bRun == false) {
//                        }
//                    }
                    while (App.getApp().getIsInStre().available() == 0) {
                        while (bRun == false) {
                        }
                    }
                    while (true) {
                        if (!bThread)//跳出循环
                            return;
//                        num = is.read(buffer);         //读入数据
                        num = App.getApp().getIsInStre().read(buffer);
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
                        Log.e("this====", "" + s);
                        smsg = s;   //写入接收缓存
//                        if (is.available() == 0) break;  //短时间没有数据才跳出进行显示
                        if (App.getApp().getIsInStre().available() == 0) break;  //短时间没有数据才跳出进行显示
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
            Log.e("DotLocationActivity----", "" + smsg);
        }
    };

    public void onSendButtonClicked(String type) {
        int i = 0;
        int n = 0;
        try {
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

}
