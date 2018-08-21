package control.camera.com.comcameracontrol.utls;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import control.camera.com.comcameracontrol.App;

public  class ReadThread extends Thread{
    private String smsg = "";    //显示用数据缓存
    private String fmsg = "";    //保存用数据缓存
    boolean bRun = true;
    boolean bThread = false;

    private ReadThreadMesg readThreadMesg;

    public void setReadThreadMesg(ReadThreadMesg readThreadMesg) {
        this.readThreadMesg = readThreadMesg;
    }



    @Override
    public void run() {
        super.run();
        int num = 0;
        byte[] buffer = new byte[1024];
        byte[] buffer_new = new byte[1024];
        int i = 0;
        int n = 0;
        bRun = true;
        //接收线程
        while (true) {
            try {
                while (App.getApp().getIsInStre().available() == 0) {
                    while (bRun == false) {
                    }
                }
                while (true) {
                    if (!bThread)//跳出循环
                        return;
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
                    smsg = s;   //写入接收缓存
                    if (App.getApp().getIsInStre().available() == 0) break;  //短时间没有数据才跳出进行显示
                }
                //发送显示消息，进行显示刷新
                handler.sendMessage(handler.obtainMessage());
            } catch (IOException e) {
            }
        }
    }


    //消息处理队列
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            readThreadMesg.onMesg(smsg);
        }
    };



}
