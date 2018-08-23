package control.camera.com.comcameracontrol;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import control.camera.com.comcameracontrol.activity.SplashActivity;
import control.camera.com.comcameracontrol.frag.HomeVideoFrag;
import control.camera.com.comcameracontrol.utls.ReadThread;


public class App extends Application{

    private static App app;

    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    BluetoothDevice _device = null;     //蓝牙设备
    private InputStream isInStre;    //输入流，用来接收蓝牙数据
    private ReadThread thread;

    public ReadThread getThread() {
        return thread;
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

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
        thread=new ReadThread();
    }

    public static App getApp() {
        return app;
    }


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
