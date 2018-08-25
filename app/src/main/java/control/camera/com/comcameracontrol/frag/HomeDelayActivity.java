package control.camera.com.comcameracontrol.frag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import control.camera.com.comcameracontrol.App;
import control.camera.com.comcameracontrol.R;
import control.camera.com.comcameracontrol.utls.AppUtis;
import control.camera.com.comcameracontrol.utls.ContextUtil;

/**
 * 延时拍照
 */
public class HomeDelayActivity extends AppCompatActivity implements View.OnClickListener {
    private String smsg = "";    //显示用数据缓存
    boolean bRun = true;
    boolean bThread = false;
    private String fmsg = "";    //保存用数据缓存

    public TextView main_frame_delay;
    public TextView main_frame_video;

    public EditText frag_delay_time_ed;//间隔时间
    public EditText frag_delay_num_ed;//拍摄数量

    public LinearLayout delay_ab;
    public ImageView delay_ab_im;

    public LinearLayout delay_start;
    public ImageView delay_start_im;
    public TextView delay_start_tv;


    public LinearLayout delay_ba;
    public ImageView delay_ba_im;

    public EditText delay_shutter_time;//快门时间
    public EditText frag_delay_distance_ed;

    public ProgressBar frag_delay_quantity_progress;
    public ProgressBar frag_delay_distance_progress;

    public TextView frag_delay_complete_num;
    public boolean IsabsSelected = false;
    public boolean IsStartSelected = false;
    public boolean IsbasSelected = false;
    public boolean direction = false;

//    private InputStream HomeDelayio;    //输入流，用来接收蓝牙数据

    public String delayTime="";
    public String delaynNum="";
    public String distance="";
    public String shutterTime="";

    public boolean isDelayTime=false;
    public boolean isDelaynNum=false;
    public boolean isDistance=false;
    public boolean isShutterTime=false;

    public int amount=0;
    public int completion=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_delay);
        initView();
        initData();
        onSendButtonClicked(ContextUtil.MSP);
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
        frag_delay_quantity_progress=findViewById(R.id.frag_delay_quantity_progress);
        frag_delay_distance_progress=findViewById(R.id.frag_delay_distance_progress);
        frag_delay_distance_ed=findViewById(R.id.frag_delay_distance_ed);

        frag_delay_complete_num=findViewById(R.id.frag_delay_complete_num);


        delay_ab.setOnClickListener(this);
        delay_start.setOnClickListener(this);
        delay_ba.setOnClickListener(this);


        frag_delay_quantity_progress.setMax(100);
        frag_delay_quantity_progress.setProgress(100);
        frag_delay_distance_progress.setMax(100);
    }


    public void initData() {

//        try {
//            HomeDelayio = App.getApp().get_socket().getInputStream();
//        } catch (IOException e) {
//            Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
//            return;
//        }


        frag_delay_distance_ed.setFocusable(true);
        frag_delay_distance_ed.setFocusableInTouchMode(true);
        frag_delay_distance_ed.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        /**
         * 步距离
         */
        frag_delay_distance_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    distance = v.getText().toString();
                    if (TextUtils.isEmpty(distance)){
                        Toast.makeText(HomeDelayActivity.this, "步距离不能为空", Toast.LENGTH_SHORT).show();
                    }else {

//                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        if (imm.isActive()) {
//                            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
//                        }
                        isDistance=true;
                        onSendButtonClicked(ContextUtil.SYBJ + distance + "#");
                        frag_delay_time_ed.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });

        /**
         * 间隔时间
         */
        frag_delay_time_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    delayTime = v.getText().toString();
                    if (TextUtils.isEmpty(delayTime)){
                        Toast.makeText(HomeDelayActivity.this, "间隔时间不能为空", Toast.LENGTH_SHORT).show();
                    }else {
                        isDelayTime=true;
                        onSendButtonClicked(ContextUtil.SYJG + AppUtis.speedTime(delayTime) + "#");
                        //隐藏软键盘
//                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        if (imm.isActive()) {
//                            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
//                        }
                        frag_delay_num_ed.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });

        /**
         * 拍摄数量
         */
        frag_delay_num_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    delaynNum = v.getText().toString();
                    if (TextUtils.isEmpty(delaynNum)){
                        Toast.makeText(HomeDelayActivity.this, "拍摄数量不能为空", Toast.LENGTH_SHORT).show();
                    }else {
                        isDelaynNum=true;
                        amount=Integer.valueOf(delaynNum);
                        onSendButtonClicked(ContextUtil.SYZS + AppUtis.SykDelaynNum(delaynNum) + "#");
                        //隐藏软键盘
//                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        if (imm.isActive()) {
//                            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
//                        }
                        delay_shutter_time.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });


        /**
         * 快门时间
         */
        delay_shutter_time.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    shutterTime = v.getText().toString();
                    if (TextUtils.isEmpty(shutterTime)){
                        Toast.makeText(HomeDelayActivity.this, "快门时间不能为空", Toast.LENGTH_SHORT).show();
                    }else {
                        isShutterTime=true;
                        onSendButtonClicked(ContextUtil.SYKS + AppUtis.shutterTime(shutterTime) + "#");
                        //隐藏软键盘
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm.isActive()) {
                            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_frame_video:
                startActivity(new Intent(this, HomeVideoActivity.class));
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

                if (!direction) {
                        Toast.makeText(this, "请选择运动方向", Toast.LENGTH_SHORT).show();
                }else if (!isDistance){
                    Toast.makeText(this, "请输入步距值", Toast.LENGTH_SHORT).show();
                }else if (!isDelayTime){
                    Toast.makeText(this, "请输入间隔时间", Toast.LENGTH_SHORT).show();
                }else if (!isDelaynNum){
                    Toast.makeText(this, "请输入拍摄张数", Toast.LENGTH_SHORT).show();
                }else if (!isShutterTime){
                    Toast.makeText(this, "请输入按下快门时间", Toast.LENGTH_SHORT).show();
                }else {
                    if (IsStartSelected) {
                        IsStartSelected = false;
                        delay_start_im.setSelected(false);
                        delay_start_tv.setText("开始");
                        onSendButtonClicked(ContextUtil.SYTZ);
                    } else {
                        IsStartSelected = true;
                        delay_start_im.setSelected(true);
                        delay_start_tv.setText("暂停");
                        onSendButtonClicked(ContextUtil.SYQD);
                    }
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

    Thread readThread =null;

    //消息处理队列
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("HomeDelayFrag----", "" + smsg);
            if (smsg.contains("SYWC")){
                String smsp=smsg.substring(5,smsg.length());
                frag_delay_complete_num.setText(""+smsp);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        readThread = new Thread() {

            public void run() {
                if(this.isInterrupted()){
                    return;
                }
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
//                    while (App.getApp().getIsInStre().available() == 0) {
//                        while (bRun == false) {
//                        }
//                    }
                        while (true) {
                            if (!bThread)//跳出循环
                                return;
                            num = App.getApp().getIsInStre().read(buffer);         //读入数据
//                        num = App.getApp().getIsInStre().read(buffer);
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
                            smsg = s;   //写入接收缓存
                            if (App.getApp().getIsInStre().available() == 0) break;  //短时间没有数据才跳出进行显示
//                        if (App.getApp().getIsInStre().available() == 0) break;  //短时间没有数据才跳出进行显示
                        }
                        //发送显示消息，进行显示刷新
                        handler.sendMessage(handler.obtainMessage());
                    } catch (IOException e) {
                    }
                }
            }
        };
            readThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        readThread.interrupt();
        handler.removeCallbacks(readThread);
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
