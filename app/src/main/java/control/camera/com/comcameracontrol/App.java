package control.camera.com.comcameracontrol;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;


public class App extends Application{

    private static App app;

    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    BluetoothDevice _device = null;     //蓝牙设备


//    public InputStream getIsInStre() {
//        return isInStre;
//    }
//
//    public void setIsInStre(InputStream isInStre) {
//        this.isInStre = isInStre;
//    }

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
    }

    public static App getApp() {
        return app;
    }


}
