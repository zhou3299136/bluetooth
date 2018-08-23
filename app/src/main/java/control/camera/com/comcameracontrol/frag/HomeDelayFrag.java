package control.camera.com.comcameracontrol.frag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

import control.camera.com.comcameracontrol.App;
import control.camera.com.comcameracontrol.R;
import control.camera.com.comcameracontrol.utls.ContextUtil;

/**
 * 延时拍照
 */
public class HomeDelayFrag extends AppCompatActivity implements View.OnClickListener {
    private String smsg = "";    //显示用数据缓存
    boolean bRun = true;
    boolean bThread = false;
    private String fmsg = "";    //保存用数据缓存

    public TextView main_frame_delay;
    public TextView main_frame_video;

    public EditText frag_delay_time_ed;//间隔时间
    public EditText frag_delay_num_ed;//拍摄长输

    public LinearLayout delay_ab;
    public ImageView delay_ab_im;

    public LinearLayout delay_start;
    public ImageView delay_start_im;
    public TextView delay_start_tv;


    public LinearLayout delay_ba;
    public ImageView delay_ba_im;

    public EditText delay_shutter_time;

    public boolean IsabsSelected = false;
    public boolean IsStartSelected = false;
    public boolean IsbasSelected = false;
    public boolean IsShutterSelected = false;
    public boolean IsCourseSelected = false;
    public boolean direction = false;

    public String delayTime="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_delay);
        initView();
        initData();
    }


    public void initView() {
        main_frame_delay = findViewById(R.id.main_frame_delay);
        main_frame_delay.setSelected(true);
        main_frame_video = findViewById(R.id.main_frame_video);
        main_frame_video.setOnClickListener(this);

        frag_delay_time_ed = findViewById(R.id.frag_delay_time_ed);
        frag_delay_num_ed = findViewById(R.id.frag_delay_num_ed);

        delay_ab = findViewById(R.id.delay_ab);
        delay_ab_im = findViewById(R.id.delay_ab_im);

        delay_start = findViewById(R.id.delay_start);
        delay_start_im = findViewById(R.id.delay_start_im);
        delay_start_tv = findViewById(R.id.delay_start_tv);

        delay_ba = findViewById(R.id.delay_ba);
        delay_ba_im = findViewById(R.id.delay_ba_im);

        delay_shutter_time = findViewById(R.id.delay_shutter_time);

        delay_ab.setOnClickListener(this);
        delay_start.setOnClickListener(this);
        delay_ba.setOnClickListener(this);


    }


    public void initData() {

        frag_delay_time_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    delayTime = v.getText().toString();
                    onSendButtonClicked(ContextUtil.SYJG + delayTime + "#");
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_frame_video:
                startActivity(new Intent(this, HomeVideoFrag.class));
                finish();
                break;
            case R.id.delay_ab:
                if (IsabsSelected) {
                    delay_ab_im.setSelected(false);
                    IsabsSelected = false;
                } else {
                    delay_ab_im.setSelected(true);
                    IsabsSelected = true;
                    direction = true;
                    onSendButtonClicked(ContextUtil.FXAB);
                }
                IsbasSelected = false;
                delay_ba_im.setSelected(false);
                break;
            case R.id.delay_start:
                if (direction) {
                    if (IsStartSelected) {
                        IsStartSelected = false;
                        delay_start_im.setSelected(false);
                        delay_start_tv.setText("开始");
                        onSendButtonClicked(ContextUtil.SYQD);
                    } else {
                        IsStartSelected = true;
                        delay_start_im.setSelected(true);
                        delay_start_tv.setText("暂停");
                        onSendButtonClicked(ContextUtil.SYTZ);
                    }
                } else {
                    Toast.makeText(this, "请选择运动方向", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.delay_ba:
                if (IsbasSelected) {
                    IsbasSelected = false;
                    delay_ba_im.setSelected(false);
                } else {
                    IsbasSelected = true;
                    delay_ba_im.setSelected(true);
                    direction = true;
                    onSendButtonClicked(ContextUtil.FXBA);
                }
                IsabsSelected = false;
                delay_ab_im.setSelected(false);
                break;

        }
    }

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
                        Log.e("HomeDelayFrag====", "" + s);
                        if (s.contains("MSV")) {
                            onSendButtonClicked(ContextUtil.speed + "50#");
                        }
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
            Log.e("HomeDelayFrag----", "" + smsg);
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
