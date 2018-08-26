package control.camera.com.comcameracontrol;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import control.camera.com.comcameracontrol.activity.DotLocationActivity;
import control.camera.com.comcameracontrol.activity.SplashActivity;
import control.camera.com.comcameracontrol.utls.ContextUtil;


public class App extends Application {

    private static App app;

    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    BluetoothDevice _device = null;     //蓝牙设备
    public InputStream isInStre;

    private String smsg = "";    //显示用数据缓存
    private Thread readThread = null;

    public SockeMsg monMesgIstener;

    public void setMonMesgIstener(SockeMsg sMesgIstener) {
        this.monMesgIstener = sMesgIstener;
    }


    public InputStream getIsInStre() {
        return isInStre;
    }

    public void setIsInStre(InputStream isInStre) {
        this.isInStre = isInStre;
    }

    public BluetoothAdapter get_bluetooth() {
        return _bluetooth;
    }

    public void set_bluetooth(BluetoothAdapter _bluetooth) {
        this._bluetooth = _bluetooth;
    }

    public BluetoothSocket get_socket() {
        return _socket;
    }

    public void set_socket(BluetoothSocket _socket) {
        this._socket = _socket;
    }

    public BluetoothDevice get_device() {
        return _device;
    }

    public void set_device(BluetoothDevice _device) {
        this._device = _device;
    }


    public interface SockeMsg{
        void onMessAge(String message);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initData();
    }

    public static App getApp() {
        return app;
    }


    public void initData() {

        readThread = new Thread() {
            public void run() {
                if (this.isInterrupted()) {
                    return;
                }
                int num = 0;
                byte[] buffer = new byte[1024];
                byte[] buffer_new = new byte[1024];
                int i = 0;
                int n = 0;
                //接收线程
                while (true) {
                    try {
                        while (App.getApp().getIsInStre().available() == 0) {

                        }
                        while (true) {
                            num = getIsInStre().read(buffer);
                            n = 0;
                            String s0 = new String(buffer, 0, num);
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
                            if (App.getApp().getIsInStre().available() == 0)
                                break;  //短时间没有数据才跳出进行显示
                        }
                        //发送显示消息，进行显示刷新
                        handler.sendMessage(handler.obtainMessage());
                    } catch (IOException e) {
                    }
                }
            }
        };

    }


    public void equipment(String address) {
        App.getApp().set_device(App.getApp().get_bluetooth().getRemoteDevice(address));
        // 用服务号得到socket
        try {
            App.getApp().set_socket(App.getApp().get_device().createRfcommSocketToServiceRecord(UUID.fromString(ContextUtil.MY_UUID)));
        } catch (IOException e) {
            Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
        }
        //连接socket
        try {
            App.getApp().get_socket().connect();
            onSendButtonClicked(ContextUtil.handshake);
            Toast.makeText(this, "连接" + App.getApp().get_device().getName() + "成功！", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            try {
                Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                App.getApp().get_socket().close();
                App.getApp().set_socket(null);
            } catch (IOException ee) {
                Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        try {
            App.getApp().setIsInStre(App.getApp().get_socket().getInputStream());
        } catch (IOException e) {
            Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
            return;
        }

        readThread.start();

    }


    //消息处理队列
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("APP",""+smsg);
            if (monMesgIstener!=null){
                monMesgIstener.onMessAge(smsg);
            }
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
